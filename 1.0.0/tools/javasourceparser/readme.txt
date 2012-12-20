##########  DATA OBJECTS JAVAPARSER AND LEXER GENERATION ############

## java -classpath ./antlr-3.2.jar;./antlr-runtime-3.2.jar;./stringtemplate-3.2.jar;./antlr-2.7.7.jar; org.antlr.Tool -o out Java.g

## Generate java parser and lexer sources from Java.g grammer to 'out' folder
## Copy generated sources to: src\main\java\com\tvh\wicketds\connector\quarixbackend\generate\parser\ folder unless package is changed
## To change generated sources package open the Java.g grammer file and change @header section package name.  

mvn exec:java -Dexec.args="-o out Java.g"