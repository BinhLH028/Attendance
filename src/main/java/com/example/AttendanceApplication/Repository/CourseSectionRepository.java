package com.example.AttendanceApplication.Repository;

import com.example.AttendanceApplication.DTO.CourseSectionDTO;
import com.example.AttendanceApplication.Model.Course;
import com.example.AttendanceApplication.Model.Relation.CourseSection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseSectionRepository extends JpaRepository<CourseSection, Integer> {

    @Query("""
            SELECT cs FROM CourseSection cs 
            WHERE (cs.course.courseId = :courseId)
                AND (cs.section.sectionId = :sectionId)
                AND (cs.team = :team)
                AND cs.delFlag = false 
            """)
    CourseSection findbySectionAndCourse(Integer sectionId, Integer courseId, String team);

    @Query("""
            SELECT cs FROM CourseSection cs 
            WHERE (cs.id = :id)
                AND cs.delFlag = false 
            """)
    CourseSection findbyCSId(Integer id);

    @Query("""
            SELECT c FROM Course c 
                LEFT JOIN CourseSection cs ON cs.course.courseId = c.courseId
                LEFT JOIN Section s ON s.sectionId = cs.section.sectionId
                LEFT JOIN StudentEnrolled se ON se.courseSection = cs
            WHERE
            (cs.section.sectionId = :sectionId)
                AND (se.student.userId = :user)
                AND c.delFlag = false 
                AND cs.delFlag = false 
                AND s.delFlag = false 
                AND se.delFlag = false 
            """)
    List<Course> findStudentCourseByUserAndSection(int sectionId, int user);


    @Query("""
            SELECT DISTINCT
            new com.example.AttendanceApplication.DTO.CourseSectionDTO(
                cs.id,
                cs.course.courseId,
                cs.section.sectionId,
                c.courseCode,
                c.courseName,
                cs.room,
                cs.team
            ) FROM Course c 
                LEFT JOIN CourseSection cs ON cs.course.courseId = c.courseId
                LEFT JOIN Section s ON s.sectionId = cs.section.sectionId
                LEFT JOIN StudentEnrolled se ON se.courseSection = cs
            WHERE
            (cs.section.sectionId = :sectionId)
                AND (se.student.userId = :user)
                AND c.delFlag = false 
                AND cs.delFlag = false 
                AND s.delFlag = false 
                AND se.delFlag = false 
            """)
    List<CourseSectionDTO> findStudentCourseInfoByUserAndSection(int sectionId, int user);

    @Query("""
            SELECT DISTINCT
            new com.example.AttendanceApplication.DTO.CourseSectionDTO(
                cs.id,
                cs.course.courseId,
                cs.section.sectionId,
                c.courseCode,
                c.courseName,
                cs.room,
                cs.team
            ) FROM Course c 
                LEFT JOIN CourseSection cs ON cs.course.courseId = c.courseId
                LEFT JOIN Section s ON s.sectionId = cs.section.sectionId
                LEFT JOIN TeacherTeach tt ON tt.courseSection = cs
            WHERE
            (cs.section.sectionId = :sectionId)
                AND (tt.teacher.userId = :user)
                AND c.delFlag = false 
                AND cs.delFlag = false 
                AND s.delFlag = false 
                AND tt.delFlag = false 
            """)
    List<CourseSectionDTO> findTeacherCourseInfoByUserAndSection(int sectionId, int user);

    @Query(value = """
            SELECT DISTINCT
            new com.example.AttendanceApplication.DTO.CourseSectionDTO(
                cs.id,
                c.courseId,
                s.sectionId,
                c.courseCode,
                c.courseName,
                cs.room,
                cs.team
            ) FROM Course c 
                JOIN CourseSection cs ON cs.course.courseId = c.courseId
                LEFT JOIN Section s ON s.sectionId = cs.section.sectionId
            WHERE
            (s.sectionId = :sectionId)
                AND c.delFlag = false 
                AND cs.delFlag = false 
                AND s.delFlag = false 
            """, countQuery = """
            SELECT COUNT(DISTINCT(cs.id)) FROM Course c 
                JOIN CourseSection cs ON cs.course.courseId = c.courseId
                LEFT JOIN Section s ON s.sectionId = cs.section.sectionId
            WHERE
            (s.sectionId = :sectionId)
                AND c.delFlag = false 
                AND cs.delFlag = false 
                AND s.delFlag = false 
            """
    )
    Page<CourseSectionDTO> findCourseInfoBySection(int sectionId, Pageable pageable);

    @Query("""
        SELECT s.student.userId
        FROM StudentEnrolled s
        JOIN CourseSection cs on cs = s.courseSection
        WHERE s.student.userId NOT IN :listStudentId
        AND cs.id = :cs
        AND s.delFlag = false 
        AND cs.delFlag = false 
    """)
    List<Integer> findStudentsNotIn(List<Integer> listStudentId, Integer cs);


    @Query("""
            SELECT cs FROM CourseSection cs 
            WHERE (cs.course.courseId = :courseId)
                AND cs.delFlag = false 
            """)
    List<CourseSection> findByCourseIdAndDelFlagFalse(int courseId);
}
