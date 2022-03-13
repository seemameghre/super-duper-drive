package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class FileController {
    @Autowired
    private FileService fileService;
    @Autowired
    private UserService userService;

    @PostMapping("/files")
    public String saveFile(Authentication authentication, @RequestParam("fileUpload") MultipartFile fileUpload, RedirectAttributes redirectAttributes){
        File newFile = new File();
        Integer userid = userService.getUser(authentication.getName()).getUserid();
        newFile.setUserid(userid);
        newFile.setFilename(fileUpload.getOriginalFilename());
        newFile.setFilesize(fileUpload.getSize());
        newFile.setContenttype(fileUpload.getContentType());
        try{
            newFile.setFiledata(fileUpload.getBytes());
            fileService.save(newFile);
        }catch (Exception ex){
            redirectAttributes.addFlashAttribute("errormsg",ex.getMessage());
            return "redirect:/home";
        }
        redirectAttributes.addFlashAttribute("success",true);
        return "redirect:/home";
    }

    @GetMapping("/files/view/{id}")
    @ResponseBody
    public ResponseEntity viewFile(@PathVariable(value="id") int id, Authentication authentication, RedirectAttributes redirectAttributes) {
        Integer userid = userService.getUser(authentication.getName()).getUserid();
        File fileToView = new File();
        fileToView.setUserid(userid);
        fileToView.setFileid(id);

        try{
            File file = fileService.getFile(fileToView);
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + file.getFilename() + "\"").body(new ByteArrayResource(file.getFiledata()));
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("errormsg",e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/files/delete/{id}")
    public String deleteFile(@PathVariable(value="id") int id, Authentication authentication, RedirectAttributes redirectAttributes){

        Integer userid = userService.getUser(authentication.getName()).getUserid();
        File fileToDelete = new File();
        fileToDelete.setFileid(id);
        fileToDelete.setUserid(userid);
        try {
            fileService.delete(fileToDelete);
        }catch (Exception ex){
            redirectAttributes.addFlashAttribute("errormsg",ex.getMessage());
            return "redirect:/home";
        }
        redirectAttributes.addFlashAttribute("success",true);
        return "redirect:/home";
    }
}
