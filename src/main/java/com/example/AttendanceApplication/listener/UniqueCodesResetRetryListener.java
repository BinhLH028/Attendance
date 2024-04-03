package com.example.AttendanceApplication.listener;

import com.example.AttendanceApplication.Service.StudentBatchService;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;

public class UniqueCodesResetRetryListener implements RetryListener {

    private final StudentBatchService studentBatchService;

    public UniqueCodesResetRetryListener(StudentBatchService studentBatchService) {
        this.studentBatchService = studentBatchService;
    }

    @Override
    public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
        return true;
    }

    @Override
    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        // Reset the uniqueCodes set before each retry attempt
        studentBatchService.resetUniqueCodes();
    }

    @Override
    public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
    }
}
