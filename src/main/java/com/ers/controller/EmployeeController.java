package com.ers.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.ers.dto.PasswordUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ers.model.Employee;
import com.ers.repository.EmployeeRepository;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class EmployeeController {
	
	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@GetMapping("/employees")
	public ResponseEntity<List<Employee>> getAllEmployees() {
		return new ResponseEntity<>(employeeRepository.findAll(), HttpStatus.OK);
	}

	@GetMapping("/employees/{empId}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable("empId") int empId) {
		Optional<Employee> empData = employeeRepository.findById(empId);
		if (empData.isPresent()) {
			return new ResponseEntity<>(empData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/managers/{empId}")
	public ResponseEntity<Employee> getManagerById(@PathVariable("empId") int empId) {
		Optional<Employee> empData = employeeRepository.findById(empId);
		if (empData.isPresent()) {
			return new ResponseEntity<>(empData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/{col3username}/{col4password}")
	public ResponseEntity <List<Employee>> findByUsername(@PathVariable("col3username") String col3username, @PathVariable("col4password")String col4password) {
		Optional<Employee> employee = employeeRepository.findByUsername(col3username);
		List<Employee> loginEmployee = new ArrayList<Employee>();
		if (col3username != null && employee.get().getCol4password().equals(col4password)) {
			loginEmployee.add(employee.get());
		}
		return new ResponseEntity <List<Employee>>(loginEmployee, HttpStatus.OK);
	}
	
	@PostMapping("/employees")
	public ResponseEntity<Employee> newEmployee(@RequestBody Map<String, Object> requestBody) {
		try {
			String titleString = requestBody.get("col5title").toString().toUpperCase();
			Employee.JobTitle jobTitle = Employee.JobTitle.valueOf(titleString);

			String encodedPassword = passwordEncoder.encode(requestBody.get("col4password").toString());

			Employee employee = new Employee(
				requestBody.get("col1fName").toString(),
				requestBody.get("col2lName").toString(),
				requestBody.get("col3username").toString(),
				encodedPassword,
				jobTitle
			);

			Employee _employee = employeeRepository.save(employee);
			return new ResponseEntity<>(_employee, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/employees/{empId}/verify-password")
	public ResponseEntity<Map<String, Boolean>> verifyPassword(@PathVariable int empId, @RequestBody Map<String, String> payload) {
		Employee employee = employeeRepository.findById(empId).orElseThrow(() -> new RuntimeException("Employee not found"));

		boolean valid = passwordEncoder.matches(payload.get("currentPassword"), employee.getCol4password());

		return ResponseEntity.ok(Map.of("valid", valid));
	}
	
	@PutMapping("/manager/{empId}/job-title")
	public ResponseEntity<Employee> updateJobTitle(@PathVariable("empId") int empId, @RequestBody Map<String, String> request) {
		Optional<Employee> optionalEmployee = employeeRepository.findById(empId);

		Employee employee = optionalEmployee.get();
		String newJobTitleStr = request.get("col5title");

		Employee.JobTitle newJobTitle = Employee.JobTitle.valueOf(newJobTitleStr.toUpperCase());
		employee.setCol5title(newJobTitle);
		return new ResponseEntity<>(employeeRepository.save(employee), HttpStatus.OK);

	}

	@PutMapping("/employees/{empId}")
	public ResponseEntity<?> updatePassword(@PathVariable int empId, @RequestBody PasswordUpdateRequest request) {
		Employee employee = employeeRepository.findById(empId).orElse(null);
		if (employee == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Employee not found"));
		}

		// Validate current password
		if (!passwordEncoder.matches(request.getCurrentPassword(), employee.getCol4password())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Current password is incorrect"));
		}

		// Check if new password is the same as the old one
		if (passwordEncoder.matches(request.getNewPassword(), employee.getCol4password())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "New password cannot be the same as the current password"));
		}

		// Hash and update the new password
		employee.setCol4password(passwordEncoder.encode(request.getNewPassword()));
		employeeRepository.save(employee);

		return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
	}
	
	@DeleteMapping("employees/{empId}")
	public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable("empId") int empId) {
		try {
			employeeRepository.deleteById(empId);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("employees")
	public ResponseEntity<HttpStatus> deleteAllEmployee() {
		try {
			employeeRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
