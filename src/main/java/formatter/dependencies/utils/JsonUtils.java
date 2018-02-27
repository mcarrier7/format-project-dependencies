/**
 * 
 */
package formatter.dependencies.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import formatter.dependencies.converters.ValueConverter;
import formatter.dependencies.converters.XmlToJsonConverter;

/**
 * Common JSON utility class for interfacing with JSON objects from different
 * input formats
 */
public class JsonUtils
{
	private static Logger sLog = LoggerFactory.getLogger(JsonUtils.class);

	/**
	 * Publicly exposed utility for interacting with JSON
	 * 
	 * @param args
	 */
	public static void main(
			String[] args ) {
		final String prettyPrintKey = "-pretty";
		if (args == null || args.length == 0) {
			sLog.info(
					"USAGE :: {} {JSON String to convert [Note: quotes must be escaped]}",
					prettyPrintKey);
		}
		else {
			String input = null, value = null;
			for (int index = 0; index < args.length; index++) {
				input = args[index];
				if (input != null && !"".equals(input.trim())) {
					if (prettyPrintKey.equalsIgnoreCase(input.trim())) {
						value = args[index + 1];
						if (value != null && !"".equals(value.trim())) {
							sLog.info(prettyPrintJSON(value));
						}
					}
				}
			}
		}
	}

	/**
	 * Converts an XML file at a specified URL to JSON
	 * 
	 * @param xmlFileIS
	 *            Input stream representing input content
	 * @return JSON array representing XML content
	 */
	public static JSONArray getXmlInputStreamAsJson(
			InputStream xmlFileIS ) {
		return getXmlInputStreamAsJson(
				xmlFileIS,
				false);
	}

	/**
	 * Converts an XML file at a specified URL to JSON
	 * 
	 * @param xmlFileIS
	 * @param isXIncludeAware
	 *            specifies if converter should convert with regards to includes
	 *            in the input XML content
	 * @return JSON object representing XML content
	 */
	public static JSONArray getXmlInputStreamAsJson(
			InputStream xmlFileIS,
			boolean isXIncludeAware ) {
		if (xmlFileIS != null) {
			@SuppressWarnings("resource")
			Scanner s = new Scanner(
					xmlFileIS,
					"UTF-8").useDelimiter("\\A");
			String content = s.hasNext() ? s.next() : "";
			return (JSONArray) XmlToJsonConverter.convert(
					content,
					isXIncludeAware);
		}
		return null;
	}

	/**
	 * Converts an XML file at a specified URL to JSON
	 * 
	 * @param xmlFileURL
	 *            URL to convert to JSON
	 * @return JSON object representing XML content
	 */
	public static JSONArray getXmlFileAsJson(
			URL xmlFileURL ) {
		return getXmlFileAsJson(
				xmlFileURL,
				false);
	}

	/**
	 * Converts an XML file at a specified URL to JSON
	 * 
	 * @param xmlFileURL
	 *            URL to XML file
	 * @param isXIncludeAware
	 *            specifies if XML file has includes (links to other xml files
	 *            to be contained within XML)
	 * @return JSON object representing XML content
	 */
	public static JSONArray getXmlFileAsJson(
			URL xmlFileURL,
			boolean isXIncludeAware ) {
		if (xmlFileURL != null) {
			try {
				return getXmlFileAsJson(
						xmlFileURL.toURI(),
						isXIncludeAware);
			}
			catch (URISyntaxException uriEx) {
				sLog.error(
						"URISyntaxException occurred getting xml file content from " + xmlFileURL + ": "
								+ uriEx.getLocalizedMessage(),
						uriEx);
			}
		}
		return null;
	}

	/**
	 * Converts an XML file at a specified URL to JSON
	 * 
	 * @param xmlFileURI
	 *            URI to XML file
	 * @return JSON object representing XML content
	 */
	public static JSONArray getXmlFileAsJson(
			URI xmlFileURI ) {
		return getXmlFileAsJson(
				xmlFileURI,
				false);
	}

	/**
	 * Converts an XML file at a specified URL to JSON
	 * 
	 * @param xmlFileURI
	 *            URI to XML file
	 * @param isXIncludeAware
	 *            specifies if XML file has includes (links to other xml files
	 *            to be contained within XML)
	 * @return JSON array representing XML content
	 */
	public static JSONArray getXmlFileAsJson(
			URI xmlFileURI,
			boolean isXIncludeAware ) {
		JSONArray retObj = null;
		try {
			String fileContent = FileUtils.readFileToString(
					new File(
							xmlFileURI),
					"UTF-8");
			if (fileContent != null && !"".equals(fileContent.trim())) {
				retObj = (JSONArray) XmlToJsonConverter.convert(
						fileContent,
						isXIncludeAware);
			}
		}
		catch (IOException ioEx) {
			sLog.error(
					"IOException occurred getting xml file content from " + xmlFileURI + ": "
							+ ioEx.getLocalizedMessage(),
					ioEx);
			retObj = null;
		}
		return retObj;
	}

	/**
	 * Retrieves an object from JSON at a specific hierarchical path
	 * 
	 * @param json
	 *            JSON object to look for object from
	 * @param targetType
	 *            desired output format target type
	 * @param fields
	 *            list of fields to iterate through to
	 * @return object at path specified, if it exists, in format specified,
	 *         otherwise null
	 */
	@SuppressWarnings("unchecked")
	public static <T> T retrieveObject(
			JSONObject json,
			Class<T> targetType,
			String... fields ) {
		Object retObj = null;
		if (json != null && fields.length != 0) {
			try {
				for (String field : fields) {
					if (json.has(field)) {
						retObj = json.get(field);
						if (retObj instanceof JSONObject) {
							json = (JSONObject) retObj;
						}
					}
					else {
						retObj = null;
					}
				}
				if (retObj != null) {
					Object converted = ValueConverter.convert(
							retObj,
							targetType);
					return (T) converted;
				}
			}
			catch (Exception ex) {
				sLog.error(
						"Exception occurred retrieving object from JSON " + json + ": " + ex.getLocalizedMessage(),
						ex);
			}
		}
		return null;
	}

	/**
	 * Converts a json object to an indented, more displayable format
	 * 
	 * @param jsonObject
	 *            JSONObject to convert
	 * @return String value representing the converted & indented format of the
	 *         specified json
	 */
	public static String prettyPrintJSON(
			JSONObject jsonObject ) {
		sLog.trace("ENTER :: prettyPrintJSON (JSONObject)");
		if (jsonObject == null) {
			return null;
		}
		return prettyPrintJSON(jsonObject.toString());
	}

	/**
	 * Converts a json array to an indented, more displayable format
	 * 
	 * @param jsonArray
	 *            JSONArray to convert
	 * @return String value representing the converted & indented format of the
	 *         specified json
	 */
	public static String prettyPrintJSON(
			JSONArray jsonArray ) {
		sLog.trace("ENTER :: prettyPrintJSON (JSONArray)");
		if (jsonArray == null) {
			return null;
		}
		return prettyPrintJSON(jsonArray.toString());
	}

	/**
	 * Converts a json string to an indented, more displayable format
	 * 
	 * @param jsonContent
	 *            JSON-representing string to convert
	 * @return String value representing the converted & indented format of the
	 *         specified json
	 */
	public static String prettyPrintJSON(
			String jsonContent ) {
		sLog.trace("ENTER :: prettyPrintJSON");
		sLog.debug(
				"Attempting to convert to pretty print JSON for JSON String {}",
				jsonContent);
		String retval = null;
		if (jsonContent == null || "".equals(jsonContent.trim())) {
			return retval;
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			Object json = mapper.readValue(
					jsonContent,
					Object.class);
			retval = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
					json);
		}
		catch (Exception ex) {
			sLog.error(
					ex.getLocalizedMessage(),
					ex);
			retval = null;
		}
		return retval;
	}

	/**
	 * Method to convert an json object to a JSONArray object
	 * 
	 * @param object
	 *            Object to convert
	 * @return JSONArray object populated with specified object
	 */
	public static JSONArray convertObjectToJSONArray(
			Object object ) {
		JSONArray retval = null;
		if (object != null) {
			retval = new JSONArray();
			if (object instanceof JSONObject) {
				retval.add((JSONObject) object);
			}
			else if (object instanceof JSONArray) {
				retval = (JSONArray) object;
			}
		}
		return retval;
	}

	/**
	 * Method to compare the contents of two string-represented json objects
	 * 
	 * @param jsonContent1
	 *            First string-represented json object
	 * @param jsonContent2
	 *            Second string-represented json object
	 * @return true if the contents are equal, false otherwise
	 */
	public boolean compareJsonObjects(
			String jsonContent1,
			String jsonContent2 ) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode tree1 = mapper.readTree(jsonContent1);
			JsonNode tree2 = mapper.readTree(jsonContent2);
			return tree1.equals(tree2);
		}
		catch (Exception e) { 
			/* don't care about the exception, just know it was thrown */
		}
		return false;
	}
}
