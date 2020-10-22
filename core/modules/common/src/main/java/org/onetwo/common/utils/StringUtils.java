package org.onetwo.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RandomStringUtils;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.func.ReturnableClosure;

@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class StringUtils {

	public static final String ENCODING = "UTF-8";
	public static final String EMPTY = "";
	
	private static class ToStringer<T> implements ReturnableClosure<T, String> {
		@Override
		public String execute(T object) {
			return ObjectUtils.toString(object);
		}
	}

	
	/***
	 * 清除不可见unicode
	 * http://www.regular-expressions.info/unicode.html
	 * https://stackoverflow.com/questions/6198986/how-can-i-replace-non-printable-unicode-characters-in-java
	 * @param source
	 * @return
	 */
	public static String cleanInvisibleUnicode(String source){
		if(source.length()<1){
			return source;
		}
		return source.replaceAll("\\p{C}", "");
	}
	public static boolean isNullOrBlankString(Object value){
		return value==null || (String.class.isInstance(value) && org.apache.commons.lang3.StringUtils.isBlank(value.toString()));
	}
	
	public static String emptyIfNull(Object str){
		return str==null?EMPTY:str.toString();
	}

    public static String lowerCase(String str) {
        if (str == null) {
            return EMPTY;
        }
        return str.toLowerCase();
    }
    
	public static String encode(String str) {
		if (StringUtils.isBlank(str))
			return "";
		try {
			return URLEncoder.encode(str, ENCODING);
		} catch (Exception e) {
			return str;
		}
	}

	public static String decode(String str) {
		try {
			return URLDecoder.decode(str, ENCODING);
		} catch (UnsupportedEncodingException e) {
			return str;
		}
	}


	public static boolean isObjectBlank(Object obj) {
		return obj == null || obj.toString().trim().equals("");
	}

	public static boolean isBlank(String str) {
		return str == null || str.trim().equals("");
	}

	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	public static boolean hasLength(CharSequence str) {
		return (str != null && str.length() > 0);
	}

	public static boolean hasLength(String str) {
		return hasLength((CharSequence) str);
	}

	public static String bar2UnderLine(String str) {
		return str.replace('-', '_');
	}

	public static String getLastName(String clsName, String sep) {
		int index = clsName.lastIndexOf(sep);
		if (index != -1)
			return clsName.substring(index);
		else
			return clsName;
	}
	public static String getFirstWord(String str) {
		if(StringUtils.isBlank(str))
			return LangUtils.EMPTY_STRING;
		
		char[] chars = str.toCharArray();
		StringBuilder first = new StringBuilder();
		first.append(chars[0]);
		for (int i = 1; i < chars.length; i++) {
			if(Character.isUpperCase(chars[i])){
				break;
			}else{
				first.append(chars[i]);
			}
		}
		return first.toString();
	}

	public static boolean hasText(CharSequence str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	public static String append(String... strings) {
		if (strings == null || strings.length == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		for (String str : strings)
			sb.append(str);
		return sb.toString();
	}

	public static boolean equals(String str1, String str2) {
		return str1 == null ? str2 == null : str1.equals(str2);
	}

	public static String capitalize(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}
		return new StringBuffer(strLen).append(Character.toTitleCase(str.charAt(0))).append(str.substring(1)).toString();
	}

	public static String uncapitalize(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}
		return new StringBuffer(strLen).append(Character.toLowerCase(str.charAt(0))).append(str.substring(1)).toString();
	}

	public static String toPropertyName(String str) {
		return toCamel(str, false);
	}

	public static String toClassName(String str) {
		return toCamel(str, true);
	}

	public static String toCamel(String str, boolean isFirstUpper) {
		return toCamel(str, '_', isFirstUpper);
	}

	public static String toCamel(String str, char op, boolean isFirstUpper) {
		if (str==null || str.length()==0) {
			return str;
		}
		if (str.indexOf(op) == -1) {
			str = str.toLowerCase();
			if (isFirstUpper && Character.isLowerCase(str.charAt(0))) {
				return str.substring(0, 1).toUpperCase() + str.substring(1);
			} else {
				return str;
			}
		}
		char[] chars = str.toCharArray();
		StringBuilder newStr = new StringBuilder();
		boolean needUpper = isFirstUpper;
		for (int i = 0; i < chars.length; i++) {
			char c = Character.toLowerCase(chars[i]);
			if (needUpper) {
				c = Character.toUpperCase(c);
				needUpper = false;
			}
			if (c == op) {
				needUpper = true;
				continue;
			}
			newStr.append(c);
		}
		return newStr.toString();
	}
	
	/***
	 * 不把本身大写转为小写
	 * @author weishao zeng
	 * @param str
	 * @param op
	 * @param isFirstUpper 是否把第一个字符转为大写
	 * @return
	 */
	public static String toCamelWithoutConvert2LowerCase(String str, char op, boolean isFirstUpper) {
		if (str==null || str.length()==0) {
			return str;
		}
		if (str.indexOf(op) == -1) {
//			str = str.toLowerCase();
			if (isFirstUpper && Character.isLowerCase(str.charAt(0))) {
				return str.substring(0, 1).toUpperCase() + str.substring(1);
			} else {
				return str;
			}
		}
		char[] chars = str.toCharArray();
		StringBuilder newStr = new StringBuilder();
		boolean needUpper = isFirstUpper;
		for (int i = 0; i < chars.length; i++) {
//			char c = Character.toLowerCase(chars[i]);
			char c = chars[i];
			if (needUpper) {
				c = Character.toUpperCase(c);
				needUpper = false;
			}
			if (c == op) {
				needUpper = true;
				continue;
			}
			newStr.append(c);
		}
		return newStr.toString();
	}

	public static boolean hasUpper(String str) {
		char[] chars = str.toCharArray();
		for (char ch : chars) {
			if (Character.isUpperCase(ch))
				return true;
		}
		return false;
	}

	public static String convert2UnderLineName(String name) {
		return convertWithSeperator(name, "_");
	}

	public static String convertWithSeperator(String name, String op) {
		if(name==null)
			return "";
		StringBuffer table = new StringBuffer();
		char[] chars = name.toCharArray();
		table.append(Character.toLowerCase(chars[0]));
		for (int i = 1; i < chars.length; i++) {
			char ch = chars[i];
			if (Character.isUpperCase(ch)) {
				table.append(op);
				ch = Character.toLowerCase(ch);
			}
			table.append(ch);
		}
		return table.toString();
	}

	public static String getSimpleBeanName(String clsName) {
		if (clsName.indexOf('.') == -1)
			return clsName;
		int index = clsName.lastIndexOf('.');
		String sn = clsName.substring(index + 1);
		return uncapitalize(sn);
	}

	public static String getClassShortName(Object obj) {
		Assert.notNull(obj);
		Class<?> cls = ReflectUtils.getObjectClass(obj);
		return uncapitalize(cls.getSimpleName());
	}
	
	/*
	 * from apache common org.apache.commons.lang3.StringUtils 
	 * 
	 * 
	 */

	public static String[] split(String str, char separatorChar) {
		return splitWorker(str, separatorChar, false);
	}

	private static String[] splitWorker(String str, char separatorChar, boolean preserveAllTokens) {
		// Performance tuned for 2.0 (JDK1.4)

		if (str == null) {
			return null;
		}
		int len = str.length();
		if (len == 0) {
			return ArrayUtils.EMPTY_STRING_ARRAY;
		}
		List list = new ArrayList();
		int i = 0, start = 0;
		boolean match = false;
		boolean lastMatch = false;
		while (i < len) {
			if (str.charAt(i) == separatorChar) {
				if (match || preserveAllTokens) {
					list.add(str.substring(start, i));
					match = false;
					lastMatch = true;
				}
				start = ++i;
				continue;
			} else {
				lastMatch = false;
			}
			match = true;
			i++;
		}
		if (match || (preserveAllTokens && lastMatch)) {
			list.add(str.substring(start, i));
		}
		return (String[]) list.toArray(new String[list.size()]);
	}

	public static List<String> splitWithRetainSeparator(String str, String separatorChars, String retainSeparator) {
		if (separatorChars == null) {
			separatorChars = " ";
		}
		separatorChars += trimToEmpty(retainSeparator);
		int len = str.length();
		int wordIndex = 0;
		List<String> strlist = new ArrayList<String>();
		String w = null;
		char ch;
		for (int i = 0; i < len; i++) {
			ch = str.charAt(i);
			if (separatorChars.indexOf(ch) != -1) {
				w = str.substring(wordIndex, i);
				strlist.add(w);
				if (retainSeparator.indexOf(ch) != -1) {
					strlist.add(String.valueOf(ch));
				}
				wordIndex = i + 1;
			}
		}
		if (wordIndex < len) {
			w = str.substring(wordIndex);
			strlist.add(w);
		}
		return strlist;
	}

	public static String[] split(String str, String separatorChars) {
		return org.apache.commons.lang3.StringUtils.split(str, separatorChars);
	}

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}
	


	public static String substring(String str, int beginIndex, int endIndex) {
		return org.apache.commons.lang3.StringUtils.substring(str, beginIndex, endIndex);
	}

	public static String substringAfter(String str, String separator) {
		return org.apache.commons.lang3.StringUtils.substringAfter(str, separator);
	}

	public static String substringBefore(String str, String separator) {
		return org.apache.commons.lang3.StringUtils.substringBefore(str, separator);
	}

	public static String substringBefore(String str, String separator, int fromIndex) {
		return org.apache.commons.lang3.StringUtils.substringBefore(str, separator);
	}

	public static int indexOfAny(String str, String... searchStrs) {
		return org.apache.commons.lang3.StringUtils.indexOfAny(str, searchStrs);
	}

	public static String buildString(Object obj) {
		if (obj == null)
			return "null";
		StringBuilder toString = new StringBuilder("{");
		String str = null;
		if (Collection.class.isAssignableFrom(obj.getClass()) || obj.getClass().isArray()) {
			int index = 0;
			List list = asList(obj);
			if (list == null)
				return "null";
			for (Object o : (Collection) list) {
				str = buildString(o);
				if (StringUtils.isBlank(str))
					continue;
				if (index != 0)
					toString.append(", ");
				toString.append(str);
				index++;
			}
		} else if (Map.class.isAssignableFrom(obj.getClass())) {
			int index = 0;
			for (Object o : ((Map) obj).entrySet()) {
				str = buildString(o);
				if (StringUtils.isBlank(str))
					continue;
				if (index != 0)
					toString.append(", ");
				toString.append(str);
			}
		} else {
			str = obj.toString();
			return "[" + str + "]";
		}
		toString.append("}");
		return toString.toString();
	}

	public static List asList(Object array) {
		if (array == null)
			return null;
		List list = null;
		if (Collection.class.isAssignableFrom(array.getClass())) {
			if (List.class.isAssignableFrom(array.getClass()))
				list = (List) array;
			else {
				list = new ArrayList();
				list.addAll((Collection) array);
			}
		} else if (array.getClass().isArray()) {
			list = new ArrayList();
			for (Object o : (Object[]) array) {
				list.add(o);
			}
		} else {
			list = new ArrayList();
			list.add(array);
		}
		return (list == null || list.isEmpty()) ? null : list;
	}

	public static String join(String separator, Object... array) {
		if (array == null) {
			return EMPTY;
		}
		return join(array, separator, 0, array.length);
	}

	public static String join(Object[] array, String separator) {
		if (array == null) {
			return EMPTY;
		}
		return join(array, separator, 0, array.length);
	}

	public static <T> String join(T[] array, String separator, ReturnableClosure<T, String> it) {
		if (array == null) {
			return EMPTY;
		}
		return join(array, separator, 0, array.length, it);
	}

	public static <T> String join(Iterator<T> iterator, String separator) {
		return join(iterator, separator, new ToStringer<T>());
	}

	public static <T> String join(Iterator<T> iterator, String separator, ReturnableClosure<T, String> it) {
		if (it==null) {
			it = new ToStringer<T>();
		}
		// handle null, zero and one elements before building a buffer
		if (iterator == null) {
			return EMPTY;
		}
		if (!iterator.hasNext()) {
			return EMPTY;
		}
		T first = iterator.next();
		if (!iterator.hasNext()) {
			return it.execute(first);
		}

		// two or more elements
		StringBuilder buf = new StringBuilder(256); // Java default is 16,
													// probably too small
		if (first != null) {
			buf.append(it.execute(first));
		}

//		int index = 0;
		while (iterator.hasNext()) {
			if (separator != null) {
				buf.append(separator);
			}
			T obj = iterator.next();
			if (obj != null) {
				if (it != null)
					buf.append(it.execute(obj));
				else
					buf.append(obj);
			}
//			index++;
		}
		return buf.toString();
	}

	public static String join(Collection<?> collection, String separator) {
		if (collection == null) {
			return EMPTY;
		}
		return join(collection.iterator(), separator);
	}

	public static <K, V> String join(Map<K, V> map, String separator) {
		return join(map, ":", separator);
	}
	
	public static <K, V> String join(Map<K, V> map, String entrySeparator, String separator) {
		return join(map.entrySet(), separator, entry -> {
			return entry.getKey().toString() + entrySeparator + entry.getValue().toString();
		});
	}
	
	public static <T> String join(Collection<T> collection, String separator, ReturnableClosure<T, String> it) {
		if (collection == null) {
			return EMPTY;
		}
		return join(collection.iterator(), separator, it);
	}

	public static <T> String join(T[] array, String separator, int startIndex, int endIndex) {
		return join(array, separator, startIndex, endIndex, null);
	}

	public static <T> String join(T[] array, String separator, int startIndex, int endIndex, ReturnableClosure<T, String> it) {
		if (array == null) {
			return EMPTY;
		}
		if (separator == null) {
			separator = EMPTY;
		}

		// endIndex - startIndex > 0: Len = NofStrings *(len(firstString) +
		// len(separator))
		// (Assuming that all Strings are roughly equally long)
		int bufSize = (endIndex - startIndex);
		if (bufSize <= 0) {
			return EMPTY;
		}

		bufSize *= ((array[startIndex] == null ? 16 : array[startIndex].toString().length()) + separator.length());

		StringBuffer buf = new StringBuffer(bufSize);

		for (int i = startIndex; i < endIndex; i++) {
			if (i > startIndex) {
				buf.append(separator);
			}
			if (array[i] != null) {
				// buf.append(array[i]);
				if (it != null)
					buf.append(it.execute(array[i]));
				else
					buf.append(array[i]);
			}
		}
		return buf.toString();
	}

	public static String unicodeToString(String str) {
		Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
		Matcher matcher = pattern.matcher(str);
		char ch;
		while (matcher.find()) {
			ch = (char) Integer.parseInt(matcher.group(2), 16);
			str = str.replace(matcher.group(1), ch + "");
		}
		return str;
	}

	public static String stringToUnicode(String str) {
		String result = "";
		for (int i = 0; i < str.length(); i++) {
			result += "\\u" + Integer.toHexString(str.charAt(i));
		}
		return result;
	}

	public static String trimToEmpty(Object str) {
		return str == null ? EMPTY : str.toString().trim();
	}

	public static String[] trim(String[] strs) {
		List<String> strList = new ArrayList<String>(strs.length);
		for (String str : strs) {
			if (str == null)
				continue;
			strList.add(str.trim());
		}
		return strList.toArray(new String[strList.size()]);
	}

	public static void addWithTrim(Collection collections, boolean ignoreBlank, String... strs) {
		for (String str : strs) {
			if (ignoreBlank) {
				if (isBlank(str))
					continue;
			} else {
				// defaut is ignore null
				if (str == null)
					continue;
			}
			collections.add(str.trim());
		}
	}

	public static void addWithTrim(Collection collections, String str, String op) {
		String[] strs = split(str, op);
		addWithTrim(collections, false, strs);
	}

	public static String defaultValue(String val, String def){
		if(isBlank(val))
			return def;
		return val;
	}

	public static String defaultValues(String val, String... defs){
		if(isBlank(val)){
			for(String def : defs){
				if(isNotBlank(def))
					return def;
			}
		}
		return val;
	}

	public static String firstNotBlank(Object... defs){
		if(LangUtils.isEmpty(defs))
			return LangUtils.EMPTY_STRING;
		for(Object def : defs){
			if(def!=null && isNotBlank(def.toString()))
				return def.toString();
		}
		return LangUtils.EMPTY_STRING;
	}

	public static boolean matchPrefix(String key, String... prefixs) {
		for (String prefix : prefixs) {
			if(StringUtils.isBlank(prefix))
				continue;
			VerySimpleStartMatcher match = VerySimpleStartMatcher.create(prefix);
			if (match.match(key)) {
				return true;
			}
		}
		return false;
	}

	public static String trimStartWith(String path, String prefix) {
		/*if (path == null)
			path = EMPTY;
		while (path.startsWith(prefix)) {
			path = path.substring(prefix.length(), path.length());
		}
		return path;*/
		return trimLeft(path, prefix);
	}

	public static String surroundWith(String path, String prefix) {
		if (path == null)
			path = EMPTY;
		return appendEndWith(appendStartWith(path, prefix), prefix);
	}
	

	public static String appendStartWithSlash(String path) {
		return appendStartWith(path, FileUtils.SLASH);
	}

	public static String appendStartWith(String path, String prefix) {
		if (path == null)
			path = EMPTY;
		if (!path.startsWith(prefix)) {
			path = prefix + path;
		}
		return path;
	}

	public static String appendEndWithSlash(String path) {
		return appendEndWith(path, FileUtils.SLASH);
	}
	
	public static String appendEndWith(String path, String postfix) {
		if (path == null)
			path = EMPTY;
		if (path.endsWith(postfix)) {
			return path;
		}
		return path + postfix;
	}
	public static String appendArroundWith(final String str, String appendString) {
		String newString = null;
		if (str == null)
			newString = EMPTY;
		newString = appendStartWith(str, appendString);
		newString = appendEndWith(newString, appendString);
		return newString;
	}
	public static String getSqlLikeString(final String str) {
		return StringUtils.appendArroundWith(str, "%");
	}

	public static String trimEndWith(String path, String postfix) {
		/*if (path == null)
			path = EMPTY;
		while (path.endsWith(postfix)) {
//			return path.substring(0, path.length() - postfix.length());
			path = path.substring(0, path.length() - postfix.length());
		}
		return path;*/
		return trimRight(path, postfix);
	}

	public static String ellipsis(String source, int size, String ellipsisStr) {
		if (isBlank(source))
			return "";
		if (source.length() > size) {
			return source.substring(0, size) + ellipsisStr;
		} else {
			return source;
		}
	}

	public static String getFirstNotBlank(String... strs) {
		if (LangUtils.isEmpty(strs))
			return EMPTY;
		for (String str : strs) {
			if (isNotBlank(str))
				return str;
		}
		return EMPTY;
	}



    public static String trimRight(String text, String trimstr) {
        if(isBlank(text) || isBlank(trimstr))
        	return text;
        String rs = text;
        while(rs.endsWith(trimstr)){
        	rs = rs.substring(0, rs.length()-trimstr.length());
        }
        return rs;
    }
    public static String trimLeft(String text, String trimstr) {
        if(isBlank(text) || isBlank(trimstr))
        	return text;
        String rs = text;
        int index = 0;
        while(rs.startsWith(trimstr, index)){
//        	rs = rs.substring(trimstr.length());
        	index += trimstr.length();
        }
        rs = index>0?rs.substring(index, rs.length()):rs;
        return rs;
    }

    public static String trim(String text, String trimstr) {
        if(isBlank(text) || isBlank(trimstr))
        	return text;
        return trimRight(trimLeft(text, trimstr), trimstr);
    }

    public static String replaceEach(String text, String search, String replacement) {
        return replaceEach(text, new String[]{search}, new String[]{replacement});
    }
    

    public static String replaceEach(String text, String[] searchList, String[] replacementList) {
        return org.apache.commons.lang3.StringUtils.replaceEach(text, searchList, replacementList);
    }
    

    public static String stripStart(final String str, final String stripChars) {
        return org.apache.commons.lang3.StringUtils.stripStart(str, stripChars);
    }
    

    public static String stripEnd(final String str, final String stripChars) {
        return org.apache.commons.lang3.StringUtils.stripEnd(str, stripChars);
    }
    
    /****
     * no space
     * @author wayshall
     * @param count
     * @return
     */
    public static String randomAscii(final int count) {
        return RandomStringUtils.random(count, 33, 127, false, false);
    }
    
    
	public static void main(String[] args) {

	}
}
