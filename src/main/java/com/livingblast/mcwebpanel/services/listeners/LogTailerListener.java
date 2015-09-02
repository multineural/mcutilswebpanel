package com.livingblast.mcwebpanel.services.listeners;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.input.TailerListenerAdapter;

import com.livingblast.mcwebpanel.services.EmailService;
import com.livingblast.mcwebpanel.services.NotificationEvent;
import com.livingblast.mcwebpanel.services.PropertyService;

public class LogTailerListener extends TailerListenerAdapter {

	private static String joinPhrase = PropertyService.instance().getPropsConfig().getString("joined.phrase");
	private static String leftPhrase = PropertyService.instance().getPropsConfig().getString("left.phrase");
	private static String validNamePrefix = PropertyService.instance().getPropsConfig().getString("valid.prefix");
	private static String invalidNamePrefix = PropertyService.instance().getPropsConfig().getString("invalid.prefix");

	private static List<Object> players = PropertyService.instance().getPropsConfig().getList("email.when.player");

	@Override
	public void fileNotFound() {
		// TODO Auto-generated method stub
		super.fileNotFound();
	}

	@Override
	public void fileRotated() {
		// TODO Auto-generated method stub
		super.fileRotated();
	}

	@Override
	public void handle(Exception ex) {
		throw new RuntimeException(ex);
	}

	@Override
	public void handle(String line) {

		Date time = Calendar.getInstance().getTime();

		if (line.contains(joinPhrase)) {
			String playerName = extractPlayerName(line);
			boolean isValid = validateLine(playerName, line);
			if (isValid) {
				EmailService.sendNotification(extractPlayerName(line), NotificationEvent.JOINED, time);
			}
		} else if (line.contains(leftPhrase)) {
			String playerName = extractPlayerName(line);
			boolean isValid = validateLine(playerName, line);
			if (isValid) {
				EmailService.sendNotification(extractPlayerName(line), NotificationEvent.LEFT, time);
			}
		}

	}

	private boolean validateLine(String playerName, String line) {
		boolean retval = (playerName.isEmpty() == false && line.contains(invalidNamePrefix + " " + playerName) == false && line
				.contains(validNamePrefix + " " + playerName) == true);
		return retval;
	}

	private String extractPlayerName(String line) {
		String retval = "";
		for (Object player : players) {
			String playerName = (String) player;
			int found = line.indexOf(playerName);
			if (found > 0) {
				retval = line.substring(found, found + playerName.length());
				break;
			}

		}
		return retval;
	}
}