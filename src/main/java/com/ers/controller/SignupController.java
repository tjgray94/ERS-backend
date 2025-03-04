package com.ers.controller;

import com.ers.dto.SignupRequest;
import com.ers.model.Employee;
import com.ers.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/signup")
public class SignupController {
    private final AuthService authService;
    @Autowired
    public SignupController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest) {
        Employee createdEmployee = authService.createEmployee(signupRequest);
        if (createdEmployee != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create employee");
        }
    }
}
