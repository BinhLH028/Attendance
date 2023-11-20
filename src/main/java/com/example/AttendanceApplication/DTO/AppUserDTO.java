package com.example.AttendanceApplication.DTO;

import com.example.AttendanceApplication.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDTO {
    private Integer userId;

    private String userName;

    private String email;

    private Integer age;

    private Role role;

//    @Column(name = "gender")
//    private Integer gender;

//    @Column(name = "playlist_id")
//    private Integer playlistId;
}
