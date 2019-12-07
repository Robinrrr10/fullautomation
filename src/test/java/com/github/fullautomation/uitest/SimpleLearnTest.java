package com.github.fullautomation.uitest;

import org.testng.annotations.Test;

import com.github.fullautomation.ui.pages.SimpleLearnHome;

public class SimpleLearnTest {

	@Test
	public void testSearchField() throws InterruptedException {
		
		System.out.println("start test");
		SimpleLearnHome home = new SimpleLearnHome();
		home.openHome();
		home.search("Big Data");
		Thread.sleep(7000);
		home.clickOnSearch("Big Data Engineer");
		System.out.println("done test");
	}
	
}
