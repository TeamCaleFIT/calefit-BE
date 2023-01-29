package com.calefit.workout.exception;

import com.calefit.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotFoundWorkoutException extends BusinessException {

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
