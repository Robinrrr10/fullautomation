package com.github.fullautomation;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.github.fullautomation.client.ApacheHttpClient;

/**
 * Unit test for simple App.
 */

public class AppTest 
{
	//Listeners
	Logger logger = LogManager.getLogger(getClass());
   
	@Test
	public void testApacheHttp() throws ClientProtocolException, IOException {
		logger.info("Starting test 1");
		ApacheHttpClient apacheHttp = new ApacheHttpClient();
		apacheHttp.checkApacheHttpGet();
		logger.info("Done test 1");
	}
	
	@Test
	public void testApacheHttpPost() throws ClientProtocolException, IOException {
		logger.info("Starting test 2");
		ApacheHttpClient apacheHttp = new ApacheHttpClient();
		apacheHttp.checkApacheHttpPost();
		logger.info("Done test 2");
	}
}
