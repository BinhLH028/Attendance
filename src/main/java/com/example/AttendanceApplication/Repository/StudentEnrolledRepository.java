package com.example.AttendanceApplication.Repository;

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
            SELECT a FROM AppUser a 
            JOIN StudentEnrolled e ON a.userId = e.student.userId
            WHERE e.courseSection.id = :id
            AND e.delFlag = FALSE  
            """)
    List<Student> findStudentsByCSId(int id);

    List<StudentEnrolled> findByIdInAndDelFlagFalse(List<Integer> request);

    @Query("""
            SELECT enroll FROM StudentEnrolled enroll 
            WHERE (enroll.courseSection.section.sectionId = :id)
            AND enroll.delFlag = false
            """)
    Set<StudentEnrolled> findStudentEnrollsByCSId(Integer id);
}
