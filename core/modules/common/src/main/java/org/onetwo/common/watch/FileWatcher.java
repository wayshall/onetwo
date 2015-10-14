package org.onetwo.common.watch;

import java.io.File;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.propconf.ResourceAdapter;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.list.JFishList;
import org.onetwo.common.utils.list.NoIndexIt;
import org.slf4j.Logger;

/********
 * 用ScheduledThreadPoolExecutor实现，代替FileMonitor
 * @author wayshall
 *
 */
public class FileWatcher {
	private static final int DEFAULT_INIT_DELAY = 3;
	private static final int DEFAULT_THREAD_COUNT = 2;
	
	public static final FileWatcher GLOBAL_WATCHER = newWatcher(DEFAULT_THREAD_COUNT);
	
	public static FileWatcher newWatcher(int threadCount){
		return new FileWatcher(threadCount, DEFAULT_INIT_DELAY);
	}

	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	private final ScheduledExecutorService executor;
	private int initDelay = DEFAULT_INIT_DELAY;
	
	private FileWatcher(){
		this(DEFAULT_THREAD_COUNT, DEFAULT_INIT_DELAY);
	}
	private FileWatcher(int threadCount, int initDelay){
		executor = new ScheduledThreadPoolExecutor(threadCount);
		this.initDelay = initDelay;
	}

	public void watchFile(FileChangeListener listener, ResourceAdapter<?>...files){
		watchFile(1, listener, files);
	}
	public void watchFile(long periodSecond, FileChangeListener listener, ResourceAdapter<?>...files){
		Assert.notEmpty(files);
		WatchFileTask task = new WatchFileTask(files, listener);
		addFileTask(periodSecond, task);
	}
	
	public void addFileTask(long periodSecond, WatchFileTask task){
		logger.info(task.listFileString("\n watch file : "));
		//固定延迟
		executor.scheduleWithFixedDelay(task, initDelay, periodSecond, TimeUnit.SECONDS);
	}
	
	public static class WatchFileTask implements Runnable {
		private final JFishList<FileState> fileStates;
		private final FileChangeListener listener;
		
		private WatchFileTask(ResourceAdapter<?>[] files, FileChangeListener listener) {
			super();
			fileStates = JFishList.newList(files.length);
			for(ResourceAdapter<?> file : files){
				if(file.isSupportedToFile())
					fileStates.add(new FileState(file.getFile()));
			}
			this.listener = listener;
		}

		@Override
		public void run() {
			for(FileState state : fileStates){
				if(state.isModified())
					listener.fileChanged(state.file);
			}
		}
		
		public String listFileString(final String beforeElement){
			final StringBuilder str = new StringBuilder();
			fileStates.each(new NoIndexIt<FileWatcher.FileState>() {

				@Override
				protected void doIt(FileState element) throws Exception {
					str.append(beforeElement).append(element.file.getPath());
				}
				
			});
			return str.toString();
		}
		
	}
	
	private static class FileState {
		private File file;
		private long lastModified;
		private FileState(File file) {
			super();
			this.file = file;
			this.lastModified = file.lastModified();
		}
		public boolean isModified(){
			long latestChange = this.file.lastModified();
			if (this.lastModified != latestChange) {
				this.lastModified = latestChange;
				return true;
			}
			return false;
		}
	}
}
