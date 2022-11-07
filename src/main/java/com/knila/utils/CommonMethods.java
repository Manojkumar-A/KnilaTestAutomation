package com.knila.utils;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CommonMethods {

	private static String homeWindow = null;

	/**
	 * Method to accept alert
	 */
	public static void accept_Alert(WebDriver driver) {
		try {
			Alert alert = driver.switchTo().alert();
			alert.accept();
			Log.message("alert accepted successfully");
		} catch (Exception e) {
			Log.message("Alert is not accepted" + e.toString());
		}
	}

	/**
	 * Method to switch to the newly opened window
	 */
	public static void switchToWindow(WebDriver driver) {
		homeWindow = driver.getWindowHandle();
		for (String window : driver.getWindowHandles()) {
			driver.switchTo().window(window);
			Log.message("Successfully switched to new window");
		}
	}

	/**
	 * To navigate to the main window from child window
	 */
	public static void switchToMainWindow(WebDriver driver) {
		for (String window : driver.getWindowHandles()) {
			if (!window.equals(homeWindow)) {
				driver.switchTo().window(window);
				driver.close();
			}
			driver.switchTo().window(homeWindow);
			Log.message("Successfully switched to main window");
		}
	}

	/**
	 * This method returns the no.of windows present
	 *
	 * @return count
	 */
	public static int getWindowCount(WebDriver driver) {
		return driver.getWindowHandles().size();
	}

	/**
	 * To switch into frame
	 */
	public static void frames(WebElement frameElement, WebDriver driver) {
		try {
			driver.switchTo().frame(frameElement);
			Log.message("successfully switched to frame");
		} catch (Exception e) {
			Log.message("failed while switching to frame");
		}
	}

	/**
	 * Bring back to default frame
	 */
	public static void switchToDefaultcontent(WebDriver driver) {
		try {
			driver.switchTo().defaultContent();
			Log.message("successfully switched to default frame");
		} catch (NoSuchElementException e) {
			Log.message("failed switched to default frame");
		}
	}

	/**
	 * Navigate to Url
	 */
	public static void navigateToUrl(String url, WebDriver driver) {
		try {
			driver.navigate().to(url);
			Log.message("Application launched successfully to" + url);

		} catch (Exception e) {
			Log.message("Failed to load the url" + url + e.getMessage());
		}
	}

	/**
	 * Close the current window
	 */
	public static void closeBrowser(WebDriver driver) {
		try {
			driver.close();
			driver.quit();
			Log.message("Killing Chrome driver process after");
			Runtime.getRuntime().exec("taskkill /F /IM ChromeDriver.exe");
			pause(3000);
			Log.message("Browser closed successfully");
		} catch (Exception e) {
			Log.message("Browser is not closed");
		}
	}

	/**
	 * Clear end enter text in text box
	 */
	public static void setText(WebElement element, String value, String fieldName) {
		try {
			element.clear();
			element.sendKeys(value);
			Log.message(value + " entered in " + fieldName + " textbox successfully");
		} catch (Exception e) {
			Log.message("failed to enter" + value + "into" + "textbox " + element.toString());
		}
	}

	/**
	 * Verifying the visibility of element only for assert conditions
	 */

	public static boolean isElementPresent(WebElement element, WebDriver driver) {
		boolean elementPresent = false;
		try {
			waitForElementVisibility(element, driver);
			if (element.isDisplayed()) {
				elementPresent = true;
			}
			Log.message("Element displayed successfully");
		} catch (Exception e) {
			Log.message("Verify Element Present failed" + e.toString());
		}
		return elementPresent;
	}

	/**
	 * Verifying the visibility of element only for assert conditions
	 */

	public static boolean isElementNotPresent(WebElement element) {
		boolean elementNotPresent = true;
		try {
			if (element.isDisplayed()) {
				elementNotPresent = false;
			}
			Log.message("Element is Displayed successfully");
		} catch (Exception e) {
			Log.message("Verify Element Present failed" + e.getMessage());
		}
		return elementNotPresent;
	}

	/**
	 * Method to click the element
	 *
	 * @param element
	 */
	public static void click(WebElement element, String fieldName) {
		try {
			element.click();
			Log.message(fieldName + " is clicked successfully");

		} catch (Exception e) {
			Log.message(element.toString() + "element is not clicked" + e.getMessage());
		}
	}

	/**
	 * getting the text from non editable field
	 */

	public static String getText(WebElement element, WebDriver driver, String fieldName) {
		String text = null;
		try {
			waitForElementVisibility(element, driver);
			if (element.getText() != null) {
				text = element.getText();
			}
			Log.message(fieldName + " retrieved successfully from element");
		} catch (Exception e) {
			Log.message("text is not retrieved from element" + element.toString() + e.getMessage());
		}
		return text;
	}

	/**
	 * Method to get the value from textbox
	 *
	 * @param element
	 * @return
	 */
	public static String getValue(WebElement element, WebDriver driver) {
		String value = null;
		try {
			waitForElementVisibility(element, driver);
			if (element.getAttribute("value") != null) {
				value = element.getAttribute("value");
			}
			Log.message("text retrieved successfully from element" + element.toString());
		} catch (Exception e) {
			Log.message("text is not retrieved from element" + element.toString() + e.getMessage());
		}
		return value;
	}

	/**
	 * Method to select the option from dropdown by value
	 */
	public static void selectByValue(WebElement element, String value) {
		try {
			Select obj_select = new Select(element);
			obj_select.selectByValue(value);
			Log.message(value + "selected from dropdown " + element.toString());
		} catch (Exception e) {
			Log.message("failed to select" + value + "from " + element.toString());
		}
	}

	/**
	 * Method to select the option from drop down by visible text
	 */
	public static void selectByText(WebElement element, String text) {
		try {

			Select obj_select = new Select(element);
			Log.message("Text to select:" + text);

			List<String> allTextInDD = getOptionFromDropDown(element);
			for (String string : allTextInDD) {
				Log.message("text is:" + string);
				if (string.equalsIgnoreCase(text)) {
					Log.message("in if");
					obj_select.selectByVisibleText(string);
					break;
				} else {
					Log.message("in else");
					continue;
				}
			}
			Log.message(text + "selected from dropdown " + element.toString());
		} catch (Exception e) {
			Log.message("failed to select" + text + "from " + element.toString());
		}

	}

	/**
	 * Method to select the option from dropdown by index
	 */
	public static void selectByIndex(WebElement element, int index) {
		try {
			Select obj_select = new Select(element);
			obj_select.selectByIndex(index);
			Log.message(index + "index selected from dropdown " + element.toString());
		} catch (Exception e) {
			Log.message("failed to select" + index + "index" + "from " + element.toString());
		}
	}

	/**
	 * Method retrieve the value selected in the drop down
	 */
	public String getValueSelectedInTheDropDown(WebElement element) {
		String getSelectedItem = "";
		try {
			Select obj_select = new Select(element);
			getSelectedItem = obj_select.getFirstSelectedOption().getText();
			Log.message("Successfully fetched fetched the selected item from the drop down.");
			return getSelectedItem;
		} catch (Exception e) {
			Log.message("failed to retrieve the selected value from the drop down" + element.toString());
			return getSelectedItem;
		}

	}

	/**
	 * To pause execution until get expected elements visibility
	 *
	 * @param element
	 */
	public static void waitForElementVisibility(WebElement element, WebDriver driver) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(60))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(NoSuchElementException.class);
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	/**
	 * To pause execution until get expected elements clickable
	 *
	 * @param element
	 */
	public static void waitForElementClickable(WebElement element, WebDriver driver) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
			wait.until(ExpectedConditions.elementToBeClickable(element));
			Log.message("Element is clickable at this point");
		} catch (Exception e) {
			Log.message("Element is not clickable at this point exception");
		}
	}

	/**
	 * To pause the execution @throws
	 */
	public static void pause(int milliSeconds) throws InterruptedException {
		Thread.sleep(milliSeconds);
	}

	/**
	 * Method to get the available option from dropdown
	 *
	 * @return
	 */
	public static List<String> getOptionFromDropDown(WebElement element) {
		List<String> AvailableOptions = new ArrayList<String>();
		try {

			Select obj_select = new Select(element);
			List<WebElement> optionElements = obj_select.getOptions();
			for (int i = 0; i < optionElements.size(); i++) {
				AvailableOptions.add(optionElements.get(i).getText());
			}
			Log.message("get available options from dropdown is success" + element.toString());
		} catch (Exception e) {
			Log.message("get available options from dropdown is failed" + e.getMessage() + element.toString());
		}
		return AvailableOptions;
	}

	/**
	 * Method to perform mouseover action on required element
	 *
	 * @param element
	 */
	public void jsMouseOver(WebElement element, WebDriver driver) {
		try {
			String code = "var fireOnThis = arguments[0];" + "var evObj = document.createEvent('MouseEvents');"
					+ "evObj.initEvent( 'mouseover', true, true );" + "fireOnThis.dispatchEvent(evObj);";
			((JavascriptExecutor) driver).executeScript(code, element);
			Log.message("Mouseover to the element" + element.toString() + "is success");

		} catch (Exception e) {
			Log.message("Mouseover to the element" + element.toString() + "is failed");
		}
	}

	/**
	 * Method to perform mouseover action
	 */
	public static void mouseOver(WebElement element, WebDriver driver) {
		try {
			Actions action = new Actions(driver);
			action.moveToElement(element).build().perform();
			Log.message("Mouseover to the element" + element.toString() + "is success");
		} catch (Exception e) {
			Log.message("Mouseover to the element" + element.toString() + "is failed");
		}
	}

	/**
	 * Method to wait for page load using javascript
	 */
	public static void jsWaitForPageLoad(WebDriver driver) {
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(120));
		String pageReadyState = (String) ((JavascriptExecutor) driver).executeScript("return document.readyState");
		while (!pageReadyState.equals("complete")) {
			pageReadyState = (String) ((JavascriptExecutor) driver).executeScript("return document.readyState");
		}

	}

	/**
	 * Method to get random string
	 */
	public static String getRandomString(int length) {
		char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		if (sb.length() == 0)
			sb.append("test");
		return sb.toString();
	}

	/**
	 * Method to get random number
	 */
	public static String getRandomNumber(int length) {
		char[] chars = "123456789".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * Method to get selected value from dropdown
	 */
	public String dropDownSelectedValue(WebElement element) {
		Select select = new Select(element);
		String selectedOption = select.getFirstSelectedOption().getText();
		return selectedOption;
	}

	/**
	 * To get default selected value from drop down
	 *
	 * @param element
	 * @return String
	 */

	public static String getDefaultDropDownValue(WebElement element) throws InterruptedException {

		Select obj_select = new Select(element);
		return obj_select.getFirstSelectedOption().getText();

	}

	/**
	 * To get checkbox is selected or not from list of checkboxes
	 *
	 * @param List<WebElement>
	 */
	public static boolean isCheckBoxSelectedInDropdown(List<WebElement> elements) {
		boolean flag = true;
		int noOfCheckBox = elements.size();
		for (int i = 0; i < noOfCheckBox; i++) {
			flag = elements.get(i).isSelected();
			if (flag == true)
				break;
		}
		return flag;
	}

	/**
	 * Method to perform javascript click
	 */
	public static void clickjs(WebElement element, WebDriver driver) {
		try {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", element);
			Log.message(element.toString() + "element " + element.getText() + " is clicked successfully");
		} catch (Exception e) {
			Log.message(element.toString() + "element is not clicked" + e.getMessage());
		}
	}

	/**
	 * Method to perform click
	 */
	public static void clickAction(WebElement element, WebDriver driver) {
		try {
			Actions elementToClick = new Actions(driver);
			elementToClick.click().perform();
			Log.message(element.toString() + "element is clicked successfully Using Actions");

		} catch (Exception e) {
			Log.message(element.toString() + "element is not clicked" + e.getMessage());
		}
	}

	/**
	 * Method to scroll page up for element visibility using java script
	 */
	public static void jsScrollPageUp(WebElement element, WebDriver driver) {
		try {
			int yScrollPosition = element.getLocation().getY();
			Log.message("yScrollPosition:" + yScrollPosition);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollBy(0, " + -yScrollPosition + ")", "");
			Log.message("scroll page up" + "page is scrolled up successfully");
		} catch (Exception e) {
			Log.message("page is not scrolled up ");
		}
	}

	/**
	 * quit Browser
	 */
	public static void quitBrowser(WebDriver driver) {
		try {
			driver.quit();
			Log.message("Browser quited successfully");
		} catch (Exception e) {
			Log.message("Browser quited successfully: " + e);
		}
	}

	/**
	 * verify if list in Ascending Order
	 *
	 * @return boolean
	 */
	public boolean checkAscendingOrder(List<String> list) {
		String previous = "";
		boolean flag = true;
		for (String current : list) {
			Log.message("current:" + current);
			Log.message("previous:" + previous);
			if (current != null && !current.isEmpty() && current.compareTo(previous) < 0) {
				Log.message("in if condition");
				flag = false;
				return flag;
			}
			previous = current;
		}
		return flag;
	}

	/**
	 * verify if list in descending Order
	 *
	 * @return boolean
	 */

	public boolean checkDescendingOrder(List<String> list) {
		String previous = list.get(0);
		boolean flag = true;
		for (String current : list) {
			Log.message("current:" + current);
			Log.message("previous:" + previous);
			if (previous.compareTo(current) < 0) {
				flag = false;
				return flag;
			}
			previous = current;
		}
		return flag;
	}

	/**
	 * verify Option is available In DropDown
	 *
	 * @param Dropdown and option
	 * @return boolean
	 */
	public boolean verifyOptionIsAvailableInDropDown(WebElement dropDown, String option) {
		boolean flag = false;
		List<String> TaxSetupOption = getOptionFromDropDown(dropDown);
		for (String string : TaxSetupOption) {
			Log.message("option is:" + string);
			if (string.contains(option)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	/**
	 * Method to scroll page completely up
	 *
	 * @param
	 * @throws AWTException
	 */
	public static void ScrollPageCompletelyUp() throws AWTException {
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_PAGE_UP);
	}

	/**
	 * Method to validate vertical scroll bar is present
	 */
	public boolean isVerticalScrollBarPresent(WebDriver driver) {
		JavascriptExecutor javascript = (JavascriptExecutor) driver;
		Boolean VertscrollStatus = (Boolean) javascript
				.executeScript("return document.documentElement.scrollHeight>document.documentElement.clientHeight;");
		return VertscrollStatus;
	}

	/**
	 * Method to validate vertical scroll bar is present
	 */
	public boolean isHorizontalScrollBarPresent(WebDriver driver) {
		JavascriptExecutor javascript = (JavascriptExecutor) driver;
		Boolean VertscrollStatus = (Boolean) javascript
				.executeScript("return document.documentElement.scrollWidth>document.documentElement.clientWidth;");
		if (VertscrollStatus)
			Log.message("Vertical Scroll Bar is present");
		return VertscrollStatus;
	}

	/**
	 * select specific Number Of checkBoxes by Index
	 *
	 * @param list of checkboxes, no of checkbox to select
	 * @throws InterruptedException
	 */
	public void selectspecificNumberOfcheckBoxesFromIndex(List<WebElement> checkBox, int NoOfCheckboxesToClick)
			throws InterruptedException {

		for (int i = 0; i < NoOfCheckboxesToClick; i++) {
			checkBox.get(i).click();
			pause(1000);
		}
	}

	/**
	 * deselect all the checkboxes
	 *
	 * @param list of checkboxes
	 * @throws InterruptedException
	 */

	public void deselectAllTheCheckboxes(List<WebElement> checkBox) throws InterruptedException {

		for (WebElement webElement : checkBox) {
			if (webElement.isSelected()) {
				Log.message("checkbox is selected");
				webElement.click();
				pause(1000);
			} else
				continue;
		}
	}

	/**
	 * verify if attribute present in element
	 */
	public static boolean isAttribtuePresent(WebElement element, String attribute) {
		Boolean result = false;
		try {
			String value = element.getAttribute(attribute);
			if (value != null) {
				result = true;
				Log.message("inside attribute if:" + result);
			}
		} catch (Exception e) {
		}

		return result;
	}

	/**
	 * verify if attribute present in element
	 */
	public static boolean isAttribtuePresent(WebElement element, String attribute, String expectedValue) {
		Boolean result = false;
		try {
			String value = element.getAttribute(attribute);
			if (value.equals(expectedValue)) {
				result = true;
				Log.message("inside attribute if:" + result);
			}
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * wait For Specific element to disappear by locator
	 *
	 * @param List
	 * @return flag
	 */
	public static void waitUntilSpecificEementInvisibile(By locator, WebDriver driver) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofMinutes(80))
				.pollingEvery(Duration.ofSeconds(10)).ignoring(NoSuchElementException.class);

		wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
	}

	/**
	 * wait For Specific text to be present
	 */
	public static void waitForTextToBePresent(WebElement element, String text, WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		wait.until(ExpectedConditions.textToBePresentInElement(element, text));
	}

	/**
	 * method to scroll down
	 */
	public static void scrollDownToElementView(String elementXpath, WebDriver driver) throws Throwable {
		try {
			WebElement element = driver.findElement(By.xpath(elementXpath));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
			Thread.sleep(500);
		} catch (WebDriverException we) {
			System.out.println("Unable to scroll to the web element: " + we);
			Log.message("Unable to scroll to the webelement: " + we);

		}
	}

}
