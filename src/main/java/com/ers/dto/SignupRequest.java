package com.ers.dto;

import com.ers.model.Employee;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {

    private String col1fName;
    private String col2lName;
    private String col3username;
    private String col4password;
    private Employee.JobTitle col5title;
}
