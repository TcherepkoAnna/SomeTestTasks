package googleDrive;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import org.apache.log4j.Logger;

public class FolderCreator {

    private static final Logger LOG = Logger.getLogger(FolderCreator.class);

    public static final File createGoogleFolder(Drive driveService, String folderIdParent, String folderName) throws IOException {

        File fileMetadata = new File();
        fileMetadata.setName(folderName);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        if (folderIdParent != null) {
            List<String> parents = Arrays.asList(folderIdParent);
            fileMetadata.setParents(parents);
        }
        // Create a Folder.
        // Returns File object with id & name fields will be assigned values
        File file = driveService.files().create(fileMetadata).setFields("id, name").execute();
        LOG.debug("Created folder with id= "+ file.getId());
        LOG.debug("folder name= "+ file.getName());
        return file;
    }



}
