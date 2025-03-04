package com.ers.service.jwt;

import com.ers.model.Employee;
import com.ers.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class EmployeeServiceImpl implements UserDetailsService {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String col3username) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByCol3username(col3username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username " + col3username));
        return new org.springframework.security.core.userdetails.User(employee.getCol3username(),
                employee.getCol4password(), Collections.emptyList());
    }
}
