
package edu.ntnu.idi.idatt.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for reading and writing objects to/from JSON files using GSON
 */
public class JsonHandler {
  private static final Logger LOGGER = Logger.getLogger(JsonHandler.class.getName());
  private static final Gson gson = new GsonBuilder()
          .setPrettyPrinting()
          .serializeNulls()
          .create();

  /**
   * Writes an object to a JSON file
   *
   * @param object   The object to write
   * @param filePath The path where the JSON file will be saved
   * @throws IOException If there's an error writing to the file
   */
  public static void writeToJson(Object object, String filePath) throws IOException {
    if (object == null) {
      throw new IllegalArgumentException("Cannot serialize null object");
    }

    Path path = Paths.get(filePath);
    // Create directories if they don't exist
    try {
      Files.createDirectories(path.getParent());
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Failed to create directories for path: " + path.getParent(), e);
      throw new IOException("Failed to create directories for JSON file", e);
    }

    try (Writer writer = new FileWriter(filePath)) {
      gson.toJson(object, writer);
      LOGGER.info("Successfully wrote JSON to: " + filePath);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Error writing JSON to file: " + filePath, e);
      throw e;
    }
  }

  /**
   * Reads an object from a JSON file
   *
   * @param <T>      The type of object to read
   * @param filePath The path to the JSON file
   * @param classOfT The class of the object to read
   * @return The object read from the JSON file
   * @throws IOException If there's an error reading from the file
   */
  public static <T> T readFromJson(String filePath, Class<T> classOfT) throws IOException {
    if (filePath == null || filePath.isEmpty()) {
      throw new IllegalArgumentException("File path cannot be null or empty");
    }
    if (classOfT == null) {
      throw new IllegalArgumentException("Class type cannot be null");
    }

    Path path = Paths.get(filePath);
    if (!Files.exists(path)) {
      LOGGER.warning("JSON file does not exist: " + filePath);
      throw new IOException("File not found: " + filePath);
    }

    try (Reader reader = new FileReader(filePath)) {
      T result = gson.fromJson(reader, classOfT);
      if (result == null) {
        LOGGER.warning("Failed to deserialize JSON from: " + filePath);
        throw new IOException("Failed to deserialize JSON content");
      }
      return result;
    } catch (JsonSyntaxException e) {
      LOGGER.log(Level.SEVERE, "Invalid JSON syntax in file: " + filePath, e);
      throw new IOException("Invalid JSON format", e);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Error reading JSON from file: " + filePath, e);
      throw e;
    }
  }
}