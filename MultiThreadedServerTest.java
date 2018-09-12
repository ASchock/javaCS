package client;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import general.Util;
import model.Faculty;
import model.FacultyStatus;

public class MultiThreadedServerTest {
	public static HttpResponse get(String completeUrl, String id) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(completeUrl + "/?id=" + id);
			return client.execute(get);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public static HttpResponse post(String completeUrl, String body) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(completeUrl);
		httpPost.setHeader("Content-type", "application/json");
		try {
			StringEntity stringEntity = new StringEntity(body);
			httpPost.getRequestLine();
			httpPost.setEntity(stringEntity);

			return httpClient.execute(httpPost);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static String getRandomString() {
        String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder stringBuilder = new StringBuilder();
        Random rnd = new Random();
        while (stringBuilder.length() < 8) {
            int index = (int) (rnd.nextFloat() * CHARS.length());
            stringBuilder.append(CHARS.charAt(index));
        }
        return stringBuilder.toString();
    }
	
	private static int getRandomInt() {
		Random random = new Random();
		return random.nextInt(999999)+100000;
    }
	
	public static void main(String []args) throws JsonGenerationException, JsonMappingException, IOException {
		String getUrl = "http://" + Util.BASE_URL + ":" + Util.PORT.toString() + "/" + Util.GET_FACULTY_ENDPOINT;
		String postUrl = "http://" + Util.BASE_URL + ":" + Util.PORT.toString() + "/" + Util.CREATE_FACULTY_ENDPOINT;
		int numRequests = 1000;
		for (int i=0;i<numRequests;i++) {
			//Get request
			HttpResponse response = get(getUrl, String.valueOf(getRandomInt()));
			
			//Post request
			Faculty fac = new Faculty(String.valueOf(getRandomInt()), getRandomString(), getRandomString(),
					FacultyStatus.FULL_TIME);
			ObjectMapper mapper = new ObjectMapper();
			String requestBody = mapper.writeValueAsString(fac);
			HttpResponse response1 = post(postUrl, requestBody);
		}
	}

}
