package com.example.AttendanceApplication.Service;

import com.example.AttendanceApplication.Model.Course;
import com.example.AttendanceApplication.Model.Relation.CourseSection;
import com.example.AttendanceApplication.Model.Section;
import com.example.AttendanceApplication.Repository.CourseSectionRepository;
import com.example.AttendanceApplication.Repository.SectionRepository;
import com.example.AttendanceApplication.Request.SectionRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SectionService {

    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private CourseSectionRepository csRepo;
    @Autowired
    private CourseSectionService csService;
    @Autowired
    MessageSource messageSource;

    private String msg = "";

    public ResponseEntity getSections() {
        List<Section> sections = sectionRepository.findByDelFlagFalse();
        Collections.sort(sections, Comparator.comparingInt(entity -> Integer.parseInt(entity.getYear().split("-")[0])));
        if (sections.size() <= 0 ) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(sections,HttpStatus.OK);
    }


    public ResponseEntity addSection(SectionRequest request) {
        if (validateRequest(request)){
            Section section = new Section(request.getSemester(), request.getYear());
            sectionRepository.save(section);
            msg = messageSource.getMessage("S01",
                    new String[]{request.getSemester().toString(),request.getYear()}, Locale.getDefault());
            return new ResponseEntity(msg,HttpStatus.OK);
        }

        return new ResponseEntity(msg,HttpStatus.BAD_REQUEST);
    }

    private boolean validateRequest(SectionRequest request) {
        boolean isValid = true;

        if (sectionRepository.semesterPerYear(request.getYear()) >= 2) {
            isValid = false;
            msg = messageSource.getMessage("S03",
                    new String[]{}, Locale.getDefault());
        }
        Optional<Section> section =
                sectionRepository.findBySemesterAndYear(request.getSemester(),request.getYear());
        if (!section.isEmpty()){
            isValid = false;
            msg = messageSource.getMessage("S02",
                    new String[]{request.getSemester().toString(),request.getYear().toString()}, Locale.getDefault());
        }
        return isValid;
    }

    @Transactional
    public ResponseEntity<?> deleteSection(int id) {
        Section temp = sectionRepository.findSectionById(id);

        if (temp == null) {
            msg = messageSource.getMessage("S04",
                    new String[]{String.valueOf(id)}, Locale.getDefault());
            return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
        }

        temp.delFlag = true;
        sectionRepository.save(temp);

        List<CourseSection> listCs = csRepo.findByCourseIdAndDelFlagFalse(id);
        listCs.forEach(cs -> csService.deleteCourseSection(cs.getId()));

        msg = messageSource.getMessage("S05",
                new String[]{String.valueOf(temp.getSemester()), temp.getYear()}, Locale.getDefault());
        return new ResponseEntity(msg, HttpStatus.OK);
    }
}
