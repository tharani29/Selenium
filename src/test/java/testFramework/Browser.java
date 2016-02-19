
@Singleton
public final class Browser implements AutoCloseable {

  private RemoteWebDriver webDriver;
	private final int MIL_SECONDS = 5000;
	public String parentWindowHandle;
	private MyLog = log;
	private ConfigProperties configProperties;
	
	@Inject
	public Browser(MyLog log, ConfigProperties configProperties) {
    this.log = log;
		this.configProperties = configProperties;
		webDriver = createWebDriver();
		webDriver.manage().window().maximize();
		parentWindowHandle = webDriver.getWindowHandle();

  }
  
  public RemoteWebDriver getCurrentDriver() {
		return webDriver;
	}
  
  public boolean isAt(PageObject page) {

		String finalUrl = page.getPageUrl().startsWith("/") ? configProperties.BASE_URL + page.getPageUrl() : page.getPageUrl();

		boolean passed = finalUrl.equalsIgnoreCase(webDriver.getCurrentUrl());

		if (!passed)
			return false;

		page.getPageVisitConditions()
				.stream()
				.filter(condition -> condition != null)
				.forEach(condition -> new WebDriverWait(webDriver, MIL_SECONDS).until(condition));

		return true;
	}

	public void goToUrl(String url) {
		String finalUrl = url.startsWith("/") ? configProperties.BASE_URL + url : url;
		webDriver.get(finalUrl);
	}

	public String getCurrentUrl() {
		String url = webDriver.getCurrentUrl();
		String decodedUrl = url;
		try {
			decodedUrl = URLDecoder.decode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.logInfo("Failed to decode url: " + url);
		}
		return decodedUrl;
	}

	public String getTitle() {
		return webDriver.getTitle();
	}

	public void closeBrowser() {
		if (webDriver != null) {
			webDriver.close();
		}
	}

	public String getPageSource() {
		return webDriver.getPageSource();
	}

	public void removeCookies() {
		webDriver.manage().deleteAllCookies();
	}

	public void setCookie(String name, String value){
		Cookie ck = new Cookie(name, value);
		webDriver.manage().addCookie(ck);
	}


	public void reloadPage() {
		webDriver.navigate().refresh();
	}

	private RemoteWebDriver createWebDriver() {
		if (configProperties.IS_LOCAL) {
			if (configProperties.BINARY_PATH == null ) {
				throw new Error("BROWSER_BINARY_PATH missing!");
			}
			if (configProperties.BROWSER_DRIVER.equalsIgnoreCase(ConfigProperties.PHANTOMJS_DRIVER_NAME)) {
				DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
				desiredCapabilities.setJavascriptEnabled(true);
				desiredCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, configProperties.BINARY_PATH);
				return new PhantomJSDriver(desiredCapabilities);
			}
			return new FirefoxDriver();
		}

		URL url;
		RemoteWebDriver driver = null;

		try {
			url = new URL("http://" + configProperties.BROWSER_HOST + ":4444/wd/hub");
			DesiredCapabilities c = DesiredCapabilities.firefox();
			c.setBrowserName(ConfigProperties.FIREFOX_DRIVER_NAME.toLowerCase());
			c.setPlatform(configProperties.PLATFORM);
			driver = new RemoteWebDriver(url, c);
			driver.setFileDetector(new LocalFileDetector());
		}
		catch (MalformedURLException e) {
			log.logError("Browser could not be initialized, {}.", e.getMessage());
		}
		catch (UnreachableBrowserException e) {
			String message =
					"Can't start browser. If you want to run on local server, make sure to start server first." +
							"\n" + "java -jar selenium-server-standalone-2.45.0.jar";
			log.logError(message);
			log.logError(e.getMessage());
			System.exit(1);
		}
		return driver;
	}

	@Override
	public void close() throws Exception {
		webDriver.close();
		webDriver.quit();
	}

  
}
