package pages.onliner;

import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import util.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Videocards {

    WebDriver driver;
    JavascriptExecutor jsExecutor;
    private static final Logger LOG = Logger.getLogger(Videocards.class);
    //TODO ok to be static final?
    public static final String URL_VIDEOCARDS = "https://catalog.onliner.by/videocard";

    @FindBy(xpath = "//span[contains(text(),'Дата выхода')]//following::div[1]")
    WebElement filterReleaseDateContainer;
    By filterReleaseDateFromLocator = By.xpath("./descendant::input[contains(@data-bind, 'facet.value.from')][1]");
    By filterReleaseDateToLocator = By.xpath("./descendant::input[contains(@data-bind, 'facet.value.to')][2]");

    @FindBy(xpath = "//div[contains(@class, 'fieldset') and contains(.,'Видеопамять')]")
    WebElement filterVideomemorySizeContainer;
    By filterVideomemorySizeMinLocator = By.xpath("./descendant::select[contains(@data-bind, 'facet.value.from')]");
    By filterVideomemorySizeMaxLocator = By.xpath(".//select[contains(@data-bind, 'facet.value.to')]");

    @FindBy(xpath = "//div[contains(@class, 'fieldset') and contains(.,'Тип видеопамяти')]")
    WebElement filterVideomemoryTypeContainer;
    By filterVideomemoryAllTypesButtonLocator = By.xpath("./descendant::div[contains(text(), 'Все')]");
    String filterVideomemoryAllTypesContainerLocator = "//div[contains(@class, 'schema-filter-popover__inner')]";

    @FindBy(xpath = "//a[contains(@class,'schema-order__link')]")
    WebElement orderByButton;
    String orderByContainerLocator = "//div[contains(@class,'schema-order__list')]";

    String productContainerLocator = "//div[contains(@class, 'schema-product__group')]";
    By productRatingValueLocator = By.xpath(".//span[contains(@class, 'rating')]");
    By productTitleTextLocator = By.xpath(".//div[contains(@class,'schema-product__title')]//span");
    By productPriceTextLocator = By.xpath(".//a[contains(@class,'schema-product__price-value_primary')]" + "//span");
    private By productOffersLinkLocator = By.xpath(".//div[contains(@class, 'schema-product__offers')]//a");


    public Videocards(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        jsExecutor = (JavascriptExecutor) driver;
    }

    public boolean checkUrl() {
        return driver.getCurrentUrl().equals(URL_VIDEOCARDS);
    }

    public void setReleaseDate(int notOlderThan) {
        Calendar calendar = Calendar.getInstance();
        Integer currentYear = calendar.get(Calendar.YEAR);
        Integer fromYear = currentYear - notOlderThan + 1;

        Util.scrollIntoView(jsExecutor, filterReleaseDateContainer);
        WebElement filterReleaseDateFromField = filterReleaseDateContainer.findElement(filterReleaseDateFromLocator);
        filterReleaseDateFromField.sendKeys(fromYear.toString());
        WebElement filterReleaseDateToField = filterReleaseDateContainer.findElement(filterReleaseDateToLocator);
        filterReleaseDateToField.sendKeys(currentYear.toString());
    }

    public void setVideomemorySize(String min, String max) {

        //scroll container into view
        Util.scrollIntoView(jsExecutor, filterVideomemorySizeContainer);

        Select filterVideomemorySizeMinField = new Select(filterVideomemorySizeContainer.findElement(filterVideomemorySizeMinLocator));
        Select filterVideomemorySizeMaxField = new Select(filterVideomemorySizeContainer.findElement(filterVideomemorySizeMaxLocator));

        filterVideomemorySizeMinField.selectByVisibleText(min); //selectByValue("4gb");
        filterVideomemorySizeMaxField.selectByVisibleText(max); //selectByValue("8gb");

    }


    public void setVideomemoryType(String type) {
        //scroll container into view
        Util.scrollIntoView(jsExecutor, filterVideomemoryTypeContainer);
        WebElement filterVideomemoryAllTypesButton = filterVideomemoryTypeContainer.findElement(filterVideomemoryAllTypesButtonLocator);
        filterVideomemoryAllTypesButton.click();
        WebElement filterVideomemoryAllTypesOption = driver.findElement(By.xpath(filterVideomemoryAllTypesContainerLocator + "//*[.='" + type + "']"));
        Util.scrollIntoView(jsExecutor, filterVideomemoryAllTypesOption);
        filterVideomemoryAllTypesOption.click(); //value="gddr5"
    }


    public void orderBy(String orderHow) {

        Util.scrollIntoView(jsExecutor, orderByButton);
        orderByButton.click();
        WebElement orderByOption = driver.findElement(By.xpath(orderByContainerLocator + "//span[.='" + orderHow + "']"));
        Util.scrollIntoView(jsExecutor, orderByOption);
        orderByOption.click();
        orderByButton.click();

    }

    public List<WebElement> selectWithRating(double ratingMin) {
        List<WebElement> products = getAllProducts();
        List<WebElement> productsSelected = new ArrayList<>();
        int i = 0;
        for (WebElement p :
                products) {
            LOG.debug("Product: " + (i + 1));
            WebElement titleEl = p.findElement(productTitleTextLocator);
            LOG.debug("Product title: " + titleEl.getText());
            WebElement ratingEl = null;
            try {
                ratingEl = p.findElement(productRatingValueLocator);
            } catch (NoSuchElementException e) {
                LOG.debug("no rating");
            }
            if (ratingEl != null) {
                String ratingStr = ratingEl.getAttribute("class");
                Double ratingValue = parseRating(ratingStr);
                LOG.debug("rating value is: " + ratingValue);
                if (ratingValue >= ratingMin) {
                    productsSelected.add(p);
                }
            }
            i++;
        }
        LOG.debug("Found products: " + productsSelected.size());
        return productsSelected;
    }

    public double parseRating(String ratingStr) {
        return (Integer.valueOf(ratingStr.substring(ratingStr.indexOf('_') + 1))) / 10.0;
    }

    public List<WebElement> getAllProducts() {
        return driver.findElements(By.xpath(productContainerLocator));
    }

    public WebElement getMinPriceProduct(List<WebElement> products) {
        if (products.isEmpty()) {
            products = getAllProducts();
        }
        WebElement minPriceProduct = null;
        Double minPriceValue = Double.MAX_VALUE;
        for (WebElement p :
                products) {
            String priceStr = p.findElement(productPriceTextLocator).getText();
            Double priceValue = parsePrice(priceStr);
            LOG.debug("Parsed price: " + priceValue);
            if (priceValue < minPriceValue) {
                minPriceValue = priceValue;
                minPriceProduct = p;
            }
        }

        LOG.info("--->Min price product: " + minPriceProduct.findElement(productTitleTextLocator).getText() + " --->Min price value: " + minPriceValue);
        return minPriceProduct;
    }

    public Double parsePrice(String priceStr) {
        return Double.valueOf(priceStr.substring(0, priceStr.indexOf(",")) + "." + priceStr.substring(priceStr.indexOf(",") + 1, priceStr.indexOf(" ")));
    }

    public Offers getOffers(WebElement product) {
        WebElement offersLink = product.findElement(productOffersLinkLocator);
        String urlOffers = offersLink.getAttribute("href");
        Util.scrollIntoView(jsExecutor, offersLink);
        offersLink.click();
        return new Offers(driver, urlOffers);
    }
}
