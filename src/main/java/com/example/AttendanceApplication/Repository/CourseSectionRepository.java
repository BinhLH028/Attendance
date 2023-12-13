package com.example.AttendanceApplication.Repository;

import com.example.AttendanceApplication.DTO.CourseSectionDTO;
import com.example.AttendanceApplication.Model.Course;
import com.example.AttendanceApplication.Model.Relation.CourseSection;
import com.example.AttendanceApplication.Request.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseSectionRepository extends JpaRepository<CourseSection, Integer> {

    @Query("""
            SELECT cs FROM CourseSection cs WHERE
            (cs.course.courseId = :courseId)
            AND (cs.section.sectionId = :sectionId)
            """)
    CourseSection findbySectionAndCourse(Integer sectionId, Integer courseId);

    @Query("""
            SELECT cs FROM CourseSection cs WHERE
            (cs.id = :id)
            """)
    CourseSection findbyCSId(Integer id);

    @Query("""
            SELECT c FROM Course c 
            LEFT JOIN CourseSection cs ON cs.course.courseId = c.courseId
            LEFT JOIN Section s ON s.sectionId = cs.section.sectionId
            LEFT JOIN StudentEnrolled se ON se.courseSection.id = cs.id
            WHERE
            (cs.section.sectionId = :sectionId)
            AND (se.student.userId = :user)
            """)
    List<Course> findStudentCourseByUserAndSection(int sectionId, int user);


    @Query("""
            SELECT DISTINCT
            new com.example.AttendanceApplication.DTO.CourseSectionDTO(
                cs.id,
                cs.course.courseId,
                cs.section.sectionId,
                c.courseCode,
                c.courseName
            ) FROM Course c 
            LEFT JOIN CourseSection cs ON cs.course.courseId = c.courseId
            LEFT JOIN Section s ON s.sectionId = cs.section.sectionId
            LEFT JOIN StudentEnrolled se ON se.courseSection.id = cs.id
            WHERE
            (cs.section.sectionId = :sectionId)
            AND (se.student.userId = :user)
            """)
    List<CourseSectionDTO> findStudentCourseInfoByUserAndSection(int sectionId, int user);

    @Query("""
            SELECT DISTINCT
            new com.example.AttendanceApplication.DTO.CourseSectionDTO(
                cs.id,
                cs.course.courseId,
                cs.section.sectionId,
                c.courseCode,
                c.courseName
            ) FROM Course c 
            LEFT JOIN CourseSection cs ON cs.course.courseId = c.courseId
            LEFT JOIN Section s ON s.sectionId = cs.section.sectionId
            LEFT JOIN TeacherTeach tt ON tt.courseSection.id = cs.id
            WHERE
            (cs.section.sectionId = :sectionId)
            AND (tt.teacher.userId = :user)
            """)
    List<CourseSectionDTO> findTeacherCourseInfoByUserAndSection(int sectionId, int user);

//    List<Course> findTeacherCourseByUserAndSection(int sectionId, int user);
}
