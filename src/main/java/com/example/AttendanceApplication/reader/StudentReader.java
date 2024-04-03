package com.example.AttendanceApplication.reader;

import com.example.AttendanceApplication.Config.SpringBatchConfig;
import com.example.AttendanceApplication.DTO.StudentDTO;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.BufferedReaderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.SingleItemPeekableItemReader;
import org.springframework.core.io.Resource;

import java.beans.PropertyEditorSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

public class StudentReader implements ItemStreamReader<StudentDTO>, ResourceAwareItemReaderItemStream<StudentDTO> {

    private SingleItemPeekableItemReader<StudentDTO> delegate;

    public StudentReader(Resource resource) throws IOException {
        this.delegate = new SingleItemPeekableItemReader<>();
        this.delegate.setDelegate(createFlatFileItemReader(resource));
    }

    @Override
    public StudentDTO read() throws Exception, UnexpectedInputException, ParseException {
        return delegate.read();
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        delegate.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        delegate.update(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        delegate.close();
    }

    @Override
    public void setResource(Resource resource) {
        // This should not be necessary as we set the resource in the constructor
    }

    private FlatFileItemReader<StudentDTO> createFlatFileItemReader(Resource resource) throws IOException {
        FlatFileItemReader<StudentDTO> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(resource);
        flatFileItemReader.setLineMapper(createLineMapper());
        flatFileItemReader.setBufferedReaderFactory(new NonClosingBufferedReaderFactory());
        flatFileItemReader.setLinesToSkip(1); // Skip the first line
        return flatFileItemReader;
    }

    private static class NonClosingBufferedReaderFactory implements BufferedReaderFactory {

        @Override
        public BufferedReader create(Resource resource, String encoding) throws IOException {
            return resource == null ? null : new NonClosingBufferedReader(resource.getInputStream(), encoding);
        }
    }

    private static class NonClosingBufferedReader extends BufferedReader {

        NonClosingBufferedReader(java.io.InputStream in, String charsetName) throws java.io.UnsupportedEncodingException {
            super(new java.io.InputStreamReader(in, charsetName));
        }

        @Override
        public void close() throws IOException {
            // Do nothing
        }
    }

    private DefaultLineMapper<StudentDTO> createLineMapper() {
        DefaultLineMapper<StudentDTO> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("userName", "userCode", "dob", "gender");

        BeanWrapperFieldSetMapper<StudentDTO> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(StudentDTO.class);

        // Register custom converter for DOB field
        fieldSetMapper.setCustomEditors(Collections.singletonMap(Date.class, new StudentReader.CustomDateEditor()));

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    // Custom converter for Date field
    private  class CustomDateEditor extends PropertyEditorSupport {
        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date date = formatter.parse(text);
                setValue(date);
            } catch (java.text.ParseException e) {
                throw new IllegalArgumentException("Invalid date format", e);
            }
        }
    }
}
