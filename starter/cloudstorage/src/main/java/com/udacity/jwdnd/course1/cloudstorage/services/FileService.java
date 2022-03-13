package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.exception.AlreadyExistsExcepetion;
import com.udacity.jwdnd.course1.cloudstorage.exception.DoesNotExistException;
import com.udacity.jwdnd.course1.cloudstorage.exception.UserNotAuthorizedException;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    @Autowired
    private FileMapper fileMapper;

    public int save(File file) throws AlreadyExistsExcepetion {
        if(isDuplicateName(file)){
            throw new AlreadyExistsExcepetion("File already exists!");
        }
        return fileMapper.insert(file);
    }
    public int delete(File file) throws DoesNotExistException, UserNotAuthorizedException {
        if(file.getFileid() == null || getFileById(file.getFileid()) == null){
            throw new DoesNotExistException("File does not exist.");
        }else if(! isUserAuthorized(file)){
            throw new UserNotAuthorizedException("User is not authorized to delete the file.");
        }
        return fileMapper.delete(file.getFileid());
    }
    public File getFile(File file) throws DoesNotExistException, UserNotAuthorizedException {
        if(file.getFileid() == null || getFileById(file.getFileid()) == null){
            throw new DoesNotExistException("File does not exist");
        }else if(! isUserAuthorized(file)){
            throw new UserNotAuthorizedException("User not authorized to view the file.");
        }
        return getFileById(file.getFileid());
    }
    public File getFileById(Integer fileid){
        return fileMapper.getFileById(fileid);
    }
    public List<File> getFileListForUser(Integer userid){
        return fileMapper.getFileListByUserId(userid);
    }
    public List<File> getAllFilesForUser(Integer userid){
        return fileMapper.getFilesByUserId(userid);
    }
    private boolean isDuplicateName(File file){
        return getFileListForUser(file.getUserid()).stream().anyMatch(f -> f.getFilename().equals(file.getFilename()));
    }
    private boolean isUserAuthorized(File file){
        return getFileById(file.getFileid()).getUserid() == file.getUserid();
    }
}
