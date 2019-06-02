package tests;

import config.Config;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.onliner.Catalog;
import pages.onliner.Offers;
import pages.onliner.Seller;
import pages.onliner.Videocards;

import java.util.List;

public class TestOnliner extends TestBase {

    private final static Logger LOG = Logger.getLogger(TestOnliner.class);
    private Config config = new Config();


    @Test
    public void testSearch() {
        String onlinerURL = config.getOnlinerCatalogUrl();
        driver.get(onlinerURL);

        Catalog catalog = getCatalog();

        Videocards videocards = getVideocards(catalog);
        List<WebElement> products = videocards.selectWithRating(config.getVideocardsMinRating());
        WebElement minPriceProduct = videocards.getMinPriceProduct(products);

        Offers offers = videocards.getOffers(minPriceProduct);
        Assert.assertTrue(offers.checkUrl());
        WebElement minPriceOffer = offers.getMinPriceOffer(offers.getAllOffers());

        Seller seller = offers.getSeller(minPriceOffer);
        Assert.assertTrue(seller.checkUrl());
        String sellerName = seller.getName();
        String sellerURL = driver.getCurrentUrl();
        LOG.info("--->Seller: "+sellerURL + " : " + sellerName);

    }

    public Videocards getVideocards(Catalog catalog) {
        Videocards videocards = catalog.goToVideocards();
        Assert.assertTrue(videocards.checkUrl());
        videocards.setReleaseDate(config.getVideocardsMaxAge());
        videocards.setVideomemorySize(config.getVideocardsMinSize(), config.getVideocardsMaxSize());
        videocards.setVideomemoryType(config.getVideocardsMemoryType());
        videocards.orderBy(config.getVideocardsOrderBy());
        return videocards;
    }

    public Catalog getCatalog() {
        Catalog catalog = new Catalog(driver);
        Assert.assertTrue(catalog.checkUrl());
        catalog.goToComputersAndNetworks();
        catalog.goToAccessories();
        return catalog;
    }
}
