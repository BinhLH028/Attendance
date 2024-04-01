package com.example.AttendanceApplication.Controller;

import com.example.AttendanceApplication.Request.SaveAttendanceRequest;
import com.example.AttendanceApplication.Service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/API/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping(value = "")
    public ResponseEntity<?> getAttendanceData(@RequestParam int cs) {
        try {
            return new ResponseEntity(attendanceService.getAttendanceData(cs), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(value = "/save")
    public ResponseEntity<?> saveAttendanceSesstion(@RequestParam int cs,
                                                    @RequestBody SaveAttendanceRequest request
    ) {
        try {
            return attendanceService.saveAttendanceSession(cs, request);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "")
    public ResponseEntity<?> activeAttendanceSession(@RequestParam int cs
    ) {
        try {
            return attendanceService.activeAttendanceSession(cs);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
