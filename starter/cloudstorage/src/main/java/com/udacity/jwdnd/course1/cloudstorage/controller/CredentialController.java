package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CredentialController {
    @Autowired
    CredentialService credentialService;
    @Autowired
    UserService userService;

    @PostMapping("/credentials")
    public String saveCredential(Authentication authentication, @ModelAttribute Credential credential, Model model){
        Integer userid = userService.getUser(authentication.getName()).getUserid();
        credential.setUserid(userid);
        credentialService.save(credential);
        model.addAttribute("success",true);
        return "result";
    }
    @GetMapping("/credentials/delete/{id}")
    public String deleteCredential(@PathVariable(value="id") int id,Model model){
        credentialService.delete(id);
        model.addAttribute("success",true);
        return "result";
    }
}
