package com.ers.service;

import com.ers.dto.SignupRequest;
import com.ers.model.Employee;
import com.ers.repository.EmployeeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Employee createEmployee(SignupRequest signupRequest) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(signupRequest, employee);

        // Hash the password before saving
        String hashPassword = passwordEncoder.encode(signupRequest.getCol4password());
        employee.setCol4password(hashPassword);
        Employee createdEmployee = employeeRepository.save(employee);
        employee.setEmpId(createdEmployee.getEmpId());
        return employee;
    }
}
