package uk.ac.aston.dc2300.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Set of utilities to interact with the filesystem
 *
 * @author Dan Cotton
 * @since 03/06/17
 */
public class FileUtils {

    private File file;

    public FileUtils(File source) {
        file = source;
    }

    /**
     * Writes the given data and headers into the file
     *
     * @param data the data to write
     * @param headers the headers to write
     * @throws IOException encountered error
     */
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

        bufferedWriter.close();
        fileWriter.close();

    }
}
