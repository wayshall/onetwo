package org.onetwo.common.web.solr;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.CharReader;
import org.apache.solr.analysis.HTMLStripCharFilter;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.StringUtils;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class HitCounter {
	private static Logger LOGGER = Logger.getLogger(HitCounter.class);
	
	private static Comparator<Map.Entry<String, Integer>> COMPARATOR = new Comparator<Map.Entry<String, Integer>>(){
		@Override
		public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
			return -(o1.getValue()-o2.getValue());
		}
	};
	
	protected Map<String, Integer> map = new LinkedHashMap<String, Integer>();
	
	protected String text;
	
	private boolean changed = false;
	
	private boolean useSmart;
	
	public HitCounter(boolean useSmart, String... text){
		this.useSmart = useSmart;
		this.text = MyUtils.append(text);
		count();
	}
	
	public HitCounter(String... text){
		this(false, text);
	}
	
	public void setChanged() {
		this.changed = true;
	}

	public boolean isChanged() {
		return changed;
	}

	protected void count(){
		try {
			StringReader sr = new StringReader(text);
		    Reader in = new HTMLStripCharFilter(CharReader.get(sr));
		    IKSegmenter ikseg = new IKSegmenter(in, useSmart);
			
			Lexeme lex;
			lex = ikseg.next();
			String word = null;
			while(lex!=null){
				word = lex.getLexemeText();
				hit(word);
				lex = ikseg.next();
			}
		} catch (IOException e) {
			LOGGER.error("count error!", e);
		}
	}

    protected Integer hit(String key) {
    	if(StringUtils.isBlank(key) || key.length()==1)
    		return null;
    	Integer count = map.get(key);
    	if(count==null){
    		count = Integer.valueOf(1);
    		map.put(key, count);
    	}else{
    		count++;
    		map.put(key, count);
    	}
    	setChanged();
		return count;
	}
    
    public List<Map.Entry<String, Integer>> getTop(int count){
    	List<Map.Entry<String, Integer>> keys = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
    	if(isChanged())
    		Collections.sort(keys, COMPARATOR);
    	if(count>keys.size())
    		count = keys.size();
    	List<Map.Entry<String, Integer>> subkeys = keys.subList(0, count);
    	return subkeys;
    }
    
    public List<String> getTopWord(int count){
    	List<Map.Entry<String, Integer>> tops = getTop(count);
    	if(tops==null || tops.isEmpty())
    		return null;
    	List<String> topWords = new ArrayList<String>(tops.size());
    	for(Map.Entry<String, Integer> entry : tops){
    		topWords.add(entry.getKey());
    	}
    	return topWords;
    }
    
    public String getTopWordString(int count){
    	return getTopWordString(count, " ");
    }
    
    public String getTopWordString(int count, String separetor){
    	List<Map.Entry<String, Integer>> tops = getTop(count);
    	if(tops==null || tops.isEmpty())
    		return null;
    	if(separetor==null)
    		separetor = " ";
    	StringBuilder str = new StringBuilder();
    	int index = 0;
    	for(Map.Entry<String, Integer> entry : tops){
    		if(index!=0)
    			str.append(separetor);
    		str.append(entry.getKey());
    		index++;
    	}
    	return str.toString();
    }
    

	public static void main(String[] args) throws Exception {

	}
}
