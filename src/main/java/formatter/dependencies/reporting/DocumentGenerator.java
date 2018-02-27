/**
 * 
 */
package formatter.dependencies.reporting;

import java.io.OutputStream;

/**
 * Basic interface for defining document generation against generic objects
 *
 */
public interface DocumentGenerator
{
	/**
	 * Definition for method to generate a document from a generic object
	 * 
	 * @param objectToConvert
	 *            Generic object to convert to XML and transform against the
	 *            XSLT stylesheet
	 * @param output
	 *            Output stream to write the transformed output to
	 * @param templateName
	 *            path/name of the XSLT template to transform the generated XML
	 *            against
	 * @return Generated output document from generic object and specified XSLT
	 *         stylesheet
	 */
	public <T> OutputStream generateDocument(
			T objectToConvert,
			OutputStream output,
			String templateName );
}