package com.example.AttendanceApplication.Service;

import com.example.AttendanceApplication.Auth.Token.ConfirmationToken;
import com.example.AttendanceApplication.Auth.Token.ConfirmationTokenService;
import com.example.AttendanceApplication.Common.Const;
import com.example.AttendanceApplication.CsvRepresentation.StudentRepresentation;
import com.example.AttendanceApplication.CsvRepresentation.TeacherRepresentation;
import com.example.AttendanceApplication.DTO.AppUserDTO;
import com.example.AttendanceApplication.Email.EmailSender;
import com.example.AttendanceApplication.Enum.Role;
import com.example.AttendanceApplication.Model.AppUser;
import com.example.AttendanceApplication.Model.Student;
import com.example.AttendanceApplication.Model.Teacher;
import com.example.AttendanceApplication.Repository.AppUserRepository;
import com.example.AttendanceApplication.Repository.StudentRepository;
import com.example.AttendanceApplication.Repository.TeacherRepository;
import com.example.AttendanceApplication.Request.ChangeUserRequest;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.time.DateUtils.parseDate;

@Service
public class AppUserService {


    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private ConfirmationTokenService confirmationTokenService;
    @Autowired
    private EmailSender emailSender;

    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MessageSource messageSource;

    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    private String msg = "";

    private List<String> resultMsg = new ArrayList<>();

    public String registerNewAccount(AppUser user, String userCode) {

        user.setDob(new Date());
        switch (user.getRole()) {
            case USER -> {
                Student student = new Student();
                modelMapper.map(user, student);
                student.setUsercode(userCode);
                studentRepository.save(student);
                user = student;
            }
            case TEACHER -> {
                Teacher teacher = new Teacher();
                modelMapper.map(user, teacher);
                teacherRepository.save(teacher);
                user = teacher;
            }
            case ADMIN -> {
                appUserRepository.save(user);
            }
        }

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );

        confirmationTokenService.saveConfirmationToken(
                confirmationToken);

        // Sendmail
        String link = Const.LOCAL_MAIL_LINK;
        emailSender.send(
                user.getEmail(),
                buildEmail(user.getEmail(), link));

        return token;
    }

    @Cacheable("userEmails")
    public List<String> getAllUserEmails() {
        List<String> userEmails = appUserRepository.findAllEmails();
        return userEmails;
    }

    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }

    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

    public ResponseEntity updateUser(ChangeUserRequest request, Principal connectedUser) {

        if (validateRequest(request, connectedUser)) {
            AppUser user = appUserRepository
                    .findAppUserByUserIdAndDelFlagFalse(request.getUserId());


            if (request.getNewPassword() != "" || request.getNewPassword() != null) {
                user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            }

            appUserRepository.save(user);

            return new ResponseEntity(messageSource.getMessage("U05", new String[]{}, Locale.getDefault()), HttpStatus.OK);
        }
        return new ResponseEntity(resultMsg, HttpStatus.BAD_REQUEST);
    }

    private boolean validateRequest(ChangeUserRequest request, Principal connectedUser) {
        boolean isValid = true;

        var currentUser = (AppUser) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        String msg = "";

        if (request.getOldPassword() == "" && request.getNewPassword() == "") {
            if (!passwordEncoder.matches(request.getOldPassword(), currentUser.getPassword())) {
                msg = messageSource.getMessage("U06", new String[]{}, Locale.getDefault());
                isValid = false;
                resultMsg.add(msg);
            }
        }
        return isValid;
    }

    public ResponseEntity<?> uploadFile(MultipartFile file, boolean isTeacherFile) {
        resultMsg.clear();
        try {
            if (isTeacherFile) {
                msg = processTeacherFile(file);
            } else {
                msg = processStudentFile(file);
            }
        } catch (IOException ioe) {
            return new ResponseEntity<>("Error processing the file", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RuntimeException e) {
            return new ResponseEntity(resultMsg, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(msg, HttpStatus.OK);
    }

    @CacheEvict(value = "userEmails", allEntries = true)
    public String processStudentFile(MultipartFile file) throws IOException {
        Set<Student> students = parseStudentFile(file);

        if (!resultMsg.isEmpty()) {
            throw new RuntimeException(resultMsg.toString());
        }
        List<String> emails = getAllUserEmails();

        List<Student> dupStudents = students.stream()
                .filter(student -> emails.contains(student.getEmail())) // Filter out students whose emails are in the allEmails list
                .toList();

        if (!dupStudents.isEmpty()) {
            dupStudents.forEach(t -> {
                msg = messageSource.getMessage("U03", new String[]{t.getEmail()}, Locale.getDefault());
                resultMsg.add(msg);
            });
        }
        if (!resultMsg.isEmpty()) {
            throw new RuntimeException(resultMsg.toString());
        }
        studentRepository.saveAll(students);

        return messageSource.getMessage("ST03", new String[]{String.valueOf(students.size())}, Locale.getDefault());
    }

    private Set<Student> parseStudentFile(MultipartFile file) throws IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            HeaderColumnNameMappingStrategy<StudentRepresentation> strategy =
                    new HeaderColumnNameMappingStrategy<>();
            strategy.setType(StudentRepresentation.class);
            CsvToBean<StudentRepresentation> csvToBean =
                    new CsvToBeanBuilder<StudentRepresentation>(reader)
                            .withMappingStrategy(strategy)
                            .withIgnoreEmptyLine(true)
                            .withIgnoreLeadingWhiteSpace(true)
                            .build();
            AtomicInteger index = new AtomicInteger(1);
            return csvToBean.parse()
                    .stream()
                    .map(csvLine -> {
                                String msv = csvLine.getUserCode();
                                String userName = csvLine.getUserName();
                                String email = csvLine.getEmail().toLowerCase();
                                String phone = csvLine.getPhone();
                                Date dob = getDate(csvLine.getDob());
                                String stringDob = csvLine.getDob();
                                String schoolYear = csvLine.getSchoolYear();

                                if (userName == null || userName == "") {
                                    msg = messageSource.getMessage("U07",
                                            new String[]{String.valueOf(index.getAndIncrement())}, Locale.getDefault());
                                    userName = "";
                                    resultMsg.add(msg);
                                }
                                if (email == null || email == "") {
                                    msg = messageSource.getMessage("U08",
                                            new String[]{String.valueOf(index.getAndIncrement())}, Locale.getDefault());
                                    email = "";
                                    resultMsg.add(msg);
                                }
                                if (dob == null || stringDob == "") {
                                    msg = messageSource.getMessage("U09",
                                            new String[]{String.valueOf(index.getAndIncrement())}, Locale.getDefault());
                                    resultMsg.add(msg);
                                }
                                if (msv == null || msv == "") {
                                    msg = messageSource.getMessage("U10",
                                            new String[]{String.valueOf(index.getAndIncrement())}, Locale.getDefault());
                                    resultMsg.add(msg);
                                }
                                String rawPw = concatenateArrayElements(stringDob.split("/"));

                                return new Student(userName, passwordEncoder.encode(rawPw),
                                        true, email,
                                        Const.checkNullString(phone),
                                        dob, Role.USER, msv,
                                        Const.checkNullString(schoolYear));
                            }
                    )
                    .collect(Collectors.toSet());
        }
    }

    @CacheEvict(value = "userEmails", allEntries = true)
    public String processTeacherFile(MultipartFile file) throws IOException {
        Set<Teacher> teachers = parseTeacherCsv(file);

        if (!resultMsg.isEmpty()) {
            throw new RuntimeException(resultMsg.toString());
        }
        List<String> emails = getAllUserEmails();

        List<Teacher> dupTeacher = teachers.stream()
                .filter(teacher -> emails.contains(teacher.getEmail())) // Filter out teachers whose emails are in the allEmails list
                .toList();

        if (!dupTeacher.isEmpty()) {
            dupTeacher.forEach(t -> {
                msg = messageSource.getMessage("U03", new String[]{t.getEmail()}, Locale.getDefault());
                resultMsg.add(msg);
            });
        }
        if (!resultMsg.isEmpty()) {
            throw new RuntimeException(resultMsg.toString());
        }
        teacherRepository.saveAll(teachers);

        return messageSource.getMessage("T04", new String[]{String.valueOf(teachers.size())}, Locale.getDefault());
    }

    private Set<Teacher> parseTeacherCsv(MultipartFile file) throws IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            HeaderColumnNameMappingStrategy<TeacherRepresentation> strategy =
                    new HeaderColumnNameMappingStrategy<>();
            strategy.setType(TeacherRepresentation.class);
            CsvToBean<TeacherRepresentation> csvToBean =
                    new CsvToBeanBuilder<TeacherRepresentation>(reader)
                            .withMappingStrategy(strategy)
                            .withIgnoreEmptyLine(true)
                            .withIgnoreLeadingWhiteSpace(true)
                            .build();
            AtomicInteger index = new AtomicInteger(1);
            return csvToBean.parse()
                    .stream()
                    .map(csvLine -> {
                                String userName = csvLine.getUserName();
                                String email = csvLine.getEmail().toLowerCase();
                                String phone = csvLine.getPhone();
                                Date dob = getDate(csvLine.getDob());
                                String stringDob = csvLine.getDob();
                                String department = csvLine.getDepartment();

                                if (userName == null || userName.isEmpty()) {
                                    msg = messageSource.getMessage("U07",
                                            new String[]{String.valueOf(index.getAndIncrement())}, Locale.getDefault());
                                    userName = "";
                                    resultMsg.add(msg);
                                }
                                if (email == null || email.isEmpty()) {
                                    msg = messageSource.getMessage("U08",
                                            new String[]{String.valueOf(index.getAndIncrement())}, Locale.getDefault());
                                    email = "";
                                    resultMsg.add(msg);
                                }
                                if (dob == null || stringDob == "") {
                                    msg = messageSource.getMessage("U09",
                                            new String[]{String.valueOf(index.getAndIncrement())}, Locale.getDefault());
                                    resultMsg.add(msg);
                                }

                                String rawPw = concatenateArrayElements(stringDob.split("/"));

                                return new Teacher(userName, passwordEncoder.encode(rawPw),
                                        true, email,
                                        Const.checkNullString(phone),
                                        dob, Role.TEACHER,
                                        Const.checkNullString(department));
                            }
                    )
                    .collect(Collectors.toSet());
        }
    }

    private static Date getDate(String csvLine) {
        Date dob;
        try {
            dob = parseDate(csvLine, "dd/MM/yyyy");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return dob;
    }

    public String concatenateArrayElements(String[] array) {
        StringBuilder sb = new StringBuilder();
        for (String element : array) {
            sb.append(element);
        }
        return sb.toString();
    }

    public ResponseEntity updateUserById(String type, Integer id, AppUserDTO user) {
        if (type.equals("u")) {
            Student student = studentRepository.findStudentByUserIdAndDelFlagFalse(id);
            if (student == null) {
                return new ResponseEntity("update failed", HttpStatus.BAD_REQUEST);
            }
            student.setUserName(user.getUserName());
            student.setPhone(user.getPhone());
            student.setSchoolyear(user.getSchoolYear());
            studentRepository.save(student);
            return new ResponseEntity("update successfully", HttpStatus.OK);
        }
        Teacher teacher = teacherRepository.findTeacherByUserIdAndDelFlagFalse(id);
        if (teacher == null) {
            return new ResponseEntity("update failed", HttpStatus.BAD_REQUEST);
        }
        teacher.setUserName(user.getUserName());
        teacher.setPhone(user.getPhone());
        teacher.setDepartment(user.getDepartment());
        teacherRepository.save(teacher);
        return new ResponseEntity("update successfully", HttpStatus.OK);
    }
}
