package org.onetwo.common.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Map;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.func.Block;

import com.thoughtworks.xstream.XStream;

@SuppressWarnings("unchecked")
public class XMLReader {
	
	public static XMLReader New(){
		return new XMLReader();
	}
	
	public static XMLReader New(boolean primitiveAsAttribute){
		return new XMLReader(primitiveAsAttribute);
	}
	
	private XStream xstream;
	private boolean primitiveAsAttribute = true;

	public XMLReader(){
		this.xstream = newXStream();
	}
	public XMLReader(boolean primitiveAsAttribute){
		this.xstream = newXStream();
		this.primitiveAsAttribute = primitiveAsAttribute;
	}
	
	protected XStream newXStream(){
//		XStream xstream = new XStream(new DomDriver());
		xstream = new XStream();//xpp3
		if(primitiveAsAttribute){
			xstream.useAttributeFor(String.class);
			xstream.useAttributeFor(Integer.class);
			xstream.useAttributeFor(int.class);
			xstream.useAttributeFor(Long.class);
			xstream.useAttributeFor(long.class);
			xstream.useAttributeFor(Boolean.class);
			xstream.useAttributeFor(boolean.class);
		}
		return xstream;
	}
	
	public XMLReader map(Map<String, Class> mapping){
		if(mapping==null || mapping.isEmpty()){
			return this;
		}
		for(Map.Entry<String, Class> entry : mapping.entrySet()){
			this.xstream.alias(entry.getKey(), entry.getValue());
		}
		return this;
	}
	
	public XMLReader maps(Object...params){
		return map(CUtils.asMap(params));
	}
	
	
	public XStream getXstream() {
		return xstream;
	}

	
	public void toXML(Object datas, String filename){
		toXML(datas, null, filename);
	}
	
	public void toXML(Object datas, String charset, String filename){
		if(StringUtils.isBlank(charset))
			charset = "utf-8";
		String realPath = FileUtils.getResourcePath(filename);
		File file = new File(realPath);
		try {
			OutputStream out = new FileOutputStream(file);
			this.xstream.toXML(datas, new OutputStreamWriter(out, Charset.forName(charset)));
		} catch (FileNotFoundException e) {
			throw new ServiceException("write to xml error : " + e.getMessage(), e);
		}
	}

	public Object path(String path){
		String realPath = FileUtils.getResourcePath(path);
		final File file = new File(realPath);
		if(!file.exists())
			throw new ServiceException("找不到文件：" +  realPath);
		
		Object datas = parse(new Block(){

			@Override
			public Object execute(Object... objects) {
				try {
					Object datas = getXstream().fromXML(new FileInputStream(file));
					return datas;
				} catch (Exception e) {
					throw new BaseException("parse error : " + e.getMessage(), e);
				}
			}

		});
		return datas;
	}
	
	protected Object parse(Block closure){
		Object datas = null;
		try {
			datas = closure.execute();
		} catch (Exception e) {
			throw new ServiceException("parse error: " + e.getMessage(), e);
		}
		return datas;
	}
}
