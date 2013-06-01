package org.onetwo.common.utils;

import java.util.ArrayList;
import java.util.List;

public class Stringer {
	
	public static Stringer wrap(String s) {
		return wrap(s, " ");
	}
	public static Stringer wrap(String s, String... ops) {
		Stringer g = new Stringer(s, ops);
		return g;
	}
	public static Stringer wrap(String s, List<String> ops, List<String> ratainOps) {
		Stringer g = new Stringer(s, ops, ratainOps);
		return g;
	}

	private static final String RETAIN_OP_MARK = "&";

	// private String source;
	private List<String> op;
	private String joinOp;
	private List<String> retainOp;
	private List<String> strList;

	private Stringer(String source, String... ops) {

		Assert.hasText(source);
		if (LangUtils.isEmpty(ops)) {
			ops = new String[] { " " };
		}
		this.op = new ArrayList<String>(ops.length);
		this.retainOp = new ArrayList<String>(ops.length);
		for (String _op : ops) {
			if (_op.length()>1 && _op.startsWith(RETAIN_OP_MARK)) {
				_op = _op.substring(RETAIN_OP_MARK.length());
				this.retainOp.add(_op);
			}
			this.op.add(_op);
		}
		this.joinOp = op.get(0);

		this.parse(source);
	}
	
	private Stringer(String source, List<String> ops, List<String> retailOps) {

		Assert.hasText(source);
		int size = (ops==null?0:ops.size())+(retailOps==null?0:retailOps.size());
		this.op = new ArrayList<String>(size);
		this.retainOp = retailOps;
		if(ops!=null)
			this.op.addAll(ops);
		if(retailOps!=null)
			this.op.addAll(retailOps);
		this.joinOp = op.get(0);

		this.parse(source);
	}
	
	protected void parse(String source){
    	int len = source.length();
    	int wordIndex = 0;
    	strList = new ArrayList<String>();
    	String w = null;
//    	boolean match = false;
		while(wordIndex<len){
			String matchSep = "";
			int minIndex = -1;
			int curIndex = -1;
			for(int i=0; i<op.size(); i++){
	    		String sep = op.get(i);
	    		curIndex = source.indexOf(sep, wordIndex);
	    		if(curIndex==-1)
	    			continue;
	    		//匹配最少index的
    			if(minIndex==-1){
    				minIndex = curIndex;
    				matchSep = sep;
    			}else if(curIndex<minIndex){
    				minIndex = curIndex;
    				matchSep = sep;
    			}
			}
			if(minIndex==-1){
				minIndex = len;
			}
			w = source.substring(wordIndex, minIndex);
    		this.addToList(w, matchSep);
    		wordIndex = minIndex+matchSep.length();
		}  
	}
	
	protected void addToList(String word, String sep){
		this.strList.add(word);
		if(this.retainOp.contains(sep))
			this.strList.add(sep);
	}

	public Stringer removeBlank() {
		if (isEmpty())
			return this;
		List<String> temp = new ArrayList<String>(strList);
		for (String str : temp) {
			if (StringUtils.isBlank(str)) {
				strList.remove(str);
			}
		}
		return this;
	}

	public boolean contails(String val) {
		return strList.contains(val);
	}

	public Stringer remove(String val) {
		if (contails(val))
			strList.remove(val);
		return this;
	}

	public Stringer add(String val) {
		strList.add(val);
		return this;
	}

	public Stringer addIfNotExist(String val) {
		if (contails(val))
			return this;
		add(val);
		return this;
	}

	public String[] getArrays() {
		return strList.toArray(new String[strList.size()]);
	}

	public List<SToken> getSTokenList() {
		List<SToken> stokens = new ArrayList<SToken>();
		int index = 0;
		for(String str : strList){
			stokens.add(new SToken(str, index++));
		}
		return stokens;
	}

	public boolean isEmpty() {
		return strList.isEmpty();
	}

	public boolean isBlankString() {
		return StringUtils.isBlank(toString());
	}

	public String toString() {
		return StringUtils.join(strList, joinOp);
	}

}
