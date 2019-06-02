package pages.ruFilesFm;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DownloadPage {

    private WebDriver driver;
    private JavascriptExecutor jsExecutor;
    private static final Logger LOG = Logger.getLogger(DownloadPage.class);

    private String urlDownload;


    @FindBy(xpath = "//div[contains(@id,'download__button')]")
    WebElement downloadButton;
    @FindBy(xpath = "//div[contains(@id, 'head_download__dropdown__content')]//a[contains(text(), 'Скачать этот файл')]")
    WebElement downloadOptionLink;

    public static final By FILE_CONTAINER_LOCATOR = By.xpath("//div[contains(@class, 'item file')]");
    public static final By FILE_LINK_LOCATOR = By.xpath(".//a[contains(@class, 'tools_button_download')]");
    public static final By FILE_NAME_LOCATOR = By.xpath(".//div[contains(@class, 'item_name')]");

    public DownloadPage(WebDriver driver, String url) {
        this.driver = driver;
        this.urlDownload = url;
        jsExecutor = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
    }

    public boolean checkUrl() {
        return driver.getCurrentUrl().contains(urlDownload);
    }

    private String getFileName(WebElement container) {
        return container.findElement(FILE_NAME_LOCATOR).getText();
    }

    private String getFileLink(WebElement container) {
        return container.findElement(FILE_LINK_LOCATOR).getAttribute("href");
    }

    public Map<String, String> getAllFilesToDownload() {
        Map<String, String> map = new HashMap<>();
        for (WebElement e : getAllFilesContainers()) {
            map.put(getFileName(e), getFileLink(e));
        }
        return map;
    }

    private List<WebElement> getAllFilesContainers() {
        return driver.findElements(FILE_CONTAINER_LOCATOR);
    }
}
