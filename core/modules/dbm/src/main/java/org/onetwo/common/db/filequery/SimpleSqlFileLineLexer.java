package org.onetwo.common.db.filequery;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.utils.StringUtils;

import com.google.common.collect.ImmutableList;

public class SimpleSqlFileLineLexer {

//    public final static byte EOI = 0x1A;//eof
    
	public static final String GLOBAL_NS_KEY = "global";
	public static final String COMMENT = "--";
	public static final String STAR = "*";
	public static final String MULTIP_COMMENT_START = "/*";
	public static final String MULTIP_COMMENT_END = "*/";
	public static final String CONFIG_PREFIX = "--@@";
	public static final String NAME_PREFIX = "--@";
	public static final String EQUALS_MARK = "=";
	
	public static enum LineToken {
		ONE_LINE_COMMENT,
		MULTIP_COMMENT,
		CONTENT,
		EOF
	}
	
	final private SimpleSqlFileLineReader lineReader;
	private String nextLine;
	private List<String> lineBuf = new ArrayList<String>();
	private LineToken lineToken;
	
	public SimpleSqlFileLineLexer(SimpleSqlFileLineReader lineReader) {
		super();
		this.lineReader = lineReader;
		scanNextLine();
	}

	public boolean nextLineToken(){
		lineBuf.clear();
		while(true){
			if(isEOF()){
				return false;
			}
			
			if(isBlankLine()){
				scanNextLine();
				continue;
				
			}else if(isOneCommentStart()){
				scanOneLineComment();
				scanNextLine();
//				return true;
				continue;
				
			}else if(isMultipCommentStart()){
				scanMultipComment();
				scanNextLine();
				return true;
				
			}else{
				scanConetnt();
				return true;
			}
		}
	}
	
	protected boolean isBlankLine(){
		return StringUtils.isBlank(nextLine);
	}
	
	protected boolean isOneCommentStart(){
		return nextLine.startsWith(COMMENT);
	}
	
	protected boolean isMultipCommentStart(){
		return nextLine.startsWith(MULTIP_COMMENT_START);
	}
	
	
	protected void scanConetnt(){
//		saveToBuf();
		while(!isEOF()){
//			if(isBlankLine() || isOneCommentStart() || isMultipCommentStart() ){
			if(isBlankLine() || isOneCommentStart() || isMultipCommentStart() ){
				break;
			}
			saveToBuf();
			scanNextLine();
		}
		this.lineToken = LineToken.CONTENT;
	}
	
	protected void scanOneLineComment(){
		this.lineToken = LineToken.ONE_LINE_COMMENT;
	}
	
	protected void scanMultipComment(){
//		saveToBuf();
		while(!isEOF()){
			if(nextLine.endsWith(MULTIP_COMMENT_END)){
				break;
			}
			trimStar();
			saveToBuf();
			scanNextLine();
		}
		this.lineToken = LineToken.MULTIP_COMMENT;
	}
	
	protected void trimStar(){
		String content = StringUtils.trimLeft(nextLine.trim(), MULTIP_COMMENT_START);
		content = StringUtils.trimRight(content, MULTIP_COMMENT_END);
		content = StringUtils.trim(content, STAR).trim();
		this.nextLine = content;
	}

	protected boolean isReaderEOF(){
		return !lineReader.hasNextLine();
	}

	protected boolean isEOF(){
		return this.lineToken==LineToken.EOF;
	}
	
	protected void scanNextLine(){
		if(isReaderEOF()){
			this.lineToken = LineToken.EOF;
//			this.nextLine = Byte.toString(EOI);
			this.nextLine = "";
			return ;
		}
		this.nextLine = lineReader.nextLine().trim();
	}
	
	final protected void saveToBuf(){
		this.lineBuf.add(nextLine);
	}

	public LineToken getLineToken() {
		return lineToken;
	}

	public List<String> getLineBuf() {
		return ImmutableList.copyOf(lineBuf);
	}

	public SimpleSqlFileLineReader getLineReader() {
		return lineReader;
	}

	public String getNextLine() {
		return nextLine;
	}
	
}
