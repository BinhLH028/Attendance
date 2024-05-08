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

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private List<AttendanceSheet> absenceList = new ArrayList<>();

//    private Map<Integer,AttendanceSheet> mapAttendance = new HashMap<>();

    private List<AttendanceDataDTO> listAttendanceData = new ArrayList<>();


    public ResponseEntity getAttendanceData(int cs) {
        listAttendanceData.clear();
        if (validateCourse(cs)){
            studentEnrolledSet.forEach(enroll -> {
                Student student = enroll.getStudent();
                AttendanceSheet temp = attendanceRepository.findSheetById(enroll.getId());
                String date = new SimpleDateFormat("dd/MM/yyyy").format(student.getDob());
                if (temp != null) {
                    AttendanceDataDTO data = new AttendanceDataDTO(student.getUserId(), student.getUsercode(),
                            student.getUsername(), date, temp.getId(), csData.getTeam(), temp);
                    listAttendanceData.add(data);
                }
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
            msg = messageSource.getMessage("C03",
                    new String[]{String.valueOf(cs)}, Locale.getDefault());
            isValid = false;
            return isValid;
        }
        studentEnrolledSet = csData.getStudentEnrolleds();
        if (studentEnrolledSet.size() <= 0) {
            msg = messageSource.getMessage("A01",
                    new String[]{String.valueOf(cs)}, Locale.getDefault());
            isValid = false;
            return isValid;
        }
        return isValid;
    }

    public ResponseEntity saveAttendanceSession(int cs, SaveAttendanceRequest request) {
        int lectureNum = request.getLectureNum();

        attendanceSheetList.clear();
        absenceList.clear();

        // combination of QRs and edit
        List<Integer> combinedList = Stream.concat(request.getListStudentId().stream(),
                        request.getListEditUserAttends().stream())
                .collect(Collectors.toList());

        List<Integer> absenceLists = csRepo.findStudentsNotIn(combinedList, cs);

        if ((request.getListStudentId() == null ||
                request.getListStudentId().size() == 0 ) &&
                request.getListEditUserAttends().size() == 0
        ) {
            return new ResponseEntity("No student scan QR", HttpStatus.BAD_REQUEST);
        }

        if (validateCourse(cs)){
            // the students that attend
            combinedList.forEach(student -> {
                AttendanceSheet temp = attendanceRepository.findSheetByStudentIdAndCSId(student,cs);
                if (temp != null) {
                    getLecture(true, lectureNum, temp);
                    attendanceSheetList.add(temp);
                }
            });
            // the students that don't attend
            absenceLists.forEach(student -> {
                AttendanceSheet temp = attendanceRepository.findSheetByStudentIdAndCSId(student,cs);
                if (temp != null) {
                    getLecture(false, lectureNum, temp);
                    attendanceSheetList.add(temp);
                }
            });
            // list that edit by hand

            // if the session is open by teacher
            if (csData.isEnableAttendance())
                attendanceRepository.saveAll(attendanceSheetList);
            else {
                msg = messageSource.getMessage("A03",
                        new String[]{}, Locale.getDefault());
                return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
            }
            msg = messageSource.getMessage("A02",
                    new String[]{String.valueOf(lectureNum), csData.getCourse().getCourseCode()}, Locale.getDefault());

            changeAttendanceStatus(false);
            return new ResponseEntity(msg, HttpStatus.OK);
        }
        return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<?> activeAttendanceSession(int cs, int lec) {
        msg = "";
        if (validateCourse(cs)){
            if (csData.getStartWeek() <= lec) {
                changeAttendanceStatus(true);
            } else {
                msg = messageSource.getMessage("A04",
                        new String[]{String.valueOf(lec),
                                String.valueOf(csData.getStartWeek())}, Locale.getDefault());
                return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity(msg, HttpStatus.OK);
        }
        return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
    }

    private void changeAttendanceStatus(boolean enable) {
        csData.setEnableAttendance(enable);
        csRepo.save(csData);
    }

    private void getLecture(boolean isAttend, int lectureNum, AttendanceSheet temp) {
        switch (lectureNum) {
            case 1:
                temp.setLecture1(isAttend);
                break;
            case 2:
                temp.setLecture2(isAttend);
                break;
            case 3:
                temp.setLecture3(isAttend);
                break;
            case 4:
                temp.setLecture4(isAttend);
                break;
            case 5:
                temp.setLecture5(isAttend);
                break;
            case 6:
                temp.setLecture6(isAttend);
                 break;
            case 7:
                temp.setLecture7(isAttend);
                 break;
            case 8:
                temp.setLecture8(isAttend);
                 break;
            case 9:
                temp.setLecture9(isAttend);
                 break;
            case 10:
                temp.setLecture10(isAttend);
                 break;
            case 11:
                temp.setLecture11(isAttend);
                 break;
            case 12:
                temp.setLecture12(isAttend);
                 break;
            case 13:
                temp.setLecture13(isAttend);
                 break;
            case 14:
                temp.setLecture14(isAttend);
                 break;
            case 15:
                temp.setLecture15(isAttend);
                 break;
            case 16:
                temp.setLectureOption1(isAttend);
                 break;
            case 17:
                temp.setLectureOption2(isAttend);
                 break;
        }
    }


}
