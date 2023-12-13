package com.example.AttendanceApplication.Service;

import com.example.AttendanceApplication.Model.Section;
import com.example.AttendanceApplication.Repository.SectionRepository;
import com.example.AttendanceApplication.Request.SectionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class SectionService {

    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    MessageSource messageSource;

    private String msg = "";

    public ResponseEntity getSections() {
        List<Section> sections = sectionRepository.findAll();
        if (sections.size() <= 0 ) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(sections,HttpStatus.OK);
    }


    public ResponseEntity addSection(SectionRequest request) {
        if (validateRequest(request)){
            Section section = new Section(request.getSemester(), request.getYear());
            sectionRepository.save(section);
            msg = messageSource.getMessage("05",
                    new String[]{request.getSemester().toString(),request.getYear().toString()}, Locale.getDefault());
            return new ResponseEntity(msg,HttpStatus.OK);
        }

        return new ResponseEntity(msg,HttpStatus.BAD_REQUEST);
    }

    private boolean validateRequest(SectionRequest request) {
        boolean isValid = true;

        if (sectionRepository.semesterPerYear(request.getYear()) >= 2) {
            isValid = false;
            msg = messageSource.getMessage("07",
                    new String[]{}, Locale.getDefault());
        }
        Optional<Section> section =
                sectionRepository.findBySemesterAndYear(request.getSemester(),request.getYear());
        if (!section.isEmpty()){
            isValid = false;
            msg = messageSource.getMessage("06",
                    new String[]{request.getSemester().toString(),request.getYear().toString()}, Locale.getDefault());
        }
        return isValid;
    }
}
