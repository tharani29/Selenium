package testFramework.actions;

import org.openqa.selenium.WebElement;
import testFramework.annotations.LogSteps;

public class ClickElementAction extends BaseUserAction {
    @Override
    public void doPerformAction(ActionArguments arguments, WebElement... webElements) {
        for (WebElement webElement : webElements) {
            waitForElementToBeClickable(webElement, waitTime);
            webElement.click();
        }
    }

    @Override
    public ElementActions getSupportedAction() {
        return ElementActions.CLICK;
    }
}
