package com.sentieo.rest.base;

import static com.jayway.restassured.RestAssured.given;

import java.util.HashMap;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.specification.RequestSpecification;

public class RestAPISpecs {

	protected String apid = "";
	protected String usid = "";
	

	protected RequestSpecification mobileFormParamsSpec(HashMap<String, Object> formParams) {
		formParams.put("csrfmiddlewaretoken", "a");
		formParams.put("loc", "ios");
		
		//System.out.println("Request parameters" + formParams);
		return given().contentType("application/x-www-form-urlencoded; charset=UTF-8")
					.accept(ContentType.JSON)
					.cookie("csrftoken", "a")
					.cookie("apid", apid)
					.cookie("usid", usid)
					.cookie("app_version", "7.2")
					.cookie("device_name","iphone")
					.cookie("ios_version","13.2.2")
					.formParameters(formParams);
		
		
	}
	
	protected RequestSpecification mobileLoginSpec(HashMap<String, String> formParams) {
		try {
		formParams.put("csrfmiddlewaretoken", "a");
		formParams.put("loc", "ios");
		return given().contentType("application/x-www-form-urlencoded; charset=UTF-8")
				.accept(ContentType.JSON)
				.cookie("csrftoken", "a")
				.cookie("app_version", "7.2")
				.cookie("device_name","iphone")
				.cookie("ios_version","13.2.2")
				.formParameters(formParams);
		        
		
		
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		//System.out.println("Request parameters" +formParams);
		return null;
	}
}
