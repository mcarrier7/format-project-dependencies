/*
 * 
 */
package formatter.dependencies.reporting;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.dom.DOMSource;

import org.apache.xmlgraphics.util.MimeConstants;

/**
 * Base class to define a PDF document generator
 * 
 * @author mark.carrier
 */
public class PDFGenerator extends
		FileFormatGenerator
{

	public OutputStream transform(
			DOMSource xmlContent,
			InputStream xslTemplateStream,
			OutputStream output ) {
		return transform(
				xmlContent,
				xslTemplateStream,
				output,
				MimeConstants.MIME_PDF);
	}
}