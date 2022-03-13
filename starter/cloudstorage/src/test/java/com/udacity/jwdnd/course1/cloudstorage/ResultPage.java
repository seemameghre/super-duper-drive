package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ResultPage {
    public ResultPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
        this.webDriverWait = new WebDriverWait(webDriver, 2);
    }
    private WebDriverWait webDriverWait;

    @FindBy(tagName = "a")
    private WebElement link;

    @FindBy(id = "success")
    private WebElement successMessage;

    @FindBy(id = "error")
    private WebElement error;

    @FindBy(id = "errormsg")
    private WebElement errorMessage;

    public void clickLink(){
        link.click();
    }

    public boolean isSuccess(){
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success")));
        return successMessage.isDisplayed();
    }
    public boolean isError(){
        webDriverWait.until(ExpectedConditions.visibilityOf(error));
        return error.isDisplayed();
    }
    public String getErrorMsg(){
        webDriverWait.until(ExpectedConditions.visibilityOf(errorMessage));
        return errorMessage.getText();
    }
}
