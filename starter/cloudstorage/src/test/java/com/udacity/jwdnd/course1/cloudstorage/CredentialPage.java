package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class CredentialPage {
    public CredentialPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
        this.webDriverWait = new WebDriverWait(webDriver, 2);
    }
    private WebDriverWait webDriverWait;

    @FindBy(id="nav-credentials-tab")
    private WebElement credentialsTab;

    @FindBy(id="add-credential-btn")
    private WebElement addCredentialButton;

    @FindBy(id="credentialModal")
    private WebElement credentialModal;

    @FindBy(id="credential-modal-submit" )
    private WebElement submitButton;

    @FindBy(id="credential-edit-btn")
    private WebElement editButton;

    @FindBy(id="credential-delete")
    private WebElement deleteLink;

    @FindBy(id="credential-url")
    private WebElement credentialUrlField;

    @FindBy(id="credential-username")
    private WebElement credentialUsernameField;

    @FindBy(id="credential-password")
    private WebElement credentialPwdField;

    @FindBy(className="credential-url")
    private WebElement credentialUrl;

    @FindBy(className ="credential-username")
    private WebElement credentialUsername;

    @FindBy(className ="credential-pwd")
    private WebElement credentialPwd;

    @FindBy(className = "credential-url")
    private List<WebElement> credentials;


    public void selectCredentialTab(){
        credentialsTab.click();
    }

    public void addCredentials(String url, String username, String password){
        selectCredentialTab();
        webDriverWait.until(ExpectedConditions.visibilityOf(addCredentialButton));
        addCredentialButton.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(credentialModal));
        credentialUrlField.sendKeys(url);
        credentialUsernameField.sendKeys(username);
        credentialPwdField.sendKeys(password);
        submitButton.click();
    }
    public void editCredentials(String url, String username, String password){
        selectCredentialTab();
        webDriverWait.until(ExpectedConditions.visibilityOf(editButton));
        editButton.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(credentialModal));
        credentialUrlField.clear();
        credentialUrlField.sendKeys(url);
        credentialUsernameField.clear();
        credentialUsernameField.sendKeys(username);
        credentialPwdField.clear();
        credentialPwdField.sendKeys(password);
        submitButton.click();
    }
    public void delete(){
        selectCredentialTab();
        webDriverWait.until(ExpectedConditions.visibilityOf(deleteLink));
        deleteLink.click();
    }
    public boolean isDeleted(String url){
        return !credentials.contains(url);
    }

    public Credential getFirstCredential(){
        webDriverWait.until(ExpectedConditions.visibilityOf(credentialUrl));
        Credential result = new Credential();
        result.setUrl(credentialUrl.getText());
        result.setUsername(credentialUsername.getText());
        result.setPassword(credentialPwd.getText());
        return result;
    }
}
