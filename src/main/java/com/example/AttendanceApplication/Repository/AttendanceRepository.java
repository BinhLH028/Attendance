package com.example.AttendanceApplication.Repository;

import com.example.AttendanceApplication.Model.AppUser;
import com.example.AttendanceApplication.Model.AttendanceSheet;
import com.example.AttendanceApplication.Model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceSheet, Integer> {


    @Query("""
            SELECT data FROM AttendanceSheet data 
            WHERE (data.id = :id)
                AND data.delFlag = false 
            """)
    AttendanceSheet findSheetById(Integer id);



    @Query("""
            SELECT data FROM AttendanceSheet data 
            JOIN StudentEnrolled enroll ON (data.id = enroll.id)
            WHERE (enroll.student.userId = :id)
                AND enroll.courseSection.id = :cs
                AND enroll.delFlag = false 
                AND data.delFlag = false 
            """)
    AttendanceSheet findSheetByStudentIdAndCSId(Integer id, int cs);
}
