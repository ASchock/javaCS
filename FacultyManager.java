package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Faculty;
import model.FacultyStatus;

public class FacultyManager {

	private IFacultyData facultyData;

	public FacultyManager(IFacultyData facultyData) {
		this.facultyData = facultyData;
	}

	/**
	 * Receive Faculty id and return the Faculty record to the client from
	 * faculty.data file if found, otherwise return null.
	 */
	synchronized public Faculty getFaculty(String id) {
		Faculty faculty = null;
		if (id == null) {
			return faculty;
		}

		try (BufferedReader br = facultyData.getReader()) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.equals("[" + id + "]")) {
					faculty = getFacultyFromLines(new String[] { line, br.readLine(), br.readLine(), br.readLine() });
					break;
				}
			}
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
		return faculty;
	}

	public ObservableList<Faculty> getFacultyAsObservable(String id) {
		ObservableList<Faculty> facultys = FXCollections.observableArrayList();
		Faculty fac = getFaculty(id);
		if (fac != null) {
			facultys.add(fac);
		}
		return facultys;
	}

	/**
	 * Receive Faculty record from client and append to the faculty.data file.
	 */
	synchronized public void saveFaculty(Faculty faculty) {
		String content = "";
		String newLine = System.getProperty("line.separator");
		boolean found = false;

		try (BufferedReader br = facultyData.getReader()) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.equals("[" + faculty.getId() + "]") && br.readLine().equals(faculty.getLastName())) {
					found = true;
					content += "[" + faculty.getId() + "]" + newLine;
					content += "lastname=" + faculty.getLastName() + newLine;
					content += "firstname=" + faculty.getFirstName() + newLine;
					content += "status=" + faculty.getStatus() + newLine;
					br.readLine();
				} else {
					content += line + newLine;
				}
			}
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}

		if (!found) {
			content += "[" + faculty.getId() + "]" + newLine;
			content += "lastname=" + faculty.getLastName() + newLine;
			content += "firstname=" + faculty.getFirstName() + newLine;
			content += "status=" + faculty.getStatus() + newLine;
		}

		try (BufferedWriter writer = facultyData.getWriter()) {
			writer.write(content);
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException ex) {
			System.err.println(ex.getMessage());
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
	}

	private Faculty getFacultyFromLines(String[] lines) {
		System.out.println("Build facility with lines: " + Arrays.toString(lines));
		Faculty faculty = new Faculty(lines[0].replace("[", "").replace("]", ""), lines[1].split("=")[1],
				lines[2].split("=")[1], FacultyStatus.valueOf(lines[3].split("=")[1]));
		System.out.println("Built faculty >>>  " + faculty);
		return faculty;
	}

}
