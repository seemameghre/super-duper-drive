package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.exception.AlreadyExistsExcepetion;
import com.udacity.jwdnd.course1.cloudstorage.exception.DoesNotExistException;
import com.udacity.jwdnd.course1.cloudstorage.exception.UserNotAuthorizedException;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteMapper noteMapper;

    public int saveNote(Note note) throws AlreadyExistsExcepetion, DoesNotExistException, UserNotAuthorizedException {
        if(note.getNoteid() == null){
            //Insert operation
            if(isDuplicate(note.getNotetitle(), note.getUserid()))
                throw new AlreadyExistsExcepetion("Note title already exists!");
            else
                return noteMapper.insert(note);
        }
        else{
            //Update operation
            if(getNote(note.getNoteid()) == null){
                //There is no note in DB with this id
                throw new DoesNotExistException("Note does not exist.");
            }else if(!isUserAuthorized(note)){
                //Note does not belong to the logged in user
                throw new UserNotAuthorizedException("Unauthorized operation");
            }
            return noteMapper.update(note);
        }

    }

    public int deleteNote(Note note) throws DoesNotExistException, UserNotAuthorizedException {
        if(note.getNoteid() == null || getNote(note.getNoteid()) == null){
            throw new DoesNotExistException("Note does not exist.");
        }else if(! isUserAuthorized(note)){
            throw new UserNotAuthorizedException("Unauthorized to delete the note.");
        }
        return noteMapper.delete(note.getNoteid());
    }

    public Note getNote(Integer noteId){
        return noteMapper.getNoteById(noteId);
    }

    public List<Note> getAllNotesForUser(Integer userId){
        return noteMapper.getNotesByUserId(userId);
    }

    private boolean isDuplicate(String notetitle, Integer userid){
        return getAllNotesForUser(userid).stream().anyMatch(n -> n.getNotetitle().equals(notetitle));
    }
    private boolean isUserAuthorized(Integer userid, Integer noteid){
        return getNote(noteid).getUserid() == userid;
    }
    private boolean isUserAuthorized(Note note){
        return note.getUserid() == getNote(note.getNoteid()).getUserid();
    }
}
