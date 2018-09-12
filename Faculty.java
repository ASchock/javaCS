package model;


public class Faculty {
	private String id;
	private String lastName;
	private String firstName;
	private FacultyStatus status;
	
	public Faculty() {
		
	}
	
	
	public Faculty(String id, String lastName, String firstName, FacultyStatus status) {
		super();
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public FacultyStatus getStatus() {
		return status;
	}

	public void setStatus(FacultyStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Faculty [id=" + id + ", lastName=" + lastName + ", firstName=" + firstName + ", status=" + status + "]";
	}
}
