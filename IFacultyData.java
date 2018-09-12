package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public interface IFacultyData {

	public BufferedReader getReader() throws IOException;

	public BufferedWriter getWriter() throws IOException;

}
