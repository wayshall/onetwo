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

@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class StringUtils {

	public static final String ENCODING = "UTF-8";
	public static final String EMPTY = "";


    public static String lowerCase(String str) {
        if (str == null) {
            return null;
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
		return toJavaName(str, false);
	}

	public static String toClassName(String str) {
		return toJavaName(str, true);
	}

	public static String toJavaName(String str, boolean isFirstUpper) {
		return toJavaName(str, '_', isFirstUpper);
	}

	public static String toJavaName(String str, char op, boolean isFirstUpper) {
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

	public static boolean hasUpper(String str) {
		char[] chars = str.toCharArray();
		for (char ch : chars) {
			if (Character.isUpperCase(ch))
				return true;
		}
		return false;
	}

	public static String convert2UnderLineName(String name) {
		return convert2UnderLineName(name, "_");
	}

	public static String convert2UnderLineName(String name, String op) {
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
		return splitWorker(str, separatorChars, -1, false);
	}

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static String substringAfter(String str, String separator) {
		if (isEmpty(str)) {
			return str;
		}
		if (separator == null) {
			return EMPTY;
		}
		int pos = str.indexOf(separator);
		if (pos == -1) {
			return EMPTY;
		}
		return str.substring(pos + separator.length());
	}

	public static String substringBefore(String str, String separator) {
		if (isEmpty(str) || separator == null) {
			return str;
		}
		if (separator.length() == 0) {
			return EMPTY;
		}
		int pos = str.indexOf(separator);
		if (pos == -1) {
			return str;
		}
		return str.substring(0, pos);
	}

	public static String[] splitWorker(String str, String separatorChars, int max, boolean preserveAllTokens) {
		// Performance tuned for 2.0 (JDK1.4)
		// Direct code is quicker than StringTokenizer.
		// Also, StringTokenizer uses isSpace() not isWhitespace()

		if (str == null) {
			return null;
		}
		int len = str.length();
		if (len == 0) {
			return ArrayUtils.EMPTY_STRING_ARRAY;
		}
		List list = new ArrayList();
		int sizePlus1 = 1;
		int i = 0, start = 0;
		boolean match = false;
		boolean lastMatch = false;
		if (separatorChars == null) {
			// Null separator means use whitespace
			while (i < len) {
				if (Character.isWhitespace(str.charAt(i))) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				} else {
					lastMatch = false;
				}
				match = true;
				i++;
			}
		} else if (separatorChars.length() == 1) {
			// Optimise 1 character case
			char sep = separatorChars.charAt(0);
			while (i < len) {
				if (str.charAt(i) == sep) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				} else {
					lastMatch = false;
				}
				match = true;
				i++;
			}
		} else {
			// standard case
			while (i < len) {
				if (separatorChars.indexOf(str.charAt(i)) >= 0) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				} else {
					lastMatch = false;
				}
				match = true;
				i++;
			}
		}
		if (match || (preserveAllTokens && lastMatch)) {
			list.add(str.substring(start, i));
		}
		return (String[]) list.toArray(new String[list.size()]);
	}

	public static int indexOfAny(String str, String... searchStrs) {
		if ((str == null) || (searchStrs == null)) {
			return -1;
		}
		int sz = searchStrs.length;

		// String's can't have a MAX_VALUEth index.
		int ret = Integer.MAX_VALUE;

		int tmp = 0;
		for (int i = 0; i < sz; i++) {
			String search = searchStrs[i];
			if (search == null) {
				continue;
			}
			tmp = str.indexOf(search);
			if (tmp == -1) {
				continue;
			}

			if (tmp < ret) {
				ret = tmp;
			}
		}

		return (ret == Integer.MAX_VALUE) ? -1 : ret;
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

	public static String join(Object[] array, String separator) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length);
	}

	public static String join(Object[] array, String separator, SimpleBlock<Object, String> it) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length, it);
	}

	public static String join(Iterator iterator, String separator) {
		return join(iterator, separator, null);
	}

	public static String join(Iterator iterator, String separator, SimpleBlock<Object, String> it) {

		// handle null, zero and one elements before building a buffer
		if (iterator == null) {
			return null;
		}
		if (!iterator.hasNext()) {
			return EMPTY;
		}
		Object first = iterator.next();
		if (!iterator.hasNext()) {
			return ObjectUtils.toString(first);
		}

		// two or more elements
		StringBuilder buf = new StringBuilder(256); // Java default is 16,
													// probably too small
		if (first != null) {
			buf.append(first);
		}

//		int index = 0;
		while (iterator.hasNext()) {
			if (separator != null) {
				buf.append(separator);
			}
			Object obj = iterator.next();
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

	public static String join(Collection collection, String separator) {
		if (collection == null) {
			return null;
		}
		return join(collection.iterator(), separator);
	}

	public static String join(Collection collection, String separator, SimpleBlock<Object, String> it) {
		if (collection == null) {
			return null;
		}
		return join(collection.iterator(), separator, it);
	}

	public static String join(Object[] array, String separator, int startIndex, int endIndex) {
		return join(array, separator, startIndex, endIndex, null);
	}

	public static String join(Object[] array, String separator, int startIndex, int endIndex, SimpleBlock<Object, String> it) {
		if (array == null) {
			return null;
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

	public static String UnicodeToString(String str) {
		Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
		Matcher matcher = pattern.matcher(str);
		char ch;
		while (matcher.find()) {
			ch = (char) Integer.parseInt(matcher.group(2), 16);
			str = str.replace(matcher.group(1), ch + "");
		}
		return str;
	}

	public static String StringToUnicode(String str) {
		String result = "";
		for (int i = 0; i < str.length(); i++) {
			result += "\\u" + Integer.toHexString(str.charAt(i));
		}
		return result;
	}

	public static String trimToEmpty(String str) {
		return str == null ? EMPTY : str.trim();
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

	public static String firstNotBlank(String... defs){
		if(LangUtils.isEmpty(defs))
			return LangUtils.EMPTY_STRING;
		for(String def : defs){
			if(isNotBlank(def))
				return def;
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
		if (path == null)
			path = EMPTY;
		if (path.startsWith(prefix)) {
			return path.substring(prefix.length(), path.length());
		}
		return path;
	}

	public static String appendStartWith(String path, String prefix) {
		if (path == null)
			path = EMPTY;
		if (!path.startsWith(prefix)) {
			path = prefix + path;
		}
		return path;
	}

	public static String appendEndWith(String path, String postfix) {
		if (path == null)
			path = EMPTY;
		if (path.endsWith(postfix)) {
			return path;
		}
		return path + postfix;
	}

	public static String trimEndWith(String path, String postfix) {
		if (path == null)
			path = EMPTY;
		if (path.endsWith(postfix)) {
			return path.substring(0, path.length() - postfix.length());
		}
		return path;
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

    public static String replaceEach(String text, String[] searchList, String[] replacementList) {
        return replaceEach(text, searchList, replacementList, false, 0);
    }
    
	private static String replaceEach(
            String text, String[] searchList, String[] replacementList, boolean repeat, int timeToLive) {

        // mchyzer Performance note: This creates very few new objects (one major goal)
        // let me know if there are performance requests, we can create a harness to measure

        if (text == null || text.length() == 0 || searchList == null ||
                searchList.length == 0 || replacementList == null || replacementList.length == 0) {
            return text;
        }

        // if recursing, this shouldn't be less than 0
        if (timeToLive < 0) {
            throw new IllegalStateException("Aborting to protect against StackOverflowError - " +
                                            "output of one loop is the input of another");
        }

        int searchLength = searchList.length;
        int replacementLength = replacementList.length;

        // make sure lengths are ok, these need to be equal
        if (searchLength != replacementLength) {
            throw new IllegalArgumentException("Search and Replace array lengths don't match: "
                + searchLength
                + " vs "
                + replacementLength);
        }

        // keep track of which still have matches
        boolean[] noMoreMatchesForReplIndex = new boolean[searchLength];

        // index on index that the match was found
        int textIndex = -1;
        int replaceIndex = -1;
        int tempIndex = -1;

        // index of replace array that will replace the search string found
        // NOTE: logic duplicated below START
        for (int i = 0; i < searchLength; i++) {
            if (noMoreMatchesForReplIndex[i] || searchList[i] == null ||
                    searchList[i].length() == 0 || replacementList[i] == null) {
                continue;
            }
            tempIndex = text.indexOf(searchList[i]);

            // see if we need to keep searching for this
            if (tempIndex == -1) {
                noMoreMatchesForReplIndex[i] = true;
            } else {
                if (textIndex == -1 || tempIndex < textIndex) {
                    textIndex = tempIndex;
                    replaceIndex = i;
                }
            }
        }
        // NOTE: logic mostly below END

        // no search strings found, we are done
        if (textIndex == -1) {
            return text;
        }

        int start = 0;

        // get a good guess on the size of the result buffer so it doesn't have to double if it goes over a bit
        int increase = 0;

        // count the replacement text elements that are larger than their corresponding text being replaced
        for (int i = 0; i < searchList.length; i++) {
            if (searchList[i] == null || replacementList[i] == null) {
                continue;
            }
            int greater = replacementList[i].length() - searchList[i].length();
            if (greater > 0) {
                increase += 3 * greater; // assume 3 matches
            }
        }
        // have upper-bound at 20% increase, then let Java take over
        increase = Math.min(increase, text.length() / 5);

        StringBuilder buf = new StringBuilder(text.length() + increase);

        while (textIndex != -1) {

            for (int i = start; i < textIndex; i++) {
                buf.append(text.charAt(i));
            }
            buf.append(replacementList[replaceIndex]);

            start = textIndex + searchList[replaceIndex].length();

            textIndex = -1;
            replaceIndex = -1;
            tempIndex = -1;
            // find the next earliest match
            // NOTE: logic mostly duplicated above START
            for (int i = 0; i < searchLength; i++) {
                if (noMoreMatchesForReplIndex[i] || searchList[i] == null ||
                        searchList[i].length() == 0 || replacementList[i] == null) {
                    continue;
                }
                tempIndex = text.indexOf(searchList[i], start);

                // see if we need to keep searching for this
                if (tempIndex == -1) {
                    noMoreMatchesForReplIndex[i] = true;
                } else {
                    if (textIndex == -1 || tempIndex < textIndex) {
                        textIndex = tempIndex;
                        replaceIndex = i;
                    }
                }
            }
            // NOTE: logic duplicated above END

        }
        int textLength = text.length();
        for (int i = start; i < textLength; i++) {
            buf.append(text.charAt(i));
        }
        String result = buf.toString();
        if (!repeat) {
            return result;
        }

        return replaceEach(result, searchList, replacementList, repeat, timeToLive - 1);
    }

	public static void main(String[] args) {

	}
}
