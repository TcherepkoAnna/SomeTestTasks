package pages.tutby;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

public class Homepage {

    WebDriver driver;
    private static final Logger LOG = Logger.getLogger(Homepage.class);
    public static final String URL_HOMEPAGE = "https://www.tut.by";
    @FindBy(how = How.ID, using = "search_from_str")
    WebElement searchInput;

    @FindBy(how = How.XPATH, using = "//input[@name='search'][@type='submit']")
    WebElement searchButton;

    public Homepage(WebDriver driver) {
        LOG.debug("creating TutBy Homepage");
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public SearchResults searchSite(String input) {
        LOG.debug("sending input to search field: " + input);
        searchInput.sendKeys(input);
        searchButton.click();
        return new SearchResults(driver);
    }

    public boolean checkUrl() {
        String actual = driver.getCurrentUrl();
        String shouldContain = URL_HOMEPAGE.substring(URL_HOMEPAGE.indexOf('/'));
        LOG.debug("Verifying urls: actual url: " + actual + ", should contain: " + shouldContain);
        return actual.contains(shouldContain);
    }

}
