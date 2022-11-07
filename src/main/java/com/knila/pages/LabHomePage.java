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

public class LabHomePage extends LoadableComponent<LabHomePage> {

	private final WebDriver driver;
	private boolean isPageLoaded;

	@FindBy(css = "#content h4")
	WebElement txtLoggedUserInfo;

	@FindBy(css = "#content h2")
	WebElement txtContentInfo;

	@FindBy(css = "#content a")
	List<WebElement> lstLabContents;

	/**
	 * constructor of the class
	 *
	 * @param driver
	 */
	public LabHomePage(WebDriver driver) {

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
		Log.assertThat(url.equals("HOME"), "Page loaded successfully", "Page not loaded successfully");
	}

	/**
	 * Method to get section title
	 *
	 */
	public String veriyLoggedUserInfo() {
		String txtInformation = null;
		try {
			CommonMethods.waitForElementVisibility(txtLoggedUserInfo, driver);
			txtInformation = CommonMethods.getText(txtLoggedUserInfo, driver, "Logged in user information").trim();
			Log.message("User Info : " + txtInformation);
			return txtInformation;
		} catch (Exception e) {
			Log.message("Exception occured while verifying user info : " + e.getMessage(), driver);
		}
		return txtInformation;
	}

	public void pickLaboratoryContent(String contentType) {
		String contentTypeText = null;
		try {
			for (WebElement ele : lstLabContents) {
				contentTypeText = ele.getText().trim();
				Log.event(contentTypeText);
				if (contentTypeText.equalsIgnoreCase(contentType)) {
					Log.message("content type : " + contentTypeText + " is displayed in Laboratory home page");
					CommonMethods.click(ele, contentTypeText);
					break;
				}
			}
			Log.message("Successfully clicked on content type : " + contentTypeText, driver);
		} catch (Exception e) {
			Log.message("Exception occured when selecting content type : " + e.getMessage(), driver);
		}
	}

}
