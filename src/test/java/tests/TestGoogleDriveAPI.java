package tests;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import googleDrive.FileCreator;
import googleDrive.FolderCreator;
import googleDrive.GoogleDriveUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class TestGoogleDriveAPI {

    private Drive driveService;

    @BeforeClass
    public void setUp() throws IOException {

        driveService = GoogleDriveUtils.getDriveService();

    }

    @Test
    public void testUpload() throws IOException {
        // Create a Root Folder
        getAllFiles();
        File folder = new FolderCreator().createGoogleFolder(driveService, null, "TEST-FOLDER2");
        getAllFiles();


        java.io.File uploadFile = new java.io.File(System.getProperty("user.dir") + "\\src\\main\\resources\\data\\testing.txt");
        // Create Google File:
        FileCreator fileCreator = new FileCreator(driveService);
        File googleFile = fileCreator.createGoogleFile(folder.getId(), "text/plain", "testing.txt", uploadFile);


    }

    @Test
    public void testDownload() {

    }

    private void getAllFiles() throws IOException {

        // Print the names and IDs for up to 10 files.
        FileList result = driveService.files().list().setPageSize(10).setFields("nextPageToken, files(id, name)").execute();
        List<File> files = result.getFiles();
        if (files == null || files.isEmpty()) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            for (File file : files) {
                System.out.printf("%s (%s)\n", file.getName(), file.getId());
            }
        }
    }
}
