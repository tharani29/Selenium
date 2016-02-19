package testFramework.pages;

import static testFramework.config.ApplicationConstants.FIRST_NAME;
import static testFramework.config.ApplicationConstants.LAST_NAME;
import static testFramework.config.ApplicationConstants.LOGIN;
import static testFramework.config.TestDataConstants.USER_NAME;

import com.google.inject.Inject;
import java.util.Arrays;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import testFramework.Browser;
import testFramework.annotations.LogSteps;
import testFramework.utilities.BasePage;

@Singleton
@LogSteps
public class StartPage extends BasePage {

	@Inject
	public StartPage(Browser browser) {
		super(browser);
		PAGE_TITLE = "some title";
		PAGE_URL = "/someUrl";
	}

	@FindBy (xpath = "//input[contains(@name, 'firstname')])
	private WebElement enterFirstNameTextField;

	@FindBy (xpath = "//input[contains(@name, 'lastname')]")
	private WebElement enterLastNameTextField;


	public boolean enterFirstNameFieldExists() {
		return elementExists(enterFirstNameTextField);
	}

	public boolean enterLastNameFieldExists() {
		return elementExists(enterLastNameTextField);
	}

}

