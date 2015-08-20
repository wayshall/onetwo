package org.onetwo.common.utils.xml;

import java.io.File;
import java.util.Map;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.file.FileUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


@SuppressWarnings({"unchecked", "rawtypes"})
abstract public class XmlUtils {

	public static XStream createXStreamBy(boolean userAttribute, Map<String, Class> aliasClass, Map<String, Class> attributeFors){
		XStream xstream = new XStream(new DomDriver());
		
		if(aliasClass!=null){
			for(Map.Entry<String, Class> entry : aliasClass.entrySet()){
				xstream.alias(entry.getKey(), entry.getValue());
			}
		}
		if(attributeFors!=null){
			for(Map.Entry<String, Class> entry : attributeFors.entrySet()){
				xstream.useAttributeFor(entry.getKey(), entry.getValue());
			}
		}
		
		if(userAttribute){
			xstream.useAttributeFor(String.class);
			xstream.useAttributeFor(Integer.class);
			xstream.useAttributeFor(int.class);
			xstream.useAttributeFor(Boolean.class);
			xstream.useAttributeFor(boolean.class);
		}
		
		return xstream;
	}
	
	public static XStream createXStream(boolean userAttribute, Object...alias){
		return createXStreamBy(userAttribute, LangUtils.asMap(alias), null);
	}
	
	public static XStream createXStream(Object...alias){
		return createXStream(false, alias);
	}
	
	public static String toXML(Object bean, Object...alias){
		XStream stream = createXStream(alias);
		return stream.toXML(bean);
	}
	
	public static String toXML(Object bean, boolean userAttribute, Object...alias){
		XStream stream = createXStream(userAttribute, alias);
		return stream.toXML(bean);
	}
	
	public static <T> T toBeanFrom(File file, Object...alias){
		XStream stream = createXStream(alias);
		String xml = FileUtils.readAsString(file);
		return (T)stream.fromXML(xml);
	}
	
	public static <T> T toBean(String xml, Object...alias){
		XStream stream = createXStream(alias);
		return (T)stream.fromXML(xml);
	}
}
