package org.onetwo.common.lexer;

import java.io.File;

import org.onetwo.common.file.FileLineCallback;
import org.onetwo.common.file.FileUtils;

public class FileSourceReader implements SourceReader {

	private String text;
	private int pos;
	private File file;
	
	public FileSourceReader(File file){
		this.file = file;
		this.text = readAsString(file);
	}

	protected final String readAsString(File file){
		final StringBuilder sb = new StringBuilder();
		FileUtils.reader(FileUtils.asBufferedReader(file), new FileLineCallback() {
			
			@Override
			public boolean doWithLine(String line, int lineIndex) {
				sb.append(line).append('\n');
				return true;
			}
		});
		return sb.toString();
	}
	@Override
	public char readNext() {
		return text.charAt(pos++);
	}
	
	public boolean isEOF(){
		if(pos>=text.length())
			return true;
		return false;
	}

	@Override
	public void reset() {
		this.text = readAsString(file);
		pos = 0;
	}

}
