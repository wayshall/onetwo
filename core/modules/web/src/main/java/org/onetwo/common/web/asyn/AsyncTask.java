package org.onetwo.common.web.asyn;




abstract public class AsyncTask implements Runnable {

	
	private String name = "AsyncTask";
//	private Tasker<T> tasker;
//	private int threadPriority = Thread.NORM_PRIORITY;
	
//	private boolean finished;
	private Exception exception;
	
	private int dataCount = 0;
	
	public AsyncTask(String name, int dataCount) {
		super();
		this.name = name;
		this.dataCount = dataCount;
//		this.tasker = tasker;
	}
	

/*
	public void start(){
		try {

			final Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						execute();
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
	}*/
	
	@Override
	final public void run() {
		try {
			execute();
		} catch (Exception e) {
			exception = e;
		}
//		finished = true;
	}



	abstract public void execute() throws Exception; 

	public String getName() {
		return name;
	}

	/*public boolean isFinished() {
		return finished;
	}*/

	public Exception getException() {
		return exception;
	}
	
	public boolean isError(){
		return getException()!=null;
	}

	public int getDataCount() {
		return dataCount;
	}

}
