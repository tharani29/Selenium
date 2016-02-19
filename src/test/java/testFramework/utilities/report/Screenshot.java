package testFramework.utilities.report;

import com.google.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import testFramework.Browser;
import testFramework.logging.MyLog;

public class Screenshot extends ReportObject {

	public Screenshot(boolean isPass, Date date){
		super(isPass, date, ".jpg");
	}
	@Inject private Browser browser;
	@Inject private MyLog log;

	public void takeScreenshot() {
		File screenshotFile = ((TakesScreenshot) browser.getCurrentDriver())
				.getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(screenshotFile, new File(path, fileName));
			log.logInfo("#\t " + path + fileName);
		} catch (IOException e) {
			log.logError("Copy file problem when saving screenshot: " + e.getMessage());
		}
	}

}
