package org.onetwo.plugins.fmtagext.ui.aglybuilder;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.fmtag.annotation.JEntryViewMeta;
import org.onetwo.plugins.fmtagext.ui.AbstractHtmlElement;
import org.onetwo.plugins.fmtagext.ui.DefaultViewEntryManager;
import org.onetwo.plugins.fmtagext.ui.UIComponent;
import org.onetwo.plugins.fmtagext.ui.ViewEntry;
import org.slf4j.Logger;

abstract public class BaseEntityUIBuilder<T extends UIComponent> implements EntityUIBuilder<T>{
	
	public static final String[] DEFAULT_EXCLUDES = new String[]{"createTime", "lastUpdateTime"};

	protected final Logger logger = MyLoggerFactory.getLogger(this.getClass());
	protected final Class<?> entityClass;

	public BaseEntityUIBuilder(Class<?> entityClass) {
		super();
		this.entityClass = entityClass;
	}

	protected boolean isAcceptedField(String[] excludeFields, String fieldName){
		if(LangUtils.isEmpty(excludeFields)){
			excludeFields = DEFAULT_EXCLUDES;
		}
		return !ArrayUtils.contains(excludeFields, fieldName);
	}

	protected <B> B getHighestBean(Class<B> beanClass){
		return (B)SpringApplication.getInstance().getSpringHighestOrder(beanClass);
	}
	
	protected DefaultViewEntryManager getViewEntryManager(){
		DefaultViewEntryManager vem = getHighestBean(DefaultViewEntryManager.class);
		return vem;
	}
	
	protected ViewEntry getJFishViewEntry(Class<?> entityClass){
		ViewEntry entry = getViewEntryManager().getViewEntry(entityClass);
		return entry;
	}
	
	protected void _buildTitlte(AbstractHtmlElement html, ViewEntry entry){
		if(StringUtils.isBlank(html.getTitle())){
			html.setTitle(entry.getTitle());
		}
	}
	

	protected void beforeBuildEntityFields() {
		
	}
	
	protected void afterBuildEntityFields() {
		
	}
	
	
}
