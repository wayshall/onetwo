package org.onetwo.common.excel.utils;


public class SimpleExcelExpression {
 
	private final String start;
	private final String end;
	
	
	public SimpleExcelExpression(String start, String end) {
		this.start = start;
		this.end = end;
	}
	
	public boolean isExpresstion(String text) {
		return !ExcelUtils.isBlank(text) && text.indexOf(start) != -1 && text.indexOf(end) != -1;
	}

	
	public String parse(String text, ValueProvider provider) {

		int beginIndex = text.indexOf(start);
		if (beginIndex == -1)
			return text;

		String var = null;
		StringBuilder sb = new StringBuilder();
		int off = 0;
		while (beginIndex != -1) {
			int endIndex = text.indexOf(end, beginIndex);
			if (endIndex == -1)
				break;

			sb.append(text.substring(off, beginIndex));

			var = text.substring(beginIndex + start.length(), endIndex);
			// sb.delete(beginIndex, endIndex+1);
			if (ExcelUtils.isBlank(var)){
				beginIndex = text.indexOf(start, endIndex);
				off = endIndex + end.length();
				continue;
			}
			String value = provider.findString(var);
			// sb.insert(beginIndex, value);
			if (value != null)
				sb.append(value);
			beginIndex = text.indexOf(start, endIndex);
			off = endIndex + end.length();

		}
		if (off < text.length()) {
			sb.append(text.substring(off));
		}
		return sb.toString();
	}
	
	
	public interface ValueProvider {
		
		public String findString(String var);

	}

}
