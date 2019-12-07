package com.github.fullautomation.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.github.fullautomation.ui.BaseUIDriver;
import com.github.fullautomation.ui.constant.Locators;

public class SimpleLearnHome extends BaseUIDriver{
	
	public void openHome() {
		driver.get(Locators.SIMPLILEARN_BASE_URL);
	}
	
	public void search(String searchText) {
		driver.findElement(By.id(Locators.id_search)).sendKeys(searchText);
		driver.findElement(By.className(Locators.class_search_icon)).click();
	}
	
	public void clickOnSearch(String searchText) {
		driver.findElement(By.xpath("//h2[text() = '"+searchText+"']")).click();
	}

	public void closeSimpleLearn() {
		driver.close();
	}
}
