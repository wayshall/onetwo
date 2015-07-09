package org.onetwo.boot.plugins.data.utils;

import java.io.FileInputStream;
import java.util.List;

import org.onetwo.boot.plugins.data.vo.DictInfo;
import org.onetwo.boot.plugins.data.vo.DictTypeInfo;
import org.onetwo.boot.plugins.data.vo.DictionaryList;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.LangUtils;
import org.springframework.core.io.Resource;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

final public class DataUtils {

	public static DictionaryList readDictResource(Resource config){
		DictionaryList m = null;
		try {
			XStream xstream = registerDictModel();
			if(config.exists()){
				m = (DictionaryList)xstream.fromXML(new FileInputStream(config.getFile()));
			}else{
				m = (DictionaryList)xstream.fromXML(config.getInputStream());
			}
		} catch (Exception e) {
			throw new ServiceException("读取模板["+config+"]配置出错：" + e.getMessage(), e);
		}
		
		return m;
	}
	
	public static XStream registerDictModel(){
		XStream xstream = new XStream(new DomDriver());
		xstream.alias("dictionary", DictionaryList.class);
		xstream.alias("dictType", DictTypeInfo.class);
		xstream.alias("dicts", List.class);
		xstream.alias("dict", DictInfo.class);
		xstream.useAttributeFor(String.class);
		for(Class<?> btype : LangUtils.getBaseTypeClass()){
			xstream.useAttributeFor(btype);
		}
		return xstream;
	}
	
	private DataUtils(){}
}
