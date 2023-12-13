package com.example.AttendanceApplication.Service;

import com.example.AttendanceApplication.DTO.CourseSectionDTO;
import com.example.AttendanceApplication.Enum.Role;
import com.example.AttendanceApplication.Model.Course;
import com.example.AttendanceApplication.Model.Relation.CourseSection;
import com.example.AttendanceApplication.Model.Section;
import com.example.AttendanceApplication.Model.Teacher;
import com.example.AttendanceApplication.Repository.CourseRepository;
import com.example.AttendanceApplication.Repository.CourseSectionRepository;
import com.example.AttendanceApplication.Repository.SectionRepository;
import com.example.AttendanceApplication.Request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class CourseSectionService {

    @Autowired
    private CourseSectionRepository csRepo;
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    MessageSource messageSource;

    private String msg = "";

    private CourseSection courseSection;
    private Section section;
    private Course course;

    public ResponseEntity<?> addCourseSection(CourseSection request) {
        if (validateRequest(request)){
            CourseSection courseSection = new CourseSection();
            courseSection.setSection(section);
            courseSection.setCourse(course);

            csRepo.save(courseSection);
            UpdateCourseAndSection(courseSection);

            msg = messageSource.getMessage("13",
                    new String[]{course.getCourseCode().toString(),
                            section.getSemester().toString(),
                            section.getYear().toString()},
                            Locale.getDefault());
            return new ResponseEntity(msg, HttpStatus.OK);
        }
        return new ResponseEntity(msg,HttpStatus.BAD_REQUEST);
    }

    private void UpdateCourseAndSection(CourseSection courseSection) {
        var temp = section.getCourseSections();
        temp.add(courseSection);
        section.setCourseSections(temp);

        temp = course.getCourseSections();
        temp.add(courseSection);
        course.setCourseSections(temp);
        sectionRepository.save(section);
        courseRepository.save(course);
    }


    private boolean validateRequest(CourseSection request) {
        boolean isValid = true;
        section = sectionRepository.findSectionById(request.getSection().getSectionId());
        course = courseRepository.findCourseById(request.getCourse().getCourseId());
        courseSection = csRepo.findbySectionAndCourse(section.getSectionId(),
                course.getCourseId());
        if (courseSection != null) {
            msg = messageSource.getMessage("14",
                    new String[]{course.getCourseCode().toString(),
                            section.getSemester().toString(),
                            section.getYear().toString()},
                    Locale.getDefault());
            isValid = false;
        }

        if (section == null) {
            msg = messageSource.getMessage("12",
                    new String[]{section.getSectionId().toString()}, Locale.getDefault());
            isValid = false;
        }
        if (course == null) {
            msg = messageSource.getMessage("10",
                    new String[]{course.getCourseId().toString()}, Locale.getDefault());
            isValid = false;
        }
        return isValid;
    }

    public ResponseEntity getCourseSection() {
        List<CourseSection> csRepoAll = csRepo.findAll();
        if (csRepoAll.size() <= 0 ) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(csRepoAll,HttpStatus.OK);
    }

    public ResponseEntity getCourseSectionByUser(int sectionId, UserRequest user) {
        List<CourseSectionDTO> listCourse = new ArrayList<CourseSectionDTO>();
        if (user.getRole() == Role.USER) {
            listCourse = csRepo.findStudentCourseInfoByUserAndSection(sectionId, user.getUserId());
        } else {
            listCourse = csRepo.findTeacherCourseInfoByUserAndSection(sectionId, user.getUserId());
        }
        return new ResponseEntity(listCourse,HttpStatus.OK);
    }


//    private boolean validateRequest(CourseSectionRequest request) {
//        boolean isValid = true;
//        section = sectionRepository.findSectionById(request.getSectionId());
//        courses = new ArrayList<Course>();
//        if (section == null) {
//            msg = messageSource.getMessage("12",
//                    new String[]{request.getSectionId().toString()}, Locale.getDefault());
//            isValid = false;
//        }
//        try {request.getCourseId().forEach(c -> {
//            var temp = courseRepository.findCourseById(c);
//            if (temp == null) {
//                throw new BreakException("Course not found: " + c);
//            }
//            courses.add(temp);
//        });
//        } catch (RuntimeException e) {
//            msg = e.getMessage();
//            isValid = false;
//        }
//        return isValid;
//    }
}
