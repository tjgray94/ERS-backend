package com.ers.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ers.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
	List<Employee> findByCol5title(String col5title);
	List<Employee> findByCol1fName(String col1fName);
	List<Employee> findByCol2lName(String col2lName);
	
	@Query(value = "SELECT * FROM employees WHERE username = :col3username", nativeQuery = true)
	Optional<Employee> findByUsername(@Param ("col3username")String col3username);
	
	Optional<Employee> findByCol3username(String col3username);
}
