package org.onetwo.common.web.s2.interceptor;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import com.opensymphony.xwork2.interceptor.NoParameters;
import com.opensymphony.xwork2.interceptor.ParameterNameAware;
import com.opensymphony.xwork2.interceptor.ParametersInterceptor;
import com.opensymphony.xwork2.util.ClearableValueStack;
import com.opensymphony.xwork2.util.LocalizedTextUtil;
import com.opensymphony.xwork2.util.MemberAccessValueStack;
import com.opensymphony.xwork2.util.TextParseUtil;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.ValueStackFactory;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import com.opensymphony.xwork2.util.reflection.ReflectionContextState;

/***
 * 防止用户在浏览器地址栏提交远程命令的参数过滤，来自struts2的官方网站
 * @author weishao
 *
 */
@SuppressWarnings("serial")
public class FixParametersInterceptor extends MethodFilterInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(FixParametersInterceptor.class);

    boolean ordered = false;
    Set<Pattern> excludeParams = Collections.emptySet();
    Set<Pattern> acceptParams = Collections.emptySet();
    static boolean devMode = false;

    private String acceptedParamNames = "[a-zA-Z0-9\\.\\]\\[_'\\s]+";
    private Pattern acceptedPattern = Pattern.compile(acceptedParamNames);

    private ValueStackFactory valueStackFactory;

    @Inject
    public void setValueStackFactory(ValueStackFactory valueStackFactory) {
        this.valueStackFactory = valueStackFactory;
    }

    @Inject("devMode")
    public static void setDevMode(String mode) {
        devMode = "true".equals(mode);
    }

    public void setAcceptParamNames(String commaDelim) {
        Collection<String> acceptPatterns = asCollection(commaDelim);
        if (acceptPatterns != null) {
            acceptParams = new HashSet<Pattern>();
            for (String pattern : acceptPatterns) {
                acceptParams.add(Pattern.compile(pattern));
            }
        }
    }

    /**
     * Compares based on number of '.' characters (fewer is higher)
     */
    static final Comparator<String> rbCollator = new Comparator<String>() {
        public int compare(String s1, String s2) {
            int l1 = 0, l2 = 0;
            for (int i = s1.length() - 1; i >= 0; i--) {
                if (s1.charAt(i) == '.') l1++;
            }
            for (int i = s2.length() - 1; i >= 0; i--) {
                if (s2.charAt(i) == '.') l2++;
            }
            return l1 < l2 ? -1 : (l2 < l1 ? 1 : s1.compareTo(s2));
        }

    };

    @Override
    public String doIntercept(ActionInvocation invocation) throws Exception {
        Object action = invocation.getAction();
        if (!(action instanceof NoParameters)) {
            ActionContext ac = invocation.getInvocationContext();
            final Map<String, Object> parameters = retrieveParameters(ac);

            if (LOG.isDebugEnabled()) {
                LOG.debug("Setting params " + getParameterLogMap(parameters));
            }

            if (parameters != null) {
                Map<String, Object> contextMap = ac.getContextMap();
                try {
                    ReflectionContextState.setCreatingNullObjects(contextMap, true);
                    ReflectionContextState.setDenyMethodExecution(contextMap, true);
                    ReflectionContextState.setReportingConversionErrors(contextMap, true);

                    ValueStack stack = ac.getValueStack();
                    setParameters(action, stack, parameters);
                } finally {
                    ReflectionContextState.setCreatingNullObjects(contextMap, false);
                    ReflectionContextState.setDenyMethodExecution(contextMap, false);
                    ReflectionContextState.setReportingConversionErrors(contextMap, false);
                }
            }
        }
        return invocation.invoke();
    }

    /**
     * Gets the parameter map to apply from wherever appropriate
     *
     * @param ac The action context
     * @return The parameter map to apply
     */
    protected Map<String, Object> retrieveParameters(ActionContext ac) {
        return ac.getParameters();
    }


    /**
     * Adds the parameters into context's ParameterMap
     *
     * @param ac        The action context
     * @param newParams The parameter map to apply
     *                  <p/>
     *                  In this class this is a no-op, since the parameters were fetched from the same location.
     *                  In subclasses both retrieveParameters() and addParametersToContext() should be overridden.
     */
    protected void addParametersToContext(ActionContext ac, Map<String, Object> newParams) {
    }

    protected void setParameters(Object action, ValueStack stack, final Map<String, Object> parameters) {
        ParameterNameAware parameterNameAware = (action instanceof ParameterNameAware)
                ? (ParameterNameAware) action : null;

        Map<String, Object> params;
        Map<String, Object> acceptableParameters;
        if (ordered) {
            params = new TreeMap<String, Object>(getOrderedComparator());
            acceptableParameters = new TreeMap<String, Object>(getOrderedComparator());
            params.putAll(parameters);
        } else {
            params = new TreeMap<String, Object>(parameters);
            acceptableParameters = new TreeMap<String, Object>();
        }

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String name = entry.getKey();

            boolean acceptableName = acceptableName(name)
                    && (parameterNameAware == null
                    || parameterNameAware.acceptableParameterName(name));

            if (acceptableName) {
                acceptableParameters.put(name, entry.getValue());
            }
        }

        ValueStack newStack = valueStackFactory.createValueStack(stack);
        boolean clearableStack = newStack instanceof ClearableValueStack;
        if (clearableStack) {
            //if the stack's context can be cleared, do that to prevent OGNL
            //from having access to objects in the stack, see XW-641
            ((ClearableValueStack)newStack).clearContextValues();
            Map<String, Object> context = newStack.getContext();
            ReflectionContextState.setCreatingNullObjects(context, true);
            ReflectionContextState.setDenyMethodExecution(context, true);
            ReflectionContextState.setReportingConversionErrors(context, true);

            //keep locale from original context
            context.put(ActionContext.LOCALE, stack.getContext().get(ActionContext.LOCALE));
        }

        boolean memberAccessStack = newStack instanceof MemberAccessValueStack;
        if (memberAccessStack) {
            //block or allow access to properties
            //see WW-2761 for more details
            MemberAccessValueStack accessValueStack = (MemberAccessValueStack) newStack;
            accessValueStack.setAcceptProperties(acceptParams);
            accessValueStack.setExcludeProperties(excludeParams);
        }

        for (Map.Entry<String, Object> entry : acceptableParameters.entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue();
            try {
                newStack.setValue(name, value);
            } catch (RuntimeException e) {
                if (devMode) {
                    String developerNotification = LocalizedTextUtil.findText(ParametersInterceptor.class, "devmode.notification", ActionContext.getContext().getLocale(), "Developer Notification:\n{0}", new Object[]{
                             "Unexpected Exception caught setting '" + name + "' on '" + action.getClass() + ": " + e.getMessage()
                    });
                    LOG.error(developerNotification);
                    if (action instanceof ValidationAware) {
                        ((ValidationAware) action).addActionMessage(developerNotification);
                    }
                }
            }
        }

        if (clearableStack && (stack.getContext() != null) && (newStack.getContext() != null))
            stack.getContext().put(ActionContext.CONVERSION_ERRORS, newStack.getContext().get(ActionContext.CONVERSION_ERRORS));

        addParametersToContext(ActionContext.getContext(), acceptableParameters);
    }

    /**
     * Gets an instance of the comparator to use for the ordered sorting.  Override this
     * method to customize the ordering of the parameters as they are set to the
     * action.
     *
     * @return A comparator to sort the parameters
     */
    protected Comparator<String> getOrderedComparator() {
        return rbCollator;
    }

    private String getParameterLogMap(Map<String, Object> parameters) {
        if (parameters == null) {
            return "NONE";
        }

        StringBuilder logEntry = new StringBuilder();
        for (Map.Entry entry : parameters.entrySet()) {
            logEntry.append(String.valueOf(entry.getKey()));
            logEntry.append(" => ");
            if (entry.getValue() instanceof Object[]) {
                Object[] valueArray = (Object[]) entry.getValue();
                logEntry.append("[ ");
		if (valueArray.length > 0 ) {
                    for (int indexA = 0; indexA < (valueArray.length - 1); indexA++) {
                        Object valueAtIndex = valueArray[indexA];
                        logEntry.append(String.valueOf(valueAtIndex));
                        logEntry.append(", ");
                    }
                    logEntry.append(String.valueOf(valueArray[valueArray.length - 1]));
                }
                logEntry.append(" ] ");
            } else {
                logEntry.append(String.valueOf(entry.getValue()));
            }
        }

        return logEntry.toString();
    }

    protected boolean acceptableName(String name) {
        if (isAccepted(name) && !isExcluded(name)) {
            return true;
        }
        return false;
    }    

    protected boolean isAccepted(String paramName) {
        if (!this.acceptParams.isEmpty()) {
            for (Pattern pattern : acceptParams) {
                Matcher matcher = pattern.matcher(paramName);
                if (matcher.matches()) {
                    return true;
                }
            }
            return false;
        } else
            return acceptedPattern.matcher(paramName).matches();
    }

    protected boolean isExcluded(String paramName) {
        if (!this.excludeParams.isEmpty()) {
            for (Pattern pattern : excludeParams) {
                Matcher matcher = pattern.matcher(paramName);
                if (matcher.matches()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Whether to order the parameters or not
     *
     * @return True to order
     */
    public boolean isOrdered() {
        return ordered;
    }

    /**
     * Set whether to order the parameters by object depth or not
     *
     * @param ordered True to order them
     */
    public void setOrdered(boolean ordered) {
        this.ordered = ordered;
    }

    /**
     * Gets a set of regular expressions of parameters to remove
     * from the parameter map
     *
     * @return A set of compiled regular expression patterns
     */
    protected Set getExcludeParamsSet() {
        return excludeParams;
    }

    /**
     * Sets a comma-delimited list of regular expressions to match
     * parameters that should be removed from the parameter map.
     *
     * @param commaDelim A comma-delimited list of regular expressions
     */
    public void setExcludeParams(String commaDelim) {
        Collection<String> excludePatterns = asCollection(commaDelim);
        if (excludePatterns != null) {
            excludeParams = new HashSet<Pattern>();
            for (String pattern : excludePatterns) {
                excludeParams.add(Pattern.compile(pattern));
            }
        }
    }

    /**
     * Return a collection from the comma delimited String.
     *
     * @param commaDelim the comma delimited String.
     * @return A collection from the comma delimited String. Returns <tt>null</tt> if the string is empty.
     */
    private Collection<String> asCollection(String commaDelim) {
        if (commaDelim == null || commaDelim.trim().length() == 0) {
            return null;
        }
        return TextParseUtil.commaDelimitedStringToSet(commaDelim);
    }

}
