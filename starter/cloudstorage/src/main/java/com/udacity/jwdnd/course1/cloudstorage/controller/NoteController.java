package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.exception.AlreadyExistsExcepetion;
import com.udacity.jwdnd.course1.cloudstorage.exception.DoesNotExistException;
import com.udacity.jwdnd.course1.cloudstorage.exception.UserNotAuthorizedException;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class NoteController {

    @Autowired
    private NoteService noteService;
    @Autowired
    private UserService userService;

    @GetMapping("/notes/delete/{id}")
    public String delete(@PathVariable (value="id") int id, Authentication authentication, Model model, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("tab","notes");
        Integer userid = userService.getUser(authentication.getName()).getUserid();
        Note note = new Note();
        note.setNoteid(id);
        note.setUserid(userid);

        try{
            this.noteService.deleteNote(note);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errormsg",e.getMessage());
            return "redirect:/home";
        }
        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/home";
    }
    @PostMapping("/notes")
    public String saveNote(Authentication authentication, @ModelAttribute Note note, Model model, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("tab","notes");
        Integer userid = userService.getUser(authentication.getName()).getUserid();
        note.setUserid(userid);
        try{
            this.noteService.saveNote(note);
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("errormsg",e.getMessage());
            return "redirect:/home";
        }
        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/home";
    }
}
