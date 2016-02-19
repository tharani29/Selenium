package testFramework.utilities.report;


import com.google.inject.Inject;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import testFramework.Browser;
import testFramework.logging.MyLog;


public class SourcePage extends ReportObject {

	public SourcePage(boolean isPass, Date date) {
		super(isPass, date, ".log");
	}

	@Inject private Browser browser;
	@Inject private MyLog log;

	public void captureSourcePage() {

		String sourcePageFile = path + fileName;
		try {
			PrintWriter writer = new PrintWriter(sourcePageFile, "UTF-8");
			writer.write(browser.getPageSource());
			writer.close();

			log.logInfo("#\t " + sourcePageFile);

		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			log.logError(e.getMessage(), e.getStackTrace().toString());
		}
	}
}
