package pages.onliner;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import util.Util;

import java.util.List;

public class Offers {

    private WebDriver driver;
    private JavascriptExecutor jsExecutor;
    public final static Logger LOG = Logger.getLogger(Offers.class);
    private String urlOffers;

    public static final By OFFER_CONTAINER_LOCATOR = By.xpath("//tr[contains(@class, 'state_add-to-cart') and not(contains(@style, 'display: none'))]");
    public static final By OFFER_PRICE_LOCATOR = By.xpath(".//p[contains(@class, 'price-primary')]//span");
    public static final By OFFER_LOGOLINK_LOCATOR = By.xpath(".//a[contains(@class, 'logo')]");

    public Offers(WebDriver driver, String url) {
        LOG.debug("creating Offers page");
        this.driver = driver;
        this.urlOffers = url;
        PageFactory.initElements(driver, this);
        jsExecutor = (JavascriptExecutor) driver;
    }

    public WebElement getMinPriceOffer(List<WebElement> offers) {
        WebElement minPriceOffer = null;
        Double minPriceValue = Double.MAX_VALUE;
        for (WebElement s :
                offers) {
            String priceStr = s.findElement(OFFER_PRICE_LOCATOR).getText();
            Double priceValue = parsePrice(priceStr);
            LOG.debug("Parsed price: " + priceValue);
            if (priceValue < minPriceValue) {
                minPriceValue = priceValue;
                minPriceOffer = s;
            }
        }

        LOG.info("--->Min price offer: " + minPriceOffer.findElement(OFFER_LOGOLINK_LOCATOR).getAttribute("href") + " --->Min price value: " + minPriceValue);
        return minPriceOffer;
    }

    private Double parsePrice(String priceStr) {
        return Double.valueOf(priceStr.substring(0, priceStr.indexOf(",")) + "." + priceStr.substring(priceStr.indexOf(",") + 1));
    }

    public List<WebElement> getAllOffers() {
        return driver.findElements(OFFER_CONTAINER_LOCATOR);
    }

    public Seller getSeller(WebElement offer) {
        LOG.debug("getting link from seller's logo image");
        WebElement offerLogoLink = offer.findElement(OFFER_LOGOLINK_LOCATOR);
        String urlSeller = offerLogoLink.getAttribute("href");
        LOG.debug("seller's url: " + urlSeller);
        Util.scrollIntoView(jsExecutor, offerLogoLink);
        offerLogoLink.click();
        return new Seller(driver, urlSeller);
    }

    public boolean checkUrl() {
        String actual = driver.getCurrentUrl();
        String shouldContain = urlOffers.substring(urlOffers.indexOf('/'));
        LOG.debug("Verifying urls: actual url: " + actual + ", should contain: " + shouldContain);
        return actual.contains(shouldContain);
    }

}
