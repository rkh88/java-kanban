package service;

import java.io.IOException;

public class TaskValidationException extends RuntimeException {

    public TaskValidationException (String message) {
        super(message);
    }
}
