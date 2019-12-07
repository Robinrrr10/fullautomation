package com.github.fullautomation.ui;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


public class BaseUIDriver {
	
	protected static WebDriver driver;
	
	static {
		System.setProperty("webdriver.chrome.driver", "drivers/chrome/chromedriver.exe");
		driver = new ChromeDriver();
	}
	

}
