package org.onetwo.common.script;

import java.io.InputStreamReader;
import java.io.Reader;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;

public class ScriptTest {

	public static void main(String[] args) throws Exception{
		// Get the JavaScript engine
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");

        // Set JavaScript variables
        Bindings vars = new SimpleBindings();
        vars.put("demoVar", "value set in ScriptDemo.java");
        vars.put("strBuf", new StringBuffer("string buffer"));
        
        // Run DemoScript.js
        Reader scriptReader = new InputStreamReader(
        		ScriptTest.class.getResourceAsStream("demo.js"));
        try {
            engine.eval(scriptReader, vars);
        } finally {
            scriptReader.close();
        }
        
        // Get JavaScript variables
        Object demoVar = vars.get("demoVar");
        System.out.println("[Java] demoVar: " + demoVar);
        System.out.println("    Java object: " + demoVar.getClass().getName());
        System.out.println();
        Object strBuf = vars.get("strBuf");
        System.out.println("[Java] strBuf: " + strBuf);
        System.out.println("    Java object: " + strBuf.getClass().getName());
        System.out.println();
        Object newVar = vars.get("newVar");
        System.out.println("[Java] newVar: " + newVar);
        System.out.println("    Java object: " + newVar.getClass().getName());
        System.out.println();
    }
}
