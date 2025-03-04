package com.ers.service;

import com.ers.dto.SignupRequest;
import com.ers.model.Employee;

public interface AuthService {
    Employee createEmployee(SignupRequest signupRequest);
}
