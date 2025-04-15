package edu.ntnu.idi.idatt.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for reading and writing objects to/from JSON files using GSON
 */
public class JsonHandler {
  private static final Gson gson = new GsonBuilder()
          .setPrettyPrinting()
          .create();

  /**
   * Writes an object to a JSON file
   *
   * @param object The object to write
   * @param filePath The path where the JSON file will be saved
   * @throws IOException If there's an error writing to the file
   */
  public static void writeToJson(Object object, String filePath) throws IOException {
    Path path = Paths.get(filePath);
    // Create directories if they don't exist
    Files.createDirectories(path.getParent());

    try (Writer writer = new FileWriter(filePath)) {
      gson.toJson(object, writer);
    }
  }

  /**
   * Reads an object from a JSON file
   *
   * @param <T> The type of object to read
   * @param filePath The path to the JSON file
   * @param classOfT The class of the object to read
   * @return The object read from the JSON file
   * @throws IOException If there's an error reading from the file
   */
  public static <T> T readFromJson(String filePath, Class<T> classOfT) throws IOException {
    try (Reader reader = new FileReader(filePath)) {
      return gson.fromJson(reader, classOfT);
    }
  }
}