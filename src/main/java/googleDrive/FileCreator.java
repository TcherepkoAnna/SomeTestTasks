package googleDrive;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;


import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import org.apache.log4j.Logger;

public class FileCreator {
    private static final Logger LOG = Logger.getLogger(FileCreator.class);
    private Drive driveService;

    public FileCreator(Drive service) {
        LOG.debug("getting FileCreator object");
        this.driveService = service;
    }

    // PRIVATE!
    private File _createGoogleFile(String googleFolderIdParent, String contentType, //
                                   String customFileName, AbstractInputStreamContent uploadStreamContent) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(customFileName);
        List<String> parents = Arrays.asList(googleFolderIdParent);
        fileMetadata.setParents(parents);
        File file = driveService.files().create(fileMetadata, uploadStreamContent)
                .setFields("id, name, webContentLink, webViewLink, parents").execute();
        LOG.debug("Created Google file: " + file.getName());
        LOG.debug("WebContentLink: " + file.getWebContentLink());
        LOG.debug("WebViewLink: " + file.getWebViewLink());
        LOG.debug("Parents: "+file.getParents());
        return file;
    }

    // Create Google File from byte[]
    public File createGoogleFile(String googleFolderIdParent, String contentType, //
                                 String customFileName, byte[] uploadData) throws IOException {
        LOG.debug("creating from byte array");
        AbstractInputStreamContent uploadStreamContent = new ByteArrayContent(contentType, uploadData);
        return _createGoogleFile(googleFolderIdParent, contentType, customFileName, uploadStreamContent);
    }

    // Create Google File from java.io.File
    public File createGoogleFile(String googleFolderIdParent, String contentType, //
                                 String customFileName, java.io.File uploadFile) throws IOException {
        LOG.debug("creating from java.io.File");
        AbstractInputStreamContent uploadStreamContent = new FileContent(contentType, uploadFile);
        return _createGoogleFile(googleFolderIdParent, contentType, customFileName, uploadStreamContent);
    }

    // Create Google File from InputStream
    public File createGoogleFile(String googleFolderIdParent, String contentType, //
                                 String customFileName, InputStream inputStream) throws IOException {
        LOG.debug("creating from InputStream");
        AbstractInputStreamContent uploadStreamContent = new InputStreamContent(contentType, inputStream);
        return _createGoogleFile(googleFolderIdParent, contentType, customFileName, uploadStreamContent);
    }

}