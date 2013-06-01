package org.onetwo.common.web.utils.html;

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
 * 读取html_filter的配置文件类
 * @author weishao
 *
 */

@SuppressWarnings({"serial"})
public class HtmlFilterConfigHandler extends DefaultHandler implements Serializable {
	private Map<String, HtmlFilterConfig> configMap = new LinkedHashMap<String, HtmlFilterConfig>();
	private String tagName = "";
	private StringBuffer sb;
	private HtmlFilterConfig filterConfig;
	
	private Resource config;

	public HtmlFilterConfigHandler() {
		config = new ClassPathResource("html_filter.xml");
		this.init();
	}

	public HtmlFilterConfigHandler(String config) {
		this.config = new ClassPathResource(config);
		this.init();
	}

	public void init() {
		try {
			this.configMap.clear();
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			parser.parse(config.getFile(), this);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	public void addBb(HtmlFilterConfig bb) {
		this.configMap.put(bb.getTagName(), bb);
	}

	public Collection<HtmlFilterConfig> getFilterConfigs() {
		return this.configMap.values();
	}

	public HtmlFilterConfig findByName(String tagName) {
		return (HtmlFilterConfig) this.configMap.get(tagName);
	}

	public void startElement(String uri, String localName, String tag, Attributes attrs) {
		if (tag.equals("match")) {
			this.sb = new StringBuffer();
			this.filterConfig = new HtmlFilterConfig();

			String tagName = attrs.getValue("name");
			if (tagName != null) {
				this.filterConfig.setTagName(tagName);
			}
		}

		this.tagName = tag;
	}

	public void endElement(String uri, String localName, String tag) {
		if (tag.equals("match")) {
			this.addBb(this.filterConfig);
		} else if (this.tagName.equals("replace")) {
			this.filterConfig.setReplace(this.sb.toString().trim());
			this.sb.delete(0, this.sb.length());
		} else if (this.tagName.equals("regex")) {
			this.filterConfig.setRegex(this.sb.toString().trim());
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
		HtmlFilterConfigHandler bh = new HtmlFilterConfigHandler();
		System.out.println(bh.getFilterConfigs().size());
	}
}