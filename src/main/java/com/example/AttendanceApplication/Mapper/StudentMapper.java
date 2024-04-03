package com.example.AttendanceApplication.Mapper;

import com.example.AttendanceApplication.DTO.StudentDTO;
import com.example.AttendanceApplication.DTO.StudentEnrolledDTO;
import com.example.AttendanceApplication.Model.Relation.CourseSection;
import com.example.AttendanceApplication.Model.Relation.StudentEnrolled;
import com.example.AttendanceApplication.Model.Student;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    private static final Logger logger = LoggerFactory.getLogger(Student.class);

    @Autowired
    ModelMapper modelMapper;

    public StudentDTO convertToDto(Student student) {
        try {
            StudentDTO dto = modelMapper.map(student, StudentDTO.class);
            dto.setDob(student.getAppUser().getDob());
            if (student.getAppUser().getAge() != null)
                dto.setAge(student.getAppUser().getAge());
            if (student.getAppUser().getGender() != null)
                dto.setGender(student.getAppUser().getGender());

            return dto;
        }
        catch (Exception ex) {
            throw new RuntimeException("Failed to convert student");
        }
    }

    public Student convertToEntity(StudentDTO dto) {
        try {
            return modelMapper.map(dto, Student.class);
        }
        catch (Exception ex) {
            logger.warn(ex.getMessage());
            throw new RuntimeException("Failed to convert CourseSection");
        }
    }
}
