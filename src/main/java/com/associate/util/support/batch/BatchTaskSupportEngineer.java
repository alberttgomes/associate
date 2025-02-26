package com.associate.util.support.batch;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Albert Gomes Cabral
 */
public class BatchTaskSupportEngineer {

    public static boolean checkFileExistsOnPath(String fileExtension, String filePath) {
        File file = new File(filePath);

        if (file.exists()) {
            String fileName = file.getName();

            return fileName.endsWith(fileExtension);
        }

        return false;
    }

    public static Map<String, String> readCompanyProperties(String content) {
        Pattern pattern = Pattern.compile(
                "(company\\.(email|phone|name)\\.props)=((\\w*.*)+)");

        Matcher matcher = pattern.matcher(content);

        Map<String, String> companyProperties = new HashMap<>();

        while (matcher.find()) {
            companyProperties.put(matcher.group(1), matcher.group(3));
        }

        return companyProperties;
    }

    public static String readContentByFilePath(String filePath) {
        if (filePath.isEmpty()) {
            return "";
        }

        ClassLoader classLoader =
                BatchTaskSupportEngineer.class.getClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream(filePath)) {
            if (inputStream == null) {
                return "";
            }

            return new String(inputStream.readAllBytes());
        }
        catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

}
