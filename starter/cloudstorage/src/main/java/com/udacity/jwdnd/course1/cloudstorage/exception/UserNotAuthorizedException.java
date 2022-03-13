package com.udacity.jwdnd.course1.cloudstorage.exception;

public class UserNotAuthorizedException extends Exception{
    public UserNotAuthorizedException(String errorMessage){
        super(errorMessage);
    }
}
