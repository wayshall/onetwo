package org.onetwo.common.spring.utils;

import java.io.FileReader;
import java.io.Reader;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.script.Compilable;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.onetwo.common.utils.CUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class JavaScriptInvoker {
	private static final String SCRIPT_ENGINE_NAME = "nashorn";
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private ScriptEngineManager scriptEngineManager;
	
	public JavaScriptInvoker() {
		this.scriptEngineManager = new ScriptEngineManager();
	}

	public ScriptEnginer createScriptEnginer(){
		return new ScriptEnginer();
	}
	

	public <T> T eval(String script, Object... args){
		return this.createScriptEnginer().eval(script, args);
	}
	
	
	public class ScriptEnginer {
		final private ScriptEngine scriptEngine = scriptEngineManager.getEngineByName(SCRIPT_ENGINE_NAME);
		private Optional<Consumer<Throwable>> errorHandler = Optional.empty();

		public ScriptEngine getScriptEngine() {
			return scriptEngine;
		}
		
		public ScriptEnginer whenError(Consumer<Throwable> errorHandler) {
			this.errorHandler = Optional.ofNullable(errorHandler);
			return this;
		}

		public Compilable getCompilable(){
			Compilable compilable = (Compilable)scriptEngine;
			return compilable;
		}
		public Invocable getInvocable(){
			return (Invocable) scriptEngine;
		}
		
		private void processError(Throwable e, Supplier<RuntimeException> exceptionSupplier){
			errorHandler.orElseThrow(exceptionSupplier).accept(e);
		}
		
		public ScriptEnginer evalClassPathFile(String jsfile, Object... args){
			configScriptContext(scriptEngine.getContext(), args);
			ClassPathResource res = new ClassPathResource(jsfile);
			try {
				/*CompiledScript compiledScript = getCompilable().compile(new FileReader(res.getFile()));
				compiledScript.eval();*/
				this.eval(new FileReader(res.getFile()), args);
			} catch (Exception e) {
				processError(e, ()-> new RuntimeException("eval javascript error:"+jsfile, e));
			}
			return this;
		}
		
		@SuppressWarnings("unchecked")
		public <T> T eval(String script, Object... args){
			try {
				if(args.length>0){
					configScriptContext(scriptEngine.getContext(), args);
				}
				return (T)scriptEngine.eval(script);
			} catch (ScriptException e) {
				logger.error("eval javascript error: {}, script: {}", e.getMessage(), script);
				processError(e, ()-> new RuntimeException("eval javascript error", e));
			}
			return null;
		}

		private ScriptContext configScriptContext(ScriptContext context, Object... args){
			Map<String, Object> params = CUtils.asMap(args);
			params.entrySet().forEach(e->{
				context.setAttribute(e.getKey(), e.getValue(), ScriptContext.ENGINE_SCOPE);
			});
			return context;
		}
		
		@SuppressWarnings("unchecked")
		public <T> T eval(Reader reader, Object... args){
			try {
				if(args.length>0){
					configScriptContext(scriptEngine.getContext(), args);
				}
				return (T)scriptEngine.eval(reader);
			} catch (ScriptException e) {
				logger.error("eval javascript error: {}, script reader: {}", e.getMessage(), reader);
				processError(e, ()-> new RuntimeException("eval javascript error", e));
			}
			return null;
		}
		
		@SuppressWarnings("unchecked")
		public <T> T invokeFunc(String funName, Object...args){
			try {
				T res = (T)getInvocable().invokeFunction(funName, args);
				return res;
			} catch (Exception e) {
				logger.error("eval javascript error: {}, funName: {}", e.getMessage(), funName);
				processError(e, ()-> new RuntimeException("eval javascript error", e));
			}
			return null;
		}
		@SuppressWarnings("unchecked")
		public <T> T invokeMethod(Object thisObject, String method, Object...args){
			try {
				T res = (T)getInvocable().invokeMethod(thisObject, method, args);
				return res;
			} catch (Exception e) {
				logger.error("eval javascript error: {}, method: {}", e.getMessage(), method);
				processError(e, ()-> new RuntimeException("eval javascript error", e));
			}
			return null;
		}
	}
	

}
