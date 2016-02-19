package tests.utilities;

import org.junit.rules.ExternalResource;
import testFramework.Browser;

public class TestResources extends ExternalResource {

    private TestResources() {
        doSetupOnce();
    }

    private static final TestResources instance = new TestResources();

    public static TestResources getTestResources () {
        return instance;
    }

    protected void before() {}

    protected void after() {
        Browser.terminate();
    }

    private void doSetupOnce() {}

}
