package com.example.AttendanceApplication.Config;

import com.example.AttendanceApplication.DTO.StudentDTO;
import com.example.AttendanceApplication.Model.Student;
import com.example.AttendanceApplication.Repository.StudentRepository;
import com.example.AttendanceApplication.Service.StudentBatchService;
import com.example.AttendanceApplication.partition.ColumnRangePartition;
import com.example.AttendanceApplication.processor.StudentProcessor;
import com.example.AttendanceApplication.reader.StudentReader;
import com.example.AttendanceApplication.writer.StudentWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

@Configuration
public class SpringBatchConfig {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private StudentRepository repository;

    @Autowired
    private StudentBatchService studentBatchService;

    @Autowired
    private StudentWriter studentWriter;

//    @Bean
//    public FlatFileItemReader<StudentDTO> reader() {
//        FlatFileItemReader<StudentDTO> itemReader = new FlatFileItemReader<>();
//        itemReader.setResource(new FileSystemResource("src/main/resources/data/csv/1000data.csv"));
//        itemReader.setName("csvReader");
//        itemReader.setLinesToSkip(1);
//        itemReader.setLineMapper(lineMapper());
//        return itemReader;
//    }
    @Bean
    public StudentReader reader() {
        try {
            return new StudentReader(new FileSystemResource("src/main/resources/data/csv/1000data.csv"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public StudentProcessor processor() {
        return new StudentProcessor();
    }

    @Bean
    public ColumnRangePartition partitioner() {
        return new ColumnRangePartition();
    }

    @Bean
    public PartitionHandler partitionHandler() {
        TaskExecutorPartitionHandler taskExecutorPartitionHandler = new TaskExecutorPartitionHandler();
        taskExecutorPartitionHandler.setGridSize(10);
        taskExecutorPartitionHandler.setTaskExecutor(taskExecutor());
        taskExecutorPartitionHandler.setStep(slaveStep());
        return taskExecutorPartitionHandler;
    }

    @Bean
    public Step slaveStep() {
        return new StepBuilder("csvImportSlave", jobRepository)
                .<StudentDTO, Student>chunk(100, platformTransactionManager)
                .reader(reader())
                .processor(processor())
                .writer(studentWriter)
                .faultTolerant()
//                .retryLimit(2)
//                .retry(IllegalArgumentException.class)
//                .listener(new UniqueCodesResetRetryListener(studentBatchService)) // Register retry listener
                .build();
    }

    @Bean
    public Step masterStep() {
        return new StepBuilder("csvImportMaster", jobRepository)
                .partitioner(slaveStep().getName(), partitioner())
                .partitionHandler(partitionHandler())
                .build();
    }


//    @Bean
//    @Transactional
//    public RepositoryItemWriter<Student> writer() {
//        RepositoryItemWriter<Student> writer = new RepositoryItemWriter<>();
//        writer.setRepository(repository);
//        writer.setMethodName("save");
//        return writer;
//    }

//    @Bean
//    public Step step1() {
//        return new StepBuilder("csvImport", jobRepository)
//                .<StudentDTO, Student>chunk(100, platformTransactionManager)
//                .reader(reader())
//                .processor(processor())
//                .writer(writer())
//                .taskExecutor(taskExecutor())
//                .faultTolerant()
//                .build();
//    }

//    @Bean
//    public Job runJob() {
//        return new JobBuilder("importStudents", jobRepository)
//                .start(step1())
//                .build();
//    }

    @Bean
    public Job runJob() {
        return new JobBuilder("importStudents", jobRepository)
                .flow(masterStep()).end().build();

    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setQueueCapacity(10);
        return taskExecutor;
    }

    private LineMapper<StudentDTO> lineMapper() {
        DefaultLineMapper<StudentDTO> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("userName", "userCode", "dob", "gender");

        BeanWrapperFieldSetMapper<StudentDTO> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(StudentDTO.class);

        // Register custom converter for DOB field
        fieldSetMapper.setCustomEditors(Collections.singletonMap(Date.class, new CustomDateEditor()));

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
            } catch (ParseException e) {
                throw new IllegalArgumentException("Invalid date format", e);
            }
        }
    }
}

