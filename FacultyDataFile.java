package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FacultyDataFile implements IFacultyData {

	private File facultyFile;

	public FacultyDataFile(File facultyFile) {
	        this.facultyFile = facultyFile;
	    }

	public BufferedReader getReader() throws IOException {
		if (!facultyFile.isFile() || !facultyFile.canRead()) {
			throw new IOException("Can't find faculty file!");
		}

		return new BufferedReader(new FileReader(facultyFile));
	}

	public BufferedWriter getWriter() throws IOException {
		if (!facultyFile.isFile() || !facultyFile.canWrite()) {
			throw new IOException("Can't write to faculty file!");
		}

		return new BufferedWriter(new FileWriter(facultyFile));
	}

}
