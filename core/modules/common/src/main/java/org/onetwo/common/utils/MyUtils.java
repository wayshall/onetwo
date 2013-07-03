package org.onetwo.common.utils;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.onetwo.common.db.ExtQueryUtils;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.list.L;

@SuppressWarnings("unchecked")
public class MyUtils {
	protected static Logger logger = Logger.getLogger(MyUtils.class);

	public static final char CHOMP = '\r';
	public static final char LINE = '\n';
	public static final char SPACE = ' ';
	public static final String HTML_BLANK = "&nbsp;";
	public static final String HTML_NEWLINE = "<br/>";
	public static final String USER_REGEX = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
	public static final Pattern USERNAME_PATTER = Pattern.compile(USER_REGEX);

	private MyUtils() {
	}
/*
	public static <T> T convertValue(Object value, Class<T> toType){
		return (T)OgnlOps.convertValue(value, toType);
	}*/
	
	public static Object getValue(String expression, Object root){
		return getValue(root, expression);
	}
	
	public static Object getValue(Object root, String expression){
		try {
			return ReflectUtils.getExpr(root, expression);
		} catch (Exception e){
			String msg = "get value error : [expression=" + expression + ", root="+root.getClass()+", msg="+e.getMessage()+"]";
			throw new ServiceException(msg, e);
		}
	}
	
	/*public static Object getValue(Object root, String expression){
		try {
			return Ognl.getValue(expression, root);
		} catch (OgnlException e) {
			logger.error("get value error : [expression=" + expression + ", root="+root.getClass()+"]", e);
		} catch (Exception e){
			String msg = "get value error : [expression=" + expression + ", root="+root.getClass()+", msg="+e.getMessage()+"]";
			throw new ServiceException(msg, e);
		}
		return null;
	}*/
	
	/*public static void setValue(Object root, String expression, Object value){
		try {
			Ognl.setValue(expression, root, value);
		} catch (OgnlException e) {
			logger.error("set value error : [expression=" + expression + ", root="+root.getClass()+"]", e);
		}
	}*/
	
	public static void setValue(Object root, String expression, Object value){
		try {
			ReflectUtils.setExpr(root, expression, value);
		} catch (Exception e) {
			logger.error("set value error : [expression=" + expression + ", root="+root.getClass()+"]", e);
		}
	}

	

	public static <T> T simpleConvert(Object val, Class<T> toType){
		return simpleConvert(val, toType, null);
	}

	public static <T> T simpleConvert(Object val, Class<T> toType, T def){
		if(val==null)
			return def;
		Object newValue = null;
		if(toType==null)
			toType = (Class<T>)String.class;
		
		if(toType.isAssignableFrom(val.getClass()))
			return (T)val;
		
		if(val.getClass().isArray()){
			if(Array.getLength(val)==0)
				return def;
			val = Array.get(val, 0);
			if(val==null)
				return def;
			return simpleConvert(val, toType, def);
		}
		String strVal = val.toString();
		if(toType==Long.class || toType==long.class){
			newValue = NumberUtils.toLong(strVal);
		}else if(toType==Integer.class || toType==int.class){
			newValue = NumberUtils.toInt(strVal);
		}else if(toType==Double.class || toType==double.class){
			newValue = NumberUtils.toDouble(strVal);
		}else if(toType==Float.class || toType==float.class){
			newValue = NumberUtils.toFloat(strVal);
		}else if(toType==Boolean.class || toType==boolean.class){
			newValue = BooleanUtils.toBooleanObject(strVal);
		}else if(toType==String.class){
			newValue = val.toString();
		}else{
			newValue = val;
		}
		return (T)newValue;
	}


	public static boolean checkIdValid(Serializable id){
		boolean rs;
		if(id==null)
			rs = false;
		else if(id instanceof Number && ((Number)id).longValue()<1)
			rs = false;
		else
			rs = true;
		return rs;
	}

    public static String getFileExtName(String filepath) {
    	if(filepath.indexOf('.')!=-1)
    		return filepath.substring(filepath.lastIndexOf(".") + 1, filepath.length());
        return "";
    }

	public static String getCountSql(String sql){
		return getCountSql(sql, "id");
	}

	public static String getCountSql(String sql, String rep){
		String hql = sql;
		if(StringUtils.isBlank(rep))
			rep = "*";

		if(hql.indexOf("{")!=-1 || hql.indexOf("}")!=-1)
			hql = hql.replace("{", "").replace("}", "");
		
		if(hql.indexOf(" group by ")!=-1 || hql.indexOf(" distinct ")!=-1){
			int index = rep.lastIndexOf('.');
			rep = rep.substring(index+1);
			hql = "select count(count_entity." + rep + ") from (" + hql + ") count_entity ";
		}else{
			hql = StringUtils.substringAfter(hql, "from ");
			hql = StringUtils.substringBefore(hql, " order by ");
			hql = "select count(" + rep + ") from " + hql;
		}
		return hql;
	}

	public final static String htmlEncode(String s) {
		if (StringUtils.isBlank(s))
			return "";

		StringBuilder str = new StringBuilder();

		for (int j = 0; j < s.length(); j++) {
			char c = s.charAt(j);

			// encode standard ASCII characters into HTML entities where needed
			if (c < '\200') {
				switch (c) {
				case '"':
					str.append("&quot;");

					break;

				case '&':
					str.append("&amp;");

					break;

				case '<':
					str.append("&lt;");

					break;

				case '>':
					str.append("&gt;");

					break;

				case '\n':
					str.append("<BR/>");

					/*
					 * case ' ':
					 * if(Locale.CHINA.getLanguage().equals(StrutsUtils.getCurrentSessionLocale().getLanguage()))
					 * str.append(" "); else str.append(" "); break;
					 */

				default:
					str.append(c);
				}
			} else if (c < '\377') {
				String hexChars = "0123456789ABCDEF";
				int a = c % 16;
				int b = (c - a) / 16;
				String hex = "" + hexChars.charAt(b) + hexChars.charAt(a);
				str.append("&#x" + hex + ";");
			} else {
				str.append(c);
			}
		}

		return str.toString();
	}


	public static Map<Object, Object> convertParamMap(Object... params) {
		return CUtils.asMap(params);
	}
	

	public static String getLikeString(String str) {
		return ExtQueryUtils.getLikeString(str);
	}

	public static String appendPathSeparator(String path) {
		if (!path.endsWith(String.valueOf(File.separatorChar)))
			path += File.separatorChar;
		return path;
	}

	public static String append(String... strings) {
		return LangUtils.append(strings);
	}

	public static boolean isAllowUserName(String userName) {
		return USERNAME_PATTER.matcher(userName).matches();
	}
	
	public static List asList(Object array) {
		return L.exclude(array);
	}

	public static List asListExclude(Object array, Class...excludeClasses) {
		return L.exclude(array, (Object[])excludeClasses);
	}

	public static int getSize(Object array) {
		int size = 0;
		if (array == null)
			return size;
		if (array instanceof Collection) {
			size = ((Collection) array).size();
		} else if (array.getClass().isArray()) {
			size = Array.getLength(array);
		} else if (array instanceof Map) {
			size = ((Map) array).size();
		}
		return size;
	}

	public static boolean isArray(Object value) {
		return value != null && (value instanceof Collection || value.getClass().isArray());
	}

	public static Collection stripNull(Collection collection, final Object...stripValue) {
		return L.strip(collection, stripValue);
	}

	public static List array2List(Object[] array) {
		if (array == null || array.length < 1)
			return null;
		List list = new ArrayList();
		for (Object obj : array) {
			if (L.NullOrEmptyTrue.evaluate(obj))
				continue;
			list.add(obj);
		}
		return list;
	}

	public static String generateTokenStr(String... strs) {
		/*String s = MyUtils.append(strs);
		MD5 md5 = new MD5();
		return md5.calcMD5(s + new Date().getTime() + getRadomString(6));*/
		return LangUtils.generateToken(strs);
	}
	

	public static String getRadomString(int length) {
		  return LangUtils.getRadomString(length);
	}
	
	public static String subString(String s, int length) {
		return subString(s, length, 1);
	}
	
	public static String subString(String s, int length ,int charBytes) {

		if(s == null || s.length() == 0) {
			return s;
		}
		
        byte[] bytes;
		try {
			bytes = s.getBytes("Unicode");
		} catch (UnsupportedEncodingException e) {
			throw new ServiceException(e);
		}
        int n = 0; // 表示当前的字节数
        int i = 3; // 要截取的字节数，从第4个字节开始
        boolean isAppend = false;//是否要追加".."
        if(bytes.length > length * charBytes){
        	length = length-3;
        	isAppend = true;
        }
        for (; i < bytes.length && n < length; i++)
        {
            // 奇数位置，如3、5、7等，为UCS2编码中两个字节的第二个字节
            if (i % 2 == 1)
            {
                n++; // 在UCS2第二个字节时n加1
            }
            else
            {
                // 当UCS2编码的第一个字节不等于0时，该UCS2字符为汉字，一个汉字算两个字节
                if (bytes[i] != 0)
                {
                    n++;
                }
            }
        }
        // 如果i为奇数时，处理成偶数
        if (i % 2 == 1)

        {
            // 该UCS2字符是汉字时，去掉这个截一半的汉字
            if (bytes[i - 1] != 0)
                i = i - 1;
            // 该UCS2字符是字母或数字，则保留该字符
            else
                i = i + 1;
        }

        String str;
		try {
			str = new String(bytes, 0, i, "Unicode");
		} catch (UnsupportedEncodingException e) {
			throw new ServiceException(e);
		}
        if(isAppend)
        	str += "...";
        return str;
    }
	

	public static Throwable getCauseServiceException(Throwable e){
		return LangUtils.getCauseServiceException(e);
	}
	
	public static boolean macheUserName(String userName){
		Pattern p = Pattern.compile(USER_REGEX, Pattern.CASE_INSENSITIVE);//
		Matcher m = p.matcher(userName);
		boolean rs = m.matches();
		return rs;
	}
	
	public static <T> T simpleClone(T src){
		T dest = (T)ReflectUtils.newInstance(src.getClass());
		copy(dest, src);
		return dest;
	}

	
	public static <T> T copy(Class cls, Object src){
		T dest = (T)ReflectUtils.newInstance(cls);
		copy(dest, src);
		return dest;
	}
	
	public static <T> T copy(String className, Object src){
		return (T)copy(ReflectUtils.loadClass(className), src);
	}
	
	public static void copy(Object dest, Object src){
		Collection<Field> fields = ReflectUtils.findFieldsFilterStatic(dest.getClass());
		Object srcValue = null;
		for(Field field : fields){
			srcValue = ReflectUtils.getFieldValue(src, field.getName());
			ReflectUtils.setFieldValue(field, dest, srcValue);
		}
	}
	
	public static List cloneList(List list){
		if(list==null || list.isEmpty())
			return list;
		List newList = new ArrayList(list.size());
		for(Object obj : list){
			if(obj instanceof Cloneable){
				newList.add(ReflectUtils.invokeMethod("clone", obj));
			}
		}
		return newList;
	}

	public static boolean isEmpty(Object obj) {
		List list = asList(obj);
		return (list == null || list.isEmpty());
	}
	
	public static String append(String s, int alength, String append){
		int length = Math.abs(alength);
		if(s.length()==length)
			return s;
		StringBuilder str = new StringBuilder(s);
		if(str.length()<length){
			int lack = length-str.length();
			for(int i=0; i<lack; i++){
				str.insert(0, append);
			}
		}else{
			if(alength>0)
				str.delete(length, str.length());
			else
				str.delete(0, str.length()-length);
		}
		return str.toString();
	}
	
	public static void main(String[] args){
//		String[] val = new String[]{"0"};
		Double d = Math.random();
		System.out.println("D:"+d);
		Random r = new Random();
		System.out.println("r:"+Math.abs(r.nextInt(100)));
		/*
		String val = "3";
		System.out.println(simpleConvert(val, long.class, 1l));
		System.out.println(simpleConvert(val, Integer.class, 1));
		System.out.println(simpleConvert(val, Double.class, 1d));
		System.out.println(simpleConvert(val, String.class, "1"));
		
		System.out.println("====================");

		String[] val2 = new String[]{null};
		System.out.println(simpleConvert(val2, Long.class, 1l));
		System.out.println(simpleConvert(val2, Integer.class, 1));
		System.out.println(simpleConvert(val2, Double.class, 1d));
		System.out.println(simpleConvert(val2, String.class, "1"));*/
	}

}
