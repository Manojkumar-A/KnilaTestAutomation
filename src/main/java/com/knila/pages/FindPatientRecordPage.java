package com.knila.pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import com.knila.utils.CommonMethods;
import com.knila.utils.Log;

public class FindPatientRecordPage extends LoadableComponent<FindPatientRecordPage> {

	private final WebDriver driver;
	private boolean isPageLoaded;

	@FindBy(css = "#breadcrumbs li:last-child")
	WebElement txtTitleInBreadCrumbs;

	@FindBy(id = "patient-search")
	WebElement txtSearchPatient;

	@FindBy(css = "tbody[role='alert'] td")
	WebElement txtNoRecordFound;

	/**
	 * constructor of the class
	 *
	 * @param driver
	 */
	public FindPatientRecordPage(WebDriver driver) {

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
		Log.assertThat(url.equals("OpenMRS Electronic Medical Record"), "Page loaded successfully",
				"Page not loaded successfully");
	}

	public String veriyPageDetails() {
		String txtInformation = null;
		try {
			CommonMethods.waitForElementVisibility(txtTitleInBreadCrumbs, driver);
			txtInformation = CommonMethods.getText(txtTitleInBreadCrumbs, driver, "Title In BreadCrumbs").trim();
			Log.message("title in breadcrumbs : " + txtInformation);
			return txtInformation;
		} catch (Exception e) {
			Log.message("Exception occured while Verifying Page details : " + e.getMessage(), driver);
		}
		return txtInformation;
	}

	public Boolean verifyPatientRecordNotAvailable(String patientName) {
		boolean isNoRecordsFound = false;
		try {
			CommonMethods.waitForElementVisibility(txtSearchPatient, driver);
			CommonMethods.setText(txtSearchPatient, patientName+Keys.TAB, "Patient Search Box");
			CommonMethods.pause(2000);
			CommonMethods.waitForElementVisibility(txtNoRecordFound, driver);
			String alertMessage = txtNoRecordFound.getText().trim();
			Log.event(alertMessage);
			isNoRecordsFound = alertMessage.contains("No matching records found");
			return isNoRecordsFound;
		} catch (Exception e) {
			Log.message("Exception occured while Verifying Patient Record details : " + e.getMessage(), driver);
		}
		return isNoRecordsFound;
	}

}
