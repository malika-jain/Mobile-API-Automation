package com.sentieo.listener;

import org.apache.commons.lang3.StringUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.sentieo.report.ExtentManager;
import com.sentieo.report.ExtentTestManager;

public class Listener implements ITestListener {

	private static String getTestMethodName(ITestResult iTestResult) {
		return iTestResult.getMethod().getConstructorOrMethod().getName();
	}

	// <span class="label label-primary">Primary Label</span>

	// Before starting all tests, below method runs.
	@Override
	public void onStart(ITestContext iTestContext) {
		System.out.println("I am in onStart method " + iTestContext.getName());

	}

	// After ending all tests, below method runs.
	@Override
	public void onFinish(ITestContext iTestContext) {
		System.out.println("I am in onFinish method " + iTestContext.getName());
		// Do tier down operations for extentreports reporting!
		ExtentTestManager.endTest();
		ExtentManager.createInstance().flush();
	}

	@Override
	public void onTestStart(ITestResult iTestResult) {
		Object[] params = iTestResult.getParameters();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < params.length; i++) {
			if (!StringUtils.isEmpty(params[i].toString())) {
				sb.append(params[i].toString());
				if (i < params.length - 1) {
					sb.append(" , ");
				}
			}
		}
		String paramsList = sb.toString();
		
		if (paramsList.length() > 100)
			paramsList = paramsList.substring(0, Math.min(paramsList.length(), 100));
		System.out.println("I am in onTestStart method " + getTestMethodName(iTestResult) + " start");
		// Start operation for extentreports.
		ExtentTestManager.startTest(iTestResult.getTestClass().getName() + " ---> "  + iTestResult.getMethod().getMethodName()+ " (" + paramsList + ")");
	}

	@Override
	public void onTestSuccess(ITestResult iTestResult) {
			
		
		System.out.println("I am in onTestSuccess method " + getTestMethodName(iTestResult) + " succeed");
		ExtentTestManager.getTest().log(Status.PASS,
				MarkupHelper.createLabel(iTestResult.getName() + " Test Case PASSED", ExtentColor.GREEN));
		ExtentTestManager.removeTest();
	}

	@Override
	public void onTestFailure(ITestResult iTestResult) {
		System.out.println("I am in onTestFailure method " + getTestMethodName(iTestResult) + " failed");
		ExtentTestManager.getTest().log(Status.FAIL,
				MarkupHelper.createLabel(iTestResult.getName() + " - Test Case Failed", ExtentColor.RED));
		

	}

	@Override
	public void onTestSkipped(ITestResult iTestResult) {
		System.out.println("I am in onTestSkipped method " + getTestMethodName(iTestResult) + " skipped");
		// Extentreports log operation for skipped tests.
		ExtentTestManager.getTest().log(Status.SKIP,
				MarkupHelper.createLabel(iTestResult.getName() + " - Test Case Skipped", ExtentColor.ORANGE));
	}


}
