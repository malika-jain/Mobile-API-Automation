package com.sentieo.assertion;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.zjsonpatch.JsonDiff;
import com.jayway.restassured.response.Response;
import com.sentieo.report.ExtentTestManager;
import com.sentieo.utils.FileUtil;
import com.sentieo.utils.FormatterUtil;

import io.restassured.module.jsv.JsonSchemaValidationException;

public class APIAssertions extends SoftAssertion {
	private static final String ACTUAL_DECO = "<span style=\"font-weight: bold;\"> A : </span>";
	private static final String EXPECT_DECO = "<span style=\"font-weight: bold;\"> E : </span>";

	public boolean verifyStatusCode(int actual, int expected) {
		return verifyEquals(actual, expected, "verify HTTP response code");
	}

	public void verifyResponseTime(Response res, long timeInMillis) {
		String message = "";
		try {
			Assert.assertTrue(res.getTimeIn(TimeUnit.MILLISECONDS) < timeInMillis);
			message = "Response time: [" + res.getTimeIn(TimeUnit.MILLISECONDS) + " ms] is less than expected ["
					+ timeInMillis + "] milliseconds.";
			//ExtentTestManager.getTest().log(Status.PASS, message);
		} catch (JSONException je) {
			// verificationFailures.add(je);
			message = "Response time: [<font color=\"red\">" + res.getTimeIn(TimeUnit.MILLISECONDS)
					+ "</font> ms] is more than expected [" + timeInMillis + "] milliseconds.";
			ExtentTestManager.getTest().log(Status.WARNING, message);
		} catch (AssertionError ae) {
			// verificationFailures.add(ae);
			message = "Response time: [<font color=\"red\">" + res.getTimeIn(TimeUnit.MILLISECONDS)
					+ "</font> ms] is more than expected [" + timeInMillis + "] milliseconds.";
			ExtentTestManager.getTest().log(Status.WARNING, message);
		}
	}

	public boolean verifyEquals(final Object actual, final Object expected) {
		return verifyEquals(actual, expected, "");
	}

	public boolean verifyEquals(final Object actual, final Object expected, final String stepDetail) {
		boolean result = false;
		String message = "verifyEquals: [" + stepDetail + "]" + BREAK_LINE + ACTUAL_DECO
				+ (actual != null ? actual.toString() : "null") + BREAK_LINE + EXPECT_DECO
				+ (expected != null ? expected.toString() : "null");
		try {
			Assert.assertEquals(actual, expected);
			//ExtentTestManager.getTest().log(Status.PASS, message);
			result = true;
		} catch (JSONException je) {
			verificationFailures.add(je);
			ExtentTestManager.getTest().log(Status.FAIL, message);
		} catch (AssertionError ae) {
			verificationFailures.add(ae);
			ExtentTestManager.getTest().log(Status.FAIL, message);
		}
		return result;
	}

	public boolean verifyTrue(Object actual, String stepDetail) {
		boolean result = false;
		String message = "verifyTrue: [" + stepDetail + "]" + BREAK_LINE + ACTUAL_DECO
				+ (actual != null ? actual.toString() : "null");
		try {

			Assert.assertTrue(actual != null);
			ExtentTestManager.getTest().log(Status.PASS, message);
			result = true;
		} catch (JSONException je) {
			verificationFailures.add(je);
			ExtentTestManager.getTest().log(Status.FAIL, message);
		} catch (AssertionError ae) {
			verificationFailures.add(ae);
			ExtentTestManager.getTest().log(Status.FAIL, message);
		}
		return result;

	}

	public boolean assertEqualsActualContainsExpected(String actual, String expected, final String stepDetail) {
		boolean result = false;
		String message = "verifyEquals: [" + stepDetail + "]" + BREAK_LINE + ACTUAL_DECO
				+ (actual != null ? actual.toString() : "null") + BREAK_LINE + EXPECT_DECO
				+ (expected != null ? expected.toString() : "null");
		try {
			Assert.assertTrue(actual.contains(expected));
			ExtentTestManager.getTest().log(Status.PASS, message);
			result = true;
		} catch (JSONException je) {
			verificationFailures.add(je);
			ExtentTestManager.getTest().log(Status.FAIL, message);
		} catch (AssertionError e) {
			verificationFailures.add(e);
			ExtentTestManager.getTest().log(Status.FAIL, message);
		}
		return result;
	}

	public boolean jsonSchemaValidation(Response res) {
		String jsonFileName = new Throwable().getStackTrace()[1].getMethodName() + ".json";
		boolean assertJsonSchema = false;
		StringBuffer sb = new StringBuffer();
		try {
			FileUtil fileUtil = new FileUtil();
			File file = fileUtil.getFileFromResources(jsonFileName);

			sb = fileUtil.getFileContent(file);
			res.then().assertThat().body(matchesJsonSchemaInClasspath(jsonFileName));
			assertJsonSchema = true;
			ExtentTestManager.getTest().log(Status.PASS, FormatterUtil.generateFormatedResponse(res, sb.toString()));
		} catch (IllegalArgumentException e) {
			verificationFailures.add(e);
			ExtentTestManager.getTest().log(Status.FAIL, FormatterUtil.generateFormatedResponse(res, sb.toString()));
		} catch (IOException e) {
			verificationFailures.add(e);
			ExtentTestManager.getTest().log(Status.FAIL, FormatterUtil.generateFormatedResponse(res, sb.toString()));
		} catch (JsonSchemaValidationException jsve) {
			verificationFailures.add(jsve);
			ExtentTestManager.getTest().log(Status.FAIL, FormatterUtil.generateFormatedResponse(res, sb.toString()));
		} catch (AssertionError ae) {
			verificationFailures.add(new Exception("JSON schema validation failed"));
			ExtentTestManager.getTest().log(Status.FAIL, FormatterUtil.generateFormatedResponse(res, sb.toString()));
		}

		return assertJsonSchema;
	}

	public boolean jsonSchemaValidation(Response res, String jsonFileName) {
		boolean assertJsonSchema = false;
		StringBuffer sb = new StringBuffer();
		try {
			FileUtil fileUtil = new FileUtil();
			File file = fileUtil.getFileFromResources(jsonFileName);

			sb = fileUtil.getFileContent(file);
			res.then().assertThat().body(matchesJsonSchemaInClasspath(jsonFileName));
			assertJsonSchema = true;
			ExtentTestManager.getTest().log(Status.PASS, FormatterUtil.generateFormatedResponse(res, sb.toString()));
		} catch (IllegalArgumentException e) {
			verificationFailures.add(e);
			ExtentTestManager.getTest().log(Status.FAIL, FormatterUtil.generateFormatedResponse(res, sb.toString()));
		} catch (IOException e) {
			verificationFailures.add(e);
			ExtentTestManager.getTest().log(Status.FAIL, FormatterUtil.generateFormatedResponse(res, sb.toString()));
		} catch (JsonSchemaValidationException jsve) {
			verificationFailures.add(jsve);
			ExtentTestManager.getTest().log(Status.FAIL, FormatterUtil.generateFormatedResponse(res, sb.toString()));
		} catch (AssertionError ae) {
			verificationFailures.add(new Exception("JSON schema validation failed"));
			String error = ae.getMessage();
			String errorLog = jsonSchemaErrorLogs(error);
			ExtentTestManager.getTest().log(Status.FAIL,
					FormatterUtil.generateFormatedResponse(res, sb.toString(), errorLog));
		}

		return assertJsonSchema;
	}

	public boolean assertNotEquals(Object actual, Object expected, final String stepDetail) {
		boolean result = true;
		String message = "verifyEquals: [" + stepDetail + "]" + BREAK_LINE + ACTUAL_DECO
				+ (actual != null ? actual.toString() : "null") + BREAK_LINE + EXPECT_DECO
				+ (expected != null ? expected.toString() : "null");
		try {
			Assert.assertNotEquals(actual, expected);
			ExtentTestManager.getTest().log(Status.PASS, message);
			result = false;
		} catch (JSONException je) {
			verificationFailures.add(je);
			ExtentTestManager.getTest().log(Status.FAIL, message);
		} catch (AssertionError ae) {
			verificationFailures.add(ae);
			ExtentTestManager.getTest().log(Status.FAIL, message);
		}
		return result;
	}

	public boolean verifyEquals(Double actual, Double expected, final String stepDetail) {
		boolean result = false;
		String ACTUAL_DECO = "<span style=\"font-weight: bold;\"> Difference : </span>";
		String EXPECT_DECO = "<span style=\"font-weight: bold;\"> Expected Delta not more than : </span>";
		String message = "verifyEquals: [" + stepDetail + "]" + BREAK_LINE + ACTUAL_DECO
				+ (actual != null ? actual.toString() : "null") + BREAK_LINE + EXPECT_DECO
				+ (expected != null ? expected.toString() : "null");
		try {
			Assert.assertFalse(actual > expected);
			ExtentTestManager.getTest().log(Status.PASS, message);
			result = true;
		} catch (JSONException je) {
			verificationFailures.add(je);
			ExtentTestManager.getTest().log(Status.FAIL, message);
		} catch (AssertionError ae) {
			verificationFailures.add(ae);
			ExtentTestManager.getTest().log(Status.FAIL, message);
		}
		return result;
	}

	public boolean assertTrue(boolean value, String stepDetail) {
		boolean result = false;
		try {
			Assert.assertTrue(value);
			ExtentTestManager.getTest().log(Status.PASS, stepDetail);
			result = true;
		} catch (JSONException je) {
			verificationFailures.add(je);
			ExtentTestManager.getTest().log(Status.FAIL, stepDetail);
		} catch (AssertionError e) {
			verificationFailures.add(e);
			ExtentTestManager.getTest().log(Status.FAIL, stepDetail);
		}
		return result;
	}

	public boolean assertFalse(boolean value, String stepDetail) {
		boolean result = true;
		try {
			Assert.assertFalse(value);
			ExtentTestManager.getTest().log(Status.PASS, stepDetail);
			result = true;
		} catch (JSONException je) {
			verificationFailures.add(je);
			ExtentTestManager.getTest().log(Status.FAIL, stepDetail);
		} catch (AssertionError e) {
			verificationFailures.add(e);
			ExtentTestManager.getTest().log(Status.FAIL, stepDetail);
		}
		return result;
	}


	public boolean assertTrue(String alertSettings,String settings,String stepDetail) throws  IOException
	{
		boolean result =false;
		JsonNode patch =null;
		try {
			ObjectMapper mapper = new ObjectMapper();
            JsonNode json1= mapper.readTree(alertSettings);
            JsonNode json2= mapper.readTree(settings);
            patch = JsonDiff.asJson(json1, json2);
            Assert.assertTrue(patch.size()==0);
            ExtentTestManager.getTest().log(Status.PASS, stepDetail);
			result = true;
		} catch (JSONException je) {
			verificationFailures.add(je);
			ExtentTestManager.getTest().log(Status.FAIL, stepDetail+" "+patch);
		} catch (AssertionError e) {
			verificationFailures.add(e);
			ExtentTestManager.getTest().log(Status.FAIL, stepDetail+" "+patch);
		}
		return result;

	}
	
	public boolean compareDates(String actual, String expected, final String stepDetail) throws ParseException {
		boolean result = false;
		String message = "verifyEquals: [" + stepDetail + "]" + BREAK_LINE + ACTUAL_DECO
				+ (actual != null ? actual.toString() : "null") + BREAK_LINE + EXPECT_DECO
				+ (expected != null ? expected.toString() : "null");
		try {
			Date date1 = new SimpleDateFormat("M/dd/yy").parse(actual);

			Date date2 = new SimpleDateFormat("M/dd/yy").parse(expected);

			Assert.assertEquals(date1, date2);
			ExtentTestManager.getTest().log(Status.PASS, message);
			result = true;
		} catch (JSONException je) {
			verificationFailures.add(je);
			ExtentTestManager.getTest().log(Status.FAIL, message);
		} catch (AssertionError e) {
			verificationFailures.add(e);
			ExtentTestManager.getTest().log(Status.FAIL, message);
		}
		return result;
	}

	public boolean assertEquals(List<BigInteger> actual, List<BigInteger> expected, String stepDetail) {
		boolean result = false;
		String message = "verifyEquals: [" + stepDetail + "]" + BREAK_LINE + ACTUAL_DECO
				+ (actual != null ? actual.toString() : "null") + BREAK_LINE + EXPECT_DECO
				+ (expected != null ? expected.toString() : "null");
		try {
			SoftAssert softAssertion = new SoftAssert();
			softAssertion.assertEquals(actual.size(), expected.size());
			softAssertion.assertEquals(actual, expected);
			softAssertion.assertAll();
			ExtentTestManager.getTest().log(Status.PASS, message);
			result = true;
		} catch (JSONException je) {
			verificationFailures.add(je);
			ExtentTestManager.getTest().log(Status.FAIL, message);
		} catch (AssertionError e) {
			verificationFailures.add(e);
			ExtentTestManager.getTest().log(Status.FAIL, message);
		}
		return result;
	}
	public boolean assertEquals(List<String> actual, List<String> expected,  String stepDetail, boolean val) {
		boolean result = false;
		String message = "verifyEquals: [" + stepDetail + "]" + BREAK_LINE + ACTUAL_DECO
				+ (actual != null ? actual.toString() : "null") + BREAK_LINE + EXPECT_DECO
				+ (expected != null ? expected.toString() : "null");
		try {
			SoftAssert softAssertion = new SoftAssert();
			softAssertion.assertEquals(actual.size(), expected.size());
			softAssertion.assertEquals(actual, expected);
			softAssertion.assertAll();
			ExtentTestManager.getTest().log(Status.PASS, message);
			result = true;
		} catch (JSONException je) {
			verificationFailures.add(je);
			ExtentTestManager.getTest().log(Status.FAIL, message);
		} catch (AssertionError e) {
			verificationFailures.add(e);
			ExtentTestManager.getTest().log(Status.FAIL, message);
		}
		return result;
	}

	public String jsonSchemaErrorLogs(String stackTrace) {
		StringBuffer sb = new StringBuffer();
		int errorCounter = 1;
		try {
			String[] tokenisedErrorStackTrace = stackTrace.split("\\n");
			for (int i = 0; i < tokenisedErrorStackTrace.length; i++) {
				if (tokenisedErrorStackTrace[i].contains("error:")) {
					sb.append("******Error#" + String.valueOf(errorCounter++) + "******");
					sb.append(System.getProperty("line.separator"));
					++i;
					sb.append(tokenisedErrorStackTrace[++i]);
					sb.append(System.getProperty("line.separator"));
					sb.append(tokenisedErrorStackTrace[++i]);
					sb.append(System.getProperty("line.separator"));
					++i;
					sb.append(tokenisedErrorStackTrace[++i]);
					sb.append(System.getProperty("line.separator"));
					sb.append(tokenisedErrorStackTrace[++i]);
					sb.append(System.getProperty("line.separator"));
					sb.append(tokenisedErrorStackTrace[++i]);
					sb.append(System.getProperty("line.separator"));
					sb.append(System.getProperty("line.separator"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
