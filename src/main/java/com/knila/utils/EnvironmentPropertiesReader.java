package com.knila.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.testng.log4testng.Logger;

/**
 * EnvironmentPropertyReader is to set the environment variable declaration
 * mapping for config properties
 */
public class EnvironmentPropertiesReader {
	private static final Logger log = Logger.getLogger(EnvironmentPropertiesReader.class);
	private static EnvironmentPropertiesReader envProperties;
	private Properties properties;

	/**
	 * Constructor to call load properties method
	 */
	private EnvironmentPropertiesReader() {
		properties = loadProperties();
	}

	/**
	 * Load property from config properties
	 *
	 * @return property
	 */
	private Properties loadProperties() {

		Properties props = new Properties();
		try {
			InputStream cpr = EnvironmentPropertiesReader.class.getResourceAsStream("/config.properties");
			props.load(cpr);
			cpr.close();
		} catch (FileNotFoundException e) {
			log.error("config.properties is missing or corrupt : " + e.getMessage());
		} catch (IOException e) {
			log.error("read failed due to: " + e.getMessage());
		}
		return props;
	}

	/**
	 * get property instance
	 *
	 * @return property
	 */
	public static EnvironmentPropertiesReader getInstance() {
		if (envProperties == null) {
			envProperties = new EnvironmentPropertiesReader();
		}
		return envProperties;
	}

	/**
	 * Return value for provided key
	 *
	 * @param key in property
	 * @return value
	 */
	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	/**
	 * Check key is present in given property
	 *
	 * @param key for property
	 * @return boolean value
	 */
	public boolean hasProperty(String key) {
		return StringUtils.isNotBlank(properties.getProperty(key));
	}

}
