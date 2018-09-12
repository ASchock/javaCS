package server;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import org.codehaus.jackson.map.ObjectMapper;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import general.Util;
import javafx.collections.ObservableList;
import model.Faculty;

public class Main {

	private static FacultyManager fm;

	static String convertStreamToString(java.io.InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	static void parseQuery(String query, Map<String, Object> parameters) throws UnsupportedEncodingException {
		if (query != null) {
			String pairs[] = query.split("[&]");
			for (String pair : pairs) {
				String param[] = pair.split("[=]");
				String key = null;
				String value = null;
				if (param.length > 0) {
					key = URLDecoder.decode(param[0], System.getProperty("file.encoding"));
				}

				if (param.length > 1) {
					value = URLDecoder.decode(param[1], System.getProperty("file.encoding"));
				}

				if (parameters.containsKey(key)) {
					Object obj = parameters.get(key);
					if (obj instanceof List<?>) {
						List<String> values = (List<String>) obj;
						values.add(value);

					} else if (obj instanceof String) {
						List<String> values = new ArrayList<String>();
						values.add((String) obj);
						values.add(value);
						parameters.put(key, values);
					}
				} else {
					parameters.put(key, value);
				}
			}
		}
	}

	@SuppressWarnings("restriction")
	public static void main(String[] args) throws Exception {
		// Initializing the dat store
		System.out.println("Initializing .dat store");
		File datFile = new File(Util.DAT_FILE);
		datFile.createNewFile();
		fm = new FacultyManager(new FacultyDataFile(datFile));
		System.out.println(".dat store initialized");
		HttpServer server = HttpServer.create(new InetSocketAddress(Util.PORT), 0);
		server.createContext("/" + Util.CREATE_FACULTY_ENDPOINT, new CreateFacultyHandler());
		server.createContext("/" + Util.GET_FACULTY_ENDPOINT, new GetFacultyHandler());
		
		//Create a CachedThreadPool executor to allow handling of multiple clients in parallel. 
		server.setExecutor(Executors.newCachedThreadPool());
		server.start();
		System.out.println("Server started successfully");
	}

	static class CreateFacultyHandler implements HttpHandler {

		@Override
		public void handle(HttpExchange he) throws IOException {
			ObjectMapper mapper = new ObjectMapper();
			String response = "";
			try {
				String body = convertStreamToString(he.getRequestBody());
				System.out.println("Body: " + body);
				Faculty fac = mapper.readValue(body, Faculty.class);
				fm.saveFaculty(fac);
				System.out.println("Faculty::: " + fac);
			} catch (Exception ex) {
				System.err.println(ex.getMessage());
				response += ex.getMessage();
			}
			response += "Facility created successfully";
			he.sendResponseHeaders(200, response.length());
			OutputStream os = he.getResponseBody();
			os.write(response.toString().getBytes());
			os.close();
		}

	}

	static class GetFacultyHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange he) throws IOException {
			Map<String, Object> parameters = new HashMap<String, Object>();
			ObjectMapper mapper = new ObjectMapper();
			URI requestedUri = he.getRequestURI();
			String query = requestedUri.getRawQuery();
			parseQuery(query, parameters);

			
			//Print thread id
			//System.out.println("ThreadID: "+ Thread.currentThread().getId());
			
			// send response
			String response = "";
			String facilityId = (String) parameters.get("id");
			
			if (!facilityId.isEmpty()) {
				System.out.println("Facility Id: " + facilityId);
				ObservableList<Faculty> receivedFaculty = fm.getFacultyAsObservable(facilityId);
				System.out.println("Facility Object: " + receivedFaculty);
				response += mapper.writeValueAsString(receivedFaculty);
			}

			he.sendResponseHeaders(200, response.length());
			OutputStream os = he.getResponseBody();
			os.write(response.toString().getBytes());
			os.close();
		}
	}

}