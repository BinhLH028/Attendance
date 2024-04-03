package com.example.AttendanceApplication.Mapper;

import com.example.AttendanceApplication.DTO.CourseSectionDTO;
import com.example.AttendanceApplication.Model.Relation.CourseSection;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CourseSectionMapper {

    private static final Logger logger = LoggerFactory.getLogger(CourseSection.class);

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    StudentEnrolledMapper studentEnrolledMapper;

    public CourseSectionDTO convertToDto(CourseSection courseSection) {
        try {
            CourseSectionDTO dto = modelMapper.map(courseSection, CourseSectionDTO.class);

            return dto;
        }
        catch (Exception ex) {
            throw new RuntimeException("Failed to convert CourseSection");
        }
    }

    public CourseSection convertToEntity(CourseSectionDTO dto) {
        try {
            return modelMapper.map(dto, CourseSection.class);
        }
        catch (Exception ex) {
            logger.warn(ex.getMessage());
            throw new RuntimeException("Failed to convert CourseSection");
        }
    }
}
