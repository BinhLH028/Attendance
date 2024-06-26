package com.example.AttendanceApplication.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveAttendanceRequest {

    private int lectureNum;
    private List<Integer> listStudentId;
    private List<Integer> listEditUserAttends;

    public SaveAttendanceRequest(int lectureNum, List<Integer> listStudentId) {
        this.lectureNum = lectureNum;
        this.listStudentId = listStudentId;
    }
}
