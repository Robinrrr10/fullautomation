package com.github.fullautomation.client;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fullautomation.entries.User;

public class ApacheHttpClient {
	
	Logger log = LogManager.getLogger(getClass());
	
	public void checkApacheHttpGet() throws ClientProtocolException, IOException {
		
		String uri = "https://reqres.in/api/users/2";
		
		HttpClient client = HttpClients.createDefault();
		HttpGet getRequest = new HttpGet(uri);
		
		HttpResponse httpResponse = client.execute(getRequest);
		log.info("Status code:" + httpResponse.getStatusLine().getStatusCode());
		
		HttpEntity httpResponseEntity = httpResponse.getEntity();
		String stringBody = EntityUtils.toString(httpResponseEntity);
		log.info("Response Body in String:" + stringBody);
	}
	
public void checkApacheHttpPost() throws ClientProtocolException, IOException {
		
		String uri = "https://reqres.in/api/users/";
		
		HttpClient client = HttpClients.createDefault();
		HttpPost postRequest = new HttpPost(uri);
		
		User user = new User();
		user.setName("Ramu");
		user.setJob("HR");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String stringEntity = objectMapper.writeValueAsString(user);
		log.info("Entity in String:"+stringEntity);
		
		
		HttpEntity entity = new StringEntity(stringEntity);
		postRequest.setEntity(entity);
		postRequest.setHeader("Content-type","application/json");
		HttpResponse httpResponse = client.execute(postRequest);
		log.info("Status code:" + httpResponse.getStatusLine().getStatusCode());
		
		HttpEntity httpResponseEntity = httpResponse.getEntity();
		String stringBody = EntityUtils.toString(httpResponseEntity);
		log.info("Response Body in String:" + stringBody);
		
		ObjectMapper objectMapper2 = new ObjectMapper();
		User responseUser = objectMapper2.readValue(stringBody, User.class);
		log.info("User:"+responseUser.getName()+ " CreatedAt:"+responseUser.getCreatedAt());
		
		log.trace("--5----------------trace------------");
		log.debug("---4--------------------debug-------");
		log.info("--3----info------------------------");
		log.warn("--2--------warn--------------------");
		log.error("--1-----------error-----------------");
		
	}

}
