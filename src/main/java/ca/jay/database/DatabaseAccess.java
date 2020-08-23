package ca.jay.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import ca.jay.beans.Student;
import ca.jay.beans.User;

@Repository
public class DatabaseAccess {

	@Autowired
	protected NamedParameterJdbcTemplate jdbc;
	
	
	public User findUserAccount(String userName) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "SELECT * FROM sec_user WHERE userName=:userName";
		parameters.addValue("userName", userName);
		ArrayList<User> users = 
				(ArrayList<User>)jdbc.query(query, parameters, new BeanPropertyRowMapper<User>(User.class));
	
		if(users.size() > 0) {
			return users.get(0);
		}
		else {
			return null;
		}
	}
	
	
	public List<String> getRolesById(long userId){
		
		ArrayList<String> roles = new ArrayList<String>();
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		
		String query = "SELECT user_role.userId, sec_role.roleName "
				+ "FROM user_role, sec_role "
				+ "WHERE user_role.roleId=sec_role.roleId "
				+ "AND userId=:userId";
		
		parameters.addValue("userId", userId);
		
		List<Map<String, Object>> rows = jdbc.queryForList(query, parameters);
		
		for(Map<String, Object> row: rows) {
			roles.add((String)row.get("roleName"));
		}
		
		return roles;
	}
	
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	public void createNewUser(String name, String password) {
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		
		String query = "INSERT INTO SEC_User (userName, encryptedPassword, ENABLED) " + 
				"VALUES (:name, :password, 1)";
		
		parameters.addValue("name", name);
		parameters.addValue("password", passwordEncoder.encode(password));
		
		jdbc.update(query, parameters);
	}
	
	
	public void addRole(long userId, long roleId) {
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		
		String query = "insert into user_role (userId, roleId)" 
				+ "values (:userId, :roleId);";
		
		parameters.addValue("userId", userId);
		parameters.addValue("roleId", roleId);
		
		jdbc.update(query, parameters);	
	}
	
	
	
/* ----------------------------------------------------------------------------------*/
	
	
	
	
	public void addStudent(Student student) {
		
		// Mapping DB key and Value from Contact obj
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		
		// Query string
		String query = "INSERT INTO student "
				+ "(studentName, studentId, exercises, assignment1, assignment2, assignment3, midterm, finalExam) "
				+ "VALUES (:studentName, :studentId, :exercises, :assignment1, :assignment2, :assignment3, :midterm, :finalExam)";
		
		
		parameters.addValue("studentName", student.getStudentName());
		parameters.addValue("studentId", student.getStudentId());
		parameters.addValue("exercises", student.getExercises());
		parameters.addValue("assignment1", student.getAssignment1());
		parameters.addValue("assignment2", student.getAssignment2());
		parameters.addValue("assignment3", student.getAssignment3());
		parameters.addValue("midterm", student.getAssignment3());
		parameters.addValue("finalExam", student.getMidterm());
		
		
		
		// Update DB table
		jdbc.update(query, parameters);
	}
	
	
	public ArrayList<Student> getStudents(){
		
		String query = "SELECT * FROM student";
		
		// set all value for the Contact obj with value from DB
		ArrayList<Student> students = 
		  (ArrayList<Student>)jdbc.query(query, new BeanPropertyRowMapper<Student>(Student.class));
		
		return students;
	}
	
	
	public Student getStudentById(long id) {
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "SELECT * FROM student WHERE id=:id";
		parameters.addValue("id", id);
		
		ArrayList<Student> students = 
				(ArrayList<Student>)jdbc.query(query, parameters,
				new BeanPropertyRowMapper<Student>(Student.class));
		
		if (students.size()>0)
			return students.get(0);
		return null;
	}
	
	public void editStudent(Student student) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = 
				"UPDATE student SET "
				+ "studentName=:studentName, "
				+ "studentId=:studentId, "
				+ "exercises=:exercises, "
				+ "assignment1=:assignment1, "
				+ "assignment2=:assignment2, "
				+ "assignment3=:assignment3, "
				+ "midterm=:midterm, "
				+ "finalExam=:finalExam "
				+ "WHERE id=:id";
		
		parameters.addValue("id", student.getId()); // to find the ID
		parameters.addValue("studentName", student.getStudentName());
		parameters.addValue("studentId", student.getStudentId());
		parameters.addValue("exercises", student.getExercises());
		parameters.addValue("assignment1", student.getAssignment1());
		parameters.addValue("assignment2", student.getAssignment2());
		parameters.addValue("assignment3", student.getAssignment3());
		parameters.addValue("midterm", student.getMidterm());
		parameters.addValue("finalExam", student.getFinalExam());
		
		jdbc.update(query, parameters);
		
	}
	
	
	public void deleteStudent(int id) {
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		
		String query = "DELETE FROM Student WHERE id=:id";
		
		parameters.addValue("id", id);
		
		jdbc.update(query, parameters);
	}
	
	public void deleteStudentUserRole(long id) {
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		
		String query = "DELETE FROM user_role WHERE userId=:userId";
		
		parameters.addValue("userId", id);
		
		jdbc.update(query, parameters);
	}
	
	public void deleteStudentUser(long id) {
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		
		String query = "DELETE FROM sec_user WHERE userId=:userId";
		
		parameters.addValue("userId", id);
		
		jdbc.update(query, parameters);
	}
	
	public Student getStudentByName(String name) {
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "SELECT * FROM student WHERE studentname=:studentname";
		parameters.addValue("studentname", name);
		
		ArrayList<Student> students = 
				(ArrayList<Student>)jdbc.query(query, parameters,
				new BeanPropertyRowMapper<Student>(Student.class));
		
		if (students.size()>0)
			return students.get(0);
		return null;
	}
}
