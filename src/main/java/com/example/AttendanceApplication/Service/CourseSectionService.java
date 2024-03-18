package com.example.AttendanceApplication.Service;

import com.example.AttendanceApplication.Common.Const;
import com.example.AttendanceApplication.DTO.CourseSectionDTO;
import com.example.AttendanceApplication.DTO.TeacherDTO;
import com.example.AttendanceApplication.Enum.Role;
import com.example.AttendanceApplication.Mapper.CourseSectionMapper;
import com.example.AttendanceApplication.Model.Course;
import com.example.AttendanceApplication.Model.Relation.CourseSection;
import com.example.AttendanceApplication.Model.Section;
import com.example.AttendanceApplication.Model.Teacher;
import com.example.AttendanceApplication.Repository.CourseRepository;
import com.example.AttendanceApplication.Repository.CourseSectionRepository;
import com.example.AttendanceApplication.Repository.SectionRepository;
import com.example.AttendanceApplication.Repository.TeacherRepository;
import com.example.AttendanceApplication.Request.CourseSectionRequest;
import com.example.AttendanceApplication.Request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class CourseSectionService {

    @Autowired
    private CourseSectionRepository csRepo;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseSectionMapper courseSectionMapper;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    MessageSource messageSource;

    private String msg = "";

    private CourseSection courseSection;
    private Section section;
    private List<Course> listCourse;

    private List<String> resultMsg = new ArrayList<>();

    public ResponseEntity<?> addCourseSection(CourseSectionRequest request) {
        List<CourseSection> courseSectionList = new ArrayList<>();

        msg = "";
        resultMsg.clear();

        if (validateRequest(request)){
            for(Course c : listCourse) {
                CourseSection csTemp = new CourseSection();
                csTemp.setSection(section);
                csTemp.setCourse(c);
                courseSectionList.add(csTemp);
            }

            csRepo.saveAll(courseSectionList);
            for (CourseSection cs : courseSectionList) {
                UpdateCourseAndSection(cs);

                msg = messageSource.getMessage("CS01",
                        new String[]{cs.getCourse().getCourseCode().toString(),
                                section.getSemester().toString(),
                                section.getYear().toString()},
                        Locale.getDefault());

                resultMsg.add(msg);
            }

            return new ResponseEntity(resultMsg, HttpStatus.OK);
        }
        return new ResponseEntity(resultMsg,HttpStatus.BAD_REQUEST);
    }

    private void UpdateCourseAndSection(CourseSection courseSection) {
        var temp = section.getCourseSections();
        temp.add(courseSection);
        section.setCourseSections(temp);

        Course course = courseSection.getCourse();

        temp = course.getCourseSections();
        temp.add(courseSection);
        course.setCourseSections(temp);
        sectionRepository.save(section);
        courseRepository.save(course);
    }


    private boolean validateRequest(CourseSectionRequest request) {
        boolean isValid = true;

        section = sectionRepository.findSectionById(request.getSectionId());

        listCourse = courseRepository.findCourseByIdIn(request.getCourseIds());

        if (section == null) {
            msg = messageSource.getMessage("S04",
                    new String[]{section.getSectionId().toString()}, Locale.getDefault());
            isValid = false;

            if (msg != "") {
                resultMsg.add(msg);
                msg = "";
            }
        }

        for(Course c : listCourse) {
            courseSection = csRepo.findbySectionAndCourse(section.getSectionId(),
                    c.getCourseId());
            if (courseSection != null) {
                msg = messageSource.getMessage("CS02",
                        new String[]{c.getCourseCode().toString(),
                                section.getSemester().toString(),
                                section.getYear().toString()},
                        Locale.getDefault());
                isValid = false;
            }

            if (msg != "") {
                resultMsg.add(msg);
                msg = "";
            }
        }

        return isValid;
    }

    public ResponseEntity getCourseSection() {
        List<CourseSection> csRepoAll = csRepo.findAll();

        if (csRepoAll.size() <= 0 ) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(csRepoAll.stream()
                .map(cs -> courseSectionMapper.convertToDto(cs))
                .collect(Collectors.toList())
                , HttpStatus.OK);
    }

    public ResponseEntity getCourseSectionByUser(int sectionId, UserRequest user) {
        List<CourseSectionDTO> listCourse = new ArrayList<>();
        if (user.getRole() == Role.USER) {
            listCourse = csRepo.findStudentCourseInfoByUserAndSection(sectionId, user.getUserId());
        } else {
            listCourse = csRepo.findTeacherCourseInfoByUserAndSection(sectionId, user.getUserId());
        }
        return new ResponseEntity(listCourse,HttpStatus.OK);
    }

    public ResponseEntity getCourseSectionBySection(int sectionId, int page) {
        Page<CourseSectionDTO> listCourse = null;
        Section temp = sectionRepository.findSectionById(sectionId);
            if (temp != null) {
                listCourse  = csRepo.findCourseInfoBySection(sectionId, PageRequest.of(page, Const.PAGE_SIZE));
            }

            // set list teacher that teach the class
            if (listCourse.getSize() > 0) {
                listCourse.stream().forEach(c -> {
                    List<TeacherDTO> teachers = teacherRepository.findByCSId(c.getId());
                    if (teachers.size() > 0) {
                        c.setTeacherName(teachers);
                    }
                });
                return new ResponseEntity(listCourse,HttpStatus.OK);
            }
        msg = messageSource.getMessage("S04",new String[]{String.valueOf(sectionId)},
                Locale.getDefault());
        return new ResponseEntity(msg,HttpStatus.BAD_REQUEST);

    }

}
