package com.knila.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import com.knila.utils.CommonMethods;
import com.knila.utils.Log;

public class PatientDetailsVisitPage extends LoadableComponent<PatientDetailsVisitPage> {

	private final WebDriver driver;
	private boolean isPageLoaded;

	@FindBy(css = "#breadcrumbs li:last-child")
	WebElement txtTitleInBreadCrumbs;

	@FindBy(id = "attachments.attachments.visitActions.default")
	WebElement linkAttachments;

	@FindBy(id = "quick-visit-creation-dialog")
	WebElement popupStartVisit;

	@FindBy(id = "start-visit-with-visittype-confirm")
	WebElement btnConfirmVisit;

	@FindBy(id = "referenceapplication.realTime.endVisit")
	WebElement linkEndVisit;

	/**
	 * constructor of the class
	 *
	 * @param driver
	 */
	public PatientDetailsVisitPage(WebDriver driver) {
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

	public void clickAttachments() {
		try {
			CommonMethods.waitForElementVisibility(linkAttachments, driver);
			CommonMethods.click(linkAttachments, "Start Visit");
		} catch (Exception e) {
			Log.message("Exception occured while Verifying Page details : " + e.getMessage(), driver);
		}
	}
}
