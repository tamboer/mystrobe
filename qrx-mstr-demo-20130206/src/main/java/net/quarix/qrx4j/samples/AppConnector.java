package net.quarix.qrx4j.samples;


import net.mystrobe.client.connector.Config;
import net.mystrobe.client.connector.IAppConnector;
import net.mystrobe.client.connector.IConfig;
import net.mystrobe.client.connector.IServerConnector;
import net.mystrobe.client.connector.QuarixServerConnector;

public class AppConnector {
	
	private static IServerConnector srvConnector;
	
	private static boolean connectorInitialized = false; 
	
	public static IAppConnector getInstance(){
		
		if(srvConnector == null){
			// Set up the server connection
			initSrvConnector();
		}
		return srvConnector.getAppConnector(Qrx4jSampleApplication.getAppName());
	}
	
	private static synchronized void initSrvConnector() {
		if (!connectorInitialized) {
			
			Config Config = new Config();
	        Config.setValue(IConfig.APP_SERVER_URL, Qrx4jSampleApplication.getAppSereverURL());
	        
	        srvConnector  = new QuarixServerConnector(Config);
			connectorInitialized = true;
		}
	}
}
