package edu.sumdu.server.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class Group.
 */
public class Group {

    /** The faculty. */
    private int fakultyId;
    private int id;
    
    /** The number of group, unique. */
    private String number;

    private ArrayList<Student> students = new ArrayList<Student>();

    public void setFaculty (int fakultyId) {
		this.fakultyId = fakultyId;
	}
    
    public void setId (int id) {
		this.id = id;
	}
    
    @Override
    public String toString() {
        StringBuilder groupString = new StringBuilder ();
        groupString.append("Group [fakulty=");
        groupString.append(fakultyId);
        groupString.append(", number=");
        groupString.append(number);
        groupString.append("]");        
        return groupString.toString();
    }

    /**
     * Instantiates a new group.
     * 
     * @param fakulty the faculty
     * @param number the number
     */
    public Group(int fakultyId, String number) {
        setFakulty(fakultyId);
        setNumber(number);
    }
    
    /**
     * Instantiates a new group.
     * 
     * @param fakulty the faculty
     * @param number the number
     */
    public Group(String number, int id) {
        setId(id);
        setNumber(number);
    }

    public Group() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Adds the student.
     * 
     * @param student the student
     */
    public void addStudent(Student student) {
        students.add(student);
    }

    /**
     * Removes the student by id.
     * 
     * @param id the id
     * @throws ServerException the server exception
     */
    public void removeStudent(int id) throws ServerException {
        Student student = getStudentById(id);
        if (student == null)
            throw new ServerException("There is no student with id = " + id);
        students.remove(student);
    }

    /**
     * Gets the faculty.
     * 
     * @return the faculty
     */
    public int getFakulty() {
        return fakultyId;
    }

    /**
     * Sets the fakulty.
     * 
     * @param fakulty the new faculty
     */
    public void setFakulty(int fakultyId) {
        this.fakultyId = fakultyId;
    }

    /**
     * Gets the number.
     * 
     * @return the number
     */
    public String getNumber() {
        return number;
    }

    /**
     * Sets the number.
     * 
     * @param number the new number
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * Gets the List of students.
     * 
     * @return the students
     * @throws ServerException the server exception
     */
    public List<Student> getStudents() throws ServerException {
        return students;
    }

    private Student getStudentById(int id) {
        for (Student st : students) {
            if (st.getId() == id)
                return st;
        }
        return null;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }
    
    public int getId() {
		return this.id;
	}
}
