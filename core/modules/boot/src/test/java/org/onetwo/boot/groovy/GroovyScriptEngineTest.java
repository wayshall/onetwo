package org.onetwo.boot.groovy;
/**
 * @author weishao zeng
 * <br/>
 */

import org.junit.Test;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;

public class GroovyScriptEngineTest {

	@Test
	public void test() throws Exception {
		GroovyScriptEngine gse = new GroovyScriptEngine("src/test/java/org/onetwo/boot/groovy/");
		gse.run("groovytest.groovy", new Binding());
	}
}
