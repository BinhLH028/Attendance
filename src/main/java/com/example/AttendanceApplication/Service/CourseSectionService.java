package com.example.AttendanceApplication.Service;

import com.example.AttendanceApplication.Common.Const;
import com.example.AttendanceApplication.CsvRepresentation.CourseSectionRepresentation;
import com.example.AttendanceApplication.DTO.CourseSectionDTO;
import com.example.AttendanceApplication.DTO.TeacherDTO;
import com.example.AttendanceApplication.Enum.Role;
import com.example.AttendanceApplication.Model.Course;
import com.example.AttendanceApplication.Model.Relation.CourseSection;
import com.example.AttendanceApplication.Model.Relation.TeacherTeach;
import com.example.AttendanceApplication.Model.Section;
import com.example.AttendanceApplication.Model.Teacher;
import com.example.AttendanceApplication.Repository.*;
import com.example.AttendanceApplication.Request.AssignClassRequest;
import com.example.AttendanceApplication.Request.CourseSectionRequest;
import com.example.AttendanceApplication.Request.UserRequest;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
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
    private TeacherRepository teacherRepository;

    @Autowired
    private TeacherTeachRepository ttRepo;

    @Autowired
    private TeacherTeachService ttService;

    @Autowired
    private StudentEnrolledService enrolledService;

    @Autowired
    MessageSource messageSource;

    private String msg = "";

    private CourseSection courseSection;
    private Section section;
    private List<Course> listCourse;
    private List<String> resultMsg = new ArrayList<>();

    private Map<String, List<Integer>> mapData = new HashMap();

    public ResponseEntity<?> addCourseSection(CourseSectionRequest request) {
        List<CourseSection> courseSectionList = new ArrayList<>();

        msg = "";
        resultMsg.clear();

        if (request.getTeam() == null) {
            request.setTeam("CL");
        }
        if (validateRequest(request)) {
            for (Course c : listCourse) {
                CourseSection csTemp = new CourseSection();
                csTemp.setSection(section);
                csTemp.setCourse(c);
                csTemp.setTeam(request.getTeam());
                courseSectionList.add(csTemp);
            }

            csRepo.saveAll(courseSectionList);
            for (CourseSection cs : courseSectionList) {
                UpdateCourseAndSection(cs);

                msg = messageSource.getMessage("CS01",
                        new String[]{cs.getCourse().getCourseCode().toString(),
                                request.getTeam(),
                                section.getSemester().toString(),
                                section.getYear().toString()},
                        Locale.getDefault());

                resultMsg.add(msg);
            }

            return new ResponseEntity(resultMsg, HttpStatus.OK);
        }
        return new ResponseEntity(resultMsg, HttpStatus.BAD_REQUEST);
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

        courseSection = csRepo.findbySectionAndCourse(section.getSectionId(),
                request.getCourseId(), request.getTeam());

        Course course = courseRepository.findCourseById(request.getCourseId());

        if (courseSection != null && course != null) {
            msg = messageSource.getMessage("CS02",
                    new String[]{course.getCourseCode(),
                            request.getTeam(),
                            section.getSemester().toString(),
                            section.getYear().toString()},
                    Locale.getDefault());
            resultMsg.add(msg);
            isValid = false;
        }

        for (Course c : listCourse) {
            courseSection = csRepo.findbySectionAndCourse(section.getSectionId(),
                    c.getCourseId(), request.getTeam());
            if (courseSection != null) {
                msg = messageSource.getMessage("CS02",
                        new String[]{c.getCourseCode().toString(),
                                request.getTeam(),
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
        if (csRepoAll.size() <= 0) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(csRepoAll, HttpStatus.OK);
    }

    public ResponseEntity getCourseSectionByUser(int sectionId, UserRequest user) {
        List<CourseSectionDTO> listCourse = new ArrayList<>();
        if (user.getRole() == Role.USER) {
            listCourse = csRepo.findStudentCourseInfoByUserAndSection(sectionId, user.getUserId());
        } else {
            listCourse = csRepo.findTeacherCourseInfoByUserAndSection(sectionId, user.getUserId());
        }
        return new ResponseEntity(listCourse, HttpStatus.OK);
    }

    public ResponseEntity getCourseSectionBySection(int sectionId, int page) {
        Page<CourseSectionDTO> listCourse = null;
        Section temp = sectionRepository.findSectionById(sectionId);
        if (temp != null) {
            listCourse = csRepo.findCourseInfoBySection(sectionId, PageRequest.of(page, Const.PAGE_SIZE));
        }

        // set list teacher that teach the class
        if (listCourse.getSize() > 0) {
            listCourse.stream().forEach(c -> {
                List<TeacherDTO> teachers = teacherRepository.findByCSId(c.getId());
                if (teachers.size() > 0) {
                    c.setTeacherName(teachers);
                }
            });
            return new ResponseEntity(listCourse, HttpStatus.OK);
        }
        msg = messageSource.getMessage("S04", new String[]{String.valueOf(sectionId)},
                Locale.getDefault());
        return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);

    }

    @Transactional
    public ResponseEntity<?> uploadCourseSection(MultipartFile file, int sectionId) throws IOException {

        resultMsg.clear();
        Set<CourseSection> courseSections = parseCsv(file, sectionId);

        if (!resultMsg.isEmpty()) {
            return new ResponseEntity(resultMsg, HttpStatus.BAD_REQUEST);
        }

        try {
            courseSections.forEach(cs -> {
                if (validateCS(cs)) {
                    csRepo.save(cs);
                    saveDataCourseSection(cs);
                } else {
                    throw new RuntimeException(msg);
                }
            });
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }


        msg = messageSource.getMessage("C08",
                new String[]{String.valueOf(courseSections.size())}, Locale.getDefault());

        return new ResponseEntity(msg, HttpStatus.OK);
    }

    private boolean validateCS(CourseSection cs) {
        boolean isValid = true;
        Course course = cs.getCourse();
        Section section1 = cs.getSection();
        CourseSection temp = csRepo.findbySectionAndCourse(section1.getSectionId(),
                course.getCourseId(), cs.getTeam());

        if (temp != null) {
            msg = messageSource.getMessage("CS02",
                    new String[]{course.getCourseCode(),
                            temp.getTeam(),
                            section1.getSemester().toString(),
                            section1.getYear().toString()},
                    Locale.getDefault());
            isValid = false;
        }
        return isValid;
    }

    private void saveDataCourseSection(CourseSection cs) {
        List<Integer> teacherIds = mapData.get(cs.getCourse().getCourseName());
        System.out.println(teacherIds.size());
        AssignClassRequest request = new AssignClassRequest(teacherIds, cs.getId(), cs.getTeam());

//        ttService.assignTeachers(request);
        ResponseEntity<?> response = ttService.assignTeachers(request);
        if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
            throw new RuntimeException("error");
        }
    }

    private Set<CourseSection> parseCsv(MultipartFile file, int sectionId) throws IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            HeaderColumnNameMappingStrategy<CourseSectionRepresentation> strategy =
                    new HeaderColumnNameMappingStrategy<>();
            strategy.setType(CourseSectionRepresentation.class);
            CsvToBean<CourseSectionRepresentation> csvToBean =
                    new CsvToBeanBuilder<CourseSectionRepresentation>(reader)
                            .withMappingStrategy(strategy)
                            .withIgnoreEmptyLine(true)
                            .withIgnoreLeadingWhiteSpace(true)
                            .build();

            int index = 1;
            section = sectionRepository.findSectionById(sectionId);
            if (section == null) {
                msg = messageSource.getMessage("S04",
                        new String[]{String.valueOf(sectionId)}, Locale.getDefault());
                resultMsg.add(msg);
                return null;
            }
            return csvToBean.parse()
                    .stream()
                    .map(csvLine -> mappingData(csvLine, index)
                    )
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private CourseSection mappingData(CourseSectionRepresentation data, int index) {

        String courseCode = data.getCourseCode();
        if (data.getTeachers() == null || data.getTeachers() == "") {
            msg = messageSource.getMessage("CS07",
                    new String[]{}, Locale.getDefault());
            resultMsg.add(msg);
            throw new RuntimeException(msg);
        }
        List<String> teacherEmails = Arrays.asList(data.getTeachers().split(";"));
        List<Integer> teachers = new ArrayList<>();

        if (courseCode == null || courseCode == "") {
            msg = messageSource.getMessage("CS05",
                    new String[]{String.valueOf(index++)}, Locale.getDefault());
            resultMsg.add(msg);
        }
        if (!(teacherEmails.size() > 0)) {
            msg = messageSource.getMessage("CS06",
                    new String[]{String.valueOf(index++)}, Locale.getDefault());
            resultMsg.add(msg);
        }
        Course course = courseRepository.findByCourseCode(courseCode);
        if (course == null) {
            msg = messageSource.getMessage("C03",
                    new String[]{courseCode}, Locale.getDefault());
            throw new RuntimeException(msg);
        }

        teacherEmails.forEach(t -> {

            Integer teacherId = teacherRepository.findUserIdByEmailAndDelFlagFalse(t);
            if (teacherId == null) {
                msg = messageSource.getMessage("T01",
                        new String[]{t}, Locale.getDefault());
                throw new RuntimeException(msg);
            }
            teachers.add(teacherId);
        });

        mapData.put(course.getCourseName(), teachers);

        if (data.getTeam() == null || data.getTeam() == "") {
            data.setTeam("CL");
        }
        return new CourseSection(section, course, data.getTeam());
    }


    public ResponseEntity<?> deleteCourseSection(Integer csId) {
        courseSection = csRepo.findbyCSId(csId);
        if (courseSection == null) {
            msg = messageSource.getMessage("CS09",
                    new String[]{}, Locale.getDefault());
            return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
        }
        // remove teaching
        List<Integer> delTt = courseSection.getTeacherTeachs()
                .stream().map(t -> t.getTeacher().getUserId())
                .collect(Collectors.toList());
        ttService.deleteAssign(delTt, courseSection);

        // remove enroll
        List<Integer> delEnroll = courseSection.getStudentEnrolleds()
                .stream().map(t -> t.getStudent().getUserId())
                .collect(Collectors.toList());
        enrolledService.deleteEnroll(delEnroll, courseSection);

        // TODO: validate each deletion
        msg = messageSource.getMessage("CS10",
                new String[]{}, Locale.getDefault());
        return new ResponseEntity(msg, HttpStatus.OK);
    }

    public ResponseEntity<?> createCourseSection(CourseSectionRequest request) {
        msg = "";
        resultMsg.clear();

        if (validateRequest(request)) {
            Course course = courseRepository.findCourseById(request.getCourseId());
            List<Teacher> teacherList = teacherRepository.findByIdInAndDelFlagFalse(request.getTeachersId());

            CourseSection cs = new CourseSection(section, course);
            csRepo.save(cs);

            Set<TeacherTeach> ttList = teacherList.stream()
                    .map(teacher -> new TeacherTeach(teacher, cs))
                    .collect(Collectors.toSet());

            cs.setTeacherTeachs(ttList);

            ttRepo.saveAll(ttList);

            msg = messageSource.getMessage("CS01",
                    new String[]{cs.getCourse().getCourseCode().toString(),
                            request.getTeam(),
                            section.getSemester().toString(),
                            section.getYear().toString()},
                    Locale.getDefault());
            return new ResponseEntity(msg, HttpStatus.OK);
        }

        return new ResponseEntity(resultMsg.toString(), HttpStatus.BAD_REQUEST);
    }
}

