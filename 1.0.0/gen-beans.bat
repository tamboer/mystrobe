@echo off
set POC_SERVER=AppServerDC://cargomate.yonder.local:3511/qrx_rcfpg_demo
set POC_DST=./src/datatypes
set POC_PKG=net.mystrobe.client.connector.quarixbackend.datatypes
set POC_APP=wicketds

@echo on


call mvn -X exec:java -Dexec.args="-dst=%POC_DST% -pkg=%POC_PKG% -server=%POC_SERVER% %POC_APP% server.customer"
call mvn -X exec:java -Dexec.args="-dst=%POC_DST% -pkg=%POC_PKG% -server=%POC_SERVER% %POC_APP% server.salesrep"
call mvn -X exec:java -Dexec.args="-dst=%POC_DST% -pkg=%POC_PKG% -server=%POC_SERVER% %POC_APP% server.state"
