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
public class Document2PDFGenerator extends
		DocumentGeneratorImpl
{
	private static Logger sLog = LoggerFactory.getLogger(Document2PDFGenerator.class);

	/**
	 * Method to generate a PDF document output from the specified generic
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
	 * @see PDFGenerator
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
			PDFGenerator generator = new PDFGenerator();
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