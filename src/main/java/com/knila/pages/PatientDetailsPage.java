package com.knila.pages;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

public class PatientDetailsPage extends LoadableComponent<PatientDetailsPage> {

	private final WebDriver driver;
	private boolean isPageLoaded;

	@FindBy(css = "#breadcrumbs li:last-child")
	WebElement txtTitleInBreadCrumbs;

	@FindBy(id = "org.openmrs.module.coreapps.createVisit")
	WebElement linkStartVisit;

	@FindBy(id = "quick-visit-creation-dialog")
	WebElement popupStartVisit;

	@FindBy(id = "start-visit-with-visittype-confirm")
	WebElement btnConfirmVisit;

	@FindBy(css = "[class*='d-lg-block'] i.icon-off")
	WebElement linkEndVisit;

	@FindBy(id = "end-visit-dialog")
	WebElement popupEndVisit;

	@FindBy(css = "#end-visit-dialog button.confirm")
	WebElement btnConfirmEndVisit;

	@FindBy(id = "org.openmrs.module.coreapps.deletePatient")
	WebElement linkDeletePatient;

	@FindBy(id = "delete-patient-creation-dialog")
	WebElement popupDeletePatient;

	@FindBy(css = "#delete-patient-creation-dialog button.confirm")
	WebElement btnConfirmDeletePatient;

	@FindBy(id = "delete-reason")
	WebElement txtDeleteReason;

	@FindBy(css = "#att-fragment-dashboard-widget .att_thumbnail-caption-section")
	WebElement txtAttachmentDetails;

	@FindBy(css = "visitbyencountertype a")
	List<WebElement> lstVisitedDates;

	@FindBy(css = "visitbyencountertype div")
	WebElement lnkAttachmentupload;

	@FindBy(xpath = "//div[@class='toast-item toast-type-success']")
	WebElement popupToasterMsg;

	/**
	 * constructor of the class
	 *
	 * @param driver
	 */
	public PatientDetailsPage(WebDriver driver) {
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

	public String veriyPagedetails() {
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

	public void startVisitPatientDetails() {
		try {
			CommonMethods.waitForElementVisibility(linkStartVisit, driver);
			CommonMethods.click(linkStartVisit, "Start Visit");
			CommonMethods.waitForElementVisibility(popupStartVisit, driver);
			CommonMethods.click(btnConfirmVisit, "Confirm Visit");
			CommonMethods.pause(3000);
		} catch (Exception e) {
			Log.message("Exception occured when start visiting Patient details : " + e.getMessage(), driver);
		}
	}

	public String veriyAttachementdetails() {
		String txtInformation = null;
		try {
			CommonMethods.waitForElementVisibility(txtAttachmentDetails, driver);
			txtInformation = CommonMethods.getText(txtAttachmentDetails, driver, "caption of attachment").trim();
			Log.message("attachment title text : " + txtInformation);
			return txtInformation;
		} catch (Exception e) {
			Log.message("Exception occured while Verifying attachment details : " + e.getMessage(), driver);
		}
		return txtInformation;
	}

	public Boolean verifyRecentVisits() {
		boolean isVistedDateDisplayed = false;
		boolean isUploadAttachmentDisplayed = false;
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MMM.yyyy");
			LocalDateTime now = LocalDateTime.now();
			String currentDate = dtf.format(now);
			for (WebElement ele : lstVisitedDates) {
				String visitedDate = ele.getText().trim();
				Log.event(visitedDate);
				if (visitedDate.equalsIgnoreCase(currentDate)) {
					isVistedDateDisplayed = true;
					break;
				}
			}
			CommonMethods.waitForElementVisibility(lnkAttachmentupload, driver);
			isUploadAttachmentDisplayed = lnkAttachmentupload.getText().equalsIgnoreCase("Attachment Upload");
		} catch (Exception e) {
			Log.message("Exception occured while Verifying Page details : " + e.getMessage(), driver);
		}
		return (isVistedDateDisplayed && isUploadAttachmentDisplayed);
	}

	public void endVisitPatientDetails() {
		try {
			CommonMethods.waitForElementVisibility(linkEndVisit, driver);
			CommonMethods.click(linkEndVisit, "End Visit");
			CommonMethods.waitForElementVisibility(popupEndVisit, driver);
			CommonMethods.click(btnConfirmEndVisit, "Yes End Visit");
			CommonMethods.waitForElementVisibility(linkStartVisit, driver);
		} catch (Exception e) {
			Log.message("Exception occured when end visiting details : " + e.getMessage(), driver);
		}
	}

	public void deletePatientDetails() {
		try {
			CommonMethods.waitForElementVisibility(linkDeletePatient, driver);
			CommonMethods.click(linkDeletePatient, "Delete Patient");
			CommonMethods.waitForElementVisibility(popupDeletePatient, driver);
			CommonMethods.pause(3000);
			CommonMethods.setText(txtDeleteReason, "TestCompleted"+Keys.TAB, "Reason Delete");
			Log.message("Added reason for delete Patient Record",driver);
			CommonMethods.click(btnConfirmDeletePatient, "Confirm Delete Patient");
		} catch (Exception e) {
			Log.message("Exception occured when deleting Patient details : " + e.getMessage(), driver);
		}
	}

	public Boolean verifyPatientDeleteSuccessToasterMsg() {
		boolean isToasterMsgDisplayed = false;
		String expToasterMsg = "Patient has been deleted successfully";
		try {
			CommonMethods.waitForElementVisibility(popupToasterMsg, driver);
			String actToasterMsg = popupToasterMsg.getText().trim();
			Log.message("toasterMsgDisplayedAs : "+actToasterMsg);
			isToasterMsgDisplayed = actToasterMsg.contains(expToasterMsg);
			CommonMethods.pause(3000);
			return isToasterMsgDisplayed;
		} catch (Exception e) {
			Log.message("Exception occured while Verifying delete toaster message details : " + e.getMessage(), driver);
		}
		return isToasterMsgDisplayed;
	}
}
