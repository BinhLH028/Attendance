package com.example.AttendanceApplication.Repository;

import com.example.AttendanceApplication.DTO.TeacherDTO;
import com.example.AttendanceApplication.Model.Relation.TeacherTeach;
import com.example.AttendanceApplication.Model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherTeachRepository extends JpaRepository<TeacherTeach,Integer> {


    @Query("""
            SELECT t FROM TeacherTeach t 
            WHERE (t.teacher.userId = :userId)
                AND (t.courseSection.id = :id)
                AND t.delFlag = false
            """)
    TeacherTeach findByTeacherIdAndCSId(Integer userId, Integer id);

    @Query("""
            SELECT new com.example.AttendanceApplication.DTO.TeacherDTO (
                a.userId,
                a.userName,
                a.email,
                a.dob
            ) 
            FROM AppUser a 
            JOIN TeacherTeach tt ON a.userId = tt.teacher.userId
            WHERE tt.courseSection.id = :id
            AND tt.delFlag = FALSE  
            """)
    List<TeacherDTO> findTeachersByCSId(int id);

    @Query("""
            SELECT t FROM TeacherTeach t 
            WHERE t.teacher.userId IN :request
            AND t.courseSection.id = :id
            AND t.delFlag = false 
            """)
    List<TeacherTeach> findByIdInAndDelFlagFalse(List<Integer> request, Integer id);
}
