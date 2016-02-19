package testFramework.utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static testFramework.config.ApplicationMessageConstants.ERROR_MESSAGE_PROBLEM_LOADING_PAGE;

import com.google.inject.Inject;
import java.util.List;
import java.util.Set;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import testFramework.Browser;
import testFramework.actions.ActionArguments;
import testFramework.actions.ElementActions;
import testFramework.actions.UserAction;
import testFramework.annotations.LogStep;
import testFramework.config.ConfigProperties;
import testFramework.logging.MyLog;
import testFramework.pages.PageObject;

public abstract class BasePage implements PageObject {

	public String PAGE_URL;
	public String PAGE_TITLE;

	@Inject protected Browser browser;
	@Inject protected WaitForElements waitForElements;
	@Inject protected MyLog log;
	@Inject protected Set<UserAction> userActions;
	@Inject protected ConfigProperties configProperties;

	public BasePage(Browser browser) {
		this.browser = browser;
		initializeFields();
	}

	public void initializeFields() {
		PageFactory.initElements(browser.getCurrentDriver(), this);
	}

	@Override
	public String getPageUrl() {
		return PAGE_URL;
	}

	public abstract void initPage();

	public void loadPage() {
		loadPage(PAGE_URL);
	}

	@LogStep(logParameterFormat = "Going to url: {}")
	public void loadPage(String url) {
		browser.goToUrl(url);
		if (browser.getTitle().equals(ERROR_MESSAGE_PROBLEM_LOADING_PAGE)) {
			log.logError("FATAL ERROR, web page \'" + browser.getCurrentUrl() + "\' could not be loaded, " +  browser.getTitle());
		} else if (browser.getTitle().equals("Problem loading page")) {
			log.logInfo(" " + browser.getTitle());

		}

		assertTrue(browser.getTitle().contains(PAGE_TITLE));
	}

	public boolean setElementText(WebElement element, String text) {
		return setElementText(element, text, true);
	}

	public boolean setElementText(WebElement element, String text, boolean isClear) {
		ActionArguments arguments = new ActionArguments();
		arguments.setPageObjectName(this.getClass().getSimpleName().split("\\$\\$")[0]);
		arguments.setShouldClear(isClear);
		arguments.setText(text);
		arguments.setPageTitle(PAGE_TITLE);

		boolean isSuccess = performElementActions(ElementActions.SET_TEXT, arguments, element);
		String elementValue = element.getAttribute("value");
		assertTrue(text.contains(elementValue));
		assertFalse(elementValue.equals(""));

		return isSuccess;
	}

	public void isAt() {
		assertEquals(PAGE_TITLE, browser.getTitle());
	}


	public boolean clickElement(WebElement element, boolean isOptional) {
		ActionArguments arguments = new ActionArguments();
		arguments.setPageObjectName(this.getClass().getSimpleName().split("\\$\\$")[0]);
		return clickElement(arguments, element);
	}

	public boolean clickElement(WebElement element) {
		ActionArguments arguments = new ActionArguments();
		arguments.setPageObjectName(this.getClass().getSimpleName().split("\\$\\$")[0]);
		arguments.setPageTitle(PAGE_TITLE);
		return clickElement(arguments, element);
	}

	private boolean clickElement(ActionArguments arguments, WebElement... elements) {
		return performElementActions(ElementActions.CLICK, arguments, elements);
	}

	public boolean selectElement(WebElement element, String text) {
		ActionArguments arguments = new ActionArguments();
		arguments.setPageObjectName(this.getClass().getSimpleName().split("\\$\\$")[0]);
		arguments.setPageTitle(PAGE_TITLE);
		arguments.setText(text);
		return performElementActions(ElementActions.SELECT, arguments, element);
	}

	public boolean elementExistsWhenHoverOver(WebElement existsElement, WebElement hoverOverElement) {
		ActionArguments arguments = new ActionArguments();
		arguments.setPageObjectName(this.getClass().getSimpleName().split("\\$\\$")[0]);
		return performElementActions(ElementActions.ELEM_EXISTS_WHEN_HOVER, arguments, hoverOverElement, existsElement);
	}

	public boolean hoverOverElementAndClick(WebElement hoverElement, WebElement clickElement) {
		ActionArguments arguments = new ActionArguments();
		arguments.setPageObjectName(this.getClass().getSimpleName().split("\\$\\$")[0]);
		return performElementActions(ElementActions.HOVER_AND_CLICK, arguments, hoverElement, clickElement);
	}

	public boolean elementExists(WebElement element) {
		return elementExists(element, false);
	}

	public boolean elementExists(WebElement element, boolean isOptional) {
		ActionArguments arguments = new ActionArguments();
		arguments.setPageObjectName(this.getClass().getSimpleName().split("\\$\\$")[0]);
		arguments.setPageTitle(PAGE_TITLE);
		arguments.setOptional(isOptional);
		return performElementActions(ElementActions.ELEM_EXISTS, arguments, element);
	}


	public boolean textRemovedFromSource(String text) {
		ActionArguments arguments = new ActionArguments();
		arguments.setPageObjectName(this.getClass().getSimpleName().split("\\$\\$")[0]);
		arguments.setText(text);
		return performElementActions(ElementActions.TEXT_REMOVED, arguments);
	}

	public boolean elementExistsAndHasValue(WebElement element, String value){
		ActionArguments arguments = new ActionArguments();
		arguments.setPageObjectName(this.getClass().getSimpleName().split("\\$\\$")[0]);
		arguments.setValue(value);
		return performElementActions(ElementActions.ELEM_HAS_VALUE, arguments, element);
	}

	public boolean elementExistsAndHasText(WebElement element, String text) {
		ActionArguments arguments = new ActionArguments();	
		arguments.setPageObjectName(this.getClass().getSimpleName().split("\\$\\$")[0]);
		arguments.setText(text);
		return performElementActions(ElementActions.ELEM_HAS_TEXT, arguments, element);
	}

	public boolean elementWithTextDoesNotExist(WebElement element, String text, boolean isOptional) {
		ActionArguments arguments = new ActionArguments();
		arguments.setPageObjectName(this.getClass().getSimpleName().split("\\$\\$")[0]);
		arguments.setOptional(isOptional);
		arguments.setText(text);
		return performElementActions(ElementActions.ELEM_HAS_NOT_TEXT, arguments, element);
	}

	public boolean elementSrcDoesNotContainText(WebElement element, String text) {
		ActionArguments arguments = new ActionArguments();
		arguments.setPageObjectName(this.getClass().getSimpleName().split("\\$\\$")[0]);
		arguments.setText(text);
		return performElementActions(ElementActions.SRC_NOT_CONTAINS_TEXT, arguments, element);
	}

	@LogStep
	public void checkThatFieldsAreEmpty(List<WebElement> elements){
		elements.forEach(this::textFieldIsEmpty);
	}

	public boolean textFieldIsEmpty(WebElement element){
		boolean fieldIsEmpty = false;
		String textInsideElement = element.getAttribute("value");

		if(textInsideElement.isEmpty())
		{
			fieldIsEmpty = true;
		}else{
			log.logInfo("Text field is not empty: {}.", textInsideElement);
		}
		return fieldIsEmpty;
	}

	public void switchToNewlyOpenedWindow() {
		for (String windowsHandle : browser.getCurrentDriver().getWindowHandles()) {
			browser.getCurrentDriver().switchTo().window(windowsHandle);
		}
	}

	public void closeWindow() {
		browser.closeBrowser();
		browser.getCurrentDriver().switchTo().window(browser.parentWindowHandle);
	}

	private boolean performElementActions(ElementActions elementAction, ActionArguments args, WebElement... elements) {
		return userActions
				.stream()
				.filter(a -> a.getSupportedAction() == elementAction)
				.allMatch(a -> a.performAction(args, elements));
	}

	public void cleanUp() {
	}
}
