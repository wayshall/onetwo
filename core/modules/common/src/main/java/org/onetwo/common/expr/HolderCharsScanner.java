package org.onetwo.common.expr;

public class HolderCharsScanner {
	
	public static HolderCharsScanner holder(String source){
		return new HolderCharsScanner(source);
	}
	
	public static interface RelacementProvider {
		public String getHolderValue(int index);
	}
	
	final private String holderChars;

	private HolderCharsScanner(String source) {
		super();
		this.holderChars = source;
	}
	
	public String parse(String source, RelacementProvider provider){
		int varIndex = 0;
		StringBuilder sb = new StringBuilder();
		int beginIndex = source.indexOf(holderChars);
		int off = 0;
		int size = holderChars.length();
		while(beginIndex != -1){
			sb.append(source.substring(off, beginIndex));
			String val = provider.getHolderValue(varIndex++);
			sb.append(val);
			
			off = beginIndex + size;
			beginIndex = source.indexOf(holderChars, off);
		}
		String tail = source.substring(off, source.length());
		sb.append(tail);
		
		return sb.toString();
	}

}
