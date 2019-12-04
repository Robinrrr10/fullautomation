package com.github.fullautomation;

import org.testng.annotations.Test;

import com.github.fullautomation.client.RestAssuredClient;

public class RestAssuredTest {
	
	@Test
	public void testGet() {
		RestAssuredClient restAssuredClient = new RestAssuredClient();
		restAssuredClient.getCall();
	}

}
