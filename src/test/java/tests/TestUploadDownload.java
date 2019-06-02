package tests;

import config.Config;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.ruFilesFm.DownloadPage;
import pages.ruFilesFm.Homepage;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class TestUploadDownload extends TestBase {
    public static final Logger LOG = Logger.getLogger(TestUploadDownload.class);
    private Config config = new Config();
    private static final String UPLOAD_LIST_FILEPATH = System.getProperty("user.dir") + "\\src\\main\\resources\\toUploadList.txt";
    private static final String DOWNLOAD_LIST_FILEPATH = System.getProperty("user.dir") + "\\src\\main\\resources\\toDownloadList.txt";

    @Test(priority = 0)
    public void uploadingFiles() {
        String baseUrl = Homepage.URL_HOMEPAGE;
        driver.get(baseUrl);
        Homepage homepage = new Homepage(driver);

        String filePath = getFilepathForDownload(UPLOAD_LIST_FILEPATH);
        LOG.debug("files to upload: " + filePath);
        homepage.setFilePath(filePath.trim());
        String downloadPageLink = homepage.startUpload();
        addLinkToList(downloadPageLink);
        LOG.info("download link for file/-s: \n" + filePath + " ---> " + downloadPageLink);
    }

    private void addLinkToList(String downloadPageLink) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DOWNLOAD_LIST_FILEPATH, true))) {
            writer.write("\n" + downloadPageLink);
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }

    private String getFilepathForDownload(String filepath) {
        String str = readStringFromFile(filepath, true);
        return str;
    }

    private String readStringFromFile(String filepath, boolean isAbsolutePath) {
        StringBuilder str = new StringBuilder();
        //read file into stream, try-with-resources
        try (Stream<String> stream = Files.lines(Paths.get(filepath))) {

            stream.forEach(x -> {
                str.append(isAbsolutePath ? new File(x).getAbsolutePath(): x).append("\n");
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }

    @Test(priority = 1)
    public void downloadFileUsingJavaIO() {
        List<String> urlList = getDownloadPageLinks();
        for (String url : urlList) {
            if (url.isEmpty()) continue;
            LOG.info("downloading from: >" + url +"<");
            driver.get(url.trim());
            DownloadPage downloadPage = new DownloadPage(driver, url);
            Map<String, String> filesToDownload = downloadPage.getAllFilesToDownload();
            Assert.assertTrue(createDirectory(config.getDownloadDir()));
            filesToDownload.forEach((fileName, fileLink) -> {
                LOG.info("download source: " + fileLink + " for file: " + fileName);
                downloadFromLink(config.getDownloadDir() + File.separator + fileName, fileLink);
            });
        }
    }

    public void downloadFromLink(String fileName, String fileLink) {
        try (BufferedInputStream in = new BufferedInputStream(new URL(fileLink).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }

    private boolean createDirectory(String path) {
        File files = new File(path);
        if (!files.exists()) {
            if (files.mkdirs()) {
                LOG.debug("Directory created!");
                return true;
            } else {
                LOG.debug("Failed to create directory!");
                return false;
            }
        }
        LOG.debug("Directory exists.");
        return true;
    }


    private List<String> getDownloadPageLinks() {
        List<String> stringList = Arrays.asList(readStringFromFile(DOWNLOAD_LIST_FILEPATH, false).split("\n"));
        return stringList;
    }
}
