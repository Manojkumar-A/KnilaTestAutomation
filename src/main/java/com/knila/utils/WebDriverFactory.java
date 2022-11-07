package com.knila.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * common methods for webdriver initialization
 */
public class WebDriverFactory {

	/**
	 * Initiate webdriver based on config file
	 *
	 * @return webDriver
	 * @throws Exception
	 */
	public static WebDriver newWebDriverInstance(String website, String browserType) throws Exception {
		WebDriver webdriver = null;
		try {
			switch (browserType.toLowerCase()) {

			case "chrome":
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--test-type");
				options.addArguments("start-maximized");
				options.addArguments("--ignore-certificate-errors");
				options.addArguments("--disable-extensions");
				options.addArguments("--disable-web-security");
				options.addArguments("--always-authorize-plugins");
				options.addArguments("--allow-running-insecure-content");
				options.addArguments("--disable-popup-blocking");
				WebDriverManager.chromedriver().setup();
				webdriver = new ChromeDriver(options);
				((JavascriptExecutor) webdriver).executeScript("window.resizeTo(screen.width, screen.height);");
				webdriver.get(website);
				break;

			case "firefox":
				FirefoxOptions firefoxOptions = new FirefoxOptions();
				firefoxOptions.addPreference("network.automatic-ntlm-auth.trusted-uris", "http://,https://");
				firefoxOptions.addPreference("network.automatic-ntlm-auth.allow-non-fqdn", true);
				firefoxOptions.addPreference("network.negotiate-auth.delegation-uris", "http://,https://");
				firefoxOptions.addPreference("network.negotiate-auth.trusted-uris", "http://,https://");
				firefoxOptions.addPreference("network.http.phishy-userpass-length", 255);
				firefoxOptions.addPreference("security.csp.enable", false);
				firefoxOptions.addPreference("network.proxy.no_proxies_on", "");
				WebDriverManager.firefoxdriver().setup();
				webdriver = new FirefoxDriver(firefoxOptions);
				((JavascriptExecutor) webdriver).executeScript("window.resizeTo(screen.width, screen.height);");
				webdriver.get(website);
				break;

			case "ie":
				InternetExplorerOptions ieOptions = new InternetExplorerOptions();
				ieOptions.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, false);
				ieOptions.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
				ieOptions.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false);
				ieOptions.setCapability("ignoreProtectedModeSettings", true);
				ieOptions.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
				ieOptions.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
				WebDriverManager.iedriver().setup();
				webdriver = new InternetExplorerDriver(ieOptions);
				((JavascriptExecutor) webdriver).executeScript("window.resizeTo(screen.width, screen.height);");
				webdriver.get(website);
				break;

			case "chromium_edge":
				EdgeOptions edgeOptions = new EdgeOptions();
				edgeOptions.setCapability("UseChromium", true);
				edgeOptions.addArguments("headless");
				WebDriverManager.edgedriver().setup();
				webdriver = new EdgeDriver(edgeOptions);
				webdriver.get(website);

			}
		} catch (Exception e) {
			Log.exception(e);
		}
		return webdriver;

	}

}
