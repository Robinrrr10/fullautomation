package com.github.fullautomation.apitest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fullautomation.client.ApiClient;
import com.github.fullautomation.client.ApiClient.ApiMethod;
import com.github.fullautomation.entries.User;

public class ApiClientTest {
	
	

	ApiClient apiClient = new ApiClient();
	
	//@Test
	public void testGet() throws ClientProtocolException, IOException {
		HttpResponse response = apiClient.httpExecute("https://reqres.in", null, ApiMethod.GET, "/api/users?page=2", null, null, null, null, null, null);
		System.out.println("done da");
		System.out.println("Response is:"+ EntityUtils.toString(response.getEntity()));
	}
	
	//@Test
	public void testPost() throws ClientProtocolException, IOException {
		User user = new User();
		user.setName("Ramu");
		user.setJob("HR");
		ObjectMapper objectMapper = new ObjectMapper();
		String stringEntity = objectMapper.writeValueAsString(user);
		User responseUser = apiClient.httpExecute("https://reqres.in", null, ApiMethod.POST, "/api/users?page=2", null, null, stringEntity, null, User.class);
		System.out.println("done da");
		System.out.println("Response is:"+ responseUser.getId());
		System.out.println("Response is:"+ responseUser.getCreatedAt());
	}
	
	@Test
	public void testMultiPart() throws ParseException, IOException {
		Map<String, Object> multipartForm = new HashMap<String, Object>();
		multipartForm.put("key1", "value1");
		multipartForm.put("key2", "value2");
		
		//Map<String, String> headers = new HashMap<String, String>();
		//headers.put("Content-Type", "multipart/form-data");
		//headers.put("Accept", "*/*");
		
		HttpResponse response = apiClient.httpExecute("https://httpbin.org", null, ApiMethod.POST, "/post", null, null, null, multipartForm, null, null);
		System.out.println("done da");
		System.out.println("Response is:"+ EntityUtils.toString(response.getEntity()));
	}
	
}
