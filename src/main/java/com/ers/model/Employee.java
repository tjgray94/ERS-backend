package com.ers.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Employee {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int empId;

	@Column(name = "firstName", length = 20)
	private String col1fName;

	@Column(name = "lastName", length = 20)
	private String col2lName;

	@Column(name = "username")
	private String col3username;

	@Column(name = "password")
	private String col4password;

	@Column(name = "title")
	@Enumerated(EnumType.STRING)
	private JobTitle col5title;

	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<Reimbursement> reimbursements = new ArrayList<>();

	public Employee(String col1fName, String col2lName, String col3username, String col4password, JobTitle col5title) {
		this.col1fName = col1fName;
		this.col2lName = col2lName;
		this.col3username = col3username;
		this.col4password = col4password;
		this.col5title = col5title;
	}

	public enum JobTitle {
		MANAGER,
		EMPLOYEE
	}
}
