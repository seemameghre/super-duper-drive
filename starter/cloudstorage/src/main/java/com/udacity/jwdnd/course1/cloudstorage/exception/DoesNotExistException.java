package com.udacity.jwdnd.course1.cloudstorage.exception;

public class DoesNotExistException extends Exception{
    public DoesNotExistException(String errorMessage){
        super(errorMessage);
    }
}
