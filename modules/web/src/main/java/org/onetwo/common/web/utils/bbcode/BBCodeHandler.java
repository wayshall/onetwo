package org.onetwo.common.web.utils.bbcode;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.onetwo.common.exception.ServiceException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 读取bbcode的配置文件类
 * @author weishao
 *
 */

@SuppressWarnings({"serial"})
//@Component
public class BBCodeHandler extends DefaultHandler implements Serializable {
	private Map<String, BBCode> bbMap = new LinkedHashMap<String, BBCode>();
	private String tagName = "";
	private StringBuffer sb;
	private BBCode bb;
	
	private Resource config;

	public BBCodeHandler() {
		config = new ClassPathResource("bb_config.xml");
		this.init();
	}

	public BBCodeHandler(String config) {
		this.config = new ClassPathResource(config);
		this.init();
	}

	public void init() {
		try {
			this.bbMap.clear();
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			parser.parse(config.getFile(), this);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	public void addBb(BBCode bb) {
		this.bbMap.put(bb.getTagName(), bb);
	}

	public Collection<BBCode> getBbList() {
		return this.bbMap.values();
	}

	public BBCode findByName(String tagName) {
		return (BBCode) this.bbMap.get(tagName);
	}

	public void startElement(String uri, String localName, String tag, Attributes attrs) {
		if (tag.equals("match")) {
			this.sb = new StringBuffer();
			this.bb = new BBCode();

			String tagName = attrs.getValue("name");
			if (tagName != null) {
				this.bb.setTagName(tagName);
			}
		}

		this.tagName = tag;
	}

	public void endElement(String uri, String localName, String tag) {
		if (tag.equals("match")) {
			this.addBb(this.bb);
		} else if (this.tagName.equals("replace")) {
			this.bb.setReplace(this.sb.toString().trim());
			this.sb.delete(0, this.sb.length());
		} else if (this.tagName.equals("regex")) {
			this.bb.setRegex(this.sb.toString().trim());
			this.sb.delete(0, this.sb.length());
		}

		this.tagName = "";
	}

	public void characters(char ch[], int start, int length) {
		if (this.tagName.equals("replace") || this.tagName.equals("regex"))
			this.sb.append(ch, start, length);
	}

	public void error(SAXParseException exception) throws SAXException {
		throw exception;
	}

	public Resource getConfig() {
		return config;
	}

	public void setConfig(Resource config) {
		this.config = config;
	}

	public void setConfigPath(String path) {
		this.config = new ClassPathResource(path);;
	}
	
	public static void main(String[] args){
		BBCodeHandler bh = new BBCodeHandler();
		System.out.println(bh.getBbList().size());
	}
}