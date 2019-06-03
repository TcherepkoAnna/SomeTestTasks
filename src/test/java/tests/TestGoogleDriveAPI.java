package tests;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import config.Config;
import googleDrive.FileCreator;
import googleDrive.FolderCreator;
import googleDrive.GoogleDriveUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import util.Util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestGoogleDriveAPI {

    private static final Logger LOG = Logger.getLogger(TestGoogleDriveAPI.class);
    private Drive driveService;
    private Config config = new Config();
    private static final String UPLOAD_LIST_FILEPATH = System.getProperty("user.dir") + "\\src\\main\\resources\\toUploadList.txt";
    private static final String GOOGLE_DOWNLOAD_LIST_FILEPATH = System.getProperty("user.dir") + "\\src\\main\\resources\\GoogleDriveDownloadList.txt";


    @BeforeClass
    public void setUp() throws IOException {
        driveService = GoogleDriveUtils.getDriveService();
    }


    @Test(priority = 0)
    public void testUpload() throws IOException {
        log("\nTesting file Upload\n");
        // Create a Folder
        File folder = new FolderCreator().createGoogleFolder(driveService, null, "TEST-FOLDER");
        Assert.assertNotNull(folder);
        log("created folder: " + folder.getName());
        printAllFiles(getAllFiles("'root' in parents and trashed = false"));
        FileCreator fileCreator = new FileCreator(driveService);
        List<java.io.File> filesList = getFilesToUpload();
        Assert.assertTrue(filesList != null && !filesList.isEmpty());
        log("got files for upload: " + filesList.size());
        for (java.io.File uploadFile : filesList) {
            // Create Google File:
            LOG.debug(uploadFile.getName());
            File googleFile = fileCreator.createGoogleFile(folder.getId(), "text/plain", uploadFile.getName(), uploadFile);
            Assert.assertNotNull(googleFile);
            log("uploaded file: " + googleFile.getName());
            Util.appendToFile(googleFile.getWebContentLink(), GOOGLE_DOWNLOAD_LIST_FILEPATH);
            log("added file's link: " + googleFile.getWebContentLink() + " to file: " + GOOGLE_DOWNLOAD_LIST_FILEPATH);
        }
        printAllFiles(getAllFiles("'" + folder.getId() + "' in parents"));
    }

    public List<java.io.File> getFilesToUpload() {
        LOG.debug("getting filepathes for upload");
        String filesStr = Util.getFilepathForUpload(UPLOAD_LIST_FILEPATH);
        LOG.debug("filepathes to upload: \n" + filesStr);
        List<java.io.File> filesList = new ArrayList<>();
        for (String str : filesStr.split("\n")) {
            filesList.add(new java.io.File(str));
        }
        return filesList;
    }

    @Test(priority = 1)
    public void testDownload() throws IOException {

        log("\nTesting file Download\n");
        Assert.assertTrue(Util.createDirectory(config.getDownloadDir()), "Failed to create dir");
        LOG.debug("Directory exists/was created");

        List<String> urlList = Util.getDownloadPageLinks(GOOGLE_DOWNLOAD_LIST_FILEPATH);
        Assert.assertTrue(urlList != null && !urlList.isEmpty(), "failed to get links for download ");

        for (String fileUrl : urlList) {
            if (fileUrl.isEmpty()) continue;
            log("url for download: \n>" + fileUrl + "<");
            String fileId = Util.getQueryMap(fileUrl.substring(fileUrl.indexOf('?') + 1)).get("id");
            Assert.assertTrue(fileId!=null && !fileId.equals(""), "file id is empty");
            File googleFile = driveService.files().get(fileId).execute();
            log(googleFile.getName() + " is for download");
            java.io.File targetFile = new java.io.File(config.getDownloadDir() + java.io.File.separator + googleFile.getName());
            log("target file: " + targetFile);
            FileOutputStream output = new FileOutputStream(targetFile);
            Assert.assertNotNull(output, "outputstream is null");
            this.driveService.files().get(fileId).executeMediaAndDownloadTo(output);
        }

    }


    private List<File> getAllFiles(String query) throws IOException {
        FileList result = driveService.files().list().setQ(query).setPageSize(10).setFields("nextPageToken, files(id, name)").execute();
        List<File> files = result.getFiles();
        Assert.assertTrue(files != null || !files.isEmpty(), "No files found.");
        LOG.debug("Found number of files: " + files.size());
        return files;
    }

    private void printAllFiles(List<File> files) {
        if (!(files == null || files.isEmpty())) {
            LOG.debug("Files:");
            for (File file : files) {
                LOG.debug(String.format("%s (%s)", file.getName(), file.getId()));
            }
        }
    }

    private void log(String message) {
        Reporter.log(message);
        LOG.info(message);
    }


}
