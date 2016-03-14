package org.onetwo.common.db.filequery;

import java.util.Iterator;
import java.util.List;

public class SimpleSqlFileLineReader {
	final private Iterator<String> lineIterator;
	private int lineNumber;

	public SimpleSqlFileLineReader(List<String> lines) {
		super();
		this.lineIterator = lines.iterator();
	}
	
	public String nextLine(){
		lineNumber++;
		return lineIterator.next().trim();
	}
	
	public boolean hasNextLine(){
		return lineIterator.hasNext();
	}

	public int getLineNumber() {
		return lineNumber;
	}
	
}
