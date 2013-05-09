package org.onetwo.common.web.s2.tag.webtag;

import java.io.Writer;

import org.apache.log4j.Logger;
import org.apache.struts2.StrutsConstants;
import org.apache.struts2.components.Component;
import org.apache.struts2.components.UIBean;
import org.apache.struts2.components.template.Template;
import org.apache.struts2.components.template.TemplateEngine;
import org.apache.struts2.components.template.TemplateEngineManager;
import org.apache.struts2.components.template.TemplateRenderingContext;
import org.apache.struts2.views.util.ContextUtil;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.config.ConfigurationException;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.util.ValueStack;

public class TemplateBuilder {
	
	protected Logger logger = Logger.getLogger(this.getClass());

    protected String templateDir;
    protected String theme;
    
    protected String defaultTemplateDir;
    protected String defaultUITheme;
    protected TemplateEngineManager templateEngineManager;
    protected String templateSuffix;
    
    public TemplateBuilder(){
       defaultUITheme = "webtag";
    }

    public String getTemplateSuffix() {
    	if(templateSuffix==null)
    		this.templateSuffix = ContextUtil.getTemplateSuffix(getStack().getContext());
		return templateSuffix;
	}

	@Inject(StrutsConstants.STRUTS_UI_TEMPLATEDIR)
    public void setDefaultTemplateDir(String dir) {
        this.defaultTemplateDir = dir;
    }

    public void setDefaultUITheme(String theme) {
        this.defaultUITheme = theme;
    }

    @Inject
    public void setTemplateEngineManager(TemplateEngineManager mgr) {
        this.templateEngineManager = mgr;
    }

	public void setTemplateDir(String templateDir) {
		this.templateDir = templateDir;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	protected Template buildTemplateName(String myTemplate, String myDefaultTemplate) {
        String template = myDefaultTemplate;

        if (myTemplate != null) {
            template = getStack().findString(myTemplate);
        }

        String templateDir = getTemplateDir();
        String theme = getTheme();

        return new Template(templateDir, theme, template);

    }

	public void mergeTemplate(Component comp, Writer writer, String myTemplate){
    	Template template = buildTemplateName(null, myTemplate);
    	
        final TemplateEngine engine = templateEngineManager.getTemplateEngine(template, getTemplateSuffix());
        if (engine == null) {
            throw new ConfigurationException("Unable to find a TemplateEngine for template " + template);
        }

        if (logger.isDebugEnabled()) {
        	logger.debug("Rendering template " + template);
        }
        
        UIBean uibean = null;
        if(UIBean.class.isAssignableFrom(comp.getClass())){
        	uibean = (UIBean)comp;
        }else{
        	uibean = new SimpleUIBean(comp);
        }

        final TemplateRenderingContext context = new TemplateRenderingContext(template, writer, getStack(), comp.getParameters(), uibean);
        try {
			engine.renderTemplate(context);
		} catch (Exception e) {
			logger.error("merge template error: " + e.getMessage(), e);
		}
    }
    

    public String getTemplateDir() {
        String templateDir = null;

        if (this.templateDir != null) {
            templateDir = getStack().findString(this.templateDir);
        }

        // If templateDir is not explicitly given,
        // try to find attribute which states the dir set to use
        if ((templateDir == null) || (templateDir.equals(""))) {
            templateDir = getStack().findString("#attr.templateDir");
        }

        // Default template set
        if ((templateDir == null) || (templateDir.equals(""))) {
            templateDir = defaultTemplateDir;
        }

        // Defaults to 'template'
        if ((templateDir == null) || (templateDir.equals(""))) {
            templateDir = "template";
        }

        return templateDir;
    }

    public String getTheme() {
        String theme = null;

        if (this.theme != null) {
            theme = getStack().findString(this.theme);
        }

        // If theme set is not explicitly given,
        // try to find attribute which states the theme set to use
        if ((theme == null) || (theme.equals(""))) {
            theme = getStack().findString("#attr.theme");
        }

        // Default theme set
        if ((theme == null) || (theme.equals(""))) {
            theme = defaultUITheme;
        }

        return theme;
    }
    
    ValueStack getStack(){
    	return ActionContext.getContext().getValueStack();
    }
    
}
