package net.mystrobe.client.config;

interface IMyStrobeCoreSettingsProvider {
	
	Integer getBatchSize(String appName);
	
	Boolean getCacheData(String appName);
     
}
