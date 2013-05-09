package org.onetwo.justtest.vm.btrace;

import static com.sun.btrace.BTraceUtils.*;
import com.sun.btrace.annotations.*;


@BTrace
public class TraceMethodArgsAndReturn{
	   @OnMethod(
	      clazz="org.onetwo.justtest.vm.btrace.CaseObject",
	      method="execute",
	      location=@Location(Kind.RETURN)
	   )
	   public static void traceExecute(@Self CaseObject instance,int sleepTime,@Return boolean result){
	     println("call CaseObject.execute");
	     println(strcat("sleepTime is:",str(sleepTime)));
	     println(strcat("sleepTotalTime is:",str(get(field("org.onetwo.justtest.vm.btrace.CaseObject","sleepTotalTime"),instance))));
	     println(strcat("return value is:",str(result)));
	     jstack();
	   }
	}

