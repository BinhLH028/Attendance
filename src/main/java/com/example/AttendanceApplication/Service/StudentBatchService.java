package com.example.AttendanceApplication.Service;

import com.example.AttendanceApplication.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class StudentBatchService {

    @Autowired
    private StudentRepository studentRepository;

    private Set<String> uniqueCodes = new HashSet<>();

    public Set<Integer> getAllStudentCode() {
        // Fetch all existing codes from the database
        List<Integer> codeList = studentRepository.findAllStudentCode();
        return new HashSet<>(codeList);
    }

    public boolean codeExists(String code, Set<Integer> existingCodes) {
        // Check if the email exists in the set of existing emails
        return existingCodes.contains(code);
    }

    public boolean isDuplicate(String code) {
        if (uniqueCodes.contains(code)) {
            // Duplicate email found
            return true;
        } else {
            // Unique email, add it to the set
            uniqueCodes.add(code);
            return false;
        }
    }

    public void resetUniqueCodes() {
        uniqueCodes.clear();
    }
}
