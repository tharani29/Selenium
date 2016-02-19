package testFramework.utilities;

import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import testFramework.Browser;
import testFramework.logging.MyLog;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class WaitForElements {
	final static int SEC_TO_WAIT_OPTIONAL = 7;

	private Browser browser;
	private MyLog log;

	@Inject
	public WaitForElements(Browser browser, MyLog log) {
		this.browser = browser;
		this.log = log;
	}

	public  void waitForCondition(ExpectedCondition<Boolean> condition) {
		log.logInfo("Now we are waiting something to load.");
		new WebDriverWait(browser.getCurrentDriver(), SEC_TO_WAIT_OPTIONAL, 1).until(condition);
	}

}
