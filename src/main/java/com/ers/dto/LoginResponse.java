package com.ers.dto;

import lombok.Getter;

@Getter
public class LoginResponse {
    private String jwt;
    private int empId;
    private String col5title;

    public LoginResponse(String jwt, int empId, String col5title) {
        this.jwt = jwt;
        this.empId = empId;
        this.col5title = col5title;
    }
}
