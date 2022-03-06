package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class NoteController {

    private NoteService noteService;
    private UserService userService;

    public NoteController(NoteService noteService,UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }
    @GetMapping("/notes/delete/{id}")
    public String delete(@PathVariable (value="id") int id, Model model){
        this.noteService.deleteNote(id);
        model.addAttribute("success",true);
        return "result";
    }
    @PostMapping("/notes")
    public String saveNote(Authentication authentication, @ModelAttribute Note note, Model model){
        Integer userid = userService.getUser(authentication.getName()).getUserid();
        note.setUserid(userid);
        this.noteService.saveNote(note);
        model.addAttribute("success",true);
        return "result";
    }

}
