package pages.ruFilesFm;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import util.Util;

public class Homepage {

    private WebDriver driver;
    private JavascriptExecutor jsExecutor;
    private static final Logger LOG = Logger.getLogger(Homepage.class);
    public static final String URL_HOMEPAGE = "https://ru.files.fm/";

    @FindBy(xpath = "//div[contains(@class, 'start_upload_button')]")
    private WebElement startUploadButton;

    @FindBy(xpath = "//input[@type='file'and not(contains(@style,'hidden'))]")
    private WebElement uploadInput;

    @FindBy(xpath = "//a[@id='download_link']")
    private WebElement downloadLink;


    public Homepage(WebDriver driver) {
        this.driver = driver;
        jsExecutor = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
    }

    public boolean checkUrl() {
        return driver.getCurrentUrl().equals(URL_HOMEPAGE);
    }

    public void setFilePath(String filepath) {
        uploadInput.sendKeys(filepath);
    }

    public String startUpload() {
//        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(startUploadButton));
        Util.scrollIntoView(jsExecutor, startUploadButton);
        startUploadButton.click();
        return getDownloadLink();
    }

    public String getDownloadLink() {
        new WebDriverWait(driver, 30).until(ExpectedConditions.elementToBeClickable(downloadLink));
        return downloadLink.getAttribute("href");
    }
}
