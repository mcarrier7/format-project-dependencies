package formatter.dependencies.converters;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * Helper methods to perform basic JSON object conversion
 * 
 * Current implementation is via Jackson - exceptions are wrapped in Exception
 * to avoid users being directly dependent on Jackson.
 */
public final class JsonConverter
{
	/** JSON object mapper to reuse in all JSON API calls */
	private final static ObjectMapper jsonMapper = new ObjectMapper();

	/**
	 * Static initializer to setup mapper options
	 */
	static {
		// Do not fail if we have unmapped fields when demarshaling an object
		// from JSON
		jsonMapper.configure(
				DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		jsonMapper.setSerializationInclusion(Include.NON_NULL);
	}

	/**
	 * Private constructor to prevent accidental instantiation.
	 */
	private JsonConverter() {}

	/**
	 * Converts an object to a JSON byte array Null input returns an empty byte
	 * array
	 * 
	 * @param object
	 *            Object to encode
	 * @return Byte array of the encoded JSON
	 * @throws Exception
	 *             Any JSON exception that occurs during processing
	 */
	public static byte[] objectToJsonBytes(
			Object object )
			throws Exception {
		try {
			if (object == null) {
				return new byte[0];
			}
			return jsonMapper.writeValueAsBytes(object);
		}
		catch (JsonProcessingException ex) {
			throw new Exception(
					"Exception converting to JSON: ",
					ex);
		}
	}

	/**
	 * Converts an object to a JSON String Null input returns an empty string
	 * 
	 * @param object
	 *            Object to encode
	 * @return String of the encoded JSON
	 * @throws Exception
	 *             Any JSON exception that occurs during processing
	 */
	public static String objectToJsonString(
			Object object )
			throws Exception {
		try {
			if (object == null) {
				return "";
			}
			if (object instanceof JSONObject) {
				return object.toString();
			}
			return jsonMapper.writeValueAsString(object);
		}
		catch (JsonProcessingException ex) {
			throw new Exception(
					"Exception converting to JSON: ",
					ex);
		}
	}

	/**
	 * Converts a map of objects to a JSON string.
	 * 
	 * @param object
	 *            map of objects to convert to a JSON string
	 * @return String representing the specified map of objects
	 * @throws Exception
	 *             If an error occurred converting the map to a string
	 */
	public static String mapToJsonString(
			Map<String, Object> object )
			throws Exception {
		try {
			if (object == null) {
				return "";
			}
			return jsonMapper.writeValueAsString(object);
		}
		catch (JsonProcessingException ex) {
			throw new Exception(
					"Exception converting to JSON: ",
					ex);
		}
	}

	/**
	 * Converts a JSON string to a map of values and maps Null input returns an
	 * empty map
	 * 
	 * @param json
	 *            JSON string
	 * @return Map of values and maps
	 * @throws Exception
	 *             Any JSON exception that occurs during processing
	 */
	public static Map<String, Object> jsonToMap(
			String json )
			throws Exception {
		try {
			if (json == null) {
				return Collections.emptyMap();
			}
			return jsonMapper.readValue(
					json,
					new TypeReference<HashMap<String, Object>>() {});
		}
		catch (IOException ex) {
			throw new Exception(
					"Exception converting from JSON: ",
					ex);
		}
	}

	/**
	 * Convert a JSON input stream to a map
	 * 
	 * @param json
	 *            InputStream of JSON content to convert
	 * @return Map representation of the JSON
	 * @throws Exception
	 *             If an error occurred converting the input content to a map
	 */
	public static Map<String, Object> jsonToMap(
			InputStream json )
			throws Exception {
		try {
			if (json == null) {
				return Collections.emptyMap();
			}
			return jsonMapper.readValue(
					json,
					new TypeReference<HashMap<String, Object>>() {});
		}
		catch (IOException ex) {
			throw new Exception(
					"Exception converting from JSON: ",
					ex);
		}
	}

	/**
	 * Convert JSON from a Reader to a map
	 * 
	 * @param json
	 *            Reader JSON content to convert
	 * @return Map representation of the JSON
	 * @throws Exception
	 *             If an error occurred converting the input content to a map
	 */
	public static Map<String, Object> jsonToMap(
			Reader json )
			throws Exception {
		try {
			if (json == null) {
				return Collections.emptyMap();
			}
			return jsonMapper.readValue(
					json,
					new TypeReference<HashMap<String, Object>>() {});
		}
		catch (IOException ex) {
			throw new Exception(
					"Exception converting from JSON: ",
					ex);
		}
	}

	/**
	 * Converts a JSON byte array to a map of values and maps Null input returns
	 * an empty map
	 * 
	 * @param json
	 *            JSON byte array
	 * @return Map of values and maps
	 * @throws Exception
	 *             Any JSON exception that occurs during processing
	 */
	public static Map<String, Object> jsonToMap(
			byte[] json )
			throws Exception {
		try {
			if (json == null) {
				return Collections.emptyMap();
			}
			return jsonMapper.readValue(
					json,
					new TypeReference<HashMap<String, Object>>() {});
		}
		catch (IOException ex) {
			throw new Exception(
					"Exception converting from JSON: ",
					ex);
		}
	}

	/**
	 * Converts a JSON string to an object of the specified class Null input
	 * returns null
	 * 
	 * @param <X>
	 *            Class to convert the JSON into
	 * @param json
	 *            JSON string
	 * @param clazz
	 *            Class to convert the JSON into
	 * @return Object of type X or null if JSON is null
	 * @throws Exception
	 *             Any JSON exception that occurs during processing
	 */
	public static <X> X jsonToObject(
			String json,
			Class<X> clazz )
			throws Exception {
		try {
			if (json == null) {
				return null;
			}
			return jsonMapper.readValue(
					json,
					clazz);
		}
		catch (IOException ex) {
			throw new Exception(
					"Exception converting from JSON: ",
					ex);
		}
	}

	/**
	 * Converts a JSON byte array to an object of the specified class <br/>
	 * Null input returns null
	 * 
	 * @param <X>
	 *            Class to convert the JSON into
	 * @param json
	 *            JSON string
	 * @param clazz
	 *            Class to convert the JSON into
	 * @return Object of type X or null if JSON is null
	 * @throws Exception
	 *             Any JSON exception that occurs during processing
	 */
	public static <X> X jsonToObject(
			byte[] json,
			Class<X> clazz )
			throws Exception {
		try {
			if (json == null) {
				return null;
			}
			return jsonMapper.readValue(
					json,
					clazz);
		}
		catch (IOException ex) {
			throw new Exception(
					"Exception converting from JSON: ",
					ex);
		}
	}

	/**
	 * Converts JSON content from an input stream to a specific target class
	 * type
	 * 
	 * @param json
	 *            JSON from a input stream
	 * @param clazz
	 *            Target class type to convert the JSON value to
	 * @return Converted value to target type
	 * @throws Exception
	 *             If an error occurred converting the JSON value to the target
	 *             type
	 */
	public static <X> X jsonToObject(
			InputStream json,
			Class<X> clazz )
			throws Exception {
		try {
			if (json == null) {
				return null;
			}
			return jsonMapper.readValue(
					json,
					clazz);
		}
		catch (IOException ex) {
			throw new Exception(
					"Exception converting from JSON: ",
					ex);
		}
	}

	/**
	 * Converts JSON content from a reader to a specific target class type
	 * 
	 * @param json
	 *            JSON from a Reader instance or character stream
	 * @param clazz
	 *            Target class type to convert the JSON value to
	 * @return Converted value to target type
	 * @throws Exception
	 *             If an error occurred converting the JSON value to the target
	 *             type
	 */
	public static <X> X jsonToObject(
			Reader json,
			Class<X> clazz )
			throws Exception {
		try {
			if (json == null) {
				return null;
			}
			return jsonMapper.readValue(
					json,
					clazz);
		}
		catch (IOException ex) {
			throw new Exception(
					"Exception converting from JSON: ",
					ex);
		}
	}
}