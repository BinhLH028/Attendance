package com.example.AttendanceApplication.Service;

import com.example.AttendanceApplication.Exception.BreakException;
import com.example.AttendanceApplication.Model.Relation.CourseSection;
import com.example.AttendanceApplication.Model.Relation.TeacherTeach;
import com.example.AttendanceApplication.Model.Teacher;
import com.example.AttendanceApplication.Repository.CourseSectionRepository;
import com.example.AttendanceApplication.Repository.TeacherRepository;
import com.example.AttendanceApplication.Repository.TeacherTeachRepository;
import com.example.AttendanceApplication.Request.AssignClassRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TeacherTeachService {

    @Autowired
    private CourseSectionRepository csRepo;
    @Autowired
    private TeacherTeachRepository ttRepo;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    MessageSource messageSource;

    private String msg = "";
    private CourseSection courseSection;
    private Set<Teacher> teacherSet = new HashSet<>();
    private Set<TeacherTeach> teacherTeachSet = new HashSet<>();

    public ResponseEntity<?> assignTeachers(AssignClassRequest request) {
        if (validateRequest(request)){
            teacherSet.forEach(teacher -> {
                TeacherTeach teach = ttRepo.findByTeacherIdAndCSId(teacher.getUserId(),
                        courseSection.getId());
                if (teach == null) {

                    teach = new TeacherTeach(teacher,courseSection);
                    System.out.println(teacher.getUserId());
                    teacherTeachSet.add(teach);
                    updateTeacher(teacher,teach);
                }
            });

            ttRepo.saveAll(teacherTeachSet);
            courseSection.setTeacherTeachs(teacherTeachSet);
            csRepo.save(courseSection);
            msg = "success";
            teacherRepository.saveAll(teacherSet);

            return new ResponseEntity(msg, HttpStatus.OK);
        }
        return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);

    }

    private void updateTeacher(Teacher teacher, TeacherTeach teach) {
        var temp = teacher.getTeacherTeachs();
        temp.add(teach);
        teacher.setTeacherTeachs(temp);
    }

    private boolean validateRequest(AssignClassRequest request) {
        boolean isValid = true;
        courseSection = csRepo.findbyCSId(request.getCourseSection().getId());
        try {request.getTeachers().forEach(c -> {
            Teacher teacher = teacherRepository.findStudentByStudentId(c.getUserId());
            if (teacher == null) {
                throw new BreakException("Teacher not found: " + c);
            }
//            System.out.println(temp.getUserId());
            teacherSet.add(teacher);

        });
        } catch (RuntimeException e) {
            msg = e.getMessage();
            isValid = false;
        }
        return isValid;
    }
}
