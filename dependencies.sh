#!/bin/bash

# space-separated group id's for the dependency libraries that should be considered internal projects, rather than called external
INTERNAL_GROUP_IDS="mil.nga.giat"

EFFECTIVE_POM_NAME="effective_pom.xml"
WORKSPACE="$1"
POM_FILE="pom.xml"

ROOT_DIR=$(pwd)
# main project/repository/workspace level
FORMAT_MAVEN_DEPENDENCIES_DIR="$ROOT_DIR"
FORMAT_DEPENDENCIES_DIR="$FORMAT_MAVEN_DEPENDENCIES_DIR/effective_poms"
JAR_NAME="project-dependencies-1.0.0-jar-with-dependencies.jar"
# doc or pdf extensions only
DOCUMENT_NAME="dependencies.doc"

# type of report to compose - all or individual
#REPORT_TYPE="individual"
REPORT_TYPE="all"

TEMP_MESSAGES_FILE=$FORMAT_MAVEN_DEPENDENCIES_DIR"/.messages.tmp"
MAVEN_COMMAND="mvn help:effective-pom --log-file $TEMP_MESSAGES_FILE -Doutput=$EFFECTIVE_POM_NAME"

# list the projects to be visited, respective of the main level specified in "$FORMAT_MAVEN_DEPENDENCIES_DIR"
declare -a arr=( "analytics" )

if [ ! -e $FORMAT_DEPENDENCIES_DIR ]
then
   mkdir $FORMAT_DEPENDENCIES_DIR
fi

repo() {
  dir="$1"
  repo="${dir##*/}"
  echo "repo: " $repo

  if [ -f $POM_FILE ]
  then
    MOD_EFFECTIVE_POM_NAME=$repo"_"$EFFECTIVE_POM_NAME
    echo "Generating effective_pom.xml for repo [$repo] with name:" $MOD_EFFECTIVE_POM_NAME
    $MAVEN_COMMAND

    if [ -f $EFFECTIVE_POM_NAME ]
    then
      echo "Moving" $EFFECTIVE_POM_NAME "to directory: " $FORMAT_DEPENDENCIES_DIR " with filename: " $MOD_EFFECTIVE_POM_NAME
      mv $EFFECTIVE_POM_NAME $FORMAT_DEPENDENCIES_DIR/$MOD_EFFECTIVE_POM_NAME
      if [ -f $FORMAT_DEPENDENCIES_DIR/$MOD_EFFECTIVE_POM_NAME ]
      then
        echo "Effective pom moved successfully"
      else
        echo "An error occurred moving effective pom [$EFFECTIVE_POM_NAME] to directory: " $FORMAT_DEPENDENCIES_DIR " with filename: "
      fi
    fi
  fi

  dir_count=$(ls -d */ | wc -l);
  zero=0;
  if [ "$dir_count" -ne "$zero" ]
  then
    for dir in $(ls -d */);
  do
  
  dir=${dir%*/}
  DIR_NAME=${dir##*/ | sed -r 's/[.]+/_/g'}
  DIR_NAME=${DIR_NAME//[.]/_}
  echo "Looking inside directory:" ${dir##*/}
  
  if [ -f $POM_FILE ]
  then
    cd $dir
    echo "Generating effective_pom.xml for repo [$repo] project [$DIR_NAME] with name:" $EFFECTIVE_POM_NAME

    $MAVEN_COMMAND

    if [ -f $EFFECTIVE_POM_NAME ]
    then
      MOD_EFFECTIVE_POM_NAME=$repo"_"$DIR_NAME"_"$EFFECTIVE_POM_NAME
      echo "Moving" $EFFECTIVE_POM_NAME "to directory:" $FORMAT_DEPENDENCIES_DIR "with filename:" $MOD_EFFECTIVE_POM_NAME
      mv $EFFECTIVE_POM_NAME $FORMAT_DEPENDENCIES_DIR/$MOD_EFFECTIVE_POM_NAME
      if [ -f $FORMAT_DEPENDENCIES_DIR/$MOD_EFFECTIVE_POM_NAME ]
      then
        echo "Effective pom moved successfully"
      else
        echo "An error occurred moving effective pom"
      fi
    fi
    cd ../
  fi
  done
fi
}

cd $WORKSPACE
echo "WORKSPACE: $WORKSPACE"

if [ $REPORT_TYPE == 'all' ]
then
  for repo in "${arr[@]}"
  do
    echo $'\n'
    echo "Looking in repository: $repo"
    cd $repo
    repo $repo
    cd ../
  done

  cd $FORMAT_MAVEN_DEPENDENCIES_DIR
  echo $'\n'

cd $ROOT_DIR
  # now that all the effective poms are generated, process them into a word document table
  # reference to executable jar may need tweaking based on system being run on
  java -cp target/$JAR_NAME formatter.dependencies.FormatMavenDependencies $FORMAT_DEPENDENCIES_DIR $DOCUMENT_NAME $INTERNAL_GROUP_IDS
  echo $'\n'
else
  if [ $REPORT_TYPE == 'individual' ]
  then
    echo "Generating individual dependency reports for GeoWave repositories"

    for repo in "${arr[@]}"
    do
      echo $'\n'
      echo "Generating document for repository: $repo"

      cd $WORKSPACE

      rm $FORMAT_DEPENDENCIES_DIR/*

      cd $repo
      repo $repo
      cd ../

      cd $FORMAT_MAVEN_DEPENDENCIES_DIR

      echo $'\n'
      java -cp $FORMAT_MAVEN_DEPENDENCIES_DIR/target/$JAR_NAME formatter.dependencies.FormatMavenDependencies $FORMAT_DEPENDENCIES_DIR $repo"_"$DOCUMENT_NAME $INTERNAL_GROUP_IDS
      echo $'\n'
    done
  fi
fi

# remove temp file from system
rm $TEMP_MESSAGES_FILE

echo "Effective POM analysis completed"
