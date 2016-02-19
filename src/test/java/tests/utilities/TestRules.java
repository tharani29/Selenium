package tests.utilities;

import com.google.inject.Inject;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import testFramework.logging.Reporter;
import testFramework.logging.MyLog;

public class TestRules extends TestWatcher {

	private MyLog log;

	@Inject
	public TestRules(MyLog log) {
		this.log = log;
	}

	@Override
	protected void starting(Description description) {
		log.logStarting(description.getMethodName());
	}

	@Override
	protected void failed(Throwable e, Description description) {
		log.logTestCaseFailure(description.getMethodName(), e);
	}

	@Override
	protected void succeeded(Description description) {
		log.logPass(description.getMethodName());
	}

	@Override
	protected void finished(Description description) {
		log.logFinished(description.getMethodName());
	}

}
