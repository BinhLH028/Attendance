package com.example.AttendanceApplication.Request;

import com.example.AttendanceApplication.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    private int userId;

    private Role role;

}
