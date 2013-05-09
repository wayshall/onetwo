package org.onetwo.common.spring.web.mvc.view;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.AbstractTemplateView;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

public class JFishContentNegotiatingViewResolver extends ContentNegotiatingViewResolver implements ViewResolver, Ordered {

	// private ContentNegotiatingViewResolver viewResolver;
	private Collection<String> map2FtlExtensions;
	private String extensions;

	@PostConstruct
	public void init() {
		if (this.map2FtlExtensions == null)
			this.map2FtlExtensions = new HashSet<String>();
		if (StringUtils.isBlank(extensions)) {
			this.map2FtlExtensions.add("do");
		} else {
			StringUtils.addWithTrim(this.map2FtlExtensions, extensions, ",");
		}
		if (!this.map2FtlExtensions.contains(""))
			this.map2FtlExtensions.add("");
	}

	public View resolveViewName(String viewName, Locale locale) throws Exception {
		View view = super.resolveViewName(viewName, locale);
		if(!AbstractTemplateView.class.isInstance(view))
			return view;
		String ext = JFishWebUtils.requestExtension();
		if (this.map2FtlExtensions.contains(ext)) {
			if (!FreeMarkerView.class.isInstance(view))
				throw new BaseException("can not find ftl template for view :" + viewName);
		}
		return view;
	}

	public Collection<String> getMap2FtlExtensions() {
		return map2FtlExtensions;
	}

	public void setMap2FtlExtensions(Collection<String> map2FtlExtensions) {
		this.map2FtlExtensions = map2FtlExtensions;
	}

	public String getExtensions() {
		return extensions;
	}

	public void setExtensions(String extensions) {
		this.extensions = extensions;
	}

}
