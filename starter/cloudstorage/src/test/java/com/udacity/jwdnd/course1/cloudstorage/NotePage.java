package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class NotePage {
    public NotePage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
        this.webDriverWait = new WebDriverWait(webDriver, 2);
    }
    private WebDriverWait webDriverWait;

    @FindBy(id = "nav-notes-tab")
    private WebElement notesTab;

    @FindBy(id = "add-note-button")
    private WebElement addNoteButton;

    @FindBy(id = "noteModalLabel")
    private WebElement noteModal;

    @FindBy(id = "note-title")
    private WebElement noteTitleField;

    @FindBy(id = "note-description")
    private WebElement noteDescriptionField;

    @FindBy(id = "note-modal-submit")
    private WebElement submitButton;

    @FindBy(className = "note-title")
    private WebElement noteTitle;

    @FindBy(className = "note-description")
    private WebElement noteDescription;

    @FindBy(id="edit-note-button" )
    private WebElement editButton;

    @FindBy(id="delete-note")
    private WebElement deleteLink;

    @FindBy(className = "note-title")
    private List<WebElement> titles;

    public void selectNotesTab(){
        notesTab.click();
    }
    public void addNote(String title, String description){
        selectNotesTab();
        webDriverWait.until(ExpectedConditions.visibilityOf(addNoteButton));
        addNoteButton.click();

        webDriverWait.until(ExpectedConditions.visibilityOf(noteModal));
        noteTitleField.sendKeys(title);
        noteDescriptionField.sendKeys(description);
        submitButton.click();

    }
    public Note getFirstNote(){
        webDriverWait.until(ExpectedConditions.visibilityOf(noteTitle));
        Note result = new Note();
        result.setNotetitle(noteTitle.getText());
        result.setNotedescription(noteDescription.getText());
        return result;
    }
    public void editNote(String newTitle, String newDescription){
        selectNotesTab();
        webDriverWait.until(ExpectedConditions.visibilityOf(editButton));
        editButton.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(noteModal));
        noteTitleField.clear();
        noteTitleField.sendKeys(newTitle);
        noteDescriptionField.clear();
        noteDescriptionField.sendKeys(newDescription);
        submitButton.click();
    }
    public void deleteNote(){
        webDriverWait.until(ExpectedConditions.visibilityOf(deleteLink));
        deleteLink.click();
    }
    public boolean isDeleted(String title){
        return !titles.contains(title);
    }
}
