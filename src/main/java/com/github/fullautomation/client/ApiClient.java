package com.github.fullautomation.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class ApiClient {
	
	Logger log = LogManager.getLogger(getClass());
	
	public HttpResponse httpExecute(String hostName, Map<String, String> headers, ApiMethod method, String endPoint, Map<String, String> pathParams , Map<String, String> queryParams, String payload, Map<String, Object> form, BodyType contentType, BodyType accept) throws ClientProtocolException, IOException {
		
		log.info("Http Request");
		
		HttpResponse httpResponse = null;
		HttpClient client = HttpClients.createDefault();
		
		if(endPoint != null && pathParams != null && pathParams.size() > 0) {
			for(Map.Entry<String, String> pathParam : pathParams.entrySet()) {
				endPoint = endPoint.replace("{"+pathParam.getKey()+"}", pathParam.getValue());
			}
		}
		
		String uri = hostName;
		if(endPoint != null)
			uri = uri + endPoint;
		
		if(headers == null && contentType != null) {
			headers = new HashMap<String, String>();
			headers.put("Content-Type", contentType.getValue());
		}
		
		if(queryParams != null && queryParams.size() > 0) {
			uri = uri + "?";
			for(Map.Entry<String, String> param : queryParams.entrySet()) {
				if(!uri.endsWith("?"))
					uri = uri + "&";
				uri = uri + param.getKey() + "=" + param.getValue();
			}
		}
		
		switch(method) {
		case GET:{
			log.info("Http Get request");
			HttpGet getRequest = new HttpGet(uri);
			if(headers != null&& headers.size() > 0) {
				for(Map.Entry<String, String> head : headers.entrySet()) {
					getRequest.addHeader(head.getKey(), head.getValue());
				}
			}
			log.info("http execute");
			httpResponse = client.execute(getRequest);
			log.info("http executed");
			break;
		}
		case POST:{
			HttpPost postRequest = new HttpPost(uri);
			if(headers != null && headers.size() > 0) {
				for(Map.Entry<String, String> head : headers.entrySet()) {
					postRequest.addHeader(head.getKey(), head.getValue());
				}
			}
			if(payload != null) {
				HttpEntity entity = new StringEntity(payload);
				postRequest.setEntity(entity);
			}
			
			if(form != null && form.size() > 0) {
				if(contentType == BodyType.MULTIPART_FORM) {
				postRequest.removeHeaders("Content-Type");  //below method will add correct Content-Type
				MultipartEntityBuilder multipart = MultipartEntityBuilder.create();
				multipart.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
				for(Map.Entry<String, Object> eachForm : form.entrySet()) {
					if(eachForm.getValue() instanceof File) {
						multipart.addBinaryBody(eachForm.getKey(), (File)eachForm.getValue());
					}else {
						log.info("Adding multipart");
						multipart.addTextBody(eachForm.getKey(), eachForm.getValue().toString());
					}
				}
				HttpEntity formEntity = multipart.build();
				postRequest.setEntity(formEntity);
				}else if(contentType == BodyType.URL_ENCODED_FORM) {
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					for(Map.Entry<String, Object> eachForm : form.entrySet()) {
					nameValuePairs.add(new BasicNameValuePair(eachForm.getKey(), eachForm.getValue().toString()));
					}
					UrlEncodedFormEntity urlEncodedForm = new UrlEncodedFormEntity(nameValuePairs, "utf-8");
					urlEncodedForm.setContentType(contentType.getValue());
					postRequest.setEntity(urlEncodedForm); 
				}
			}
			httpResponse = client.execute(postRequest);
			break;
		}
		case PUT:{
			HttpPut putRequest = new HttpPut(uri);
			if(headers != null&& headers.size() > 0) {
				for(Map.Entry<String, String> head : headers.entrySet()) {
					putRequest.addHeader(head.getKey(), head.getValue());
				}
			}
			if(payload != null) {
				HttpEntity entity = new StringEntity(payload);
				putRequest.setEntity(entity);
			}
			httpResponse = client.execute(putRequest);
			break;
		}
		case DELETE:{
			HttpDelete deleteRequest = new HttpDelete(uri);
			if(headers != null&& headers.size() > 0) {
				for(Map.Entry<String, String> head : headers.entrySet()) {
					deleteRequest.addHeader(head.getKey(), head.getValue());
				}
			}
			httpResponse = client.execute(deleteRequest);
			break;
		}
		}
		log.info("done http");
		return httpResponse;
	}
	

	public <T> T httpExecute(String hostName, Map<String, String> header, ApiMethod method, String endPoint, Map<String, String> pathParam, Map<String, String> queryParam, String payload, Map<String, Object> form, BodyType contentType, BodyType accept ,Class<T> responseClassName) throws ParseException, IOException {
		
		HttpResponse httpResponse = this.httpExecute(hostName, header, method, endPoint, pathParam, queryParam, payload, form, contentType, accept);
		
		String responseContentType = null;
		Header[] headers = httpResponse.getHeaders("Content-Type");
		responseContentType = headers[0].getValue();
		
		HttpEntity httpResponseEntity = httpResponse.getEntity();
		String stringBody = EntityUtils.toString(httpResponseEntity);
		
		T t = null ;
		
		if(responseContentType != null && responseContentType.contains("json")) {
			ObjectMapper objectMapper = new ObjectMapper();	
			t = objectMapper.readValue(stringBody, responseClassName);
		}else if(responseContentType != null && responseContentType.contains("xml")) {
			XmlMapper xmlMapper = new XmlMapper();
			t = xmlMapper.readValue(stringBody, responseClassName);
		}
		return t;
	}
	
	//public HttpResponse httpExecute(String hostName, Map<String, String> header, ApiMethod method, String endPoint, Map<String, String> pathParam, Map<String, String> queryParam, Map<String, Object> form, BodyType contentType, BodyType accept, Object payload) throws ParseException, IOException {
	public <T> HttpResponse httpExecute(String hostName, Map<String, String> header, ApiMethod method, String endPoint, Map<String, String> pathParam, Map<String, String> queryParam, T payload,  BodyType contentType, BodyType accept, Map<String, Object> form) throws ParseException, IOException {
		String stringEntity = null;
		if(contentType != null && contentType == BodyType.APPLICATION_JSON) {
			ObjectMapper objectMapper = new ObjectMapper();
			stringEntity = objectMapper.writeValueAsString(payload);
		}else if(contentType != null && contentType == BodyType.APPLICATION_XML) {
			XmlMapper xmlMapper = new XmlMapper();
			stringEntity = xmlMapper.writeValueAsString(payload);
		}
		HttpResponse httpResponse = this.httpExecute(hostName, header, method, endPoint, pathParam, queryParam, stringEntity, form, contentType, accept);
		return httpResponse;
	}
	
	public <T> T httpExecute(String hostName, Map<String, String> header, ApiMethod method, String endPoint, Map<String, String> pathParam, Map<String, String> queryParam, Map<String, Object> form, BodyType contentType, BodyType accept ,T payload, Class<T> responseClassName) throws ParseException, IOException {
		
		String stringEntity = null;
		if(contentType != null && contentType == BodyType.APPLICATION_JSON) {
			ObjectMapper objectMapper = new ObjectMapper();
			stringEntity = objectMapper.writeValueAsString(payload);
		}else if(contentType != null && contentType == BodyType.APPLICATION_XML) {
			XmlMapper xmlMapper = new XmlMapper();
			stringEntity = xmlMapper.writeValueAsString(payload);
		}
		
		HttpResponse httpResponse = this.httpExecute(hostName, header, method, endPoint, pathParam, queryParam, stringEntity, form, contentType, accept);
		
		String responseContentType = null;
		Header[] headers = httpResponse.getHeaders("Content-Type");
		responseContentType = headers[0].getValue();
		
		HttpEntity httpResponseEntity = httpResponse.getEntity();
		String stringBody = EntityUtils.toString(httpResponseEntity);
		
		T t = null ;
		
		if(responseContentType != null && responseContentType.contains("json")) {
			ObjectMapper objectMapper = new ObjectMapper();	
			t = objectMapper.readValue(stringBody, responseClassName);
		}else if(responseContentType != null && responseContentType.contains("xml")) {
			XmlMapper xmlMapper = new XmlMapper();
			t = xmlMapper.readValue(stringBody, responseClassName);
		}
		return t;
	}
	
	public enum ApiMethod {	
		
		GET,
		POST,
		PUT,
		DELETE
		
	}


	public File responseToFile(HttpResponse response, String filePath) throws UnsupportedOperationException, IOException {
		File file = null;
		InputStream inputStream = response.getEntity().getContent();
		FileOutputStream fileOutpuStream = new FileOutputStream(new File(filePath));
		int inByte;
		while((inByte = inputStream.read()) != -1)
			fileOutpuStream.write(inByte);
		inputStream.close();
		fileOutpuStream.close();
		file = new File(filePath);
		return file;
	}
	
	public enum BodyType {
		
		APPLICATION_JSON("application/json"),
		APPLICATION_XML("application/xml"),
		MULTIPART_FORM("multipart/form-data"),
		URL_ENCODED_FORM("application/x-www-form-urlencoded");
		
		String value;
		
		BodyType(String value){
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
	}
}
