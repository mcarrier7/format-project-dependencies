/**
 * 
 */
package formatter.dependencies.converters;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

//import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

/**
 * General utility class for handling converting XML content to JSON
 */
public class XmlToJsonConverter
{
	private static Logger sLog = LoggerFactory.getLogger(XmlToJsonConverter.class);

	/**
	 * Method for converting xml content with xInclude awareness required
	 * 
	 * @param value
	 *            XML value to convert
	 * @param isXIncludeAware
	 *            boolean specifying if the XML content being converted should
	 *            be XIncludeAware
	 * @return Converted input value to JSON
	 */
	public static JSONArray convert(
			String value,
			boolean isXIncludeAware ) {
		if (isXIncludeAware) {
			sLog.trace(
					"Attempting to convert XIncludeAware XML value {} to JSON",
					value);
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				factory.setNamespaceAware(true);
				factory.setXIncludeAware(true);
				DocumentBuilder docBuilder = factory.newDocumentBuilder();
				Document doc = docBuilder.parse(new InputSource(
						new StringReader(
								value)));
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				transformer.setOutputProperty(
						OutputKeys.INDENT,
						"yes");
				StreamResult result = new StreamResult(
						new StringWriter());
				DOMSource source = new DOMSource(
						doc);
				transformer.transform(
						source,
						result);
				return convert(result.getWriter().toString());
			}
			catch (Throwable t) {
				sLog.error(
						"Error converting xml document content: " + t.getLocalizedMessage(),
						t);
			}
		}
		else {
			return convert(value);
		}
		return null;
	}

	/**
	 * Converts XML content to a JSON object
	 * 
	 * @param value
	 *            XML content to convert
	 * @return Converted input value to JSON
	 */
	public static JSONArray convert(
			String value ) {
		sLog.trace(
				"Attempting to convert XML value {} to JSON",
				value);
		XMLSerializer serializer = new XMLSerializer();
		JSON json = serializer.read(value);
		JSONArray retval = new JSONArray();
		if (json.isArray()) {
			retval.add(json.toString());
		}
		else {
			retval.add(JSONObject.fromObject(json.toString()));
		}
		return retval;
	}

	public static JSONArray convertXmlNodeToJSON(
			Node node ) {
		JSONArray retval = null;
		if (node != null) {
			DOMImplementationLS lsImpl = (DOMImplementationLS) node.getOwnerDocument().getImplementation().getFeature(
					"LS",
					"3.0");
			LSSerializer lsSerializer = lsImpl.createLSSerializer();
			lsSerializer.getDomConfig().setParameter(
					"xml-declaration",
					false);
			NodeList childNodes = node.getChildNodes();
			StringBuilder sb = new StringBuilder();
			if (childNodes != null && childNodes.getLength() != 0) {
				for (int nodeIndex = 0; nodeIndex < childNodes.getLength(); nodeIndex++) {
					if (childNodes.item(nodeIndex) != null && childNodes.item(
							nodeIndex).getNodeType() == Node.ELEMENT_NODE) {
						sb.append(lsSerializer.writeToString(childNodes.item(nodeIndex)));
					}
				}
			}
			if (sb != null && sb.toString() != null && !"".equals(sb.toString().trim())) {
				retval = convert(sb.toString());
			}
		}
		return retval;
	}
}
