package com.ers.controller;

import com.ers.dto.LoginRequest;
import com.ers.dto.LoginResponse;
import com.ers.model.Employee;
import com.ers.repository.EmployeeRepository;
import com.ers.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/login")
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private EmployeeRepository employeeRepository;
    private final JwtUtil jwtUtil;
    @Autowired
    public LoginController(AuthenticationManager authenticationManager, EmployeeRepository employeeRepository, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.employeeRepository = employeeRepository;
        this.jwtUtil = jwtUtil;
    }
    @PostMapping
    public LoginResponse login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) throws IOException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getCol3username(), loginRequest.getCol4password()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect username or password.");
        } catch (DisabledException disabledException) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User is not activated");
            return null;
        }

        Optional<Employee> employeeList = employeeRepository.findByUsername(loginRequest.getCol3username());
        Employee employee = employeeList.get();
        final String jwt = jwtUtil.generateToken(employee.getCol3username());
        return new LoginResponse(jwt, employee.getEmpId(), employee.getCol5title().toString());
    }
}
