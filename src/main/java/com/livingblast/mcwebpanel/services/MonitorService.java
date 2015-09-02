package com.livingblast.mcwebpanel.services;

import java.io.File;

import org.apache.commons.io.input.Tailer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.livingblast.mcwebpanel.services.listeners.LogTailerListener;

public class MonitorService {

	static Logger logger = LogManager.getLogger(MonitorService.class);
	private static MonitorService instance = new MonitorService();

	private MonitorService() {

	}

	public static MonitorService instance() {
		return instance;
	}

	public void monitorFile(String path) {

		File logFile = new File(path);
		// LogTailerListener is where all the notification logic is
		LogTailerListener listener = new LogTailerListener();
		long pollingDelay = PropertyService.instance().getPropsConfig().getLong("poll.interval");
		if (pollingDelay < 500)
			pollingDelay = 500;

		Tailer tailer = new Tailer(logFile, listener, pollingDelay, true);

		doThreading(tailer);
	}

	private void doThreading(Tailer tailer) {

		Thread monitorThread = new Thread(tailer);
		monitorThread.start();

	}
	

}
