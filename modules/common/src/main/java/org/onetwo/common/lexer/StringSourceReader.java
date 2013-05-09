package org.onetwo.common.lexer;


public class StringSourceReader implements SourceReader {

	private String text;
	private int pos = 0;
	
	/*public StringSourceReader(File file){
		final StringBuilder sb = new StringBuilder();
		FileUtils.reader(FileUtils.asBufferedReader(file), new FileLineCallback() {
			
			@Override
			public boolean doWithLine(String line, int lineIndex) {
				sb.append(line).append('\n');
				return true;
			}
		});
		this.text = sb.toString();
	}
	*/
	public StringSourceReader(String text) {
		super();
		this.text = text;
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
		pos = 0;
	}

}
