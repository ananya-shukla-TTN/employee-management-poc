package com.ttn.employeemanagement.exception;

public class DuplicateDepartmentCodeException extends RuntimeException {

    public DuplicateDepartmentCodeException(String code) {
        super("Department code already exists: " + code);
    }
}
