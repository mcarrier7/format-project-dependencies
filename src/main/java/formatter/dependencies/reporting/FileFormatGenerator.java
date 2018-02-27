/**
 * 
 */
package formatter.dependencies.reporting;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transforms xml content against an xsl template and returns it in a specific
 * output format
 */
public class FileFormatGenerator
{
	private static Logger sLog = LoggerFactory.getLogger(FileFormatGenerator.class);
	
	/**
	 * Method to transform xml content against an xslt template
	 * 
	 * @param xmlContent
	 *            XML content to transform
	 * @param xslTemplateStream
	 *            XSLT template to transform the XML content against
	 * @param output
	 *            Output to transform the XML & XSLT into
	 * @param outputFormat
	 *            Document output format (PDF, RTF)
	 * @return A document in the outputFormat output format transformed from the
	 *         specified XML and XSLT
	 */
	public OutputStream transform(
			DOMSource xmlContent,
			InputStream xslTemplateStream,
			OutputStream output,
			String outputFormat ) {
		if (xmlContent == null) {
			return null;
		}
		if (outputFormat == null || "".equals(outputFormat.trim())) {
			return null;
		}
		try {
			FopFactory fopFactory = FopFactory.newInstance();
			FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

			Fop fop = fopFactory.newFop(
					outputFormat,
					foUserAgent,
					output);
			TransformerFactory factory = TransformerFactory.newInstance();
			StreamSource stylesource = new StreamSource(
					xslTemplateStream);

			Transformer transformer = factory.newTransformer(stylesource);
			transformer.setParameter(
					"versionParam",
					"2.0");

			Result res = new SAXResult(
					fop.getDefaultHandler());
			transformer.transform(
					xmlContent,
					res);
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