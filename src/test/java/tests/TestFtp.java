package tests;

import config.Config;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import util.FtpConnection;

import java.io.IOException;

public class TestFtp {

    private final static Logger LOG = Logger.getLogger(TestFtp.class);
    private Config config = new Config();
    private FtpConnection conn;

    @Test
    public void testFtp() {

        try {
            conn = new FtpConnection(config.getFtpHostname());
            Assert.assertTrue(conn.connect());
            log("successful connection");
            Assert.assertTrue(conn.login(config.getFtpUsername(), config.getFtpPassword()));
            log("successful login");

            FTPFile[] files = conn.getDirectoriesList();
            Assert.assertTrue(files != null && files.length > 0, "no directories found");
            log("found number of directories: " + files.length);

            for (FTPFile f : files) {
                String name = f.getName();
                log("found directory: " + name);
                Assert.assertTrue(conn.changeDirectory(name), "failed to change directory");
                log("successfully changed directory");

                if (conn.createDirectory(config.getFtpDirToCreateName())) {
                    log("created new directory");
                    Assert.assertTrue(conn.deleteDirectory(config.getFtpDirToCreateName()), "failed to delete directory");
                    log("deleted directory");
                } else {log("failed to create new directory");}
            }
        } catch (IOException e) {
            LOG.error(e.getMessage());
        } finally {
            try {
                conn.disconnectFromServer();
            } catch (IOException ex) {
                LOG.error(ex.getMessage());
            }
        }
    }


    private void log(String message) {
        Reporter.log(message);
        LOG.info(message);
    }


}
