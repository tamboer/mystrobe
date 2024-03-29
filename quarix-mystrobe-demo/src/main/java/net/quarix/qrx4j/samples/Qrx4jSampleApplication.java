package net.quarix.qrx4j.samples;

import net.mystrobe.client.connector.Config;
import net.mystrobe.client.connector.IConfig;
import org.apache.wicket.protocol.http.WebApplication;

/**
 * Application object for your web application. If you want to run this
 * application without deploying, run the Start class.
 *
 * @see net.quarix.qrx4j.samples.Start#main(String[])
 */
public class Qrx4jSampleApplication extends WebApplication {

    private static String APP_SERVER_URL_PARAM = "ApplicationServerURL";
    private static String APP_NAME_PARAM = "AppName";
    private static String appServerURL;
    private static String appName;
    private Config mystrobeConfig;

    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<HomePage> getHomePage() {
        return HomePage.class;
    }

    /**
     * @see org.apache.wicket.Application#init()
     */
    @Override
    public void init() {
        super.init();

        getMarkupSettings().setStripWicketTags(true);
        // for the special symbols (like '�')
        getMarkupSettings().setDefaultMarkupEncoding("UTF-8");

        getDebugSettings().setOutputComponentPath(true);

        getDebugSettings().setAjaxDebugModeEnabled(true);

        //app server configuration parameters 
        appServerURL = getServletContext().getInitParameter(APP_SERVER_URL_PARAM);

        appName = getServletContext().getInitParameter(APP_NAME_PARAM);
    }

    public IConfig getMystrobeConfig() {
        if (mystrobeConfig == null) {
            mystrobeConfig = new Config();
            mystrobeConfig.setValue(IConfig.APP_SERVER_URL, appServerURL);
        }
        return mystrobeConfig;
    }

    public String getAppName() {
        return appName;
    }
}
