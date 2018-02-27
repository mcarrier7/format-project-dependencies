/**
 * 
 */
package formatter.dependencies;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import formatter.dependencies.reporting.Document2PDFGenerator;
import formatter.dependencies.reporting.Document2RTFGenerator;
import formatter.dependencies.reporting.DocumentGeneratorImpl;
import formatter.dependencies.utils.JsonUtils;
import formatter.dependencies.model.Dependency;

/**
 *
 */
public class FormatMavenDependencies
{
	private static Logger sLog = LoggerFactory.getLogger(FormatMavenDependencies.class);

	private static List<Dependency> dependenciesList = null;
	private static String documentName = null;

	/**
	 * @param args
	 */
	@SuppressWarnings("resource")
	public static void main(
			String[] args )
			throws Exception {
		if (args.length == 0) {
			throw new Exception(
					"No path specified to effective pom(s)");
		}
		if (args.length < 3) {
			throw new Exception(
					"USAGE :: {arg0: path to effective pom(s)} {arg1: path for output document} {arg2: internal group id(s)}");
		}
		
		FormatMavenDependencies formatter = new FormatMavenDependencies();
		
		String effective_poms_path = args[0];
		documentName = args[1];

		// dependency id(s) for identifying internal dependencies
		String[] internalGroupIds = new String[args.length-2];
		int arrIndex=0;
		for (int argIndex=2; argIndex<args.length; argIndex++) {
			internalGroupIds[arrIndex]=args[argIndex];
			arrIndex++;
		}
		sLog.info("Formatting dependencies using specified internal group id's of {}", new Object[] {internalGroupIds});
		
		if (effective_poms_path == null || "".equals(effective_poms_path.trim())) {
			throw new Exception(
					"No path specified to effective pom(s)");
		}
		if (documentName == null || "".equals(documentName.trim())) {
			throw new Exception(
					"No document name specified");
		}

		sLog.info(
				"Path to effective pom(s) specified to {}",
				effective_poms_path);

		dependenciesList = new ArrayList<Dependency>();

		File effective_poms_dir = new File(
				effective_poms_path.trim());
		if (effective_poms_dir != null && effective_poms_dir.exists()) {
			if (effective_poms_dir.isDirectory()) {
				formatter.processDir(effective_poms_dir);
			}
			else {
				formatter.processFile(effective_poms_dir);
			}
		}
		else {
			sLog.error(
					"effective poms dir does not exist at path: {}",
					effective_poms_dir.getCanonicalPath());
		}
		Collections.sort(
				dependenciesList,
				new Comparator<Dependency>() {
					public int compare(
							Dependency dependency1,
							Dependency dependency2 ) {
						return (dependency1.getArtifactId().compareTo(dependency2.getArtifactId()));
					}
				});

		final String templateName = "dependencies_template.xsl";
		OutputStream out = new FileOutputStream(
				documentName);
		DocumentGeneratorImpl generator = null;
		String fileExtension = documentName.substring(documentName.lastIndexOf(".")+1);
		if (fileExtension.equalsIgnoreCase("doc")) {
			generator = new Document2RTFGenerator();
		} else if (fileExtension.equalsIgnoreCase("pdf")) {
			generator = new Document2PDFGenerator();
		} else {
			throw new Exception(String.format("File extension %s is not currently supported. Options are doc & pdf", fileExtension));
		}
		if (generator != null) {
			generator.setXmlRootElementName("dependencies");
			try {
				JSONArray internalDependencies = new JSONArray();
				JSONArray externalDependencies = new JSONArray();
				for (Dependency dependency : dependenciesList) {
					if (dependency != null) {
						for (String internalGroupId:internalGroupIds) {
							if (dependency.getGroupId().startsWith(
									internalGroupId)) {
								internalDependencies.add(dependency.toJSON());
							}
							else {
								externalDependencies.add(dependency.toJSON());
							}
						}
					}
				}
	
				JSONObject dependenciesJSON = new JSONObject();
				if (!internalDependencies.isEmpty()) {
					dependenciesJSON.put(
							"internalDependencies",
							internalDependencies);
				}
				if (!externalDependencies.isEmpty()) {
					dependenciesJSON.put(
							"externalDependencies",
							externalDependencies);
				}
				sLog.debug(
						"dependenciesJSON: {}",
						dependenciesJSON);
				out = generator.generateDocument(
						dependenciesJSON,
						out,
						templateName);
				if (out!=null) {
					out.close();
				}
			}
			catch (IOException e) {
				sLog.error("Error: could not close output stream");
			}
			sLog.info("Completed formatting dependencies");
		}
	}

	/**
	 * Process a directory
	 * 
	 * @param directory
	 *            Directory to process
	 */
	private void processDir(
			File directory ) {
		if (directory == null) {
			return;
		}
		sLog.debug(
				"Processing files in directory {}",
				directory.getAbsolutePath());
		File[] files = directory.listFiles(getFilenameFilter());
		if (files != null && files.length != 0) {
			for (File file : files) {
				if (file != null) {
					processFile(file);
				}
			}
		}
	}

	/**
	 * Process a single file
	 * 
	 * @param file
	 *            File to process
	 */
	private void processFile(
			File file ) {
		if (file == null) {
			return;
		}
		sLog.info(
				"Processing file {}",
				file.getAbsolutePath());

		try {
			InputStream xmlFileIS = new FileInputStream(
					file);
			if (xmlFileIS != null) {
				JSONArray fileJsonArray = JsonUtils.getXmlInputStreamAsJson(xmlFileIS);
				if (fileJsonArray != null && !fileJsonArray.isEmpty()) {
					processArray(fileJsonArray);
				}
			}
		}
		catch (FileNotFoundException e) {
			sLog.error(
					e.getLocalizedMessage(),
					e);
		}
	}

	private void processArray(
			JSONArray fileJsonArray ) {
		if (fileJsonArray != null && fileJsonArray.size() != 0) {
			for (int arrayIndex = 0; arrayIndex < fileJsonArray.size(); arrayIndex++) {
				Object json = fileJsonArray.get(arrayIndex);
				if (json instanceof JSONObject) {
					processObject((JSONObject) json);
				}
				else if (json instanceof JSONArray) {
					processArray((JSONArray) json);
				}
			}
		}
	}

	private void processObject(
			JSONObject fileJsonObject ) {
		if (JsonUtils.retrieveObject(
				fileJsonObject,
				Object.class,
				"dependencies") != null) {

			JSONArray dependencies = JsonUtils.retrieveObject(
					fileJsonObject,
					JSONArray.class,
					"dependencies",
					"dependency");

			JSONObject dependencyJSON = null;
			Dependency dependency = null;
			for (int index = 0; index < dependencies.size(); index++) {
				dependencyJSON = dependencies.getJSONObject(index);
				if (dependencyJSON != null) {
					dependency = new Dependency(
							dependencyJSON);
					if (!lookupDependency(dependency)) {
						dependenciesList.add(dependency);
					}
				}
			}
		}
	}

	private boolean lookupDependency(
			Dependency dependency ) {
		if (!dependenciesList.isEmpty()) {
			for (Dependency next : dependenciesList) {
				if (next != null) {
					if (next.compareTo(dependency)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Filename filter for listing only xml files
	 * 
	 * @return xml FilenameFilter object
	 */
	private FilenameFilter getFilenameFilter() {
		return new FilenameFilter() {
			@Override
			public boolean accept(
					File arg0,
					String arg1 ) {
				return arg1.endsWith(".xml");
			}
		};
	}
}