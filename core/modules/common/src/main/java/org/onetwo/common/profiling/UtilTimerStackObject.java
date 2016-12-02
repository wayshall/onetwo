package org.onetwo.common.profiling;

import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;


/****
 * from struts 2
 *
 */
public class UtilTimerStackObject{

	public static final String PROFILE_LOGGER = "time.profile.object";
    protected ThreadLocal<ProfilingTimerBean> current = new ThreadLocal<ProfilingTimerBean>();

    private boolean active = true;
    private boolean nanoTime;
    private long minTime = 0;
    
    private TimeLogger timeLogger;
    
    public static UtilTimerStackObject createObject(){
    	final Logger logger = JFishLoggerFactory.getLogger(PROFILE_LOGGER);
    	UtilTimerStackObject obj = new UtilTimerStackObject(new Slf4jTimeLogger(logger));
    	return obj;
    }
    
    public static UtilTimerStackObject createObject(TimeLogger timeLogger, long minTime){
    	UtilTimerStackObject obj = new UtilTimerStackObject(timeLogger);
    	obj.minTime = minTime;
    	return obj;
    }

    public UtilTimerStackObject(TimeLogger timeLogger) {
		super();
		this.timeLogger = timeLogger;
	}


	public TimeLogger getOuputer() {
		return timeLogger;
	}

	public void setOuputer(TimeLogger ouputer) {
		this.timeLogger = ouputer;
	}

	public void setNanoTime(boolean nanoTime) {
		this.nanoTime = nanoTime;
	}

	/**
     * Create and start a performance profiling with the <code>name</code> given. Deal with 
     * profile hierarchy automatically, so caller don't have to be concern about it.
     * 
     * @param name profile name
     */
    public void push(String name)
    {
        if (!isActive())
            return;

        //create a new timer and start it
        ProfilingTimerBean newTimer = new ProfilingTimerBean(name, nanoTime);
        newTimer.setStartTime();

        //if there is a current timer - add the new timer as a child of it
        ProfilingTimerBean currentTimer = (ProfilingTimerBean) current.get();
        if (currentTimer != null)
        {
            currentTimer.addChild(newTimer);
        }

        //set the new timer to be the current timer
        current.set(newTimer);
    }

    /**
     * End a preformance profiling with the <code>name</code> given. Deal with
     * profile hierarchy automatically, so caller don't have to be concern about it.
     * 
     * @param name profile name
     */
    public void pop(String name)
    {
        if (!isActive())
            return;

        ProfilingTimerBean currentTimer = (ProfilingTimerBean) current.get();

        //if the timers are matched up with each other (ie push("a"); pop("a"));
        if (currentTimer != null && name != null && name.equals(currentTimer.getResource()))
        {
            currentTimer.setEndTime();
            ProfilingTimerBean parent = currentTimer.getParent();
            //if we are the root timer, then print out the times
            if (parent == null)
            {
                printTimes(currentTimer);
                current.set(null); //for those servers that use thread pooling
            }
            else
            {
//                printTimes(currentTimer);
                current.set(parent);
            }
        }
        else
        {
            //if timers are not matched up, then print what we have, and then print warning.
            if (currentTimer != null)
            {
                printTimes(currentTimer);
                current.set(null); //prevent printing multiple times
                getOuputer().log(UtilTimerStack.class, "Unmatched Timer.  Was expecting " + currentTimer.getResource() + ", instead got " + name);
            }
        }


    }

    /**
     * Do a log (at INFO level) of the time taken for this particular profiling.
     * 
     * @param currentTimer profiling timer bean
     */
    private void printTimes(ProfilingTimerBean currentTimer)
    {
    	getOuputer().log(UtilTimerStack.class, currentTimer.getPrintable(getMinTime()));
    }

    /**
     * 超出这个时间间隔，才会输出日志
     * Get the min time for this profiling, it searches for a System property
     * 
     * @return long
     */
    private long getMinTime()
    {
        return this.minTime;
    }

    /**
     * Determine if profiling is being activated, by searching for a system property
     * 'xwork.profile.activate', default to false (profiling is off).
     * 
     * @return <tt>true</tt>, if active, <tt>false</tt> otherwise.
     */
    public boolean isActive()
    {
        return active;
    }

    /****
     * @see #active()
     * @param active
     */
    public void setActive(boolean active) {
    	active(active);
    }
    /**
     * Turn profiling on or off.
     * 
     * @param active
     */
    public void active(boolean active) {
        this.active = active; 
    }


    /**
     * A convenience method that allows <code>block</code> of code subjected to profiling to be executed 
     * and avoid the need of coding boiler code that does pushing (UtilTimeBean.push(...)) and 
     * poping (UtilTimerBean.pop(...)) in a try ... finally ... block.
     * 
     * <p/>
     * 
     * Example of usage:
     * <pre>
     * 	 // we need a returning result
     *   String result = UtilTimerStack.profile("purchaseItem: ", 
     *       new UtilTimerStack.ProfilingBlock<String>() {
     *            public String doProfiling() {
     *               getMyService().purchaseItem(....)
     *               return "Ok";
     *            }
     *       });
     * </pre>
     * or
     * <pre>
     *   // we don't need a returning result
     *   UtilTimerStack.profile("purchaseItem: ", 
     *       new UtilTimerStack.ProfilingBlock<String>() {
     *            public String doProfiling() {
     *               getMyService().purchaseItem(....)
     *               return null;
     *            }
     *       });
     * </pre>
     * 
     * @param <T> any return value if there's one.
     * @param name profile name
     * @param block code block subjected to profiling
     * @return T
     * @throws Exception
     */
    public <T> T profile(String name, ProfilingBlock<T> block) throws Exception {
    	push(name);
    	try {
    		return block.doProfiling();
    	}
    	finally {
    		pop(name);
    	}
    }
    
    /**
     * A callback interface where code subjected to profile is to be executed. This eliminates the need
     * of coding boiler code that does pushing (UtilTimerBean.push(...)) and poping (UtilTimerBean.pop(...))
     * in a try ... finally ... block.
     * 
     * @version $Date: 2009-12-27 19:00:13 +0100 (Sun, 27 Dec 2009) $ $Id: UtilTimerStack.java 894087 2009-12-27 18:00:13Z martinc $
     * 
     * @param <T>
     */
    public static interface ProfilingBlock<T> {
    	
    	/**
    	 * Method that execute the code subjected to profiling.
    	 * 
    	 * @return  profiles Type
    	 * @throws Exception
    	 */
    	T doProfiling() throws Exception;
    }
}