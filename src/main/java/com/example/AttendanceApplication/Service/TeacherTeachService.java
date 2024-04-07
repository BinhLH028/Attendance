package com.example.AttendanceApplication.Service;

import com.example.AttendanceApplication.Model.Relation.CourseSection;
import com.example.AttendanceApplication.Model.Relation.TeacherTeach;
import com.example.AttendanceApplication.Model.Teacher;
import com.example.AttendanceApplication.Repository.CourseSectionRepository;
import com.example.AttendanceApplication.Repository.TeacherRepository;
import com.example.AttendanceApplication.Repository.TeacherTeachRepository;
import com.example.AttendanceApplication.Request.AssignClassRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
    private List<String> resultMsg = new ArrayList<>();
    private CourseSection courseSection;
    private Set<Teacher> teacherSet = new HashSet<>();
    private Set<TeacherTeach> teacherTeachSet = new HashSet<>();

    private boolean isUpdate = false;

    public ResponseEntity<?> getTeacherListByCSId(int id) {
        List<Teacher> teachers = ttRepo.findTeachersByCSId(id);
        if (teachers.isEmpty()) {
            msg = messageSource.getMessage("TT04", null, Locale.getDefault());
            return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(teachers, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> assignTeachers(AssignClassRequest request) {
        // clear sets data
        clearData();
        isUpdate = false;
        courseSection = csRepo.findbyCSId(request.getCourseSectionId());

        if (validateRequest(request, request.getTeacherIds())) {
            assignTeachings(request);

            msg = messageSource.getMessage("TT01",
                    new String[]{courseSection.getCourse().getCourseCode(),
                            courseSection.getTeam(),
                            courseSection.getSection().getSemester().toString(),
                            courseSection.getSection().getYear().toString()
                    }, Locale.getDefault());

            return new ResponseEntity(msg, HttpStatus.OK);
        }
        return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);

    }

    private void assignTeachings(AssignClassRequest request) {
        teacherSet.forEach(teacher -> {
            TeacherTeach teach = ttRepo.findByTeacherIdAndCSId(teacher.getUserId(),
                    courseSection.getId());
            if (teach == null) {

                teach = new TeacherTeach(teacher, courseSection);
                System.out.println(teacher.getUserId());
                teacherTeachSet.add(teach);
                updateTeacher(teacher, teach);
            }
        });

        ttRepo.saveAll(teacherTeachSet);
        courseSection.setTeacherTeachs(teacherTeachSet);
        csRepo.save(courseSection);
        teacherRepository.saveAll(teacherSet);
    }

    private void clearData() {
        teacherSet.clear();
        teacherTeachSet.clear();
    }

    private void updateTeacher(Teacher teacher, TeacherTeach teach) {
        var temp = teacher.getTeacherTeachs();
        temp.add(teach);
        teacher.setTeacherTeachs(temp);
    }

    private boolean validateRequest(AssignClassRequest request, List<Integer> teacherIds) {
        boolean isValid = true;

        try {
            createTeachings(request, teacherIds);
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

    public ResponseEntity<?> updateAssign(AssignClassRequest request) {
        isUpdate = true;
        clearData();
        courseSection = csRepo.findbyCSId(request.getCourseSectionId());
        List<Integer> listTt = courseSection.getTeacherTeachs()
                .stream()
                .map(tt -> tt.getTeacher().getUserId())
                .collect(Collectors.toList());

        // filter remove old assign
        List<Integer> removeTts = listTt.stream()
                .filter(e -> !request.getTeacherIds().contains(e))
                .collect(Collectors.toList());
        // filter new assign
        List<Integer> newTts = request.getTeacherIds().stream()
                .filter(e -> !listTt.contains(e))
                .collect(Collectors.toList());
        // update to database
        updateTeachingInfo(request, newTts, removeTts);

        msg = messageSource.getMessage("TT05",
                new String[]{courseSection.getCourse().getCourseCode(),
                        courseSection.getTeam(),
                        courseSection.getSection().getSemester().toString(),
                        courseSection.getSection().getYear().toString()
                }, Locale.getDefault());

        return new ResponseEntity(msg, HttpStatus.OK);
    }

    private void updateTeachingInfo(AssignClassRequest request, List<Integer> newTts, List<Integer> removeTts) {
        createTeachings(request, newTts);
        deleteAssign(removeTts);
        assignTeachings(request);
    }

    private void createTeachings(AssignClassRequest request, List<Integer> newTts) {
        newTts.forEach(id -> {
            TeacherTeach tt = ttRepo.findByTeacherIdAndCSId(id, request.getCourseSectionId());
            Teacher teacher = teacherRepository.findTeacherByTeacherId(id);
            if (teacher != null && tt == null) {
                msg = messageSource.getMessage("TT01",
                        new String[]{id.toString()}, Locale.getDefault());
                teacherSet.add(teacher);
            } else {
                msg = messageSource.getMessage("TT03",
                        new String[]{teacher.getUsername(),
                                courseSection.getCourse().getCourseName(),
                                courseSection.getTeam()}, Locale.getDefault());
            }
        });
    }
}
