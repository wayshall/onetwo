package org.onetwo.common.spring.ftl;

import java.io.Reader;
import java.io.StringReader;
import java.util.concurrent.ConcurrentHashMap;

import org.onetwo.common.utils.StringUtils;

import freemarker.cache.TemplateLoader;

public class StringFtlTemplateLoader implements TemplateLoader {

//	private static final String DEFAULT_TEMPLATE_KEY = "_default_template_key";

	private ConcurrentHashMap<String, Object> templates = new ConcurrentHashMap<String, Object>();

	public StringFtlTemplateLoader() {
	}

	public void addTemplateIfNotBlank(String name, String template) {
		if (StringUtils.isBlank(name) || StringUtils.isBlank(template)) {
			return;
		}
		putTemplate(name, template);
	}

    public void putTemplate(String name, String templateSource) {
        putTemplate(name, templateSource, System.currentTimeMillis());
    }
    
	public void putTemplate(String name, String templateSource, long lastModified) {
	        templates.putIfAbsent(name, new StringTemplateSource(name, templateSource, lastModified));
    }
    
    public void closeTemplateSource(Object templateSource) {
    }
    
    public Object findTemplateSource(String name) {
        return templates.get(name);
    }
    
    public long getLastModified(Object templateSource) {
        return ((StringTemplateSource)templateSource).lastModified;
    }
    
    public Reader getReader(Object templateSource, String encoding) {
        return new StringReader(((StringTemplateSource)templateSource).source);
    }
	
	 static class StringTemplateSource {
	        private final String name;
	        private final String source;
	        private final long lastModified;
	        
	        StringTemplateSource(String name, String source, long lastModified) {
	            if(name == null) {
	                throw new IllegalArgumentException("name == null");
	            }
	            if(source == null) {
	                throw new IllegalArgumentException("source == null");
	            }
	            if(lastModified < -1L) {
	                throw new IllegalArgumentException("lastModified < -1L");
	            }
	            this.name = name;
	            this.source = source;
	            this.lastModified = lastModified;
	        }
	        
	        
	        public String getName() {
				return name;
			}

			public String getSource() {
				return source;
			}

			public long getLastModified() {
				return lastModified;
			}



			public boolean equals(Object obj) {
	            if(obj instanceof StringTemplateSource) {
	                return name.equals(((StringTemplateSource)obj).name);
	            }
	            return false;
	        }
	        
	        public int hashCode() {
	            return name.hashCode();
	        }
	    }
}
