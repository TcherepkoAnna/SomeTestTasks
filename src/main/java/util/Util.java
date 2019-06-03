package util;

import googleDrive.FileCreator;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


public class Util {

    private static final Logger LOG = Logger.getLogger(Util.class);


    public static String getFilepathForUpload(String filepath) {
        String str = readStringFromFile(filepath, true);
        return str;
    }

    public static List<String> getDownloadPageLinks(String filepath) {
        List<String> stringList = Arrays.asList(readStringFromFile(filepath, false).split("\n"));
        return stringList;
    }

    private static String readStringFromFile(String filepath, boolean isAbsolutePath) {
        StringBuilder str = new StringBuilder();
        //read file into stream, try-with-resources
        try (Stream<String> stream = Files.lines(Paths.get(filepath))) {
            stream.forEach(x -> {
                str.append(isAbsolutePath ? new File(x).getAbsolutePath(): x).append("\n");
            });

        } catch (IOException e) {
            LOG.error(e.getMessage());        }
        return str.toString();
    }

    public static boolean createDirectory(String path) {
        File files = new File(path);
        if (!files.exists()) {
            if (files.mkdirs()) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    public static void downloadFromLink(String fileName, String fileLink) {
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


    public static void appendToFile(String whatToWrite, String whereToWrite) {
        LOG.debug("writing to file " + whereToWrite);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(whereToWrite, true))) {
            writer.write("\n" + whatToWrite);
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }

    public static void scrollIntoView(JavascriptExecutor executor, WebElement element) {
        executor.executeScript("arguments[0].scrollIntoView()", element);
    }

    public static Map<String, String> getQueryMap(String query)
    {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params)
        {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }




}
