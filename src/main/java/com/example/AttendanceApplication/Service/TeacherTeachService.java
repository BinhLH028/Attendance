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
import java.util.List;
import java.util.Locale;
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
    private List<String> resultMsg;
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
            teacherRepository.saveAll(teacherSet);

            msg = messageSource.getMessage("TT01",
                    new String[]{courseSection.getCourse().getCourseCode(),
                            courseSection.getSection().getSemester().toString(),
                            courseSection.getSection().getYear().toString()
                    }, Locale.getDefault());

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
        courseSection = csRepo.findbyCSId(request.getCourseSection());
        try {request.getTeacherIds().forEach(id -> {
            Teacher teacher = teacherRepository.findStudentByStudentId(id);
            if (teacher == null) {
                msg = messageSource.getMessage("TT01",
                        new String[]{id.toString()}, Locale.getDefault());
            }
            teacherSet.add(teacher);

        });
        } catch (RuntimeException e) {
            msg = e.getMessage();
            isValid = false;
        }
        return isValid;
    }

    public ResponseEntity<?> deleteAssign(List<Integer> request) {
        resultMsg.clear();
        List<TeacherTeach> ttDb = ttRepo.findByIdInAndDelFlagFalse(request);
        if (ttDb.size() > 0) {
            ttDb.stream().forEach(t -> {
                t.delFlag = true;
                msg = messageSource.getMessage("TT02",
                        new String[]{t.getTeacher().getUsername()}, Locale.getDefault());
                resultMsg.add(msg);
            });
            return new ResponseEntity(resultMsg, HttpStatus.OK);
        }
        return new ResponseEntity("Error removing teacher assign", HttpStatus.BAD_REQUEST);
    }
}
