package service;

import java.io.IOException;

public class ManagerSaveException extends IOException {

    public ManagerSaveException(String message, IOException e) {
        super(message);
    }
}
