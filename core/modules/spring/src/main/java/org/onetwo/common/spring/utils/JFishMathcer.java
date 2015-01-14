package org.onetwo.common.spring.utils;

import java.util.List;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.list.JFishList;
import org.springframework.util.AntPathMatcher;

public class JFishMathcer {
	
	public static JFishMathcer excludes(boolean caseSensitive, String... excludes){
		JFishMathcer m = new JFishMathcer(caseSensitive);
		m.addExcludes(excludes);
		return m;
	}
	
	public static JFishMathcer includes(boolean caseSensitive, String... includes){
		JFishMathcer m = new JFishMathcer(caseSensitive);
		m.addIncludes(includes);
		return m;
	}
	
	private AntPathMatcher matcher = new AntPathMatcher();
	private List<String> excludes;
	private List<String> includes;
	private boolean caseSensitive = true;
	
	
	public JFishMathcer(boolean caseSensitive) {
		super();
		this.caseSensitive = caseSensitive;
	}

	public boolean match(String apath) {
		final String path = caseSensitive?apath:apath.toLowerCase();
		
		if(LangUtils.isNotEmpty(excludes)){
			for(String ex : excludes){
				if(matcher.match(ex, path)){
					return false;
				}
			}
			return true;
			
		}else if(LangUtils.isNotEmpty(includes)){
			for(String inc : includes){
				if(matcher.match(inc, path)){
					return true;
				}
			}
			return false;
			
		}else{
			return true;
		}
		
	}
	final public JFishMathcer addExcludes(String...excludes){
		if(this.excludes==null){
			this.excludes = JFishList.create();
		}
		JFishList.wrap(this.excludes).addArray(excludes);
		return this;
	}
	public JFishMathcer addIncludes(String...includes){
		if(this.includes==null){
			this.includes = JFishList.create();
		}
		JFishList.wrap(this.includes).addArray(includes);
		return this;
	}
	public AntPathMatcher getMatcher() {
		return matcher;
	}
	public void setMatcher(AntPathMatcher matcher) {
		this.matcher = matcher;
	}
	public List<String> getExcludes() {
		return excludes;
	}
	public void setExcludes(List<String> excludes) {
		this.excludes = excludes;
	}
	public List<String> getIncludes() {
		return includes;
	}
	public void setIncludes(List<String> includes) {
		this.includes = includes;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}
	

}
