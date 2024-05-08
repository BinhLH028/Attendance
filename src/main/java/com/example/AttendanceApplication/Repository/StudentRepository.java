package com.example.AttendanceApplication.Repository;

import com.example.AttendanceApplication.DTO.FilterManagementDTO;
import com.example.AttendanceApplication.DTO.StudentDTO;
import com.example.AttendanceApplication.DTO.TeacherDTO;
import com.example.AttendanceApplication.Model.Student;
import com.example.AttendanceApplication.Model.Teacher;
import com.example.AttendanceApplication.Response.StudentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {


    @Query("""
            SELECT s FROM Student s 
            WHERE (s.userId = :studentId)
                AND s.delFlag = false 
            """)
    Student findStudentByStudentId(Integer studentId);

    Student findStudentByUserIdAndDelFlagFalse(Integer id);

    @Query("""
            SELECT s.usercode FROM Student s 
            WHERE (s.delFlag = false )
            """)
    List<Integer> findAllStudentCode();

    @Query(value = """
            SELECT DISTINCT 
            new com.example.AttendanceApplication.Response.StudentResponse (
                s.userId,
                s.usercode,
                s.userName,
                c.courseCode,
                c.courseName,
                cs.id,
                cs.team,
                att.totalAbsence
            )
            FROM Student s 
            JOIN StudentEnrolled enroll
                ON s = enroll.student
            JOIN CourseSection cs
                ON cs = enroll.courseSection
            JOIN Course c
                ON c = cs.course
            JOIN AttendanceSheet att
                ON att.studentEnrolled = enroll
            WHERE (:#{#filter.sectionId} IS NULL OR cs.section.sectionId = :#{#filter.sectionId})
            AND (:#{#filter.userCode} IS NULL OR LOWER(s.usercode) LIKE %:#{#filter.userCode}%)
            AND (:#{#filter.username} IS NULL OR LOWER(s.userName) LIKE %:#{#filter.username}%) 
            AND (:#{#filter.courseCode} IS NULL OR LOWER(c.courseCode) LIKE %:#{#filter.courseCode}%) 
            AND (:#{#filter.courseName} IS NULL OR LOWER(c.courseName) LIKE %:#{#filter.courseName}%) 
            AND (:#{#filter.team} IS NULL OR LOWER(cs.team) LIKE %:#{#filter.team}%) 
            AND (:#{#filter.totalAbsence} IS NULL OR att.totalAbsence = :#{#filter.totalAbsence})
            AND (s.delFlag = false )
            AND (enroll.delFlag = false )
            AND (cs.delFlag = false )
            AND (c.delFlag = false )
            AND (att.delFlag = false )
            """,
            countQuery = """
            SELECT COUNT(DISTINCT(att.id))  
            FROM Student s 
            JOIN StudentEnrolled enroll
                ON s = enroll.student
            JOIN CourseSection cs
                ON cs = enroll.courseSection
            JOIN Course c
                ON c = cs.course
            JOIN AttendanceSheet att
                ON att.studentEnrolled = enroll
            WHERE (:#{#filter.sectionId} IS NULL OR cs.section.sectionId = :#{#filter.sectionId})
            AND (:#{#filter.userCode} IS NULL OR LOWER(s.usercode) LIKE %:#{#filter.userCode}%)
            AND (:#{#filter.username} IS NULL OR LOWER(s.userName) LIKE %:#{#filter.username}%) 
            AND (:#{#filter.courseCode} IS NULL OR LOWER(c.courseCode) LIKE %:#{#filter.courseCode}%) 
            AND (:#{#filter.courseName} IS NULL OR LOWER(c.courseName) LIKE %:#{#filter.courseName}%) 
            AND (:#{#filter.team} IS NULL OR LOWER(cs.team) LIKE %:#{#filter.team}%) 
            AND (:#{#filter.totalAbsence} IS NULL OR att.totalAbsence = :#{#filter.totalAbsence})
            AND (s.delFlag = false )
            AND (enroll.delFlag = false )
            AND (cs.delFlag = false )
            AND (c.delFlag = false )
            AND (att.delFlag = false )
            """)
    Page<StudentResponse> findStudentsWithFilter(Pageable page, FilterManagementDTO filter);

    @Query("""
            SELECT s FROM Student s 
            WHERE (s.userName = :userName)
                AND (s.usercode = :userCode)
                AND s.delFlag = false 
            """)
    Student findStudentByNameAndCode(String userName, String userCode);

    @Query("""
    SELECT new com.example.AttendanceApplication.DTO.StudentDTO (
        a.userId,
        a.userName,
        a.usercode,
        a.email,
        a.dob
    ) from Student a 
    WHERE a.delFlag = false 
    """)
    List<StudentDTO> findByDelFlagFalse();

    @Query("""
        SELECT DISTINCT 
            new com.example.AttendanceApplication.DTO.StudentDTO (
                student.userId,
                student.userName,
                student.usercode,
                student.email,
                student.dob,
                student.phone,
                student.schoolyear
            )
        FROM Student student
        WHERE (:#{#filter.userName} IS NULL OR LOWER(student.userName) LIKE %:#{#filter.userName})
            AND (:#{#filter.userCode} IS NULL OR LOWER(student.usercode) LIKE %:#{#filter.userCode}%)
            AND (:#{#filter.email} IS NULL OR LOWER(student.email) LIKE %:#{#filter.email}%) 
            AND (:#{#filter.phone} IS NULL OR LOWER(student.phone) LIKE %:#{#filter.phone}%) 
            AND (:#{#filter.schoolYear} IS NULL OR LOWER(student.schoolyear) LIKE %:#{#filter.schoolYear}%) 
            AND student.delFlag = false 
    """)
    Page<StudentDTO> findStudentsWithFilterPaging(Pageable pageable, StudentDTO filter);
}

