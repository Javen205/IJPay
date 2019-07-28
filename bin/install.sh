#!/bin/bash

#--------------------------------------
# thanks to huTool
#--------------------------------------

mvn clean  source:jar javadoc:javadoc install -Dmaven.test.skip=false -Dmaven.javadoc.skip=false