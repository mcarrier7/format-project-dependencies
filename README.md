#format-project-dependencies
This project is for formatting the effective (maven) pom dependencies into a word or pdf output for simple reporting


# Configuring

1. Open ".dependencies.sh" file using your favorite editor (i.e. vim)

a. Find variable "INTERNAL_GROUP_IDS". Set this to a list of one or more space-separated group id's for the dependency libraries that should be considered internal projects, rather than called external

b. Find variable "FORMAT_MAVEN_DEPENDENCIES_DIR". Set this to the directory path for the main project/repository/workspace to be analyzed. 

Note :: Please ensure that this directory is not read-only.

c. Find variable "REPORT_TYPE". Set this as either "individual" or "all", to specify if multiple reports (one per project) should be generated, or if all should be bundled into one report.

d. To output in pdf, rather than the default word format, find "DOCUMENT_NAME", and change the extension from ".doc" to ".pdf". Feel free to change the name of the document if you wish.

e. Find the line starting with "declare -a arr=.......". Within the paretheses, list the projects to be visited, respective of the main level specified in "$FORMAT_MAVEN_DEPENDENCIES_DIR"


# Running

To run this script against a maven project or repository, from the 'format-project-dependencies' directory, run the following command:

./dependencies.sh {workspace directory root-level path}

Depending on the size of the project or repository, this could be quite quick, or take a couple of minutes to complete.

There will be a few XSLT-related warnings shown, please disregard those.

Once the message "Effective POM analysis completed" is shown and the script finishes without any errors, you should see the word or pdf file created in the root level of the format-project-dependencies.


