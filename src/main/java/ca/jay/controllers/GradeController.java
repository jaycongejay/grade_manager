package ca.jay.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.jay.beans.Role;
import ca.jay.beans.Student;
import ca.jay.database.DatabaseAccess;

@Controller
public class GradeController {
	
	@Autowired
	@Lazy
	protected DatabaseAccess da;

	
	@GetMapping("/")
	public String home() {
		
		return "index.html";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login.html";
	}
	
	@GetMapping("/access-denied")
	public String accessDenied() {
		return "/error/access-denied.html";
	}
	
	@GetMapping("/user/professor")
	public String gosProfessorHome() {
		return "/user/professor/professor.html";
	}
	@GetMapping("/user/student")
	public String goStudentHome() {
		return "/user/student/student.html";
	}
	
	@GetMapping("/register")
	public String goRegistration() {
		return "/registration.html";
	}
	
	@PostMapping("/register")
	public String ProcessRegistration(@RequestParam String name,
									  @RequestParam String password
									  ) {
		
		
		da.createNewUser(name, password);
		
		long userId = da.findUserAccount(name).getUserId();
		
		da.addRole(userId, 1);
			
		return "redirect:/";
	}
	
/* ----------------------------------------------------------------------------------*/	
	
	
	
	@GetMapping("/add")
	public String add(Model model) {
		
		model.addAttribute("student", new Student());
		
		return "add.html";
	}
	
	@GetMapping("/save")
	public String save(Model model, @ModelAttribute Student student) {
		
		try {
			// Create new student user and add role
			da.createNewUser(student.getStudentName(), student.getStudentId());
			
			da.addStudent(student); // Create new student record
			
		}catch(Exception e) {
			return "/error/usercreate-denied.html";
		}
		
		long newUserId = da.findUserAccount(student.getStudentName()).getUserId();
		
		da.addRole(newUserId, 2);
		
		
		model.addAttribute("student", new Student());
		
		return "add.html";
	}
	
	@GetMapping("/view")
	public String veiwContacts(Model model, Authentication auth) {
		

		// Get the current user name logged in
		ca.jay.beans.User currentUser = da.findUserAccount(auth.getName());
		List<String> roles = da.getRolesById(currentUser.getUserId());
		
		ArrayList<Student> showStudents = new ArrayList<Student>();
		
		
		if(roles.get(0).equals("ROLE_PROFESSOR")) {
			
			for(Student s: da.getStudents()) {
				showStudents.add(s);
			}
		}
		else {
			
			Student studentRecordOfcurrentUser = da.getStudentByName(auth.getName());
			
			showStudents.add(studentRecordOfcurrentUser);
		}

		
		for(Student s: showStudents) {
			s.calWeightedAvg();
			s.calLetterGrade();
		}
		
		// Current user role: e.g [ROLE_PROFESSOR]
		Role currentUserRole = new Role();
		currentUserRole.setRoleName(auth.getAuthorities().toString());
		
		// get professor's class avg
		Student avgEachCategory = new Student();
		
		// calculate avg for each category (All in class)
		calcAvgCategory(showStudents, avgEachCategory);
		
		model.addAttribute("students", showStudents);
		model.addAttribute("evgEachCategory", avgEachCategory);
		model.addAttribute("currentUserRole", currentUserRole);
		
		
		return "viewStudents.html";
	}
	
	
	private void calcAvgCategory(ArrayList<Student> showStudents, Student avgEachCategory) {
		
		double sumExercies = 0;
		double sumA1 = 0;
		double sumA2 = 0;
		double sumA3 = 0;
		double sumMidterm = 0;
		double sumFinal = 0;
		double sumWeighedAvg = 0;
		int count = 0;
		for(Student s: showStudents) {
			sumExercies += s.getExercises();
			sumA1 += s.getAssignment1();
			sumA2 += s.getAssignment2();
			sumA3 += s.getAssignment3();
			sumMidterm += s.getMidterm();
			sumFinal += s.getFinalExam();
			sumWeighedAvg += s.getWeightedAvg();
			count++;
		}
		
		avgEachCategory.setExercises(Double.parseDouble(String.format("%.1f", sumExercies / count))); 
		avgEachCategory.setAssignment1(Double.parseDouble(String.format("%.1f", sumA1 / count))); 
		avgEachCategory.setAssignment2(Double.parseDouble(String.format("%.1f", sumA2 / count))); 
		avgEachCategory.setAssignment3(Double.parseDouble(String.format("%.1f", sumA3 / count))); 
		avgEachCategory.setMidterm(Double.parseDouble(String.format("%.1f", sumMidterm / count))); 
		avgEachCategory.setFinalExam(Double.parseDouble(String.format("%.1f", sumFinal / count))); 
		avgEachCategory.setWeightedAvg(Double.parseDouble(String.format("%.1f", sumWeighedAvg / count))); 
		
	}
	
	@GetMapping("/edit/{id}")
	public String goEditPage(@PathVariable int id, Model model) {
		
		
		Student student = da.getStudentById(id);
		
		model.addAttribute("editStudent", student);
		
		return "modifyStudent.html";
	}
	
	@GetMapping("/modify")
	public String edit(@ModelAttribute Student student) {
		
		da.editStudent(student);
		student.calWeightedAvg(); // update avg
		student.calLetterGrade(); // update letterGrade
		
		return "redirect:/view";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable int id, Model model) {
		
		
		
		// Get user info
		Student selectedStudent = da.getStudentById(id);
		
		ca.jay.beans.User selectedUser 
									= da.findUserAccount(selectedStudent.getStudentName());
		
		
		// Delete from user_role table
		da.deleteStudentUserRole(selectedUser.getUserId());
		// Delete from sec_user table
		da.deleteStudentUser(selectedUser.getUserId());
		
		// Delete from student table
		da.deleteStudent(id);
		
		return "redirect:/view";
	}
	
}
