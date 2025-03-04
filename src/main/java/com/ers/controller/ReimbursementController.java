package com.ers.controller;

import java.util.List;
import java.util.Optional;

import com.ers.model.Employee;
import com.ers.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ers.model.Reimbursement;
import com.ers.repository.ReimbursementRepository;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class ReimbursementController {
	
	@Autowired
	private ReimbursementRepository reimbursementRepository;

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@GetMapping("/reimbursements")
	public ResponseEntity<List<Reimbursement>> getAllReimbursements() {
		List<Reimbursement> reimbursements = reimbursementRepository.findAll();

		for (Reimbursement r : reimbursements) {
			System.out.println("Reimb ID: " + r.getReimbId() + ", Employee ID: " +
					(r.getEmployee() != null ? r.getEmployee().getEmpId() : "NULL"));
		}
		return new ResponseEntity<>(reimbursements, HttpStatus.OK);
	}

	@GetMapping("/reimbursements/employee/{empId}")
	public ResponseEntity<List<Reimbursement>> getReimbursementByEmpId(@PathVariable("empId") int empId) {
		Optional<Employee> employeeOpt = employeeRepository.findById(empId);

		if (employeeOpt.isPresent()) {
			Employee employee = employeeOpt.get();
			List<Reimbursement> reimbursements = reimbursementRepository.findByEmployee(employee);
			return new ResponseEntity<>(reimbursements, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/reimbursements/{reimbId}")
	public ResponseEntity<Reimbursement> getReimbursementById(@PathVariable("reimbId") int reimbId) {
		Optional<Reimbursement> reimbData = reimbursementRepository.findById(reimbId);
		if (reimbData.isPresent()) {
			return new ResponseEntity<>(reimbData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/reimbursements")
	public ResponseEntity<Reimbursement> newReimbursement(@RequestBody Reimbursement reimbursement) {
		try {
			Reimbursement _reimbursement = reimbursementRepository.save(reimbursement);
			return new ResponseEntity<>(_reimbursement, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/reimbursements/{reimbId}")
	public ResponseEntity<Reimbursement> updateReimbursement(@PathVariable int reimbId, @RequestBody Reimbursement reimbursement) {
		Optional<Reimbursement> reimbData = reimbursementRepository.findById(reimbId);

		if (reimbData.isPresent()) {
			Reimbursement _reimbursement = reimbData.get();

			if (reimbursement.getEmployee() != null && reimbursement.getEmployee().getEmpId() > 0) {
				Optional<Employee> employeeOpt = employeeRepository.findById(reimbursement.getEmployee().getEmpId());
				if (employeeOpt.isPresent()) {
					_reimbursement.setEmployee(employeeOpt.get());
				}
			}

			_reimbursement.setCol2amount(reimbursement.getCol2amount());
			_reimbursement.setCol3reason(reimbursement.getCol3reason());
			_reimbursement.setCol4status(reimbursement.getCol4status());
			return new ResponseEntity<>(reimbursementRepository.save(_reimbursement), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/reimbursements/{reimbId}")
	public ResponseEntity<HttpStatus> deleteReimbursement(@PathVariable("reimbId") int reimbId) {
		try {
			reimbursementRepository.deleteById(reimbId);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("reimbursements")
	public ResponseEntity<HttpStatus> deleteAllReimbursements() {
		try {
			reimbursementRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
