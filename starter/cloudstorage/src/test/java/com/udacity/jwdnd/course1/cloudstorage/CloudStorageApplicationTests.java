package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private NotePage notePage;
	private CredentialPage credentialPage;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void getSignupPage(){
		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
	}
	public void getHomePage(){
		driver.get("http://localhost:" + this.port + "/home");
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		
		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-msg")));
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}

	
	
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 5);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));
	}

	/* Tests that home page is not accessible if user is not logged in */
	@Test
	public void testHomePageWithoutLogin(){
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}
	@Test
	public void testLogout(){
		doMockSignUp("test","test","logout3","123");
		doLogIn("logout3","123");

		Assertions.assertEquals("Home", driver.getTitle());

		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout-button")));
		WebElement logoutButton = driver.findElement(By.id("logout-button"));
		logoutButton.click();

		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}
	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling redirecting users 
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric: 
	 * https://review.udacity.com/#!/rubrics/2724/view 
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT9","123");
		
		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**------------ Notes functionality tests -----------*/
	@Test
	public void testAddNote(){
		String title = "Todo";
		String description = "This is test note";
		notePage = new NotePage(this.driver);

		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

//		doMockSignUp("Test","User","notetest18","123");
		doLogIn("notetest18","123");

		notePage.addNote(title,description);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-msg")));
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).isDisplayed());

		Note noteAdded = notePage.getFirstNote();
		Assertions.assertEquals(title, noteAdded.getNotetitle());
		Assertions.assertEquals(description, noteAdded.getNotedescription());
	}
	@Test
	public void testEditNote(){
		String title = "Todo";
		String description = "This is test note";
		String newTitle = "New title";
		String newDescription = "New description";
		notePage = new NotePage(this.driver);
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

//		doMockSignUp("Test","User","notetest20","123");
		doLogIn("notetest20","123");

		notePage.addNote(title,description);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-msg")));
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).isDisplayed());

		notePage.editNote(newTitle,newDescription);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-msg")));
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).isDisplayed());
//		goToNotes();
		Note noteEdited = notePage.getFirstNote();
		Assertions.assertEquals(newTitle, noteEdited.getNotetitle());
		Assertions.assertEquals(newDescription, noteEdited.getNotedescription());
	}
	@Test
	public void testDeleteNote(){
		String title = "Test note";
		String description = "This is test note";
		notePage = new NotePage(this.driver);
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

//		doMockSignUp("Test","User","notetest21","123");
		doLogIn("notetest20","123");
		notePage.addNote(title,description);

		notePage.deleteNote();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-msg")));
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).isDisplayed());
		Assertions.assertTrue(notePage.isDeleted(title));
	}
	/* --------------End of notes tests-----------------*/
	/*---------------Credential tests start---------***/
	@Test
	public void testAddCredential(){
		String url = "www.gmail.com";
		String username = "CredTest";
		String password = "mypwd";

		credentialPage = new CredentialPage(this.driver);
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

//		doMockSignUp("Test","User","cred4","123");
		doLogIn("cred4","123");

		credentialPage.addCredentials(url,username,password);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-msg")));
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).isDisplayed());

		Credential credentialAdded = credentialPage.getFirstCredential();
		Assertions.assertEquals(url, credentialAdded.getUrl());
		Assertions.assertEquals(username, credentialAdded.getUsername());
	}
	@Test
	public void testEditCredential(){
		String url = "www.gmail.com";
		String username = "CredTest";
		String password = "mypwd";
		String newUrl = "www.google.com";
		String newUsername = "test@gmail.com";
		String newPassword = "mynewpwd";

		credentialPage = new CredentialPage(this.driver);
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

//		doMockSignUp("Test","User","cred5","123");
		doLogIn("cred5","123");

		credentialPage.addCredentials(url,username,password);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-msg")));
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).isDisplayed());

		credentialPage.editCredentials(newUrl, newUsername, newPassword);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-msg")));
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).isDisplayed());

		Credential credentialAdded = credentialPage.getFirstCredential();
		Assertions.assertEquals(newUrl, credentialAdded.getUrl());
		Assertions.assertEquals(newUsername, credentialAdded.getUsername());
	}
	@Test
	public void testDeleteCredential(){
		String url = "www.gmail.com";
		String username = "CredTest";
		String password = "mypwd";

		credentialPage = new CredentialPage(this.driver);
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

//		doMockSignUp("Test","User","cred6","123");
		doLogIn("cred5","123");

		credentialPage.addCredentials(url,username,password);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-msg")));
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).isDisplayed());

		credentialPage.delete();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-msg")));
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).isDisplayed());

		Assertions.assertTrue(credentialPage.isDeleted(url));
	}
	/***********--End of Credential tests-----------*/
	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling bad URLs 
	 * gracefully, for example with a custom error page.
	 * 
	 * Read more about custom error pages at: 
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT3","123");
		doLogIn("UT3", "123");
		
		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
		Assertions.assertTrue(driver.getTitle().equals("Error"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code. 
	 * 
	 * Read more about file size limits here: 
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() {

		// Create a test account
//		doMockSignUp("Large File","Test","LFT4","123");
		doLogIn("LFT4", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("error-msg")));
		Assertions.assertTrue(driver.findElement(By.id("error-msg")).isDisplayed());
		Assertions.assertEquals("File size exceeded the limit of 1MB.",driver.findElement(By.id("error-msg")).getText());

/*		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}*/
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}



}
