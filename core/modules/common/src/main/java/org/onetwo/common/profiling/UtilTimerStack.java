package org.onetwo.common.profiling;


/****
 * copy from struts 2
 *
 */
public class UtilTimerStack
{

    // A reference to the current ProfilingTimerBean
    protected static ThreadLocal<ProfilingTimerBean> current = new ThreadLocal<ProfilingTimerBean>();

    public static final String ACTIVATE_PROPERTY = "onetwo.profile.activate";

    /**
     * System property that controls the min time, that if exceeded will cause a log (at INFO level) to be
     * created.
     */
    public static final String MIN_TIME = "onetwo.profile.mintime";
    
    /**
     * Initialized in a static block, it can be changed at runtime by calling setActive(...)
     */
    private static boolean active;
    private static boolean nanoTime;
    
    private static TimeLogger timeLogger;

    static {
        active = "true".equalsIgnoreCase(System.getProperty(ACTIVATE_PROPERTY));
		timeLogger = new Log4jTimeLogger();
    }

    
    public static TimeLogger getOuputer() {
		return timeLogger;
	}

	public static void setOuputer(TimeLogger ouputer) {
		UtilTimerStack.timeLogger = ouputer;
	}

	public static void setNanoTime(boolean nanoTime) {
		UtilTimerStack.nanoTime = nanoTime;
	}

	/**
     * Create and start a performance profiling with the <code>name</code> given. Deal with 
     * profile hierarchy automatically, so caller don't have to be concern about it.
     * 
     * @param name profile name
     */
    public static void push(String name)
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
    public static void pop(String name)
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
                getOuputer().log("Unmatched Timer.  Was expecting " + currentTimer.getResource() + ", instead got " + name);
            }
        }


    }

    /**
     * Do a log (at INFO level) of the time taken for this particular profiling.
     * 
     * @param currentTimer profiling timer bean
     */
    private static void printTimes(ProfilingTimerBean currentTimer)
    {
    	getOuputer().log(currentTimer.getPrintable(getMinTime()));
    }

    /**
     * Get the min time for this profiling, it searches for a System property
     * 
     * @return long
     */
    private static long getMinTime()
    {
        try
        {
            return Long.parseLong(System.getProperty(MIN_TIME, "0"));
        }
        catch (NumberFormatException e)
        {
           return -1;
        }
    }

    /**
     * Determine if profiling is being activated, by searching for a system property
     * 'xwork.profile.activate', default to false (profiling is off).
     * 
     * @return <tt>true</tt>, if active, <tt>false</tt> otherwise.
     */
    public static boolean isActive()
    {
        return active;
    }

    /****
     * @see #active()
     * @param active
     */
    @Deprecated
    public static void setActive(boolean active) {
    	//ignore
    }
    /**
     * Turn profiling on or off.
     * 
     * @param active
     */
    public static void active(boolean active) {
        if (active)
            System.setProperty(ACTIVATE_PROPERTY, "true");
        else
        	System.clearProperty(ACTIVATE_PROPERTY);

        UtilTimerStack.active = active; 
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
    public static <T> T profile(String name, ProfilingBlock<T> block) throws Exception {
    	UtilTimerStack.push(name);
    	try {
    		return block.doProfiling();
    	}
    	finally {
    		UtilTimerStack.pop(name);
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