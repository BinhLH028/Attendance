package com.example.AttendanceApplication.Repository;

import com.example.AttendanceApplication.Model.Relation.TeacherTeach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherTeachRepository extends JpaRepository<TeacherTeach,Integer> {


    @Query("""
            SELECT t FROM TeacherTeach t 
            WHERE (t.teacher.userId = :userId)
                AND (t.courseSection.Id = :id)
                AND t.delFlag = false 
            """)
    TeacherTeach findByTeacherIdAndCSId(Integer userId, Integer id);

    List<TeacherTeach> findByIdInAndDelFlagFalse(List<Integer> request);
}
