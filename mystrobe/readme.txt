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

mvn exec:java -Dexec.args="-dst=generated -pkg=com.tvh.blcore.databeans.ecommerce.sales -prefix=DeliveryAddressInfo -daoprefix=ttdeliveryaddressinfo:DeliveryAddressInfo,ttheadcustomer:HeadCustomer Ecommerce sales.ecommerce.dtdeliveryaddressinfo"

mvn exec:java -Dexec.args="-dst=generated -server=AppServer://192.168.2.147:5162/TVH_ECOM_QUARIX_APP -pkg=com.tvh.blcore.databeans.ecommerce.sales -prefix=DeliveryAddressInfo -daoprefix=ttdeliveryaddressinfo:DeliveryAddressInfo Ecommerce sales.ecommerce.dtdeliveryaddressinfo"

mvn exec:java -Dexec.args="-dst=generated -server=AppServer://192.168.2.147:5162/TVH_ECOM_QUARIX_APP -pkg=com.tvh.blcore.databeans.ecommerce.sales -prefix=DeliveryAddressInfo -daoprefix=ttdeliveryaddressinfo:DeliveryAddressInfo,ttheadcustomer:HeadCustomer Ecommerce sales.ecommerce.dtdeliveryaddressinfo"

mvn exec:java -Dexec.args="-dst=generated -server=AppServer://192.168.2.147:5162/TVH_ECOM_QUARIX_APP -pkg=com.tvh.blcore.databeans.ecommerce.sales -prefix=TransportInfo -daoprefix=tttransportinfo:TransportInfo Ecommerce sales.ecommerce.dttransportinfo"

mvn exec:java -Dexec.args="-dst=generated -server=AppServer://192.168.2.147:5162/TVH_ECOM_QUARIX_APP -pkg=com.tvh.blcore.databeans.ecommerce.sales -prefix=TransportInfo -daoprefix=ttransportinfo:TransportInfo Ecommerce sales.ecommerce.dttransportinfo"


mvn exec:java -Dexec.args="-dst=generated -server=AppServer://192.168.2.147:5162/TVH_ECOM_QUARIX_APP -pkg=com.tvh.blcore.databeans.ecommerce.sales.customer -prefix=AffiliateInfo -daoprefix=ttaffiliateinfo:AffiliateInfo Ecommerce sales.customer.dtaffiliateinfo"

mvn exec:java -Dexec.args="-dst=generated -server=AppServer://192.168.2.147:5162/TVH_ECOM_QUARIX_APP -pkg=com.tvh.blcore.databeans.ecommerce.salesbasket -prefix=SalesBasket -daoprefix=ttsalesbasket:SalesBasket Ecommerce sales.salesbasket.dtsalesbasket"



mvn exec:java -Dexec.args="-dst=generated -server=AppServer://192.168.2.147:5162/TVH_ECOM_QUARIX_APP -pkg=com.tvh.website.ecommerce.generated.order -prefix=OrderHeadInfo -daoprefix=ttordheadinfo:OrderHeadInfo Ecommerce sales.order.dtordheadinfo"

mvn exec:java -Dexec.args="-dst=generated -server=AppServer://192.168.2.147:5162/TVH_ECOM_QUARIX_APP -pkg=com.tvh.website.ecommerce.generated.webuser -prefix=WebUserPerAffiliate -daoprefix=ttwebuserperaffiliate:WebUserPerAffiliate Ecommerce sales.ecommerce.dtwebuserperaffiliate"



mvn exec:java -Dexec.args="-dst=generated -server=AppServer://192.168.2.147:5162/TVH_ECOM_QUARIX_APP -pkg=com.tvh.website.ecommerce.generated.user -prefix=CustomerInfo -daoprefix=ttcustcontpersinfo:CustomerInfo Ecommerce sales.customer.dtcustcontpersinfo"


mvn exec:java -Dexec.args="-dst=generated -server=AppServer://192.168.2.147:5162/TVH_ECOM_QUARIX_APP -pkg=com.tvh.website.ecommerce.generated.affiliate -prefix=AffiliateAccess -daoprefix=ttaffiliateaccess:AffiliateAccess Ecommerce sales.ecommerce.dtaffiliateaccess"

mvn exec:java -Dexec.args="-dst=generated -server=AppServer://192.168.2.147:5162/TVH_ECOM_QUARIX_APP -pkg=com.tvh.website.ecommerce.generated.make -prefix=EcommerceMake -daoprefix=ttecommercemake:EcommerceMake Ecommerce sales.ecommerce.dtecommercemake"
mvn exec:java -Dexec.args="-dst=generated -server=AppServer://192.168.2.147:5162/TVH_ECOM_QUARIX_APP -pkg=com.tvh.website.ecommerce.generated.settings -prefix=AffiliateAccess -daoprefix=ttaffiliateaccess:AffiliateAccess Ecommerce sales.ecommerce.dtaffiliateaccess"
mvn exec:java -Dexec.args="-dst=generated -server=AppServer://192.168.2.147:5162/TVH_ECOM_QUARIX_APP -pkg=com.tvh.website.ecommerce.generated.settings -prefix=AffiliateAccess -daoprefix=ttaffiliateaccess:AffiliateAccess Ecommerce sales.ecommerce.dtaffiliateaccess"

mvn exec:java -Dexec.args="-dst=generated -server=AppServer://192.168.2.147:5162/TVH_ECOM_QUARIX_APP -pkg=com.tvh.website.ecommerce.generated.settings -prefix=AffiliateInfo -daoprefix=ttaffiliateinfo:AffiliateInfo Ecommerce sales.customer.dtaffiliateinfo"

mvn exec:java -Dexec.args="-dst=generated -server=AppServer://192.168.2.147:5162/TVH_ECOM_QUARIX_APP -pkg=com.tvh.website.ecommerce.generated.user -prefix=CustomerInfo -daoprefix=ttcustcontpersinfo:CustomerInfo Ecommerce sales.customer.dtcustcontpersinfo"

ttWebUserAffiliateinfo
ttordlineinfo <- sales.order.dtordlineinfo
mvn exec:java -Dexec.args="-dst=generated -server=AppServer://192.168.2.147:5162/TVH_ECOM_QUARIX_APP -pkg=com.tvh.website.ecommerce.generated.order -prefix=OrderLineInfo -daoprefix=ttordlineinfo:OrderLineInfo Ecommerce sales.order.dtordlineinfo"



mvn exec:java -Dexec.args="-dst=generated -server=AppServer://tvh:5162/TVH_ECOM_QUARIX_APP -pkg=com.tvh.website.ecommerce.generated.inquiry -prefix=InquiryLineInfo -daoprefix=ttinqlineinfo:InquiryLineInfo Ecommerce sales.ecommerce.dtinqlineinfo"
                                                                                                                                                          dtordpdf
mvn exec:java -Dexec.args="-dst=generated -server=AppServer://tvh:5162/TVH_ECOM_QUARIX_APP -pkg=com.tvh.website.ecommerce.generated.pdf -prefix=PdfFile -daoprefix=ttfile:PdfFile Ecommerce sales.ecommerce.dtordpdf"

mvn exec:java -Dexec.args="-dst=generated -server=AppServer://tvh:5162/TVH_ECOM_QUARIX_APP -pkg=com.tvh.website.ecommerce.generated.lastminute -prefix=LastMinuteOrder -daoprefix=ttlastminuteinfo:LastMinuteOrder Ecommerce sales.ecommerce.dtlastminuteorder"

mvn exec:java -Dexec.args="-dst=generated -server=AppServer://tvh:5162/TVH_ECOM_QUARIX_APP -pkg=com.tvh.website.ecommerce.generated.lastminute -prefix=LastMinuteInquiry -daoprefix=ttInquiryLastMinute:LastMinuteInquiry Ecommerce sales.ecommerce.dtinquirylastminutecheck"
mvn exec:java -Dexec.args="-dst=generated -server=AppServer://tvh:5162/TVH_ECOM_QUARIX_APP -pkg=com -prefix=WebUserAux -daoprefix=ttwebuseraux:WebUserAux Ecommerce sales.ecommerce.dtwebuserauth"


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
                  472279 549 