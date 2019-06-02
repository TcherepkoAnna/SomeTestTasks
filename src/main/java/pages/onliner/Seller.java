package pages.onliner;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class Seller {

    private WebDriver driver;
    private JavascriptExecutor jsExecutor;
    public static final Logger LOG = Logger.getLogger(Seller.class);
    public static final By SELLER_NAME_LOCATOR = By.xpath("//h1[@class='sells-title']");
    private String urlSeller;


    public Seller(WebDriver driver, String url) {
        LOG.debug("creating seller's page");
        this.driver = driver;
        this.urlSeller = url;
        PageFactory.initElements(driver, this);
        jsExecutor = (JavascriptExecutor) driver;
    }

    public String getName() {
        LOG.debug("getting name");
        String name = driver.findElement(SELLER_NAME_LOCATOR).getText();
        LOG.debug("seller's name: " + name);
        return name;
    }

    public boolean checkUrl() {
        String actual = driver.getCurrentUrl();
        String shouldContain = urlSeller.substring(urlSeller.indexOf('/'));
        LOG.debug("Verifying urls: actual url: " + actual + ", should contain: " + shouldContain);
        return actual.contains(shouldContain);
    }

}
