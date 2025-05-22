
package edu.ntnu.idi.idatt.persistence;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.exceptions.FileWriteException;

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
  public static void writeToJson(Object object, String filePath) throws FileWriteException {
    if (object == null) {
      throw new FileWriteException("Cannot serialize null object");
    }

    Path path = Paths.get(filePath);
    // Create directories if they don't exist
    try {
      Files.createDirectories(path.getParent());
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Failed to create directories for path: " + path.getParent(), e);
      throw new FileWriteException("Failed to create directories for JSON file", e);
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
  public static <T> T readFromJson(String filePath, Class<T> classOfT) throws FileReadException {
    if (filePath == null || filePath.isEmpty()) {
      throw new IllegalArgumentException("File path cannot be null or empty");
    }
    if (classOfT == null) {
      throw new IllegalArgumentException("Class type cannot be null");
    }

    Path path = Paths.get(filePath);
    if (!Files.exists(path)) {
      throw new FileReadException("File not found: " + filePath);
    }

    try (Reader reader = new FileReader(filePath)) {
      T result = gson.fromJson(reader, classOfT);
      if (result == null) {
        LOGGER.warning("Failed to deserialize JSON from: " + filePath);
        throw new IOException("Failed to deserialize JSON content");
      }
      return result;
    } catch (JsonSyntaxException e) {
      throw new FileReadException("Invalid JSON format", e);
    } catch (IOException e) {
      throw new FileReadException("Invalid JSON format", e);
    }
  }
}