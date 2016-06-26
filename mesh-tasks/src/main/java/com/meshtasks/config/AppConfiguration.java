package com.meshtasks.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 * This Singleton class will read configuration and provides that configuration related data in application.
 * @author Dipeshkumar mistry
 *
 */
public class AppConfiguration {

	private static AppConfiguration applicationConfiguration = new AppConfiguration();
	
	private Properties properties = new Properties();
	
	private String applicationMode = null;
	
	private int socketPort = 0;
	
	private AppConfiguration() {
		loadConfiguration();
	}
	
	public static AppConfiguration getInstance() {
		return applicationConfiguration;
	}
	
	/**
	 * Method will read configuration file and wrap it in {@link Properties} object.
	 * @return {@link Properties}
	 */
	private void loadConfiguration() {
		InputStream inStream = null;
		try {
			inStream = AppConfiguration.class.getClassLoader().getResourceAsStream("configuration.properties");
			if ( inStream == null ) return;
			properties.load(inStream);
		} catch(IOException ioe) {
			ioe.printStackTrace();
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

	public String getApplicationMode() {
		return applicationMode;
	}

	public void setApplicationMode(String applicationMode) {
		this.applicationMode = applicationMode;
	}

	public int getSocketPort() {
		return socketPort;
	}

	public void setSocketPort(int socketPort) {
		this.socketPort = socketPort;
	}
}