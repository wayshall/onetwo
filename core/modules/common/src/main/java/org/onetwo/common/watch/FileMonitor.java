package org.onetwo.common.watch;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.onetwo.common.log.MyLoggerFactory;
import org.slf4j.Logger;


@SuppressWarnings("rawtypes")
public class FileMonitor {
	protected final Logger logger = MyLoggerFactory.getLogger(FileMonitor.class);
	// private static final FileMonitor instance = new FileMonitor();
	private Timer timer;
	private Map timerEntries;
	private final long delay = 3;//seconds

	public FileMonitor() {
		this.timerEntries = new HashMap();
		this.timer = new Timer(true);
	}

	/*
	 * public static FileMonitor getInstance() { return instance; }
	 */
	

	public void addFileChangeListener(FileChangeListener listener, File file, long period) {
		addFileChangeListener(listener, file, period, delay);
	}

	/*********
	 * 
	 * @param listener
	 * @param file
	 * @param period timeunit second
	 * @param delay timeunit second
	 */
	@SuppressWarnings("unchecked")
	public void addFileChangeListener(FileChangeListener listener, File file, long period, long delay) {
		this.removeFileChangeListener(file.getName());

		logger.info("Watching " + file.getName());

		FileMonitorTask task = new FileMonitorTask(listener, file);

		this.timerEntries.put(file.getName(), task);
		this.timer.schedule(task, TimeUnit.SECONDS.toMillis(delay), TimeUnit.SECONDS.toMillis(period));
	}

	/**
	 * Stop watching a file
	 * 
	 * @param listener
	 *            The file listener
	 * @param filename
	 *            The filename to keep watch
	 */
	public void removeFileChangeListener(String filename) {
		FileMonitorTask task = (FileMonitorTask) this.timerEntries
				.remove(filename);

		if (task != null) {
			task.cancel();
		}
	}

	private static class FileMonitorTask extends TimerTask {
		private FileChangeListener listener;
		private File monitoredFile;
		private long lastModified;

		public FileMonitorTask(FileChangeListener listener, String fileName) {
			this(listener, new File(fileName));
		}

		public FileMonitorTask(FileChangeListener listener, File file) {
			this.listener = listener;
			this.monitoredFile = file;
			if (!this.monitoredFile.exists()) {
				return;
			}

			this.lastModified = this.monitoredFile.lastModified();
		}

		public void run() {
			long latestChange = this.monitoredFile.lastModified();
			if (this.lastModified != latestChange) {
				this.lastModified = latestChange;

				this.listener.fileChanged(this.monitoredFile);
			}
		}
	}
	
	public static void main(String[] args){
		System.out.println(TimeUnit.SECONDS.toMillis(3));
	}
}
