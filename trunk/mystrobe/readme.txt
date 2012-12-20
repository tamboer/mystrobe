##########  DATA OBJECTS SOURCES GENERATION ############

## For file generation use folowing maven command.
## Do not forget to set appropriate command arguments.

## Before launching sources generator do a 'mvn clean' and 'mvn insatll' to
##  use latest framework sources.
## 
## Example below only sets some of the required parameters.
## Examples:

mvn exec:java -Dexec.args="-dst=generated ProdSearch server.dmprodscreenfield"

mvn exec:java -Dexec.args="-dst=generated -pkg=com.tvh.prodsearch.databeans -prefix=ProdScreen -daoprefix=ttprodscreenfield:Test ProdSearch server.dmprodscreenfield"

mvn exec:java -Dexec.args="-dst=generated -pkg=com.tvh.prodsearch.databeans -prefix=Property -daoprefix=ttprodproperty:Property,ttprodpropertygroup:PropertyGroup,ttpropdescr:PropertyDescription ProdSearch server.dmprodproperty"

mvn exec:java -Dexec.args="-dst=generated -pkg=com.tvh.prodsearch.databeans -sourcesFolder=sources ProdSearch server.dmprodscreen"

## Available paramaters:
## -dst=<dedtination folder> Destination folder path, relative to current path.
## -pkg=<package name> Generated package name.
## -user=<app server user> Destination folder path, relative to current path.
## -password=<app server password> Destination folder path, relative to current path.
## -server=<app server> Application server URL (AppServer://192.168.2.147:5162/TVH_DEV_PRODSEARCH).
## -sourcesFolder=<app server> Existing generated sources folder. 
##		Use it when any user code was added to previously generated sources and you want to pass(keep) it in the new sources.

## Last two parameters must be the business logic application(has to be penultimate parameter in the command) and 
##	the data set(has to be last parameter in the command) name. If any of the two is missing you will get an error message.

## Command execution log messages can be viewd in the console or in the classGeneration.log file.   
                  