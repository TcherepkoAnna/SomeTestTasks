package util;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;


public class Util {

    public static void scrollIntoView(JavascriptExecutor executor, WebElement element) {
        executor.executeScript("arguments[0].scrollIntoView()", element);
    }

}
