package com.knila.pages;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import com.knila.utils.CommonMethods;
import com.knila.utils.Log;

public class RegisterAPatientPage extends LoadableComponent<RegisterAPatientPage> {

	private final WebDriver driver;
	private boolean isPageLoaded;

	@FindBy(css = "#content h2")
	WebElement txtContentInfo;

	@FindBy(id = "next-button")
	WebElement btnNext;

	@FindBy(id = "prev-button")
	WebElement btnPrevious;

	@FindBy(css = "#formBreadcrumb span")
	List<WebElement> lstDemographics;

	@FindBy(css = "#formBreadcrumb li[class*='focused'] span")
	WebElement txtFocusedDemographic;

	@FindBy(name = "givenName")
	WebElement txtInputGivenName;

	@FindBy(name = "middleName")
	WebElement txtInputMiddleName;

	@FindBy(name = "familyName")
	WebElement txtInputFamilyName;

	@FindBy(id = "gender-field")
	WebElement drpDownGender;

	@FindBy(id = "birthdateDay-field")
	WebElement txtBirthDateDay;

	@FindBy(id = "birthdateMonth-field")
	WebElement drpDownBirthDateMonth;

	@FindBy(id = "birthdateYear-field")
	WebElement txtBirthDateYear;

	@FindBy(id = "address1")
	WebElement txtAdsress1;

	@FindBy(id = "address2")
	WebElement txtAdsress2;

	@FindBy(id = "cityVillage")
	WebElement txtCityVillage;

	@FindBy(id = "stateProvince")
	WebElement txtStateProvince;

	@FindBy(id = "country")
	WebElement txtCountry;

	@FindBy(id = "postalCode")
	WebElement txtPostalCode;

	@FindBy(name = "phoneNumber")
	WebElement txtPhoneNumber;

	@FindBy(id = "relationship_type")
	WebElement drpDownRelationShipType;

	@FindBy(css = "input[ng-model='relationship.name']")
	WebElement txtRelationName;

	@FindBy(id = "confirmation_label")
	WebElement txtConfirmationLabel;

	@FindBy(id = "confirmation")
	WebElement confirmationDetails;

	@FindBy(id = "submit")
	WebElement btnConfirm;

	@FindBy(xpath = "//*[@id='dataCanvas']//span")
	List<WebElement> lstDataCanvas;

	@FindBy(xpath = "//div[@class='toast-item toast-type-success']")
	WebElement popupToasterMsg;

	/**
	 * constructor of the class
	 *
	 * @param driver
	 */
	public RegisterAPatientPage(WebDriver driver) {
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

	public String veriyContentInfo() {
		String txtInformation = null;
		try {
			CommonMethods.waitForElementVisibility(txtContentInfo, driver);
			txtInformation = CommonMethods.getText(txtContentInfo, driver, "Content information").trim();
			Log.message("Content Info : " + txtInformation);
		} catch (Exception e) {
			Log.message("Exception occured while Verifying Page details : " + e.getMessage(), driver);
		}
		return txtInformation;
	}

	public Boolean selectDemogrphics(String demoGraphicTitle) {
		boolean isDemographicFocused = false;
		try {
			if (demoGraphicTitle.equalsIgnoreCase(txtFocusedDemographic.getText())) {
				isDemographicFocused = true;
			} else {
				for (WebElement ele : lstDemographics) {
					String demoText = ele.getText();
					if (demoText.equalsIgnoreCase(demoGraphicTitle)) {
						CommonMethods.click(ele, demoText);
						break;
					}
				}
			}
			isDemographicFocused = demoGraphicTitle.equalsIgnoreCase(txtFocusedDemographic.getText()) ? true : false;
			Log.message("Successfully selected demographic type : "+demoGraphicTitle, driver);
		} catch (Exception e) {
			Log.message("Exception occured when selecting demographic types : " + e.getMessage(), driver);
		}
		return isDemographicFocused;
	}

	public void enterPatientName(String givenName, String middleName, String familyName) {
		try {
			if (selectDemogrphics("Name")) {
				CommonMethods.waitForElementVisibility(txtInputGivenName, driver);
				CommonMethods.setText(txtInputGivenName, givenName, "Given (required)");
				CommonMethods.setText(txtInputMiddleName, middleName, "Middle");
				CommonMethods.setText(txtInputFamilyName, familyName, "Family Name (required)");
			} else {
				Log.message("Demographics -> Name section was not focused", driver);
			}
		} catch (Exception e) {
			Log.message("Exception occured when entering name details : " + e.getMessage(), driver);
		}
	}

	public void selectGender(String gender) {
		try {
			if (selectDemogrphics("Gender")) {
				CommonMethods.waitForElementVisibility(drpDownGender, driver);
				CommonMethods.selectByText(drpDownGender, gender);
			} else {
				Log.message("Demographics -> Gender section was not focused", driver);
			}
		} catch (Exception e) {
			Log.message("Exception occured when selecting gender details : " + e.getMessage(), driver);
		}
	}

	public void enterBirthDateDetails(String birthDate, String birthMonth, String birthYear) {
		try {
			if (selectDemogrphics("Birthdate")) {
				CommonMethods.waitForElementVisibility(txtBirthDateDay, driver);
				CommonMethods.setText(txtBirthDateDay, birthDate, "Day (required)");
				CommonMethods.selectByText(drpDownBirthDateMonth, birthMonth);
				CommonMethods.setText(txtBirthDateYear, birthYear, "Year (required)");
			} else {
				Log.message("Demographics -> Birthdate section was not focused", driver);
			}

		} catch (Exception e) {
			Log.message("Exception occured when entering birthdate details : " + e.getMessage(), driver);
		}
	}

	public void enterAddressDetails(String address1, String address2, String city, String state, String country,
			String postalCode) {
		try {
			if (selectDemogrphics("Address")) {
				CommonMethods.waitForElementVisibility(txtAdsress1, driver);
				CommonMethods.setText(txtAdsress1, address1, "Address 1");
				CommonMethods.setText(txtAdsress2, address2, "Address 2");
				CommonMethods.setText(txtCityVillage, city, "City/Village");
				CommonMethods.setText(txtStateProvince, state, "State/Province");
				CommonMethods.setText(txtCountry, country, "Country");
				CommonMethods.setText(txtPostalCode, postalCode, "Postal Code");
			} else {
				Log.message("Demographics -> Address section was not focused", driver);
			}

		} catch (Exception e) {
			Log.message("Exception occured when entering address details : " + e.getMessage(), driver);
		}
	}

	public void enterPhoneNumberDetails(String phoneNumber) {
		try {
			if (selectDemogrphics("Phone Number")) {
				CommonMethods.waitForElementVisibility(txtPhoneNumber, driver);
				CommonMethods.setText(txtPhoneNumber, phoneNumber, "Phone Number");
			} else {
				Log.message("Demographics -> Phone Number section was not focused", driver);
			}

		} catch (Exception e) {
			Log.message("Exception occured when entering phone number details : " + e.getMessage(), driver);
		}
	}

	public void enterRelativesDetails(String relationType, String relationName) {
		try {
			if (selectDemogrphics("Relatives")) {
				CommonMethods.waitForElementVisibility(drpDownRelationShipType, driver);
				CommonMethods.selectByText(drpDownRelationShipType, relationType);
				CommonMethods.setText(txtRelationName, relationName, "Relation Name");
			} else {
				Log.message("Demographics -> Relatives section was not focused", driver);
			}
		} catch (Exception e) {
			Log.message("Exception occured while entering Relatives details : " + e.getMessage(), driver);
		}
	}

	public void confirmDemograhicDetails(HashMap<String, String> testData) {
		try {
			CommonMethods.waitForElementVisibility(txtConfirmationLabel, driver);
			CommonMethods.click(txtConfirmationLabel, "Confirm Label");
			verifyDemographicDetails(testData);
			CommonMethods.click(btnConfirm, "Confirm Button");
		} catch (Exception e) {
			Log.message("Exception occured when clicking confirm demographics : " + e.getMessage(), driver);
		}
	}

	public void verifyDemographicDetails(HashMap<String, String> testData) {
		try {
			CommonMethods.waitForElementVisibility(confirmationDetails, driver);
			for (WebElement element : lstDataCanvas) {
				String demoGraphicField = element.getText();
				String ExpectedData = testData.get(demoGraphicField.replace(":", ""));
				String ActualData = element.findElement(By.xpath("..")).getText().replace(demoGraphicField, "");
				if (ActualData.contains(ExpectedData)) {
					Log.message("Patients Demogarphics " + demoGraphicField + " values are displayed as expected");
				} else {
					Log.failsoft("Patients Demogarphics " + demoGraphicField + " values are not displayed as expected");
					Log.message("Expected values : " + ExpectedData);
					Log.message("Actual values : " + ActualData);
				}
			}
		} catch (Exception e) {
			Log.message("Exception occured while Verifying demographic details : " + e.getMessage(), driver);
		}
	}

	public Boolean verifySuccessToasterMsg(String patientFullName) {
		boolean isToasterMsgDisplayed = false;
		String expToasterMsg = "Created Patient Record: "+patientFullName;
		try {
			CommonMethods.waitForElementVisibility(popupToasterMsg, driver);
			String actToasterMsg = popupToasterMsg.getText().trim();
			Log.message("toasterMsgDisplayedAs : "+actToasterMsg);
			isToasterMsgDisplayed = actToasterMsg.contains(expToasterMsg);
			return isToasterMsgDisplayed;
		} catch (Exception e) {
			Log.message("Exception occured while Verifying toaster message details : " + e.getMessage(), driver);
		}
		return isToasterMsgDisplayed;
	}

}
