package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class MaxFileSizeExceededHandler {
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public RedirectView handleFileSizeException(MaxUploadSizeExceededException ex, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errormsg","File size exceeded the limit of 1MB.");
        return new RedirectView("/home");
    }
}
