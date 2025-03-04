package com.ers.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "reimbursements")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Reimbursement {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int reimbId;

	@ManyToOne
	@JoinColumn(name = "empId")
	@JsonBackReference
	private Employee employee;

	@Column(name = "amount")
	private double col2amount;

	@Column(name = "reason")
	private String col3reason;

	@Column(name = "status")
	private String col4status;
	
	public Reimbursement(Employee employee, double col2amount, String col3reason, String col4status) {
		this.employee = employee;
		this.col2amount = col2amount;
		this.col3reason = col3reason;
		this.col4status = col4status;
	}

	public int getEmpId() {
		return employee != null ? employee.getEmpId() : 0;
	}
}
