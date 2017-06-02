package uk.ac.aston.dc2300.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by dan on 03/06/2017.
 */
public class FileUtils {

    private String filePath;

    public FileUtils(String filePath) {
        this.filePath = filePath;
    }

    public void writeToFile(String data) throws IOException {
        File file = new File(filePath);

        // if file doesnt exists, then create it
        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(),true);
        BufferedWriter bufferedWriter = new BufferedWriter(fw);
        bufferedWriter.write(data);
        bufferedWriter.newLine();

        if (bufferedWriter != null)
            bufferedWriter.close();

        if (fileWriter != null)
            fileWriter.close();

    }
}
