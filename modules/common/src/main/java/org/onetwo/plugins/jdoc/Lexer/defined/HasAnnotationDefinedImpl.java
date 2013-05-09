package org.onetwo.plugins.jdoc.Lexer.defined;

import java.util.Collection;
import java.util.Map;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.jdoc.Lexer.JToken;

abstract public class HasAnnotationDefinedImpl extends JDefinedImpl {

	public HasAnnotationDefinedImpl(String name, JToken... tokens) {
		super(name, tokens);
	}

	private final Map<String, AnnotationDefinedImpl> annotations = LangUtils.newHashMap();
	

	public void addAnnotation(AnnotationDefinedImpl anno) {
		if (anno == null)
			return;
		this.annotations.put(anno.getName(), anno);
	}

	@SuppressWarnings("unchecked")
	public <T extends AnnotationDefinedImpl> T getAnnotationDefined(String name) {
		return (T) this.annotations.get(name);
	}
	public boolean containsAnnotation(String name) {
		return getAnnotationDefined(name)!=null;
	}

	public String getAnnotationAttribute(String name, String attr) {
		return getAnnotationAttribute(name, attr, null);
	}
	public String getAnnotationAttribute(String name, String attr, String def) {
		AnnotationDefinedImpl anno = this.annotations.get(name);
		String value = null;
		if(anno!=null){
			value = anno.getAttribute(attr);
		}
		if(value==null)
			return def;
		else
			return value;
	}

	public Map<String, AnnotationDefinedImpl> getAnnotations() {
		return annotations;
	}
	
	public void addAnnotations(Collection<? extends AnnotationDefinedImpl> annotationDefinedImpls) {
		if (LangUtils.isNotEmpty(annotationDefinedImpls)) {
			for (AnnotationDefinedImpl jdf : annotationDefinedImpls) {
				this.addAnnotation(jdf);
			}
		}
	}
}
