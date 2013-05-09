package org.onetwo.common.web.data;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.web.config.SiteConfig;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;
import net.htmlparser.jericho.StartTagType;

@SuppressWarnings({"static-access", "serial"})
public class JerichoHtmlFilter implements Filter {

	public static final String FILTER_HOLDER = "${FILTER_EDITOR_HOLDER}";
	
	private Logger log = Logger.getLogger(getClass());
	
	public JerichoHtmlFilter() {
	}
	
	private final Set<String> INVALID_ELEMENT_NAMES = new HashSet<String>() {
		{
			String names = SiteConfig.getInstance().getConfig("editor.filter.element");
			if(names != null) {
				this.addAll(Arrays.asList(names.split(",")));
			}
		}
	};

	private final Set<String> INVALID_ATTRIBUTE_NAMES = new RegexHashSet () {
		{
			String attrs = SiteConfig.getInstance().getConfig("editor.filter.attribute");
			if(attrs != null) {
				this.addAll(Arrays.asList(attrs.split(",")));
			}
		}
	};
	
	private String iterator(Segment s) {
		OutputDocument od = new OutputDocument(s);
		List<Element> list = s.getChildElements();

		for (Element e : list) {
			if (INVALID_ELEMENT_NAMES.contains(e.getName())) {
				od.remove(e);
			} else if (e.getChildElements() != null
					&& !e.getChildElements().isEmpty()) {
				od.replace(e, processAttribute(new Source(this.iterator(e)).getFirstElement()));
			} else {
				if(StartTagType.NORMAL.equals(e.getStartTag().getTagType())) {
					od.replace(e, processAttribute(e));
				}
			}
		}
		return od.toString();
	}

	private String processAttribute(Element e) {
		OutputDocument od = new OutputDocument(e);
		StartTag startTag = e.getStartTag();
		
		for(Attribute attr : startTag.getAttributes()) {
			if (INVALID_ATTRIBUTE_NAMES.contains(attr.getKey())) {
				od.remove(attr);
			}
		}
		
		return od.toString();
	}
	
	public class RegexHashSet extends HashSet<String> {
		private static final long serialVersionUID = 1L;
		
		public RegexHashSet() {
		}
		
		public RegexHashSet(Collection<? extends String> c) {
			super(c);
		}
		
		@Override
		public boolean contains(Object o) {
			if(super.contains(o)) {
				return true;
			}
			
			if(o instanceof String) {
				for(Iterator<String> it=this.iterator(); it.hasNext();) {
					if(o.toString().matches(it.next())) {
						return true;
					}
				}
			}
			
			return false;
		}
	}
	
	@Override
	public Object doFilter(Object content) {
		try {
			if(content instanceof String) {
				String regx = MyUtils.append("\\Q", FILTER_HOLDER, "\\E");
				return this.iterator(new Source(content.toString().replaceAll(regx, "")));
			}
		} catch(Exception e) {
			log.warn("HTML过滤器在过滤以下内容时发生错误：\n" + content, e);
		}
		return (content);
	}
	
}
