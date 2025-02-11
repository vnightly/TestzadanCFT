package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileUtil {

    public static void writeToFile(String filename, List<String> data, boolean append) {
        try (FileWriter writer = new FileWriter(filename, append)) {
            for (String item : data) {
                writer.write(item + System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("Ошибка записи в файл " + filename + ": " + e.getMessage());
        }
    }
}