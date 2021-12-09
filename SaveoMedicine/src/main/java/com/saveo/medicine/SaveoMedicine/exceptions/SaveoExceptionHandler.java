package com.saveo.medicine.SaveoMedicine.exceptions;

public class SaveoExceptionHandler extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String error;
    private String message;

    public SaveoExceptionHandler(String error, String message) {
        this.error = error;
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
