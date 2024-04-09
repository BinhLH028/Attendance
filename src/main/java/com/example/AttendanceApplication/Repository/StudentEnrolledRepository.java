package com.example.AttendanceApplication.Repository;

import com.example.AttendanceApplication.DTO.StudentDTO;
import com.example.AttendanceApplication.Model.Relation.StudentEnrolled;
import com.example.AttendanceApplication.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface StudentEnrolledRepository extends JpaRepository<StudentEnrolled, Integer> {

    @Query("""
            SELECT enroll FROM StudentEnrolled enroll 
            WHERE (enroll.student.userId = :userId)
            AND (enroll.courseSection.id = :id)
            AND enroll.delFlag = false
            """)
    StudentEnrolled findByStudentIdAndCSId(Integer userId, Integer id);

    @Query("""
            SELECT new com.example.AttendanceApplication.DTO.StudentDTO (
                a.userId,
                a.userName,
                a.usercode,
                a.email,
                a.dob
            ) 
            FROM Student a 
            JOIN StudentEnrolled e ON a.userId = e.student.userId
            WHERE e.courseSection.id = :id
            AND e.delFlag = FALSE  
            """)
    List<StudentDTO> findStudentsByCSId(int id);

    @Query("""
            SELECT enroll FROM StudentEnrolled enroll 
            WHERE enroll.student.userId IN :request
            AND enroll.courseSection.id = :id
            AND enroll.delFlag = false 
            """)
    List<StudentEnrolled> findByIdInAndDelFlagFalse(List<Integer> request, Integer id);

    @Query("""
            SELECT enroll FROM StudentEnrolled enroll 
            WHERE (enroll.courseSection.section.sectionId = :id)
            AND enroll.delFlag = false
            """)
    Set<StudentEnrolled> findStudentEnrollsByCSId(Integer id);
}
