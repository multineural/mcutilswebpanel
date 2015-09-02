package com.livingblast.mcwebpanel.services;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PropertyService {

	static final Logger logger = LogManager.getLogger(PropertyService.class);
	
	private static PropertyService instance;
	private static boolean isInit = false;

	PropertiesConfiguration propsConfig;

	public static void init(String fileName) throws ConfigurationException {
		synchronized (PropertyService.class) {
			if (isInit == true) {
				throw new UnsupportedOperationException("Already initialized, hacker!");
			} else {
				instance = new PropertyService(fileName);
				isInit = true;
				logger.debug("Initialized PropertyService: " + fileName);
			}
		}
	}

	public static PropertyService instance() {

		if (isInit) { // the fastest way to return the instance well after
						// initialized.
			return instance;
		} else {
			synchronized (PropertyService.class) {
				if (isInit == false) { // check it again because another thread
										// might have been in here
					throw new UnsupportedOperationException("Must call the init method first.");
				} else {
					return instance;
				}
			}
		}
	}

	private PropertyService(String fileName) throws ConfigurationException {
		propsConfig = new PropertiesConfiguration(fileName);
	}

	public PropertiesConfiguration getPropsConfig() {
		return propsConfig;
	}
}
