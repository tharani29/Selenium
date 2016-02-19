package testFramework.utilities.report;

import com.google.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.openqa.selenium.Platform;
import testFramework.config.ConfigProperties;


public class ReportObject {
	
	protected String path;
	protected String fileName;
	private String dateAsString;
	private String verdict;

	@Inject private ConfigProperties configProperties;

	public ReportObject(boolean isPass, Date date, String fileExtension) {
		verdict = isPass ? "PASS" : "FAIL";
		dateAsString = new SimpleDateFormat("yyMMdd-HHmmss").format(date);
		fileName = constructFileName(isPass, fileExtension);
		path = constructPath();
		
	}

	public String constructPath() {
		String constructedPath = "";
		String workingDir = System.getProperty("user.dir");
		constructedPath = workingDir + "\\target\\extent-reports\\";
		if (configProperties.PLATFORM.equals(Platform.LINUX)) {
			constructedPath = constructedPath.replaceAll("\\\\", "/");
		}
		return constructedPath;
	}
	
	public String constructFileName(boolean isPass, String fileExtension) {
		String constructedFileName = "";
		constructedFileName =
				+ dateAsString
				+ fileExtension;

		return constructedFileName;
	}

	public String getReportObjectPath(){
		return path + fileName;
	}

	public String getReportObjectName() {
		return fileName;
	}

}
