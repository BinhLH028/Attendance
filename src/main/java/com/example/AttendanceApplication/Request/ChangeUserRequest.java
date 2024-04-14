package com.example.AttendanceApplication.Request;

import lombok.Data;

@Data
public class ChangeUserRequest {

    private Integer userId;

    private String userName;

    private String oldPassword;

    private String newPassword;
}
