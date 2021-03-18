package org.onetwo.common.spring.ftl;

import java.io.Reader;
import java.io.StringReader;

import org.onetwo.common.spring.ftl.StringFtlTemplateLoader.StringTemplateSource;

import freemarker.cache.TemplateLoader;

/***
 * 模板名字即模板内容
 * @author way
 *
 */
public class NameIsContentTemplateLoader implements TemplateLoader {

	public NameIsContentTemplateLoader() {
	}

    public void closeTemplateSource(Object templateSource) {
    }
    
    public Object findTemplateSource(String name) {
    	Object t = new StringTemplateSource(name, name, System.currentTimeMillis());
    	return t;
    }
    
    public long getLastModified(Object templateSource) {
        return ((StringTemplateSource)templateSource).getLastModified();
    }
    
    public Reader getReader(Object templateSource, String encoding) {
        return new StringReader(((StringTemplateSource)templateSource).getSource());
    }
	
}
