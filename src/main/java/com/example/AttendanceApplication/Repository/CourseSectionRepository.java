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
            SELECT cs FROM CourseSection cs 
            WHERE (cs.course.courseId = :courseId)
                AND (cs.section.sectionId = :sectionId)
                AND cs.delFlag = false 
            """)
    CourseSection findbySectionAndCourse(Integer sectionId, Integer courseId);

    @Query("""
            SELECT cs FROM CourseSection cs 
            WHERE (cs.Id = :id)
                AND cs.delFlag = false 
            """)
    CourseSection findbyCSId(Integer id);

    @Query("""
            SELECT c FROM Course c 
                LEFT JOIN CourseSection cs ON cs.course.courseId = c.courseId
                LEFT JOIN Section s ON s.sectionId = cs.section.sectionId
                LEFT JOIN StudentEnrolled se ON se.courseSection.Id = cs.Id
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
                cs.Id,
                cs.course.courseId,
                cs.section.sectionId,
                c.courseCode,
                c.courseName
            ) FROM Course c 
                LEFT JOIN CourseSection cs ON cs.course.courseId = c.courseId
                LEFT JOIN Section s ON s.sectionId = cs.section.sectionId
                LEFT JOIN StudentEnrolled se ON se.courseSection.Id = cs.Id
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
                cs.Id,
                cs.course.courseId,
                cs.section.sectionId,
                c.courseCode,
                c.courseName
            ) FROM Course c 
                LEFT JOIN CourseSection cs ON cs.course.courseId = c.courseId
                LEFT JOIN Section s ON s.sectionId = cs.section.sectionId
                LEFT JOIN TeacherTeach tt ON tt.courseSection.Id = cs.Id
            WHERE
            (cs.section.sectionId = :sectionId)
                AND (tt.teacher.userId = :user)
                AND c.delFlag = false 
                AND cs.delFlag = false 
                AND s.delFlag = false 
                AND tt.delFlag = false 
            """)
    List<CourseSectionDTO> findTeacherCourseInfoByUserAndSection(int sectionId, int user);

}
