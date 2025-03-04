package com.ers.repository;

import java.util.List;

import com.ers.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ers.model.Reimbursement;

public interface ReimbursementRepository extends JpaRepository<Reimbursement, Integer> {
	List<Reimbursement> findByEmployee(Employee employee);
	List<Reimbursement> findByCol4status(String col4status);
}
