package org.onetwo.common.lexer;

public interface SourceReader {

	public char readNext();
	
	public boolean isEOF();
	
	public void reset();
}
