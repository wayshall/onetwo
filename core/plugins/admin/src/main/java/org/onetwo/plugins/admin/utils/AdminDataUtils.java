package org.onetwo.plugins.admin.utils;

import java.io.FileInputStream;
import java.util.List;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.admin.model.data.entity.DictionaryEntity;
import org.onetwo.plugins.admin.model.vo.DictTypeVo;
import org.springframework.core.io.Resource;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public final class AdminDataUtils {
	

	public static List<DictTypeVo> readDictResource(Resource config){
		List<DictTypeVo> m = null;
		try {
			XStream xstream = registerDictModel();
			if(config.exists()){
				m = (List<DictTypeVo>)xstream.fromXML(new FileInputStream(config.getFile()));
			}else{
				m = (List<DictTypeVo>)xstream.fromXML(config.getInputStream());
			}
		} catch (Exception e) {
			throw new ServiceException("读取模板["+config+"]配置出错：" + e.getMessage(), e);
		}
		
		return m;
	}
	
	public static XStream registerDictModel(){
		XStream xstream = new XStream(new DomDriver());
		xstream.alias("dictionary", List.class);
		xstream.alias("dictType", DictTypeVo.class);
		xstream.alias("dicts", List.class);
		xstream.alias("dict", DictionaryEntity.class);
		xstream.useAttributeFor(String.class);
		for(Class<?> btype : LangUtils.getBaseTypeClass()){
			xstream.useAttributeFor(btype);
		}
		return xstream;
	}

	private AdminDataUtils(){
	}
}
