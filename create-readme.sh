#!/usr/bin/env bash

version=`cat src/bundle/pm/project.properties |grep "mvn.version"|sed -e 's/mvn.version=//'`

files="
	src/main/asciidoc/overview-start.adoc
	src/site/asciidoc/resources.adoc
	src/site/asciidoc/installation.adoc
	src/site/asciidoc/features.adoc
	src/site/asciidoc/concepts.adoc
	src/site/asciidoc/concepts/01-application.adoc
	src/site/asciidoc/concepts/02-input.adoc
	src/site/asciidoc/concepts/03-target.adoc
	src/site/asciidoc/concepts/04-conversion-options.adoc
	src/site/asciidoc/concepts/05-report-options.adoc
	src/site/asciidoc/getting-started.adoc
"

output_file=src/bundle/doc/README.adoc

echo > ${output_file}
echo ":release-version: ${version}" >> ${output_file}
cat src/bundle/doc-fragments/title.adoc >>${output_file}
echo "" >> ${output_file}

for file in ${files}
do
	cat $file >> ${output_file}
done
echo "" >> ${output_file}

#cat $output_file
