package com.example.AttendanceApplication.Service;

import com.example.AttendanceApplication.Model.AttendanceSheet;
import com.example.AttendanceApplication.Model.Relation.CourseSection;
import com.example.AttendanceApplication.Model.Relation.StudentEnrolled;
import com.example.AttendanceApplication.Model.Relation.TeacherTeach;
import com.example.AttendanceApplication.Model.Student;
import com.example.AttendanceApplication.Model.Teacher;
import com.example.AttendanceApplication.Repository.AttendanceRepository;
import com.example.AttendanceApplication.Repository.CourseSectionRepository;
import com.example.AttendanceApplication.Repository.StudentEnrolledRepository;
import com.example.AttendanceApplication.Repository.StudentRepository;
import com.example.AttendanceApplication.Request.EnrollRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentEnrolledService {

    @Autowired
    private StudentEnrolledRepository enrollRepo;

    @Autowired
    private CourseSectionRepository csRepo;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    MessageSource messageSource;
    private String msg = "";
    private List<String> resultMsg = new ArrayList<>();
    private CourseSection courseSection;
    private Set<Student> studentSet = new HashSet<>();
    private Set<StudentEnrolled> studentEnrolledSet = new HashSet<>();
    private List<AttendanceSheet> attendanceSheetList = new ArrayList<>();

    public StudentEnrolledService() {
    }

    public ResponseEntity<?> addNewEnrolls(EnrollRequest request) {
        clearData();
        //TODO: enroll for multi team
        if (validateRequest(request)){
            studentSet.forEach(student -> {
                StudentEnrolled enroll = enrollRepo.findByStudentIdAndCSId(student.getUserId(),
                        courseSection.getId());
                if (enroll == null) {

                    enroll = enrollTeam(student);
                    updateStudent(student,enroll);

//                    if (request.getTeam().equals("CL")) {
//                        //TODO: enroll CL
//                    }
                }
            });

            // Create Attendance Data
            createAttendanceDataOnEnroll(studentEnrolledSet);
            enrollRepo.saveAll(studentEnrolledSet);
            // Update courseSection
            courseSection.setStudentEnrolleds(studentEnrolledSet);
            csRepo.save(courseSection);

            studentRepository.saveAll(studentSet);

            msg = messageSource.getMessage("SE01",
                    new String[]{courseSection.getCourse().getCourseCode(),
                                courseSection.getSection().getSemester().toString(),
                                courseSection.getSection().getYear().toString()
                    }, Locale.getDefault());
            return new ResponseEntity(msg, HttpStatus.OK);
        }
        return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
    }

    private void clearData() {
        studentSet.clear();
        studentEnrolledSet.clear();
        attendanceSheetList.clear();
    }

    private StudentEnrolled enrollTeam(Student student) {
        StudentEnrolled enroll = new StudentEnrolled(student,courseSection);
        studentEnrolledSet.add(enroll);
        return enroll;
    }

    private void createAttendanceDataOnEnroll(Set<StudentEnrolled> studentEnrolledSet) {
        studentEnrolledSet.forEach(enroll -> {
            AttendanceSheet data = new AttendanceSheet();
            data.setStudentEnrolled(enroll);
            enroll.setAttendanceSheet(data);
            attendanceSheetList.add(data);
        });
        attendanceRepository.saveAll(attendanceSheetList);
    }

    private void updateStudent(Student student, StudentEnrolled enroll) {
        var temp = student.getStudentEnrolled();
        temp.add(enroll);
        student.setStudentEnrolled(temp);
    }

    private boolean validateRequest(EnrollRequest request) {
        boolean isValid = true;
        courseSection = csRepo.findbyCSId(request.getCourseSectionId());

        request.getStudentIds().forEach(id -> {
            Student student = studentRepository.findStudentByStudentId(id);
            if (student == null) {
                msg = messageSource.getMessage("ST01",
                        new String[]{id.toString()}, Locale.getDefault());
            }
            studentSet.add(student);

        });

        return isValid;
    }

    public ResponseEntity<?> getStudentListByCSId(int id) {
        List<Student> students = enrollRepo.findStudentsByCSId(id);
        if (students.isEmpty()) {
            msg = messageSource.getMessage("SE02", null, Locale.getDefault());
            return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(students, HttpStatus.OK);
    }

    public ResponseEntity<?> updateEnroll(EnrollRequest request) {
        clearData();
        courseSection = csRepo.findbyCSId(request.getCourseSectionId());

        List<Integer> listTt = courseSection.getStudentEnrolleds()
                .stream()
                .map(en -> en.getStudent().getUserId())
                .collect(Collectors.toList());

        // filter remove old assign
        List<Integer> removeEnroll = listTt.stream()
                .filter(e -> !request.getStudentIds().contains(e))
                .collect(Collectors.toList());
        // filter new assign
        List<Integer> newEnroll = request.getStudentIds().stream()
                .filter(e -> !listTt.contains(e))
                .collect(Collectors.toList());

        updateEnrollInfo(request, newEnroll, removeEnroll);

        msg = messageSource.getMessage("TT05",
                new String[]{courseSection.getCourse().getCourseCode(),
                        courseSection.getTeam(),
                        courseSection.getSection().getSemester().toString(),
                        courseSection.getSection().getYear().toString()
                }, Locale.getDefault());

        return new ResponseEntity(msg, HttpStatus.OK);
    }

    private void updateEnrollInfo(EnrollRequest request, List<Integer> newEnroll, List<Integer> removeEnroll) {
        createEnrolls(request, newEnroll);
        deleteEnroll(removeEnroll);
        saveEnrolls(request);
    }

    private void saveEnrolls(EnrollRequest request) {
        studentSet.forEach(student -> {
            StudentEnrolled enroll = enrollRepo.findByStudentIdAndCSId(student.getUserId(),
                    courseSection.getId());
            if (enroll == null) {

                enroll = new StudentEnrolled(student, courseSection);
                System.out.println(student.getUserId());
                studentEnrolledSet.add(enroll);
                updateStudent(student, enroll);
            }
        });

        enrollRepo.saveAll(studentEnrolledSet);
        courseSection.setStudentEnrolleds(studentEnrolledSet);
        csRepo.save(courseSection);
        studentRepository.saveAll(studentSet);
    }

    private void createEnrolls(EnrollRequest request, List<Integer> newEnroll) {
        newEnroll.forEach(id -> {
            StudentEnrolled en = enrollRepo.findByStudentIdAndCSId(id, request.getCourseSectionId());
            Student student = studentRepository.findStudentByStudentId(id);
            if (student != null && en == null) {
                msg = messageSource.getMessage("TT01",
                        new String[]{id.toString()}, Locale.getDefault());
                studentSet.add(student);
            } else {
                msg = messageSource.getMessage("TT03",
                        new String[]{student.getUsername(),
                                courseSection.getCourse().getCourseName(),
                                courseSection.getTeam()}, Locale.getDefault());
            }
        });
    }


    public ResponseEntity<?> deleteEnroll(List<Integer> request) {
        resultMsg.clear();
        List<StudentEnrolled> enrollDb = enrollRepo.findByIdInAndDelFlagFalse(request);
        if (enrollDb.size() > 0) {
            enrollDb.stream().forEach(e -> {
                e.delFlag = true;
                msg = messageSource.getMessage("SE03",
                        new String[]{e.getStudent().getUsername()}, Locale.getDefault());
                resultMsg.add(msg);
            });
            return new ResponseEntity(resultMsg, HttpStatus.OK);
        }
        return new ResponseEntity("Error removing teacher assign", HttpStatus.BAD_REQUEST);
    }

}
