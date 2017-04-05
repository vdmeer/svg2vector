#!/usr/bin/env bash

java -classpath "../lib/java/*" de.vandermeer.execs.ExecS gen-configure --property-file ../etc/configuration.properties > configure.sh
sed -i 's/\r//' configure.sh
chmod 755 configure.sh
./configure.sh -c
