package com.example.AttendanceApplication.DTO;

import com.example.AttendanceApplication.Enum.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDTO {

    private Integer userId;

    private String userName;

    @JsonIgnore
    private String oldPassword;

    @JsonIgnore
    private String newPassword;

    private String email;

    private Integer age;

    private Role role;

//    @Column(name = "gender")
//    private Integer gender;

    public AppUserDTO(Integer userId, String userName, String email, Integer age, Role role) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.age = age;
        this.role = role;
    }

    public AppUserDTO(Integer userId, String oldPassword, String newPassword) {
        this.userId = userId;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
