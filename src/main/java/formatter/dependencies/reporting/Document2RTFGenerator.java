package formatter.dependencies.reporting;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.dom.DOMSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class demonstrates the conversion of an XML file to PDF using XSLT and
 * FOP (XSL-FO).
 */
public class Document2RTFGenerator extends
		DocumentGeneratorImpl
{
	private static Logger sLog = LoggerFactory.getLogger(Document2RTFGenerator.class);

	/**
	 * Method to generate a MS-Word document output from the specified generic
	 * object and the XSLT stylesheet
	 * 
	 * @param objectToConvert
	 *            Generic object to convert to XML and transform against the
	 *            XSLT stylesheet
	 * @param output
	 *            Output stream to write the transformed output to
	 * @param templateName
	 *            path/name of the XSLT template to transform the generated XML
	 *            against
	 * @see RTFGenerator
	 */
	public <T> OutputStream generateDocument(
			T objectToConvert,
			OutputStream output,
			String templateName ) {
		if (objectToConvert == null || output == null) {
			return null;
		}
		try {
			DOMSource xmlContent = generateXmlContent(objectToConvert);
			InputStream xslTemplateStream = this.getClass().getResourceAsStream(
					"/" + templateName);
			RTFGenerator generator = new RTFGenerator();
			output = generator.transform(
					xmlContent,
					xslTemplateStream,
					output);
			xslTemplateStream.close();
		}
		catch (Exception e) {
			sLog.error(
					"Error occurred in document transformation",
					e);
		}
		finally {
			if (output != null) {
				try {
					output.close();
				}
				catch (IOException e) {
					sLog.error("Error: could not close output stream");
				}
			}
		}
		return output;
	}
}