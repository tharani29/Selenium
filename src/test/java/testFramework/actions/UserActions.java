package testFramework.actions;

import org.openqa.selenium.WebElement;
import testFramework.utilities.BasePage;

public interface UserAction {
    boolean performAction(ActionArguments arguments, WebElement... webElements);
    ElementActions getSupportedAction();
}
