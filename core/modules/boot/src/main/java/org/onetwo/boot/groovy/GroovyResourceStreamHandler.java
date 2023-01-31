package org.onetwo.boot.groovy;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.exception.ServiceException;

/**
 * @author weishao zeng
 * <br/>
 */
public class GroovyResourceStreamHandler extends URLStreamHandler {
	private GroovyScriptFinder groovyScriptFinder;

	public GroovyResourceStreamHandler(GroovyScriptFinder groovyScriptFinder) {
		super();
		this.groovyScriptFinder = groovyScriptFinder;
	}

	@Override
	protected URLConnection openConnection(URL u) throws IOException {
		String path = u.getPath();
		String[] paths = StringUtils.split(path, "/");
		String name = paths[0];
		GroovyScriptData scriptData = groovyScriptFinder.findScriptByCode(name);
		if (scriptData == null) {
			throw new ServiceException("找不到查询脚本: " + path);
		}
		GroovyResourceConnection grc = new GroovyResourceConnection(u, groovyScriptFinder, scriptData);
		return grc;
	}

}
