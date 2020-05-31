package com.sentieo.rest.base;

import static com.jayway.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import com.aventstack.extentreports.Status;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.sentieo.report.ExtentTestManager;
import com.sentieo.report.Reporter;

public class RestUtils {

	private static final String BREAK_LINE = "\n";
	protected static final Reporter reporter = Reporter.getInstance();

	public static Response post(String url, String payload, RequestSpecification spec, Map<?, ?> params)
			throws Exception {
		Response res = null;
		try {
			String infoMessage = BREAK_LINE + "<div> Type: [POST] </div>" + BREAK_LINE + "<div> Parameters: "
					+ (params == null ? "[EMPTY]" : params.toString()) + "</div>";

			infoMessage = infoMessage + reporter.generateFormatedRequestHeader(spec);

			ExtentTestManager.getTest().log(Status.INFO, infoMessage);

			if (payload == null) {
				res = given().spec(spec).when().post(url);
			} else {
				res = given().spec(spec).body(payload).when().post(url);
			}
			ExtentTestManager.getTest().log(Status.INFO, reporter.generateFormatedResponse(res));
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return res;
	}

	public static Response login(String url, String payload, RequestSpecification spec, HashMap<String, String> params)
			throws Exception {
		Response res = null;
		try {
			if (payload == null) {
				res = given().spec(spec).when().post(url);
			} else {
				res = given().spec(spec).body(payload).when().post(url);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return res;
	}

	public static Response get(String url, RequestSpecification spec, Map<?, ?> params) throws Exception {
		Response res = null;
		try {
			String infoMessage = BREAK_LINE + "<div> Type: [GET] </div>" + BREAK_LINE + "<div> Parameters: "
					+ (params == null ? "[EMPTY]" : params.toString()) + "</div>";

			infoMessage = infoMessage + BREAK_LINE + "<div> URI: " + url + "</div>";
			infoMessage = infoMessage + reporter.generateFormatedRequestHeader(spec);
			ExtentTestManager.getTest().log(Status.INFO, infoMessage);

			res = given().spec(spec).when().get(url);
			ExtentTestManager.getTest().log(Status.INFO, reporter.generateFormatedResponse(res));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return res;
	}

}
