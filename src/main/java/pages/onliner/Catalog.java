package pages.onliner;

import config.Config;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Catalog {

    private WebDriver driver;
    private JavascriptExecutor jsExecutor;
    private static final Logger LOG = Logger.getLogger(Catalog.class);
    public static final String URL_CATALOG = "https://catalog.onliner.by/";

    @FindBy(xpath = "//span[starts-with(text(),'Компьютеры')]")
    WebElement computersAndNetworks;

    @FindBy(xpath = "//div[contains(text(), 'Комплектующие')]")
    WebElement accessories;

    @FindBy(xpath = "//a[contains(@href, 'videocard')]")
    WebElement videocardsLink;

    public Catalog(WebDriver driver) {
        this.driver = driver;
        jsExecutor = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
    }

    public void goToComputersAndNetworks() {
        LOG.debug("selecting Computers and Networks");
//        Util.scrollIntoView(jsExecutor, computersAndNetworks);
        computersAndNetworks.click();
    }

    public void goToAccessories() {
        LOG.debug("selecting Accessories");
//        new WebDriverWait(driver, config.getWaitTime()).until(ExpectedConditions.elementToBeClickable(accessories));
//        Util.scrollIntoView(jsExecutor, accessories);
        accessories.click();
    }

    public Videocards goToVideocards() {
        LOG.debug("selecting Videocards");
//        new WebDriverWait(driver, config.getWaitTime()).until(ExpectedConditions.elementToBeClickable(videocardsLink));
//        Util.scrollIntoView(jsExecutor, videocardsLink);
        videocardsLink.click();
        return new Videocards(driver);
    }

    public boolean checkUrl() {
        LOG.debug("getting current page url");
        return driver.getCurrentUrl().equals(URL_CATALOG);
    }
}
