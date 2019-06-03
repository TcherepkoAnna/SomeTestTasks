package tests;

import config.Config;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.ruFilesFm.DownloadPage;
import pages.ruFilesFm.Homepage;
import util.Util;

import java.io.File;
import java.util.List;
import java.util.Map;

public class TestFilesFm extends TestBase {
    private static final Logger LOG = Logger.getLogger(TestFilesFm.class);
    private Config config = new Config();
    private static final String UPLOAD_LIST_FILEPATH = System.getProperty("user.dir") + "\\src\\main\\resources\\toUploadList.txt";
    private static final String DOWNLOAD_LIST_FILEPATH = System.getProperty("user.dir") + "\\src\\main\\resources\\toDownloadList.txt";

    @Test(priority = 0)
    public void uploadingFiles() {
        String baseUrl = Homepage.URL_HOMEPAGE;
        driver.get(baseUrl);
        Homepage homepage = new Homepage(driver);

        String filePath = Util.getFilepathForUpload(UPLOAD_LIST_FILEPATH);
        LOG.debug("files to upload: " + filePath);
        homepage.setFilePath(filePath.trim());
        String downloadPageLink = homepage.startUpload();
        Util.appendToFile(downloadPageLink, DOWNLOAD_LIST_FILEPATH);
        LOG.info("download link for file/-s: \n" + filePath + " ---> " + downloadPageLink);
    }


    @Test(priority = 1)
    public void downloadFileUsingJavaIO() {
        Assert.assertTrue(Util.createDirectory(config.getDownloadDir()), "Failed to create dir");
        log("Directory exists/was created");
        List<String> urlList = Util.getDownloadPageLinks(DOWNLOAD_LIST_FILEPATH);
        for (String url : urlList) {
            if (url.isEmpty()) continue;
            log("downloading from: >" + url + "<");
            driver.get(url.trim());
            DownloadPage downloadPage = new DownloadPage(driver, url);
            Map<String, String> filesToDownload = downloadPage.getAllFilesToDownload();
            filesToDownload.forEach((fileName, fileLink) -> {
                log("download source: " + fileLink + " for file: " + fileName);
                Util.downloadFromLink(config.getDownloadDir() + File.separator + fileName, fileLink);
            });
        }
    }

}
