#!/usr/bin/env bash

version=`cat src/bundle/pm/project.properties | grep "mvn.version" | sed -e 's/mvn.version=//'`
java=`cat src/bundle/pm/project.properties | grep "mvn.properties.compiler.source" | sed -e 's/mvn.properties.compiler.source=1.//'`

files="
	src/bundle/doc-fragments/badges.adoc
	src/main/asciidoc/overview-start.adoc
	src/bundle/doc-fragments/resources.adoc
	src/bundle/doc-fragments/concepts.adoc
	src/bundle/doc-fragments/features.adoc
	src/bundle/doc-fragments/install.adoc
		src/bundle/doc-fragments/install-app.adoc
		src/bundle/doc-fragments/install-execjar.adoc
		src/bundle/doc-fragments/install-buildsys.adoc
		src/bundle/doc-fragments/install-fromsrc.adoc
	src/bundle/doc-fragments/qs.adoc
		src/bundle/doc-fragments/qs-cli.adoc
		src/bundle/doc-fragments/qs-java.adoc
	src/site/asciidoc/user-guide/cli.adoc
	src/site/asciidoc/error-codes.adoc
"

output_file=src/bundle/doc/README.adoc

echo > ${output_file}
echo ":release-version: ${version}" >> ${output_file}
echo ":java: ${java}" >> ${output_file}
cat src/bundle/doc-fragments/title.adoc >>${output_file}
echo "" >> ${output_file}

for file in ${files}
do
	cat $file >> ${output_file}
done
echo "" >> ${output_file}

#cat $output_file
