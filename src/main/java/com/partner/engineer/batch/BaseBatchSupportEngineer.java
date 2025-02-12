package com.partner.engineer.batch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Albert Gomes Cabral
 */
public abstract class BaseBatchSupportEngineer {

    public static boolean checkValidFileExtension(String fileExtension, String filePath) {
        File file = new File(filePath);

        if (file.exists()) {
            String fileName = file.getName();

            return fileName.endsWith(fileExtension);
        }

        return false;
    }

    public static String readContent(String filePath)
        throws FileNotFoundException {

        try (InputStream inputStream = new FileInputStream(filePath)) {
            return new String(inputStream.readAllBytes());
        }
        catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

}
