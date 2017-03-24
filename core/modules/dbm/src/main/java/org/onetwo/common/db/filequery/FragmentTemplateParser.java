package org.onetwo.common.db.filequery;

import org.onetwo.common.spring.ftl.FtlUtils;
import org.onetwo.common.spring.ftl.TemplateParser;
import org.onetwo.dbm.exception.FileNamedQueryException;

import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class FragmentTemplateParser implements TemplateHashModel {
	public static final String DOT = ".";
	
	private final TemplateParser parser;
	private final ParserContext parserContext;
	private final DbmNamedQueryInfo query;
	
	public FragmentTemplateParser(TemplateParser parser, ParserContext parserContext, DbmNamedQueryInfo query) {
		super();
		this.parser = parser;
		this.parserContext = parserContext;
		this.query = query;
	}
	
	private boolean isNamespaceScope(String key){
//		return key.startsWith(AbstractPropertiesManager.NAME_PREFIX);
		return key.indexOf(DbmNamedQueryInfo.DOT_KEY)!=-1;
	}
	
	private String getQueryName(String key){
		int start = 0;//AbstractPropertiesManager.NAME_PREFIX.length();
		int end = key.indexOf(DOT);
		return key.substring(start, end);
	}

	private void checkKeyIfNamespaceScope(String key){
		String qname = getQueryName(key);
		String subkey = key.substring(qname.length()+DOT.length());
		if(!subkey.startsWith(DbmNamedQueryInfo.FRAGMENT_KEY)){
			throw new FileNamedQueryException("only can access "+DbmNamedQueryInfo.FRAGMENT_KEY+" of query, error key: " + key);
		}
	}
	/****
	 * {@link DbmNamedQueryInfo#getFragment()}
	 */
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
			value = query.getDbmNamedQueryFile().isGlobal()?key:query.getNamespace()+"."+key;
		}else{
//			checkKey(key);
//			value = query.getAttrs().get(key);
			value = query.getFragmentTemplateName(key);
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
