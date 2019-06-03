package util;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import java.io.IOException;

public class FtpConnection {

    private final static Logger LOG = Logger.getLogger(FtpConnection.class);
    FTPClient client;
    String host;

    public FtpConnection(String host) throws IOException {
        this.client = new FTPClient();
        this.host = host;
    }


    public boolean connect() throws IOException {
        LOG.debug("setting connection");
        client.connect(host);
        int replyCode = client.getReplyCode();
        LOG.debug("SERVER: "+ replyCode);
        if (FTPReply.isPositiveCompletion(replyCode)) {
            LOG.error("Connected successfully. Server reply code: " + replyCode);
            return true;
        }
        LOG.error("Operation failed. Server reply code: " + replyCode);
        return false;
    }

    public boolean login(String name, String password) throws IOException {
        boolean success = client.login(name, password);
        if (success) {
            LOG.debug("Connection established...");
            return true;
        } else {
            LOG.debug("Connection fail...");
            return false;
        }
    }

    public void disconnectFromServer() throws IOException {
        LOG.debug("trying to log out and disconnecting from server");
        if (client.isConnected()) {
            client.logout();
            client.disconnect();
        }
    }

    public FTPFile[] getDirectoriesList() throws IOException {
        LOG.debug("getting directories");
        return client.listDirectories();
    }

    public boolean deleteDirectory(String dirToCreate) throws IOException {
        LOG.debug("deleting directory");
        boolean deleted = client.removeDirectory(dirToCreate);
        showServerReply();
        if (deleted) {
            LOG.debug("The directory was removed successfully.");
        } else {
            LOG.debug("Could not delete the directory, it may not be empty");
        }
        return deleted;
    }

    public boolean createDirectory(String dirToCreate) throws IOException {
        LOG.debug("creating directory");
        boolean success = client.makeDirectory(dirToCreate);
        showServerReply();
        if (success) {
            LOG.debug("Successfully created directory: " + dirToCreate);
        } else {
            LOG.debug("Failed to create directory. See server's reply.");
        }
        return success;
    }

    public boolean changeDirectory(String name) throws IOException {
        boolean success = client.changeWorkingDirectory("/" + name);
        showServerReply();
        if (success) {
            LOG.debug("Successfully changed working directory.");
            LOG.debug("Now in: " + client.printWorkingDirectory());
        } else {
            LOG.debug("Failed to change working directory. See server's reply.");
        }
        return success;
    }

    private void showServerReply() {
        LOG.debug("getting reply from server");
        String[] replies = client.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                LOG.debug("SERVER: " + aReply);
            }
        }
    }
}
