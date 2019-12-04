package com.github.fullautomation.client;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class RestAssuredClient {
	
	public void getCall() {
		String url = "http://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22";
		Response response = RestAssured.get(url);
		System.out.println("StatusCode :"+response.getStatusCode());
		System.out.println("body as String:"+response.getBody().asString());
		System.out.println("response as string:"+response.asString());
		response.getHeaders().asList().stream().forEach(c -> System.out.println(c.getName() + " : " + c.getValue()));
	}

}
