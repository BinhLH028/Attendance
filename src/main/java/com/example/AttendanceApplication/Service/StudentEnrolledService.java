package com.example.AttendanceApplication.Service;

import com.example.AttendanceApplication.CsvRepresentation.CourseSectionRepresentation;
import com.example.AttendanceApplication.CsvRepresentation.EnrollRepresentation;
import com.example.AttendanceApplication.DTO.StudentDTO;
import com.example.AttendanceApplication.Model.*;
import com.example.AttendanceApplication.Model.Relation.CourseSection;
import com.example.AttendanceApplication.Model.Relation.StudentEnrolled;
import com.example.AttendanceApplication.Model.Relation.TeacherTeach;
import com.example.AttendanceApplication.Repository.*;
import com.example.AttendanceApplication.Request.EnrollRequest;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
    private SectionRepository sectionRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    MessageSource messageSource;
    private String msg = "";
    private List<String> resultMsg = new ArrayList<>();
    private CourseSection courseSection;

    private Section section;

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
        List<StudentDTO> students = enrollRepo.findStudentsByCSId(id);
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
                .filter(en -> en.delFlag == false)
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
        deleteEnroll(removeEnroll, courseSection);
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
        createAttendanceDataOnEnroll(studentEnrolledSet);
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


    public ResponseEntity<?> deleteEnroll(List<Integer> request, CourseSection cs) {
        resultMsg.clear();
        List<StudentEnrolled> enrollDb = enrollRepo.findByIdInAndDelFlagFalse(request, cs.getId());
        List<AttendanceSheet> listAttendance = new ArrayList<>();
        if (enrollDb.size() > 0) {
            enrollDb.stream().forEach(e -> {
                e.delFlag = true;
                msg = messageSource.getMessage("SE03",
                        new String[]{e.getStudent().getUsername()}, Locale.getDefault());
                resultMsg.add(msg);

                AttendanceSheet tempSheet = attendanceRepository
                        .findSheetByStudentIdAndCSId(e.getStudent().getUserId(), cs.getId());
                if (tempSheet != null) {
                    tempSheet.delFlag = true;
                    listAttendance.add(tempSheet);
                }
            });
            enrollRepo.saveAll(enrollDb);
            attendanceRepository.saveAll(listAttendance);
            return new ResponseEntity(resultMsg, HttpStatus.OK);
        }
        return new ResponseEntity("Error removing teacher assign", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> uploadEnroll(MultipartFile file, Integer sectionId) throws IOException {

        resultMsg.clear();
        Set<StudentEnrolled> enrolleds = parseCsv(file ,sectionId);

        if (!resultMsg.isEmpty()) {
            return new ResponseEntity(resultMsg, HttpStatus.BAD_REQUEST);
        }
        validateEnrolls(enrolleds);

        if (!resultMsg.isEmpty()) {
            return new ResponseEntity(resultMsg, HttpStatus.BAD_REQUEST);
        }

        // Create Attendance Data
        createAttendanceDataOnEnroll(enrolleds);
        enrollRepo.saveAll(enrolleds);
        // Update courseSection
        courseSection.setStudentEnrolleds(enrolleds);
        csRepo.save(courseSection);

        msg = messageSource.getMessage("SE06",
                new String[]{String.valueOf(enrolleds.size())}, Locale.getDefault());

        return new ResponseEntity(msg, HttpStatus.OK);
    }

    private boolean validateEnrolls(Set<StudentEnrolled> set) {
        boolean isValid = true;
        Set<StudentEnrolled> setDb = enrollRepo.findStudentEnrollsByCSId(section.getSectionId());

        Set<StudentEnrolled> mutableSet1 = new HashSet<>(set);
        Set<StudentEnrolled> mutableSet2 = new HashSet<>(setDb);

        mutableSet1.retainAll(mutableSet2);

        if (!mutableSet1.isEmpty()) {
            mutableSet1.forEach(o -> {
                msg = messageSource.getMessage("SE04",
                        new String[]{o.getStudent().getUsercode(),
                                o.getCourseSection().getCourse().getCourseName(),
                                o.getCourseSection().getTeam()},
                        Locale.getDefault());
                resultMsg.add(msg);
            });

            isValid = false;
        }
        return isValid;
    }

    private Set<StudentEnrolled> parseCsv(MultipartFile file, int sectionId) throws IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            HeaderColumnNameMappingStrategy<EnrollRepresentation> strategy =
                    new HeaderColumnNameMappingStrategy<>();
            strategy.setType(EnrollRepresentation.class);
            CsvToBean<EnrollRepresentation> csvToBean =
                    new CsvToBeanBuilder<EnrollRepresentation>(reader)
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
                    .map(csvLine -> mappingData(csvLine,index)
                    )
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private StudentEnrolled mappingData(EnrollRepresentation data, int index) {

        String courseCode = data.getCourseCode();
        String team = data.getTeam();
        String userCode = data.getUserCode();
        String userName = data.getUserName();

        if (courseCode == null || courseCode == "" ||
            team == null || team == "" ||
            userCode == null || userCode == "" ||
            userName == null || userName == ""
        ) {
            msg = messageSource.getMessage("CS07",
                    new String[]{}, Locale.getDefault());
            resultMsg.add(msg);
            throw new RuntimeException(msg);
        }

        if (courseCode == null || courseCode == "") {
            msg = messageSource.getMessage("CS05",
                    new String[]{String.valueOf(index++)}, Locale.getDefault());
            resultMsg.add(msg);
        }

        Course course = courseRepository.findByCourseCode(courseCode);
        if (course == null) {
            msg = messageSource.getMessage("C03",
                    new String[]{courseCode}, Locale.getDefault());
            throw new RuntimeException(msg);
        }

        courseSection = csRepo.findbySectionAndCourse(section.getSectionId(), course.getCourseId(), team);

        Student student = studentRepository.findStudentByNameAndCode(userName,userCode);
        if (student == null) {
            msg = messageSource.getMessage("ST01",
                    new String[]{userCode.toString()}, Locale.getDefault());
        }

        return new StudentEnrolled(student, courseSection);
    }
}
