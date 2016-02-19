package testFramework;

import java.util.Set;

import org.reflections.Reflections;

import testFramework.actions.BaseUserAction;
import testFramework.actions.UserAction;
import testFramework.annotations.LogStep;
import testFramework.aspects.LogPageObjectStepsAspect;
import testFramework.config.ConfigProperties;
import testFramework.logging.Log4JTypeListener;
import testFramework.logging.MyLog;
import testFramework.pages.PageObject;
import testFramework.utilities.BasePage;
import tests.utilities.TestRules;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import com.google.inject.multibindings.Multibinder;
import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.NetworkMode;

public class FrameworkWireupModule extends AbstractModule {
    
    @Override
    protected void configure() {
        bindSingletonSupportClasses();
        bindPageObjects();
        bindInterceptorsAndListeners();
        bindUserActions();
    }

    private void bindSingletonSupportClasses() {
        ExtentReports report = createReport();
        MyLog logger = new MyLog(report);
        bind(TestRules.class).toInstance(new TestRules(logger));
        bind(ExtentReports.class).toInstance(report);
        bind(MyLog.class).toInstance(logger);
        bind(ConfigProperties.class).in(Singleton.class);
        bind(Browser.class).in(Singleton.class);
    }

    private void bindPageObjects() {
        Reflections reflections = new Reflections("testFramework.pages");
        Set<Class<? extends BasePage>> allClasses = reflections.getSubTypesOf(BasePage.class);
        allClasses.stream()
                .filter(PageObject.class::isAssignableFrom)
                .forEach(c -> bind(c).in(Singleton.class));
    }

    private void bindInterceptorsAndListeners() {
        // Log interceptor
        LogPageObjectStepsAspect interceptor = new LogPageObjectStepsAspect();
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(LogStep.class), interceptor);

        // LogInjector and listener
        bindListener(Matchers.any(), new Log4JTypeListener());
        requestInjection(interceptor);
    }

    private void bindUserActions() {
        //Multibinder will bind to UserAction interface
        Multibinder<UserAction> userActionMultibinder = Multibinder.newSetBinder(binder(), UserAction.class);

        Reflections actions = new Reflections("testFramework.actions");
        Set<Class<? extends BaseUserAction>> allActionClasses = actions.getSubTypesOf(BaseUserAction.class);

        allActionClasses.stream()
                .filter(UserAction.class::isAssignableFrom)
                .forEach(action -> userActionMultibinder.addBinding().to(action));
    }
    private ExtentReports createReport() {
        String fileSeparator = System.getProperty("file.separator");
        String userDir = System.getProperty("user.dir");
        String reportFolder = "target" + fileSeparator + "extent-reports";
        String reportFile = "extent-report.html";

        String reportPath  =
                userDir + fileSeparator + reportFolder + fileSeparator + reportFile ;

        return new ExtentReports(reportPath, true, DisplayOrder.NEWEST_FIRST, NetworkMode.OFFLINE);
    }

}
