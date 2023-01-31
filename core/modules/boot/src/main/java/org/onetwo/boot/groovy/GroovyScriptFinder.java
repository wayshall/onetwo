package org.onetwo.boot.groovy;
/**
 * @author weishao zeng
 * <br/>
 */
public interface GroovyScriptFinder {
	
	GroovyScriptData findScriptByCode(String idOrCode);
	
	default long getScriptLastModified(GroovyScriptData script) {
		GroovyScriptData newScript = findScriptByCode(script.getCode());
		return newScript.getUpdateAt().getTime();
	}

}
