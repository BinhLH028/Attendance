package com.example.AttendanceApplication.Repository;

import com.example.AttendanceApplication.Model.Relation.TeacherTeach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherTeachRepository extends JpaRepository<TeacherTeach,Integer> {


    @Query("""
            SELECT t FROM TeacherTeach t WHERE
            (t.teacher.userId = :userId)
            AND (t.courseSection.id = :id)
            """)
    TeacherTeach findByTeacherIdAndCSId(Integer userId, Integer id);
}
