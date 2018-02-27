/**
 * 
 */
package formatter.dependencies.reporting;

import java.io.OutputStream;
import java.io.StringReader;
import java.text.ParseException;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;

import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import formatter.dependencies.converters.JsonConverter;

/**
 * Generates an xml document for a generic object
 */
public abstract class DocumentGeneratorImpl implements
		DocumentGenerator
{
	private static Logger sLog = LoggerFactory.getLogger(DocumentGeneratorImpl.class);

	private String xmlRootElementName = "object";

	/**
	 * Sets the value to specify as the rot element for the generated XML
	 * 
	 * @param xmlRootElementName
	 *            the value to specify as the rot element for the generated XML
	 */
	public void setXmlRootElementName(
			String xmlRootElementName ) {
		this.xmlRootElementName = xmlRootElementName;
	}

	/**
	 * Returns the value to specify as the rot element for the generated XML
	 * 
	 * @return the value to specify as the rot element for the generated XML
	 */
	public String getXmlRootElementName() {
		return this.xmlRootElementName;
	}

	/**
	 * Generate xml content from a generic object
	 * 
	 * @param objectToConvert
	 *            A generic object that is to be converted to XML
	 * @return An XML Document Object Model Source representing the object
	 *         specified
	 */
	protected <T> DOMSource generateXmlContent(
			T objectToConvert ) {
		DOMSource source = null;
		try {
			Map<String, Object> transformedDoc = transform(objectToConvert);
			if (transformedDoc != null && !transformedDoc.isEmpty()) {
				String entryJson = JsonConverter.objectToJsonString(transformedDoc);
				JSON json = JSONSerializer.toJSON(entryJson);
				sLog.debug(
						"JSON FROM ENTRY: {}",
						json.toString());
				XMLSerializer serializer = new XMLSerializer();
				serializer.setRootName(xmlRootElementName);
				serializer.setTypeHintsEnabled(false);
				String entryXml = serializer.write(json);
				sLog.debug(
						"XML FROM ENTRY: {}",
						entryXml);
				DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader(
						entryXml));
				Document doc = db.parse(is);

				if (doc != null) {
					source = new DOMSource(
							doc);
				}
			}
		}
		catch (Exception e) {
			sLog.error(
					"Error occurred generating document xml content",
					e);
		}
		return source;
	}

	/**
	 * Transform a generic object to a map
	 * 
	 * @param objectToConvert
	 *            Object to convert to a map
	 * @return A map representing the object specified
	 * @throws ParseException
	 */
	private <T> Map<String, Object> transform(
			T objectToConvert )
			throws ParseException {
		Map<String, Object> objectMap = null;
		try {
			if (objectToConvert != null) {
				if (objectToConvert instanceof JSONObject) {
					objectMap = JsonConverter.jsonToMap(objectToConvert.toString());
				}
				else {
					String objectJsonString = JsonConverter.objectToJsonString(objectToConvert);
					objectMap = JsonConverter.jsonToMap(objectJsonString);
				}
			}
		}
		catch (Exception e) {
			sLog.error(
					"Encountered Exception: " + e.getLocalizedMessage(),
					e);
			return null;
		}

		return objectMap;
	}

	/**
	 * Abstract method definition for various generator implementations
	 * 
	 * @param objectToConvert
	 *            Generic object to convert to XML and transform against the
	 *            XSLT stylesheet
	 * @param output
	 *            Output stream to write the transformed output to
	 * @param templateName
	 *            path/name of the XSLT template to transform the generated XML
	 *            against
	 */
	abstract public <T> OutputStream generateDocument(
			T objectToConvert,
			OutputStream output,
			String templateName );
}