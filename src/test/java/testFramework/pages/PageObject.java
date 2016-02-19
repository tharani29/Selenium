package testFramework.pages;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import testFramework.config.ConfigProperties;

public interface PageObject {

    default List<ExpectedCondition<Boolean>> getPageVisitConditions() {
        ExpectedCondition<Boolean> condition = webDriver -> webDriver.getTitle().toLowerCase().contains("someTitle");

        List<ExpectedCondition<Boolean>> result =  new ArrayList<>();
        result.add(condition);

        return result;
    }


    default String getPageUrl() {
        return "";
    }
}
