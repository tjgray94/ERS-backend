package com.ers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.ers.model.Employee;
import com.ers.repository.EmployeeRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class EmployeeRepositoryTests {
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private EmployeeRepository repo;
	
@Test
public void testCreateEmployee() {
	Employee employee = new Employee();
	employee.setCol1fName("Jacob");
	employee.setCol2lName("Beasley");
	employee.setCol3username("jbeasley07");
	employee.setCol4password("jbeast1010");
	employee.setCol5title("Employee");
	
	Employee savedEmployee = repo.save(employee);
	Employee existEmployee = entityManager.find(Employee.class, savedEmployee.getEmpId());
	
	assertThat(employee.getCol3username()).isEqualTo(existEmployee.getCol3username());
}

}
