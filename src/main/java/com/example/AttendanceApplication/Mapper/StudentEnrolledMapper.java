package com.example.AttendanceApplication.Mapper;

import com.example.AttendanceApplication.DTO.CourseSectionDTO;
import com.example.AttendanceApplication.DTO.StudentEnrolledDTO;
import com.example.AttendanceApplication.Model.Relation.CourseSection;
import com.example.AttendanceApplication.Model.Relation.StudentEnrolled;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StudentEnrolledMapper {

    private static final Logger logger = LoggerFactory.getLogger(CourseSection.class);

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    StudentMapper studentMapper;

    public StudentEnrolledDTO convertToDto(StudentEnrolled studentEnrolled) {
        try {
            StudentEnrolledDTO dto = modelMapper.map(studentEnrolled, StudentEnrolledDTO.class);

            return dto;
        }
        catch (Exception ex) {
            throw new RuntimeException("Failed to convert CourseSection");
        }
    }

    public StudentEnrolled convertToEntity(StudentEnrolledDTO dto) {
        try {
            return modelMapper.map(dto, StudentEnrolled.class);
        }
        catch (Exception ex) {
            logger.warn(ex.getMessage());
            throw new RuntimeException("Failed to convert CourseSection");
        }
    }
}
