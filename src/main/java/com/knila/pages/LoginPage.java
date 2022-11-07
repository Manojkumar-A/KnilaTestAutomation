package com.knila.pages;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import com.knila.utils.CommonMethods;
import com.knila.utils.Log;

/**
 * Class to hold page objects for Login page *
 */
public class LoginPage extends LoadableComponent<LoginPage> {

	private final WebDriver driver;
	private boolean isPageLoaded;

	@FindBy(id = "username")
	WebElement txtUserName;

	@FindBy(id = "password")
	WebElement txtPassword;

	@FindBy(id = "loginButton")
	WebElement btnLogin;

	@FindBy(css = "[id='sessionLocation'] li")
	List<WebElement> lstSessionLoctaions;

	@FindBy(id = "Laboratory")
	WebElement linkLaboratoryLocation;

	/**
	 * constructor of the class
	 *
	 * @param driver
	 */
	public LoginPage(WebDriver driver) {

		this.driver = driver;
		ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver, 30);
		PageFactory.initElements(finder, this);
	}

	@Override
	protected void load() {
		isPageLoaded = true;
	}

	@Override
	protected void isLoaded() throws Error {
		if (!isPageLoaded) {
			Log.fail("Page not loaded");
		}
		String url = driver.getTitle();
		Log.assertThat(url.equals("Swag Labs"), "Page loaded successfully", "Page not loaded successfully");
	}

	/**
	 * Method to perform login operation
	 *
	 * @param userName
	 * @param password
	 */
	public void loginToApplication(String userName, String password, String location) {
		try {
			CommonMethods.waitForElementVisibility(txtUserName, driver);
			CommonMethods.setText(txtUserName, userName, "UserName");
			CommonMethods.setText(txtPassword, password, "Password");
			pickLocationToLogin(location);
			CommonMethods.click(btnLogin, "Login button");
		} catch (Exception e) {
			Log.message("Exception occured when login : " + e.getMessage(),driver);
		}
	}

	/**
	 * Method to perform pick location
	 * 
	 * @param location
	 */
	public void pickLocationToLogin(String location) {
		try {
			for (WebElement ele : lstSessionLoctaions) {
				String locText = ele.getText();
				if (locText.equalsIgnoreCase(location)) {
					CommonMethods.click(ele, locText);
				}
			}
		} catch (Exception e) {
			Log.message("Exception occured when selecting location : " + e.getMessage(),driver);
		}
	}

}
