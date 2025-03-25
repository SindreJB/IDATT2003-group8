package edu.ntnu.idi.idatt.filehandling;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;

/**
 * Utility class for handling JSON file operations.
 * Provides methods to serialize objects to JSON files and deserialize JSON
 * files to objects.
 */
public class JsonHandler {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  static {
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
  }

  /**
   * Writes an object to a JSON file.
   *
   * @param object   The object to serialize
   * @param filePath The path where the JSON file will be saved
   * @param <T>      The type of the object
   * @throws IOException If an I/O error occurs
   */
  public static <T> void writeToJson(T object, String filePath) throws IOException {
    objectMapper.writeValue(new File(filePath), object);
  }

  /**
   * Reads a JSON file and converts it to an object.
   *
   * @param filePath  The path to the JSON file
   * @param valueType The class of the object to be created from the JSON file
   * @param <T>       The type of the object
   * @return The deserialized object
   * @throws IOException If an I/O error occurs
   */
  public static <T> T readFromJson(String filePath, Class<T> valueType) throws IOException {
    return objectMapper.readValue(new File(filePath), valueType);
  }
}
