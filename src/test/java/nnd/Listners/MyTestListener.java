package nnd.Listners;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import nnd.Utilities.Screenshot;

public class MyTestListener implements ITestListener {
	private WebDriver driver;
	private ExtentReports extent;
	private ExtentTest test;

	@Override
	public void onTestStart(ITestResult result) {

		// extent report - Create a new test in ExtentReports when the test starts
		test = extent.createTest(result.getMethod().getMethodName()); 

		test.log(Status.INFO, " Test Started"); // exp_code
		
	    test.log(Status.INFO, " Test Started"); // exp_code
		System.out.println("Test Started: " + result.getName());
	}

	@Override
	public void onTestSuccess(ITestResult result) {

		test.pass("Test passed" + result.getName());

		test.log(Status.INFO, " Test Successfull"); // exp_code

		System.out.println("Test Passed: " + result.getName());
	}

	@Override
	public void onTestFailure(ITestResult result) {

		// Extent report
		test.fail("Test failed" + result.getName());

		// Ensure the driver is initialized, typically from your test setup
		driver = (WebDriver) result.getTestContext().getAttribute("driver"); // Retrieve driver from the context

		// FOR SCREENSHOT - CONNECT WITH BASE CLASS
		if (driver != null) {
			Screenshot sc = new Screenshot(driver);

			try {
				sc.takeScreenshot(result);

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Driver is not available for taking the screenshot.");
		}

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()); // Generates current timestamp
		String screenshotPath = "C:/Users/dhane/eclipse-workspace/OpenCart26012025/Failed Screenshots/Screenshot_test1_"
				+ timeStamp + ".png";

// Ensure the screenshot is actually saved before attaching
		// CODE TO SHOW SCREENSHOT IN EXTENT REPORT
		File screenshotFile = new File(screenshotPath);
		if (screenshotFile.exists()) {
			test.addScreenCaptureFromPath(screenshotPath);
		} else {
			System.out.println("extent Screenshot file not found: " + screenshotPath);
		}
		//--------------------

		
		System.out.println("Test Failed: " + result.getName());
		System.out.println("Failure Reason: " + result.getThrowable());

	}

	@Override
	public void onTestSkipped(ITestResult result) {

		// extent report
		test.skip("Test Skipped");

		System.out.println("Test Skipped: " + result.getName());
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

		// extent report
		test.warning("Test failed but within success percentage");
		System.out.println("Test Failed but within success percentage: " + result.getName());
	}

	@Override
	public void onStart(ITestContext context) {

	
		// for genkins
		String buildNumber = System.getenv("BUILD_NUMBER");

		if (buildNumber != null) {

			System.out.println("the build number is : " + buildNumber);
			// String reportPath = System.getProperty("user.dir") + "/reports/ExtentReport_"
			// + buildNumber + ".html";
			String reportPath = System.getProperty("user.dir") + "\\" + "reports" + "\\" + "ExtentReport_" + buildNumber
					+ ".html";

			ExtentSparkReporter sparkReporterGenkins = new ExtentSparkReporter(reportPath);
			extent = new ExtentReports();
			extent.attachReporter(sparkReporterGenkins);

		} else {

			System.out.println("the build number is : " + buildNumber);
			// String reportPath = System.getProperty("user.dir") + "/reports/ExtentReport_"
			// + buildNumber + ".html";
			String reportPath = System.getProperty("user.dir") + "\\" + "reports" + "\\" + "ExtentReport_"
					+ "localBuild" + ".html";

			ExtentSparkReporter sparkReporterGenkins = new ExtentSparkReporter(reportPath); 
			extent = new ExtentReports();
			extent.attachReporter(sparkReporterGenkins);
		}

		// for ECLIPSE
		String extentPath = "C:\\Users\\dhane\\eclipse-workspace\\OpenCart26012025\\test-output\\extent-Reports\\";
		// Extent reports- Use ExtentSparkReporter instead of ExtentHtmlReporter
		// to removing duplicationof extentReport.html file
		String reportFilePath = "extent-report-" + System.currentTimeMillis() + ".html"; // or use build number
		ExtentSparkReporter sparkReporter = new ExtentSparkReporter(extentPath + reportFilePath);
		// extent = new ExtentReports();
		sparkReporter.config().setTheme(Theme.STANDARD);
		extent.attachReporter(sparkReporter);

		System.out.println("Test Suite Started: " + context.getName());
	}

	@Override
	public void onFinish(ITestContext context) {

		// extent report - Generate the report at the end of test execution
		extent.flush();
		System.out.println("Test Suite Finished: " + context.getName());
	}
}
