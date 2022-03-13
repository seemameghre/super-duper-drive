package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CredentialController {
    @Autowired
    CredentialService credentialService;
    @Autowired
    UserService userService;

    @PostMapping("/credentials")
    public String saveCredential(Authentication authentication, @ModelAttribute CredentialForm credentialForm, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("tab","credentials");
        Credential newCredential = new Credential();

        Integer userid = userService.getUser(authentication.getName()).getUserid();
        newCredential.setUserid(userid);

        newCredential.setCredentialid(credentialForm.getCredentialid());
        newCredential.setUrl(credentialForm.getUrl());
        newCredential.setUsername(credentialForm.getUsername());
        newCredential.setKey(credentialForm.getKey());
        newCredential.setPassword(credentialForm.getUnencryptedPassword());
        try{
            credentialService.save(newCredential);
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("errormsg",e.getMessage());
            return "redirect:/home";
        }
        redirectAttributes.addFlashAttribute("success",true);
        return "redirect:/home";
    }
    @GetMapping("/credentials/delete/{id}")
    public String deleteCredential(@PathVariable(value="id") int id,Authentication authentication, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("tab","credentials");
        Integer userid = userService.getUser(authentication.getName()).getUserid();
        Credential credential = new Credential();
        credential.setCredentialid(id);
        credential.setUserid(userid);
        try {
            credentialService.delete(credential);
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("errormsg",e.getMessage());
            return "redirect:/home";
        }
        redirectAttributes.addFlashAttribute("success",true);
        return "redirect:/home";
    }
}
