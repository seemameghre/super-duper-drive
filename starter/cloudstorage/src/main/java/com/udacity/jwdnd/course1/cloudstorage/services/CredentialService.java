package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CredentialService {
    @Autowired
    private CredentialMapper credentialMapper;
    @Autowired
    private EncryptionService encryptionService;

    public int save(Credential credential){
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);
        credential.setPassword(encryptedPassword);
        credential.setKey(encodedKey);

        if(credential.getCredentialid() != null){
            return credentialMapper.update(credential);
        }else {
            return credentialMapper.insert(credential);
        }
    }
    public int delete(Integer credentialid){
        return credentialMapper.delete(credentialid);
    }

    public List<Credential> getAllCredentialsForUser(Integer userid){
        List<Credential> credentials =  credentialMapper.getCredentialsByUserId(userid);
        if(credentials == null || credentials.isEmpty()){
            return credentials;
        }else {
            return credentials.stream().map(c -> decryptPassword(c)).collect(Collectors.toList());
        }
    }
    public Credential getCredentialById(Integer credentialid){
        Credential credential = credentialMapper.getCredentialById(credentialid);
        return decryptPassword(credential);
    }
    private Credential decryptPassword(Credential c){
        String decryptedPassword = encryptionService.decryptValue(c.getPassword(), c.getKey());
        c.setPassword(decryptedPassword);
        return c;
    }
}
