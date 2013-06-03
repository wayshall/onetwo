package org.onetwo.plugins.jdoc.Lexer;

import java.util.List;

import org.onetwo.common.lexer.JLexerUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

public class DocumentInfo {
	
	public static DocumentInfo create(String doc){
		DocumentInfo docInfo = new DocumentInfo(doc);
		docInfo.parse(doc);
		return docInfo;
	}
	
	private String document;
	private final List<DocDirectiveInfo> directives = LangUtils.newArrayList();
	
	public DocumentInfo(String document) {
		this.document = document;
	}
	protected final void parse(String document){
		if(StringUtils.isBlank(document) || document.indexOf("@")==-1)
			return ;
		StringBuilder doc = new StringBuilder();
		char[] chars = document.toCharArray();
		char ch;
		int pos=-1, buf=0;
		for (int i = 0; i < chars.length; i++) {
			ch = chars[i];
			if(ch=='*' || ch=='/'){
				//
			}else if(ch=='@'){
				if(buf>0){
					String str = document.substring(pos, pos+buf);
					addDocDirectiveInfo(str);
				}
				pos=i;
				buf=1;
			}else if(buf!=0){
				buf++;
			}else{
				doc.append(ch);
			}
		}
		if(buf>0){
			String str = document.substring(pos, pos+buf);
			addDocDirectiveInfo(str);
		}
		this.document = doc.toString();
	}
	public String getDocument() {
		return document;
	}
	public List<DocDirectiveInfo> getDirectives() {
		return directives;
	}
	

	public List<DocDirectiveInfo> getDirectiveInfos(DocDirective directive){
		List<DocDirectiveInfo> dis = LangUtils.newArrayList();
		for(DocDirectiveInfo di : this.directives){
			if(di.getDirective()==directive){
				dis.add(di);
			}
		}
		return dis;
	}
	public DocDirectiveInfo getDirectiveInfo(DocDirective directive){
		return getDirectiveInfo(directive, 0);
	}
	public DocDirectiveInfo getDirectiveInfo(DocDirective directive, int index){
		int dindex = 0;
		for(DocDirectiveInfo di : this.directives){
			if(di.getDirective()==directive){
				if(dindex==index)
					return di;
				dindex++;
			}
		}
		return null;
	}
	
	public void addDocDirectiveInfo(String str){
		int nameIndex = -1;
		str = str.trim();
		char[] chars = str.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if(JLexerUtils.isWhiteSpace(chars[i])){
				nameIndex = i;
				break;
			}
		}
		if(nameIndex==-1)
			nameIndex = str.length();
		String name = str.substring(0, nameIndex);
		DocDirective di = null;
		try {
			di = DocDirective.getByName(name);
		} catch (Exception e) {
			return ;
		}
		DocDirectiveInfo info = new DocDirectiveInfo(di);
		info.setDescription(str.substring(nameIndex));
		this.directives.add(info);
	}
	
	public String toString(){
		return document;
	}
}
