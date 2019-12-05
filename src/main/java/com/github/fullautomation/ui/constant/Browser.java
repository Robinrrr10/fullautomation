package com.github.fullautomation.ui.constant;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public enum Browser {

	CHROME("webdriver.chrome.driver", "drivers/chrome/chromedriver.exe", new ChromeDriver()),
	FIREFOX("webdriver.gecko.driver", "drivers/firefox/geckodriver.exe", new FirefoxDriver());
	
	String driverName;
	String driverPath;
	WebDriver driver;
	
	Browser(String driverName, String driverPath, WebDriver driver){
		this.driverName = driverName;
		this.driverPath = driverPath;
		this.driver = driver;
	}

	public String getDriverName() {
		return driverName;
	}

	public String getDriverPath() {
		return driverPath;
	}

	public WebDriver getDriver() {
		return driver;
	}

}
