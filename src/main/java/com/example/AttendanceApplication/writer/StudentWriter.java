package com.example.AttendanceApplication.writer;

import com.example.AttendanceApplication.Model.Student;
import com.example.AttendanceApplication.Repository.StudentRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StudentWriter implements ItemWriter<Student> {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public void write(Chunk<? extends Student> chunk) throws Exception {
        System.out.println("Thread Name : -"+Thread.currentThread().getName());
        studentRepository.saveAll(chunk);
    }
}
