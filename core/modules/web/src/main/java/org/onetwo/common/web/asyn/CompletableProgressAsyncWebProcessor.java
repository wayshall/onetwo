package org.onetwo.common.web.asyn;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;

/**
 * 使用jdk8的CompletableFeture
 * @author wayshall
 * <br/>
 */
public class CompletableProgressAsyncWebProcessor extends DefaultProgressAsyncWebProcessor {

	private List<CompletableFuture<Void>> futures = new ArrayList<>();
	private List<String> messages = Lists.newArrayList();
	private volatile AtomicInteger doneTaskCount = new AtomicInteger(0);

	public CompletableProgressAsyncWebProcessor(PrintWriter out,
			AsyncMessageHolder holder, AsyncTaskExecutor asyncTaskExecutor,
			String asynCallback) {
		super(out, holder, asyncTaskExecutor, asynCallback);
	}

	@Override
	protected void handleTask(boolean closeWriter, AsyncTask task){
		Assert.notNull(task, "task can not be null");
		CompletableFuture<Void> future = CompletableFuture.runAsync(task, this.asyncTaskExecutor)
						.thenAccept(f->{
							flushAndClearTunnelMessage();
							doAfterTaskCompleted(false, task);
//							System.out.println("doneTaskCount:"+doneTaskCount.get());
							flushProgressingMessage(getTaskProcessPercent(doneTaskCount.get()), task);
							doneTaskCount.getAndIncrement();
						});
		futures.add(future);
	}
	

	public <T> void handleList(List<T> datas, int dataCountPerTask, ProgressAsyncTaskCreator<T> creator){
		super.handleList(datas, dataCountPerTask, creator);
		
		CompletableFuture<Void> allFeture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
		while(!allFeture.isDone()){
			sleep();
			flushAndClearTunnelMessage();
		}
		
		flushAndClearTunnelMessage();
		doAfterTaskCompleted(true, null);
//		logger.info("message:\n {}", messages);
		futures.clear();
		messages.clear();
	}

	protected void doAfterAddTask(AsyncTask task){
		//do nothing
	}
	
	public void flushMessage(String content) {
		messages.add(content);
		super.flushMessage(content);
	}
}
