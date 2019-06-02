package pages.tutby;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class SearchResults {
    WebDriver driver;
    private static final Logger LOG = Logger.getLogger(SearchResults.class);

    public SearchResults(WebDriver driver) {
        LOG.debug("creating SearchResults page");
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getCurrentUrl() {
        LOG.debug("getting current url");
        return driver.getCurrentUrl();
    }
}
