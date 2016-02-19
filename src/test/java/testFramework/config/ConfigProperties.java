package testFramework.config;


import com.google.inject.Inject;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.openqa.selenium.Platform;
import testFramework.logging.MyLog;

public class ConfigProperties {

	private static final String eclipsePropertiesFile = "config.eclipse";
	public final String BASE_URL;
	public final String BROWSER_HOST;
	public final Platform PLATFORM;
	public final String BROWSER_DRIVER;
	public final String BINARY_PATH;
	public final boolean IS_LOCAL;

	public static final String PHANTOMJS_DRIVER_NAME = "PhantomJS";
	public static final String CHROME_DRIVER_NAME = "Chrome";
	public static final String FIREFOX_DRIVER_NAME = "Firefox";

	private final MyLog log;

	@Inject
	public ConfigProperties(MyLog log) {
		this.log = log;

		ResourceBundle eclipseProperties = getEclipseProperties();
		String baseUrl = null, browserHost = null, binaryPath = null, browserDriver = null;
		Platform platform = Platform.LINUX;


		if (System.getProperty("SITE_URL") == null && eclipseProperties != null) {
			System.out.println("take eclipse parameters instead of maven system properties.");

			try {
				baseUrl = eclipseProperties.getString("siteUrl");
				browserHost = eclipseProperties.getString("browserHost");
				platform = getPlatform(eclipseProperties.getString("platform"));
				browserDriver = eclipseProperties.getString("browserDriver");
				binaryPath = eclipseProperties.getString("binaryPath");

			} catch (MissingResourceException e) {
				log.logError("Can't find param {} in {}. {}", e.getKey(), eclipsePropertiesFile, e.getMessage());
			}

		} else if (System.getProperty("SITE_URL") != null ){
			System.out.println("Reading maven system properties");
			baseUrl = System.getProperty("SITE_URL");
			baseUrl = (baseUrl.lastIndexOf("/") + 1)  == baseUrl.length() ?  baseUrl.substring(0, baseUrl.lastIndexOf("/")) : baseUrl;
			browserHost = System.getProperty("BROWSER_HOST").trim();
			platform = getPlatform(System.getProperty("PLATFORM"));
			testEnvironment = System.getProperty("TEST_ENVIRONMENT");
			browserDriver = System.getProperty("BROWSER_DRIVER") == null ? FIREFOX_DRIVER_NAME : System.getProperty("BROWSER_DRIVER");
			binaryPath = System.getProperty("BROWSER_BINARY_PATH");

		} else {
			throw new Error("Invalid setup! Check startup parameters and/or eclipse properties file");
		}

		BASE_URL = baseUrl != null ? baseUrl.trim() : null;
		BROWSER_DRIVER = browserDriver ;
		PLATFORM = platform;
		BROWSER_HOST = browserHost;
		TEST_ENVIRONMENT = testEnvironment;
		BINARY_PATH = binaryPath;
		IS_LOCAL = BROWSER_HOST != null && BROWSER_HOST.equalsIgnoreCase("localhost");
	}

	private ResourceBundle getEclipseProperties() {
		try {
			return ResourceBundle.getBundle(eclipsePropertiesFile);
		} catch (MissingResourceException e) {
			System.err.println("Can't locate "+ eclipsePropertiesFile +", " + e.getMessage());
		}
		return null;
	}

	private Platform getPlatform(String platform) {
		return platform.equalsIgnoreCase("Linux") ? Platform.LINUX : Platform.WINDOWS;
	}
}
