package com.example.AttendanceApplication.Service;

import com.example.AttendanceApplication.DTO.AttendanceDataDTO;
import com.example.AttendanceApplication.Model.AttendanceSheet;
import com.example.AttendanceApplication.Model.Relation.CourseSection;
import com.example.AttendanceApplication.Model.Relation.StudentEnrolled;
import com.example.AttendanceApplication.Model.Student;
import com.example.AttendanceApplication.Repository.AttendanceRepository;
import com.example.AttendanceApplication.Repository.CourseSectionRepository;
import com.example.AttendanceApplication.Repository.StudentEnrolledRepository;
import com.example.AttendanceApplication.Request.SaveAttendanceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private CourseSectionRepository csRepo;

    @Autowired
    private StudentEnrolledRepository enrollRepo;

    @Autowired
    MessageSource messageSource;

    private String msg = "";
    private CourseSection csData;
    private Set<StudentEnrolled> studentEnrolledSet = new HashSet<>();

    private List<AttendanceSheet> attendanceSheetList = new ArrayList<>();

//    private Map<Integer,AttendanceSheet> mapAttendance = new HashMap<>();

    private List<AttendanceDataDTO> listAttendanceData = new ArrayList<>();


    public ResponseEntity getAttendanceData(int cs) {
        listAttendanceData.clear();
        if (validateCourse(cs)){
            studentEnrolledSet.forEach(enroll -> {
                Student student = enroll.getStudent();
                AttendanceSheet temp = attendanceRepository.findSheetById(enroll.getId());
                String date = new SimpleDateFormat("dd/MM/yyyy").format(student.getAppUser().getDob());
                AttendanceDataDTO data = new AttendanceDataDTO(student.getUserId(),student.getUsercode(),
                        student.getUserName(),date, temp.getId(), temp);
//                attendanceSheetList.add(temp);
//                mapAttendance.put(enroll.getStudent().getUserId(),temp);
                listAttendanceData.add(data);
            });
            listAttendanceData.sort(Comparator.comparing(AttendanceDataDTO::getUserCode));
            return new ResponseEntity(listAttendanceData, HttpStatus.OK);
        }
        return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);

    }

    private boolean validateCourse(int cs) {
        boolean isValid = true;
        csData = csRepo.findbyCSId(cs);
        if (csData == null) {
            msg = messageSource.getMessage("15",
                    new String[]{String.valueOf(cs)}, Locale.getDefault());
            isValid = false;
            return isValid;
        }
        studentEnrolledSet = csData.getStudentEnrolleds();
        if (studentEnrolledSet.size() <= 0) {
            msg = messageSource.getMessage("16",
                    new String[]{String.valueOf(cs)}, Locale.getDefault());
            isValid = false;
            return isValid;
        }
        return isValid;
    }

    public ResponseEntity saveAttendanceSesstion(int cs, SaveAttendanceRequest request) {
        int lectureNum = request.getLectureNum();
        attendanceSheetList.clear();

        if (validateCourse(cs)){
            request.getListStudentId().forEach(student -> {
                AttendanceSheet temp = attendanceRepository.findSheetByStudentId(student);
                getLecture(lectureNum,temp);
                attendanceSheetList.add(temp);
            });
            if (csData.isEnableAttendance())
                attendanceRepository.saveAll(attendanceSheetList);
            else {
                msg = messageSource.getMessage("18",
                        new String[]{}, Locale.getDefault());
                return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
            }
            msg = messageSource.getMessage("17",
                    new String[]{String.valueOf(lectureNum), csData.getCourse().getCourseCode()}, Locale.getDefault());

            changeAttendanceStatus(false);
            return new ResponseEntity(msg, HttpStatus.OK);
        }
        return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<?> activeAttendanceSession(int cs) {
        msg = "";
        if (validateCourse(cs)){
            changeAttendanceStatus(true);
            return new ResponseEntity(msg, HttpStatus.OK);
        }
        return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
    }

    private void changeAttendanceStatus(boolean enable) {
        csData.setEnableAttendance(enable);
        csRepo.save(csData);
    }

    private void getLecture(int lectureNum, AttendanceSheet temp) {
        switch (lectureNum) {
            case 1:
                temp.setLecture1(true);
                break;
            case 2:
                temp.setLecture2(true);
                break;
            case 3:
                temp.setLecture3(true);
                break;
            case 4:
                temp.setLecture4(true);
                break;
            case 5:
                temp.setLecture5(true);
                break;
            case 6:
                temp.setLecture6(true);
                 break;
            case 7:
                temp.setLecture7(true);
                 break;
            case 8:
                temp.setLecture8(true);
                 break;
            case 9:
                temp.setLecture9(true);
                 break;
            case 10:
                temp.setLecture10(true);
                 break;
            case 11:
                temp.setLecture11(true);
                 break;
            case 12:
                temp.setLecture12(true);
                 break;
            case 13:
                temp.setLecture13(true);
                 break;
            case 14:
                temp.setLecture14(true);
                 break;
            case 15:
                temp.setLecture15(true);
                 break;
//            case 16:
//                temp.setLectureOption1(true);
//                 break;
//            case 17:
//                temp.setLectureOption2(true);
//                 break;
        }
    }


}
