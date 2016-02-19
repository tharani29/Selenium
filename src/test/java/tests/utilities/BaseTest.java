package tests.utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import testFramework.Browser;
import testFramework.FrameworkWireupModule;
import testFramework.config.ConfigProperties;
import testFramework.logging.Reporter;
import testFramework.logging.MyLog;
import testFramework.utilities.BasePage;
import testRunner.GuiceModules;
import testRunner.GuiceTestRunner;

import com.google.inject.Inject;

import connection.DatabaseHelper;

@RunWith(GuiceTestRunner.class)
@GuiceModules({FrameworkWireupModule.class})
public class BaseTest {

	private DatabaseHelper dbHelper = DatabaseHelper.getInstance();
	private static Connection conn;

	@Inject protected Browser browser;
	@Inject private MyLog log;
	@Inject protected ConfigProperties config;
	@Rule
	public TestName testName = new TestName();

	@Rule
	@Inject
	public TestRules testRules;

	@ClassRule
	public static TestResources res = TestResources.getTestResources();

	private void logError(String message) {

	}

	@Before
	public void setUpBeforeEachTest() {
		Reporter.getInstance().startTestReporter(testName.getMethodName());
		browser.removeCookies();
		restoreDatabaseToDefault();
	}

	public void verifyThatOnCorrectPage(BasePage page){
		page.isAt();

		if(!page.PAGE_TITLE.equals(browser.getTitle())){
			log.logError("Expected to have title: \'" + page.PAGE_TITLE + "\' but: \'" + browser.getTitle() + "\'");
		}
		assertEquals(page.PAGE_TITLE, browser.getTitle());
	}


	public void verifyThatOnCorrectPageURL(BasePage page){
		page.isAt();
		String currentUrl = config.BASE_URL + page.PAGE_URL;
		assertEquals(currentUrl, browser.getCurrentUrl());
	}

	public void verifyElementOnPageExists(BasePage page, WebElement element){
		try{
			element.getText();
			log.logInfo("Element does exist on " + page.getClass().getSimpleName() + " with text " + element.getText());
		}catch(NoSuchElementException e){
			log.logError("Element does NOT exist on " + page.getClass().getSimpleName());
		}

		assertTrue(page.elementExists(element));
	}

	private void restoreDatabaseToDefault() {
		try {
			conn = dbHelper.getConnection();
			Statement statement = conn.createStatement();
			//TODO: restore database to default
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@After
	public void cleanUpAfterEachTest() {

	}

}
