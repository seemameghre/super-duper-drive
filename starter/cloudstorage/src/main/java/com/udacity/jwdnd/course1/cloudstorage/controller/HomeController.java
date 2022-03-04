package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private UserService userService;
    @Autowired
    private NoteService noteService;

    @GetMapping()
    public String homeView(Authentication authentication, Model model){
        Integer userid = userService.getUser(authentication.getName()).getUserid();
        model.addAttribute("notes",noteService.getAllNotesForUser(userid));
        model.addAttribute("note",new Note());
        return "home";
    }
}
