package org.onetwo.common.spring.utils;

import java.io.FileReader;
import java.io.Reader;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

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
		
		public ScriptEnginer compile(String script, Object... args){
			try {
				configScriptContext(scriptEngine.getContext(), args);
				CompiledScript compiledScript = getCompilable().compile(script);
				compiledScript.eval();
			} catch (Exception e) {
				processError(e, ()->new RuntimeException("compile javascript error:"+script, e));
			}
//			this.eval(script, null);
			return this;
		}
		
		public ScriptEnginer compileFile(String jsfile, Object... args){
			try {
				configScriptContext(scriptEngine.getContext(), args);
				CompiledScript compiledScript = getCompilable().compile(new FileReader(jsfile));
				compiledScript.eval();
			} catch (Exception e) {
				processError(e, ()-> new RuntimeException("eval javascript error:"+jsfile, e));
			}
			return this;
		}
		
		public ScriptEnginer compileClassPathFile(String jsfile, Object... args){
			try {
				configScriptContext(scriptEngine.getContext(), args);
				ClassPathResource res = new ClassPathResource(jsfile);
				CompiledScript compiledScript = getCompilable().compile(new FileReader(res.getFile()));
				compiledScript.eval();
			} catch (Exception e) {
				processError(e, ()-> new RuntimeException("eval javascript error:"+jsfile, e));
			}
			return this;
		}
		
		@SuppressWarnings("unchecked")
		public <T> T eval(String script, Object... args){
			try {
				if(args.length>0){
					ScriptContext context = configScriptContext(new SimpleScriptContext(), args);
					return (T)scriptEngine.eval(script, context);
				}else{
					return (T)scriptEngine.eval(script);
				}
			} catch (ScriptException e) {
				logger.error("eval javascript error: {}, script: {}", e.getMessage(), script);
				processError(e, ()-> new RuntimeException("eval javascript error", e));
			}
			return null;
		}

		@SuppressWarnings("unchecked")
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
					ScriptContext context = configScriptContext(new SimpleScriptContext(), args);
					return (T)scriptEngine.eval(reader, context);
				}else{
					return (T)scriptEngine.eval(reader);
				}
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
		public <T> T invokeMethod(Object thisObject, String funName, Object...args){
			try {
				T res = (T)getInvocable().invokeMethod(thisObject, funName, args);
				return res;
			} catch (Exception e) {
				logger.error("eval javascript error: {}, method: {}", e.getMessage(), funName);
				processError(e, ()-> new RuntimeException("eval javascript error", e));
			}
			return null;
		}
	}
	

}
