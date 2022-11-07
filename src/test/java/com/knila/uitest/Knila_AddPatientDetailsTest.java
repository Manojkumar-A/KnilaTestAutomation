package com.knila.uitest;

import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.knila.pages.AttachmentsPage;
import com.knila.pages.FindPatientRecordPage;
import com.knila.pages.LabHomePage;
import com.knila.pages.LoginPage;
import com.knila.pages.PatientDetailsPage;
import com.knila.pages.PatientDetailsVisitPage;
import com.knila.pages.RegisterAPatientPage;
import com.knila.utils.DataUtils;
import com.knila.utils.EmailReport;
import com.knila.utils.EnvironmentPropertiesReader;
import com.knila.utils.Log;
import com.knila.utils.WebDriverFactory;

@Listeners(EmailReport.class)
public class Knila_AddPatientDetailsTest {

	private String browser;
	private String webSite;
	private String fileName;
	private static EnvironmentPropertiesReader configProperty = EnvironmentPropertiesReader.getInstance();

	@BeforeTest
	public void init(ITestContext context) {
		webSite = System.getProperty("webSite") != null ? System.getProperty("webSite")
				: configProperty.getProperty("webSite");
		browser = System.getProperty("browser") != null ? System.getProperty("browser")
				: configProperty.getProperty("browser").toLowerCase();
		fileName = System.getProperty("fileName") != null ? System.getProperty("fileName")
				: configProperty.getProperty("fileName").toLowerCase();
	}

	@Test(description = "Verify user able to login successfully with valid credential")
	public void TC001() throws Exception {

		Log.message(System.getProperty("user.dir"));

		// Get the web driver instance
		final WebDriver driver = WebDriverFactory.newWebDriverInstance(webSite, browser);

		// Loading the test data from excel using the test case id
		HashMap<String, String> testData = DataUtils.getTestData(fileName, "LoginTest", "TC001");

		String username = testData.get("userName").toString();
		String password = testData.get("password").toString();
		String location = testData.get("location").toString();
		String infoText = configProperty.getProperty("loggedInUserInfoText");
		String contentType = configProperty.getProperty("contentTypeRegPatient");
		String findPatientRecord = configProperty.getProperty("findRecord");

		Log.testCaseInfo("Verify user able to login successfully with valid credential");
		try {

			// Login to openMRS application
			LoginPage loginPage = new LoginPage(driver);
			loginPage.loginToApplication(username, password, location);

			// Choose a Register a patent
			LabHomePage labHomePage = new LabHomePage(driver);
			Log.assertThat(labHomePage.veriyLoggedUserInfo().contains(infoText), "User logged in successfully",
					"Unable to log in", driver);
			labHomePage.pickLaboratoryContent(contentType);

			// Register a Patient details
			RegisterAPatientPage registerPatientPage = new RegisterAPatientPage(driver);
			Log.assertThat(registerPatientPage.veriyContentInfo().contains(contentType),
					"User successfully navigated to Register Patient Page", "Failed to navigate Register Patient Page",
					driver);
			String patientName = testData.get("Name");
			registerPatientPage.enterPatientName(patientName.split(", ")[0], patientName.split(", ")[1],
					patientName.split(", ")[2]);
			registerPatientPage.selectGender(testData.get("Gender"));
			String birthDate = testData.get("Birthdate");
			registerPatientPage.enterBirthDateDetails(birthDate.split(", ")[0], birthDate.split(", ")[1],
					birthDate.split(", ")[2]);
			String address = testData.get("Address");
			registerPatientPage.enterAddressDetails(address.split(", ")[0], address.split(", ")[1],
					address.split(", ")[2], address.split(", ")[3], address.split(", ")[4], address.split(", ")[5]);
			registerPatientPage.enterPhoneNumberDetails(testData.get("Phone Number"));
			String relatives = testData.get("Relatives");
			registerPatientPage.enterRelativesDetails(relatives.split(" - ")[1], relatives.split(" - ")[0]);
			registerPatientPage.confirmDemograhicDetails(testData);
			String patientFullName = testData.get("Name").replace(",", "");
			Log.assertThat(registerPatientPage.verifySuccessToasterMsg(patientFullName),
					"User successfully added Patient Details", "Failed to add Patient Details", driver);

			PatientDetailsPage patientDetailsPage = new PatientDetailsPage(driver);
			Log.assertThat(patientDetailsPage.veriyPagedetails().contains(patientFullName),
					"User successfully navigated to Patient Details Page", "Failed to navigate Patient Details Page",
					driver);
			patientDetailsPage.startVisitPatientDetails();

			PatientDetailsVisitPage patientDetailsVisitPage = new PatientDetailsVisitPage(driver);
			Log.assertThat(patientDetailsVisitPage.veriyPagedetails().contains("Visits"),
					"User successfully navigated to Patient Details visit Page",
					"Failed to navigate Patient Details vist Page", driver);
			patientDetailsVisitPage.clickAttachments();

			AttachmentsPage attachmentsPage = new AttachmentsPage(driver);
			Log.assertThat(attachmentsPage.veriyPageDetails().contains("Attachments"),
					"User successfully navigated to Attachments Page", "Failed to navigate Attachments Page", driver);
			attachmentsPage.uploadAttachments(testData.get("FileToUpload"), testData.get("FileUploadCaption"));
			attachmentsPage.redirectToPatientDetailsPage();

			Log.assertThat(patientDetailsPage.veriyPagedetails().contains(patientFullName),
					"User successfully redirected to Patient Details Page", "Failed to redirect Patient Details Page",
					driver);
			Log.assertThat(patientDetailsPage.veriyAttachementdetails().contains(testData.get("FileUploadCaption")),
					"Attachment successfully added in Patient Details Page",
					"Failed to get the Attachments in Patient Details Page", driver);
			Log.assertThat(patientDetailsPage.verifyRecentVisits(),
					"Recent Visits details successfully verified in Patient Details Page",
					"Failed to verify Recent Visits deatils in Patient Details Page", driver);
			patientDetailsPage.endVisitPatientDetails();
			patientDetailsPage.deletePatientDetails();
			Log.assertThat(patientDetailsPage.verifyPatientDeleteSuccessToasterMsg(),
					"User successfully deleted the Patient Details", "Failed to delete Patient Details", driver);

			FindPatientRecordPage findPatientRecordPage = new FindPatientRecordPage(driver);
			Log.assertThat(attachmentsPage.veriyPageDetails().contains(findPatientRecord),
					"User successfully redirected to Find Patient Record Page",
					"Failed to navigate Find Patient Record Page", driver);
			Log.assertThat(findPatientRecordPage.verifyPatientRecordNotAvailable(patientFullName),
					"Successfully verified that deleted patient record Not listed",
					"Failed to verify that deleted patient record Not listed", driver);
			Log.testCaseResult();
		} catch (Exception e) {
			Log.exception(e, driver);
		} finally {
			Log.endTestCase();
			driver.quit();
		}
	}

}
