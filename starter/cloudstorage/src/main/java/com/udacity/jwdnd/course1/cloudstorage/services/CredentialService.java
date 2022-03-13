package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.exception.DoesNotExistException;
import com.udacity.jwdnd.course1.cloudstorage.exception.UserNotAuthorizedException;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CredentialService {
    @Autowired
    private CredentialMapper credentialMapper;
    @Autowired
    private EncryptionService encryptionService;

    public int save(Credential credential) throws DoesNotExistException, UserNotAuthorizedException {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);
        credential.setPassword(encryptedPassword);
        credential.setKey(encodedKey);

        if(credential.getCredentialid() == null){
            //Insert
            return credentialMapper.insert(credential);
        }else if(getCredentialById(credential.getCredentialid()) == null){
            //Trying to update but does not exist in DB
            throw new DoesNotExistException("Credential does not exist.");
        }else if(! isUserAuthorized(credential)){
            throw new UserNotAuthorizedException("User not authorized to edit credential.");
        }else {
            return credentialMapper.update(credential);
        }
    }
    public int delete(Credential credential) throws DoesNotExistException, UserNotAuthorizedException {
        if(credential.getCredentialid() == null || getCredentialById(credential.getCredentialid()) == null){
            throw new DoesNotExistException("Credential does not exist.");
        }else if(! isUserAuthorized(credential)){
            throw new UserNotAuthorizedException("User not authorized to delete credential.");
        }
        return credentialMapper.delete(credential.getCredentialid());
    }

    public List<CredentialForm> getAllCredentialsForUser(Integer userid){
        List<Credential> credentials =  credentialMapper.getCredentialsByUserId(userid);
        List<CredentialForm> credentialForms = new ArrayList<>();
        if(credentials == null || credentials.isEmpty()){
            return credentialForms;
        }else {

            return credentials.stream().map(c -> getCredentialForm(c)).collect(Collectors.toList());
        }
    }
    private CredentialForm getCredentialForm(Credential credential){
        CredentialForm cf = new CredentialForm();
        if(credential != null){
            cf.setCredentialid(credential.getCredentialid());
            cf.setUserid(credential.getUserid());
            cf.setUrl(credential.getUrl());
            cf.setUsername(credential.getUsername());
            cf.setEncryptedPassword(credential.getPassword());
            cf.setKey(credential.getKey());
            cf.setUnencryptedPassword(encryptionService.decryptValue(credential.getPassword(),credential.getKey()));
        }
        return cf;
    }
    public Credential getCredentialById(Integer credentialid){
        return credentialMapper.getCredentialById(credentialid);
    }
    private Credential decryptPassword(Credential c){
        String decryptedPassword = encryptionService.decryptValue(c.getPassword(), c.getKey());
        c.setPassword(decryptedPassword);
        return c;
    }
    private boolean isUserAuthorized(Credential credential){
        return credential.getUserid() == getCredentialById(credential.getCredentialid()).getUserid();
    }
}
