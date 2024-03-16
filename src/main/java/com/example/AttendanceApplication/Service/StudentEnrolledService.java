package com.example.AttendanceApplication.Service;

import com.example.AttendanceApplication.Exception.BreakException;
import com.example.AttendanceApplication.Model.AttendanceSheet;
import com.example.AttendanceApplication.Model.Relation.CourseSection;
import com.example.AttendanceApplication.Model.Relation.StudentEnrolled;
import com.example.AttendanceApplication.Model.Student;
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

    private CourseSection courseSection;
    private Set<Student> studentSet = new HashSet<>();
    private Set<StudentEnrolled> studentEnrolledSet = new HashSet<>();
    private List<AttendanceSheet> attendanceSheetList = new ArrayList<>();

    public StudentEnrolledService() {
    }

    public ResponseEntity<?> addNewEnrolls(EnrollRequest request) {

        if (validateRequest(request)){
            studentSet.forEach(student -> {
                StudentEnrolled enroll = enrollRepo.findByStudentIdAndCSId(student.getUserId(),
                        courseSection.getId());
                if (enroll == null) {

                    enroll = new StudentEnrolled(student,courseSection);
                    System.out.println(student.getUserId());
                    studentEnrolledSet.add(enroll);
                    updateStudent(student,enroll);
                }
            });

            createAttendanceDataOnEnroll(studentEnrolledSet);
            enrollRepo.saveAll(studentEnrolledSet);
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
}
