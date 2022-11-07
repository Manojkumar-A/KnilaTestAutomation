package com.knila.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import com.knila.utils.CommonMethods;
import com.knila.utils.FileUtils;
import com.knila.utils.Log;

public class AttachmentsPage extends LoadableComponent<AttachmentsPage> {

	private final WebDriver driver;
	private boolean isPageLoaded;

	@FindBy(css = "#breadcrumbs li:last-child")
	WebElement txtTitleInBreadCrumbs;

	@FindBy(id = "visit-documents-dropzone")
	WebElement linkAttachmentDropZone;

	@FindBy(xpath = "//*[@id='breadcrumbs']/li[2]/a")
	WebElement linkPatientDetailsPage;

	@FindBy(css = "textarea[placeholder*='Enter']")
	WebElement txtEnterCaption;

	@FindBy(xpath = "//button[text()='Upload file']")
	WebElement btnUploadFile;

	/**
	 * constructor of the class
	 *
	 * @param driver
	 */
	public AttachmentsPage(WebDriver driver) {
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
			CommonMethods.pause(2000);
			CommonMethods.waitForElementVisibility(txtTitleInBreadCrumbs, driver);
			txtInformation = CommonMethods.getText(txtTitleInBreadCrumbs, driver, "Title In BreadCrumbs").trim();
			Log.message("title in breadcrumbs : " + txtInformation);
			return txtInformation;
		} catch (Exception e) {
			Log.message("Exception occured while Verifying Page details : " + e.getMessage(), driver);
		}
		return txtInformation;
	}

	public void uploadAttachments(String fileNamewithFormat, String captionForFileUpload) {
		try {
			CommonMethods.waitForElementVisibility(linkAttachmentDropZone, driver);
			CommonMethods.click(linkAttachmentDropZone, "Drag & Drop attachment file");
			FileUtils.uploadFile(fileNamewithFormat);
			CommonMethods.pause(3000);
			CommonMethods.setText(txtEnterCaption, captionForFileUpload, "Attachment Caption");
			CommonMethods.click(btnUploadFile, "Drag & Drop attachment file");
			// Script to validate msg
		} catch (Exception e) {
			Log.message("Exception occured while uploading attachments : " + e.getMessage(), driver);
		}
	}

	public void redirectToPatientDetailsPage() {
		try {
			CommonMethods.waitForElementVisibility(linkPatientDetailsPage, driver);
			CommonMethods.click(linkPatientDetailsPage, "Patient details link in breadcrumbs");
			CommonMethods.pause(3000);
		} catch (Exception e) {
			Log.message("Exception occured when redirecting ro patient details : " + e.getMessage(), driver);
		}
	}
	
}