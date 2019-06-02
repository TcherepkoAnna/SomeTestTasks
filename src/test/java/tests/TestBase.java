package tests;

import config.Config;
import drivers.ChromeCreator;
import drivers.FirefoxCreator;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TestBase {

    private final static Logger LOG = Logger.getLogger(TestBase.class);
    protected static WebDriver driver = null;
    private Config config = new Config();

    @BeforeSuite
    public void initialize() throws IOException {
        try {
            if (config.getPreferredBrowser().equals("firefox")) {
                driver = new FirefoxCreator().createDriver();
            } else if (config.getPreferredBrowser().equals("chrome")) {
                driver = new ChromeCreator().createDriver();
            }

            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(config.getWaitTime(), TimeUnit.SECONDS);

        } catch (WebDriverException exception) {
            LOG.error("Exception occurred: " + exception.getMessage());
        }

    }

    @AfterSuite
    public void teardown() {
        driver.quit();
    }

    public void log(String message) {
        Reporter.log(message);
        LOG.info(message);
    }
}
