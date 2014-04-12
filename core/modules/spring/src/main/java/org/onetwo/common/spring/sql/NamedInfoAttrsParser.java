package org.onetwo.common.spring.sql;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.ftl.FtlUtils;
import org.onetwo.common.utils.propconf.AbstractPropertiesManager;
import org.onetwo.common.utils.propconf.AbstractPropertiesManager.NamespaceProperty;

import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class NamedInfoAttrsParser implements TemplateHashModel {
	public static final String DOT = ".";
	
	private final FileSqlParser<?> parser;
	private final ParserContext parserContext;
	private final JFishNamedFileQueryInfo query;
	
	public NamedInfoAttrsParser(FileSqlParser<?> parser, ParserContext parserContext, JFishNamedFileQueryInfo query) {
		super();
		this.parser = parser;
		this.parserContext = parserContext;
		this.query = query;
	}
	
	private boolean isNamespaceScope(String key){
//		return key.startsWith(AbstractPropertiesManager.NAME_PREFIX);
		return key.indexOf(NamespaceProperty.DOT_KEY)!=-1;
	}
	
	private String getQueryName(String key){
		int start = 0;//AbstractPropertiesManager.NAME_PREFIX.length();
		int end = key.indexOf(DOT);
		return key.substring(start, end);
	}

	private void checkKeyIfNamespaceScope(String key){
		String qname = getQueryName(key);
		String subkey = key.substring(qname.length()+DOT.length());
		if(!subkey.startsWith(JFishNamedFileQueryInfo.TEMPLATE_KEY)){
			throw new BaseException("only can access "+JFishNamedFileQueryInfo.TEMPLATE_KEY+" of query, error key: " + key);
		}
	}
	@Override
	public TemplateModel get(String key) throws TemplateModelException {
		String value = null;
		if(isNamespaceScope(key)){
//			String qname = getQueryName(key);
//			JFishNamedFileQueryInfo queryInfo = (JFishNamedFileQueryInfo)query.getNamespaceInfo().getNamedProperty(qname);
//			String subkey = key.substring(qname.length()+DOT.length()+1);
//			checkKeyIfNamespaceScope(subkey);
//			value = (String)SpringUtils.newBeanWrapper(queryInfo).getPropertyValue(subkey);
			checkKeyIfNamespaceScope(key);
			value = key;
		}else{
//			checkKey(key);
//			value = query.getAttrs().get(key);
			value = query.getTemplateName(key);
		}
//		value = this.parser.parse(parser.asFtlContent(value), parserContext);// 不再解释，value含有星号的话，freemarker会认为是路径模糊匹配导致出错
		value = this.parser.parse(value, parserContext);
		return FtlUtils.wrapAsModel(value);
	}

	@Override
	public boolean isEmpty() throws TemplateModelException {
		return false;
	}
	
	
	
}
