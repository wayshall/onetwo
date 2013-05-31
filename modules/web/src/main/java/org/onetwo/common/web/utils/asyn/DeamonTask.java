package org.onetwo.common.web.utils.asyn;

/********
 * 使用新的 {@link org.onetwo.common.web.asyn2.DeamonTask DeamonTask} 代替
 * @see org.onetwo.common.web.asyn2.DeamonTask
 * @author wayshall
 *
 * @param <T>
 */
@Deprecated
abstract public class DeamonTask<T> {
	
	private String name;
//	private Tasker<T> tasker;
	private int threadPriority = Thread.NORM_PRIORITY;
	
	private boolean finished;
	private T result;
	private Exception exception;
	
	private int taskIndex;
	
	
	public DeamonTask(String name, int taskIndex) {
		super();
		this.name = name;
		this.taskIndex = taskIndex;
//		this.tasker = tasker;
	}


	public void start(){
		try {
			final Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						result = execute();
					} catch (Exception e) {
						exception = e;
					}
					finished = true;
				}
				
			});
			t.setName(name+"#"+taskIndex);
			t.setPriority(threadPriority);
			t.start();
		} catch (Exception e) {
			this.exception = e;
		}
	}
	
	abstract public T execute() throws Exception; 

	public String getName() {
		return name;
	}

	public boolean isFinished() {
		return finished;
	}

	public T getResult() {
		return result;
	}

	public Exception getException() {
		return exception;
	}
	
	public boolean isError(){
		return getException()!=null;
	}

	public int getTaskIndex() {
		return taskIndex;
	}

}
