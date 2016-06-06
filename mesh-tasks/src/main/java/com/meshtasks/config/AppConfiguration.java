package com.meshtasks.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.meshtasks.utils.CommonUtils;
/**
 * This Singleton class will read configuration and provides that configuration related data in application.
 * @author indmistry
 *
 */
public class AppConfiguration {

	private static AppConfiguration applicationConfiguration;
	
	private Properties properties = null;
	
	private AppConfiguration() {
		loadConfiguration();
	}
	
	public static AppConfiguration getInstance() {
		if (applicationConfiguration == null) {
			synchronized (AppConfiguration.class) {
				if (applicationConfiguration == null) {
					applicationConfiguration = new AppConfiguration();
				}
			}
		}
		return applicationConfiguration;
	}
	
	/**
	 * Method will read configuration file and wrap it in {@link Properties} object.
	 * @return {@link Properties}
	 */
	private void loadConfiguration() {
		InputStream inStream = null;
		try {
			inStream = CommonUtils.class.getClassLoader().getResourceAsStream("configuration.properties");
			if ( inStream == null ) return;
			Properties properties = new Properties();
			properties.load(inStream);
		} catch(IOException ioe) {
		} finally {
			try {
				if ( inStream != null)
					inStream.close();
			} catch(IOException ioe){}
		}
	}
	
	public String getProperty(String key) {
		return properties.getProperty(key);
	}
	
	public boolean isConfigLoaded() {
		return properties != null;
	}
}