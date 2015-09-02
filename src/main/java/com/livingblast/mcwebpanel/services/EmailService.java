package com.livingblast.mcwebpanel.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.livingblast.mcwebpanel.utils.MustacheTokenUtil;

public class EmailService {

	private static Logger logger = LogManager.getLogger(EmailService.class);
	private static boolean isInit = false;

	// stores the last time the player joined and the last time they left
	private static Map<String, Map<NotificationEvent, Date>> playerEventData;

	// Get system properties
	private static Properties properties = System.getProperties();
	// Assuming you are sending email from localhost
	private static String host = PropertyService.instance().getPropsConfig().getString("smtp.address");
	private static String username = PropertyService.instance().getPropsConfig().getString("email.username");
	private static String password = PropertyService.instance().getPropsConfig().getString("email.password");
	private static String to = PropertyService.instance().getPropsConfig().getString("email.to.address");
	private static String from = PropertyService.instance().getPropsConfig().getString("email.from.address");

	public static void init() {
		// Setup mail server
		properties.setProperty("mail.smtp.host", host);
		isInit = true;
	}

	public static synchronized void sendNotification(String playerName, NotificationEvent event, Date time) {

		if (isInit == false) {
			init();
		}

		logger.info("Received notice about " + playerName + ", " + event.name());

		boolean shouldSend = determineShouldSend(playerName, event, time);

		if (shouldSend) {

			// Get the default Session object.
			Session session = Session.getDefaultInstance(properties);

			try {
				// Create a default MimeMessage object.
				MimeMessage message = new MimeMessage(session);

				// Set From: header field of the header.
				message.setFrom(new InternetAddress(from));

				// Set To: header field of the header.
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

				String subject = PropertyService.instance().getPropsConfig().getString("email.subject.joined");
				String body = PropertyService.instance().getPropsConfig().getString("email.body.joined");

				switch (event) {
				case LEFT:
					subject = PropertyService.instance().getPropsConfig().getString("email.subject.left");
					body = PropertyService.instance().getPropsConfig().getString("email.body.left");
					break;
				default:
					break;
				}

				subject = MustacheTokenUtil.replaceTokens(subject, playerName);
				body = MustacheTokenUtil.replaceTokens(body, playerName, time.toString());

				message.setSubject(subject);

				message.setText(body);

				logger.debug("Sending email saying " + body);
				Transport.send(message, username, password);

				logger.info("Sent message successfully....");
			} catch (MessagingException mex) {
				final String msg = mex.getMessage();
				logger.error("Error sending email: " + msg);
			}
		}
	}

	private static boolean determineShouldSend(String playerName, NotificationEvent event, Date time) {

		boolean retval = false;

		if (playerEventData == null) {
			playerEventData = new HashMap<String, Map<NotificationEvent, Date>>();
		}

		long ignoreEventsInterval = PropertyService.instance().getPropsConfig().getLong("ignore.events.interval");

		Map<NotificationEvent, Date> lastEvents = playerEventData.get(playerName);
		if (lastEvents == null) {
			lastEvents = new HashMap<NotificationEvent, Date>();
		}

		if (event.equals(NotificationEvent.LEFT)) {
			// if they are leaving, don't notify, just store the event
			retval = false;
		} else {
			// if they are joining, find out how long since the last time
			// they left
			// because if they rejoin quickly, then we don't want an email
			Date lastLeft = lastEvents.get(NotificationEvent.LEFT);
			if (lastLeft != null) {
				long diff = time.getTime() - lastLeft.getTime();
				if (diff > ignoreEventsInterval) {
					retval = true;
				}
			} else {
				retval = true;
			}
		}

		// always store the event no matter what retval
		lastEvents.put(event, time);
		playerEventData.put(playerName, lastEvents);

		return retval;
	}
}
