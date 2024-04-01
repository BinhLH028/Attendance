package com.example.AttendanceApplication.Repository;

import com.example.AttendanceApplication.Model.Relation.StudentEnrolled;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentEnrolledRepository extends JpaRepository<StudentEnrolled, Integer> {

    @Query("""
            SELECT enroll FROM StudentEnrolled enroll 
            WHERE (enroll.student.userId = :userId)
            AND (enroll.courseSection.id = :id)
            AND (enroll.courseSection.team = :team)
            AND enroll.delFlag = false
            """)
    StudentEnrolled findByStudentIdAndCSId(Integer userId, Integer id, String team);
}
