package drivers;

import config.Config;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class ChromeCreator implements DriverCreator {

    private Config config = new Config();

    @Override
    public WebDriver createDriver() {
        return new ChromeDriver();
    }
}