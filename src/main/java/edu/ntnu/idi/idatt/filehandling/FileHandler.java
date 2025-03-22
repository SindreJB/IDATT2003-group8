package edu.ntnu.idi.idatt.filehandling;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHandler {

  public static void saveToFile(String content, String filePath) throws IOException {
    Path path = Paths.get(filePath);
    Files.write(path, content.getBytes());
  }

  public static String readFromFile(String filePath) throws IOException {
    Path path = Paths.get(filePath);
    return new String(Files.readAllBytes(path));
  }
}