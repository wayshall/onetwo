package org.onetwo.common.web.subdomain;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.onetwo.common.utils.StringUtils;

public class SubdomainRule {
	
	public static enum ForwardType {
		DISPATCHER,
		REDIRECT;
		
		public String toString(){
			return super.toString().toLowerCase();
		}
	}

	protected String name;
	protected String from;
	protected String to;
	protected String forward;
	protected int domainPositon = 1;
	
	private Pattern pattern;
	
	public SubdomainRule(){
		this.forward = ForwardType.DISPATCHER.toString();
	}
	
	public boolean isMatche(String path){
		if(pattern==null){
			pattern = Pattern.compile(from, Pattern.CASE_INSENSITIVE);
		}
		return pattern.matcher(path).find();
	}
	
	public String getSubdomain(String path){
		if(pattern==null){
			pattern = Pattern.compile(from, Pattern.CASE_INSENSITIVE);
		}
		Matcher m = pattern.matcher(path);
		if(m.find()){
			return m.group(this.domainPositon);
		}
		return null;
	}
	
	public String replate(String text, String... repText){
		if(pattern==null){
			pattern = Pattern.compile(from, Pattern.CASE_INSENSITIVE);
		}
		Matcher m = pattern.matcher(text);
		List<String> list = Arrays.asList(repText);
		StringBuffer sb = new StringBuffer(text);
		String replacement = null;
		int index = 1;
		while(m.find()){
			replacement = list.get(index-1);
			m.appendReplacement(sb, replacement);
			index++;
		}
		m.appendTail(sb);
		return sb.toString();
	}
	
	public String parse(String path){
		return path.replaceAll(from, to);
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public ForwardType getForwardType() {
		return ForwardType.valueOf(getForward().toUpperCase());
	}
	
	public String getForward() {
		if(StringUtils.isBlank(forward))
			forward = ForwardType.DISPATCHER.toString();
		return forward;
	}

	public void setForward(String forward) {
		this.forward = forward;
	}
	
	public boolean isDispatcher(){
		return this.getForwardType().equals(ForwardType.DISPATCHER);
	}
	public int getDomainPositon() {
		return domainPositon;
	}

	public void setDomainPositon(int domainPositon) {
		this.domainPositon = domainPositon;
	}

	
	public static void main(String[] args){
		SubdomainRule s = new SubdomainRule();
		s.from = "^/zh/([^/]+).jsp$";
		s.to = "/zh/template/$1.jsp";
		String path = "/zh/asdfas.jsp";
		String a = s.getSubdomain(path);
		System.out.println(s.replate(path, "aa"));
	}


}
