package com.livingblast.mcwebpanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.livingblast.mcwebpanel.services.EmailService;
import com.livingblast.mcwebpanel.services.MonitorService;
import com.livingblast.mcwebpanel.services.NotificationEvent;
import com.livingblast.mcwebpanel.services.PropertyService;

/**
 *
 */
public class App {

	private static Logger logger = LogManager.getLogger(App.class);

	public static void main(String[] args) {
		App app = new App();
		app.init();
	}

	public void init() {
		initializeProperties();
		monitorLogs();
	}

	private void initializeProperties() {
		String fileName = System.getProperty("propertiesLocation", "mcwebpanel.properties");
		logger.info(AppConstants.NEWLINE + ">>> mcwebpanel initialized using properties file: " + fileName);
		try {
			PropertyService.init(fileName);
			logger.info("PropertyService initialized.");
		} catch (Exception e) { // ConfigurationException
			e.printStackTrace();
			logger.info(AppConstants.NEWLINE + ">>> mcwebpanel is Running without properties. "
					+ "Please initialize with -DpropertiesLocation=[path to mcwebpanel.properties]");
			logger.error(AppConstants.NEWLINE + ">>> mcwebpanel >>> " + e.getMessage());
		}

	}

	private int monitorLogs() {
		int retval = 0;

		String path = PropertyService.instance().getPropsConfig().getString("log.file.path");
		MonitorService.instance().monitorFile(path);
		
		return retval;
	}
}
