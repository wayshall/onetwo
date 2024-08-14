package org.onetwo.boot.utils;
/**
 * @author weishao zeng
 * <br/>
 */

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

public class BatchTaskInfoTest {
	
	@Test
	public void testSplit() {
		List<BatchTaskInfo<Object>> taskList = BatchTaskInfo.splitTasks(49519, 1000, 10);
		taskList.forEach(t -> System.out.println(t));
		assertThat(taskList.size()).isEqualTo(5);
		BatchTaskInfo<Object> last = taskList.get(taskList.size()-1);
		assertThat(last.get(last.size()-1).getActualTaskSize()).isEqualTo(519);
		
		System.out.println("-----------");

		taskList = BatchTaskInfo.splitTasks(49519, 1000, 5);
		taskList.forEach(t -> System.out.println(t));
		assertThat(taskList.size()).isEqualTo(10);
		last = taskList.get(taskList.size()-1);
		assertThat(last.get(last.size()-1).getActualTaskSize()).isEqualTo(519);
		
		System.out.println("-----------");

		taskList = BatchTaskInfo.splitTasks(49519, 1000, 1);
		taskList.forEach(t -> System.out.println(t));
		assertThat(taskList.size()).isEqualTo(50);
		last = taskList.get(taskList.size()-1);
		assertThat(last.get(last.size()-1).getActualTaskSize()).isEqualTo(519);
	}

}
