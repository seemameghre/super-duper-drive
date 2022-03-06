package com.udacity.jwdnd.course1.cloudstorage.services;

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

    public int saveNote(Note note){
        if(note.getNoteid() == null)
            return noteMapper.insert(note);
        else
            return noteMapper.update(note);
    }

    public int deleteNote(Integer noteId){
        return noteMapper.delete(noteId);
    }

    public Note getNote(Integer noteId){
        return noteMapper.getNoteById(noteId);
    }

    public List<Note> getAllNotesForUser(Integer userId){
        return noteMapper.getNotesByUserId(userId);
    }
}
