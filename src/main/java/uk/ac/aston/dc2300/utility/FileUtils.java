package uk.ac.aston.dc2300.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by dan on 03/06/2017.
 */
public class FileUtils {

    private File file;

    public FileUtils(String filePath) {
        file = new File(filePath);
    }

    public FileUtils(File source) {
        file = source;
    }

    public void writeToFile(String data, String headers) throws IOException {

        boolean writeHeaders = false;

        // if file doesnt exists, then create it
        if (!file.exists()) {
            file.createNewFile();
            writeHeaders = true;
        }

        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(),true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        if (writeHeaders) {
            bufferedWriter.write(headers);
            bufferedWriter.newLine();
        }
        bufferedWriter.write(data);
        bufferedWriter.newLine();

        if (bufferedWriter != null)
            bufferedWriter.close();

        if (fileWriter != null)
            fileWriter.close();

    }
}
