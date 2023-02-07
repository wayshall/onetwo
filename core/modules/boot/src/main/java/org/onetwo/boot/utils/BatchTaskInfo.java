package org.onetwo.boot.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.onetwo.boot.utils.BatchTaskInfo.TaskInfo;
import org.onetwo.common.exception.BaseException;

import com.google.common.collect.Lists;

import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */
@SuppressWarnings("serial")
public class BatchTaskInfo<T> extends ArrayList<TaskInfo> {

	public static <R> List<BatchTaskInfo<R>> splitTasks(int total, int taskSize, int concurrentTaskSize) {
		if (total<=0) {
			throw new IllegalArgumentException("tatal can be less than 0");
		}
		int taskCount = total / taskSize;
		int lastTaskSize = total % taskSize;
		if ( lastTaskSize!= 0) {
			taskCount = taskCount + 1;
		}
		
		List<BatchTaskInfo<R>> batchTaskList = Lists.newArrayList();
		BatchTaskInfo<R> taskList  = new BatchTaskInfo<>(concurrentTaskSize);
		batchTaskList.add(taskList);
		
		int batchIndex = 0;
		taskList.setBatchIndex(batchIndex);
		for (int i = 0; i < taskCount; i++) {
			TaskInfo task = new TaskInfo();
			task.setTaskIndex(i);
			task.setTaskSize(taskSize);
			if (lastTaskSize> 0 && i==taskCount-1) {
				task.setTaskSize(lastTaskSize);
			}
			
			if (taskList.size() < concurrentTaskSize) {
				taskList.add(task);
			} else {
				batchIndex++;
				taskList = new BatchTaskInfo<>(concurrentTaskSize);
				taskList.setBatchIndex(batchIndex);
				batchTaskList.add(taskList);
				taskList.add(task);
			}
		}
		return batchTaskList;
	}
	
	private int batchIndex;
	
	public BatchTaskInfo() {
		super();
	}

	public BatchTaskInfo(int initialCapacity) {
		super(initialCapacity);
	}
	
	public List<T> getAll(Function<TaskInfo, CompletableFuture<T>> taskActioin) {
		List<CompletableFuture<T>> futures = stream().map(one -> {
        	CompletableFuture<T> f = taskActioin.apply(one);
        	return f;
        }).collect(Collectors.toList());
        CompletableFuture<Void> allResult = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        try {
        	// 等待所有计算结束
			allResult.get();
		} catch (Exception e) {
            throw new BaseException("execute batch task error: " + e.getMessage(), e);
		}
        List<T> resultList = futures.stream().map(f -> {
        	try {
    			return f.get();
    		} catch (Exception e) {
                throw new BaseException("get future error: " + e.getMessage(), e);
    		}
        }).collect(Collectors.toList());
        return resultList;
	}

	public int getBatchIndex() {
		return batchIndex;
	}

	private void setBatchIndex(int batchIndex) {
		this.batchIndex = batchIndex;
	}

	@Data
	static public class TaskInfo {
		
		int taskIndex;
		int taskSize;
		@Override
		public String toString() {
			return "(taskIndex=" + taskIndex + ", taskSize=" + taskSize + ")";
		}
	}

}