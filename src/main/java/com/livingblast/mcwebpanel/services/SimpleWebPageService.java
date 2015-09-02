package com.livingblast.mcwebpanel.services;

import java.util.Date;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Use this class to create a simple div tag inside an html page so visitors can
 * check the web page to see who's on.
 * NOTE this class isn't finished yet so feel free to contribute or wait for updates.
 * 
 * @author jim
 *
 */
public class SimpleWebPageService {

	private static Logger logger = LogManager.getLogger(SimpleWebPageService.class);
	private static boolean isInit = false;

	// Get system properties
	private static Properties properties = System.getProperties();

	// Assuming you are sending email from localhost

	public static void init() {
		isInit = true;
	}

	public static synchronized void sendNotification(String playerName, NotificationEvent event, Date time) {

		if (isInit == false) {
			init();
		}

		logger.info("Received notice about " + playerName + ", " + event.name());

		boolean shouldSend = determineShouldSend(playerName, event, time);

		if (shouldSend) {

			switch (event) {
			case LEFT:
				break;
			default:
				break;
			}

		}
	}

	private static boolean determineShouldSend(String playerName, NotificationEvent event, Date time) {
		boolean retval = PropertyService.instance().getPropsConfig().getBoolean("enable.web.page", false);
		return retval;
	}
}
