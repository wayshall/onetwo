package org.onetwo.boot.groovy;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.utils.StringUtils;

/**
 * @author weishao zeng
 * <br/>
 */
public class GroovyResourceConnection extends URLConnection {
	
	private GroovyScriptFinder groovyScriptFinder;
	private GroovyScriptData scriptData;

	protected GroovyResourceConnection(URL url, GroovyScriptFinder groovyScriptFinder, GroovyScriptData scriptData) {
		super(url);
		this.groovyScriptFinder = groovyScriptFinder;
		this.scriptData = scriptData;
	}

	@Override
	public void connect() throws IOException {
		this.connected = true;
	}

	@Override
	public long getLastModified() {
		long lastModify = groovyScriptFinder.getScriptLastModified(scriptData);
		return lastModify;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		this.connect();
		if (StringUtils.isBlank(scriptData.getContent())) {
			throw new BaseException("groovy script can not be blank");
		}
		return IOUtils.toInputStream(scriptData.getContent(), FileUtils.UTF8);
	}
	

}
