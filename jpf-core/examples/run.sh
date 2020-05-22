#!/bin/bash
java -classpath ../build/jpf.jar gov.nasa.jpf.JPF +classpath=.:./groovy-2.5.7 $1
