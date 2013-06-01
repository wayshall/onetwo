package org.onetwo.common.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.BusinessException;
import org.onetwo.common.exception.ExceptionCodeMark;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.annotation.BeanOrder;
import org.onetwo.common.utils.encrypt.MDFactory;
import org.onetwo.common.utils.list.L;
import org.onetwo.common.utils.map.M;
import org.onetwo.common.utils.map.NonCaseMap;

@SuppressWarnings({"rawtypes", "unchecked"})
public class LangUtils {

//	private static final Logger logger = LoggerFactory.getLogger(LangUtils.class); 

	public static final Pattern AWORD = Pattern.compile("^\\w+$", Pattern.CASE_INSENSITIVE);
	public static final String EMPTY_STRING = "";
	public static final Object EMPTY_OBJECT = new Object();
	public static final Object[] EMPTY_ARRAY = new Object[0];
	public static final String[] EMPTY_STRING_ARRAY = new String[0];
	
	private final static Map<String, Pattern> REGEX_CACHE = new ConcurrentHashMap<String, Pattern>(); 

	public static final Consoler CONSOLE;

	private static final List<Class<?>> BASE_CLASS;

	static {
		CONSOLE = Consoler.create(asBufferedReader(System.in));
		
		
		List<Class<?>> cls = new ArrayList<Class<?>>();
		cls.add(Boolean.class);
		cls.add(boolean.class);
		cls.add(Character.class);
		cls.add(char.class);
		cls.add(Byte.class);
		cls.add(byte.class);
		cls.add(Short.class);
		cls.add(short.class);
		cls.add(Integer.class);
		cls.add(int.class);
		cls.add(Long.class);
		cls.add(long.class);
		cls.add(Float.class);
		cls.add(float.class);
		cls.add(Double.class);
		cls.add(double.class);
//		cls.add(String.class);
//		cls.add(Date.class);
//		cls.add(Number.class);
//		cls.add(BigDecimal.class);
		BASE_CLASS = Collections.unmodifiableList(cls);
	}
	
	public static final char[] takeArr = {  '1', '2', '3', '4', '5', '6', '7', 
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 
			'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 
			'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 
			'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 
			'Y', 'Z' }; 

	
	public static final Comparator AscBeanOrderComparator = new Comparator() {
		@Override
		public int compare(Object o1, Object o2) {
			int rs = getBeanOrder(o1) - getBeanOrder(o2);
			return rs==0?(o1.getClass().getSimpleName().compareTo(o2.getClass().getSimpleName())):rs;
		}
	};
	
	public static final Comparator DescBeanOrderComparator = Collections.reverseOrder(AscBeanOrderComparator);
	
	public static TypeJudge FirstObject = new TypeJudge(){


		@Override
		public Object ifList(Object obj) {
			List list = (List) obj;
			if(list.isEmpty())
				return null;
			return list.get(0);
		}
		
		@Override
		public Object ifArray(Object obj) {
			Object[] array = (Object[]) obj;
			return array.length>0?array[0]:null;
		}

		@Override
		public Object ifCollection(Object obj) {
			Collection col = (Collection) obj;
			if(col.isEmpty())
				return null;
			return col.iterator().next();
		}

		public Object other(Object obj, Class<?> type) {
			return obj;
		}

		@Override
		public Object ifMap(Object obj) {
			return obj;
		}

		@Override
		public Object all(Object obj) {
			return obj;
		}
		
	};
	
	public static TypeJudge NotNullObject = new TypeJudgeAdapter(){

		@Override
		public Object ifArray(Object array) {
			return EMPTY_ARRAY;
		}

		@Override
		public Object ifBoolean(Object obj) {
			return Boolean.FALSE;
		}
		
		protected Object defaultValueIfOhter(Object obj){
			return EMPTY_OBJECT;
		}

		@Override
		public Object ifCollection(Object obj) {
			return Collections.emptySet();
		}

		@Override
		public Object ifDouble(Object obj) {
			return Double.valueOf(0);
		}

		@Override
		public Object ifFloat(Object obj) {
			return Float.valueOf(0);
		}

		@Override
		public Object ifInteger(Object obj) {
			return Integer.valueOf(0);
		}

		@Override
		public Object ifList(Object obj) {
			return Collections.EMPTY_LIST;
		}

		@Override
		public Object ifLong(Object obj) {
			return Long.valueOf(0);
		}

		@Override
		public Object ifMap(Object obj) {
			return Collections.EMPTY_MAP;
		}

		@Override
		public Object ifShort(Object obj) {
			return Short.valueOf((short)0);
		}

		@Override
		public Object ifString(Object obj) {
			return "";
		}

	};

	public static final TypeJudgeAdapter STRING_CAST_TYPEJUDGE = new TypeJudgeAdapter(){

		@Override
		public Object ifList(Object obj) {
			Object array = ifArray(obj);
			return asList(array);
		}

		@Override
		public Object ifCollection(Object obj) {
			return this.ifList(obj);
		}

		@Override
		public Object ifMap(Object obj) {
			String[] array = (String[]) ifArray(obj);
			if(array.length==0)
				return Collections.EMPTY_MAP;
			return asMap(array);
		}

		@Override
		public Object ifArray(Object obj) {
			if(obj==null || obj.toString().length()==0)
				return EMPTY_ARRAY;
			String str = obj.toString();
			String[] strs = null;
			if(str.indexOf(',')!=-1){
				strs = StringUtils.split(str, ',');
			}else{
				strs = new String[]{str};
			}
			return strs;
		}

		@Override
		public Object ifBoolean(Object obj) {
			try {
				return Boolean.valueOf(obj.toString());
			} catch (Exception e) {
			}
			return false;
		}

		@Override
		public Object ifByte(Object obj) {
			return Byte.valueOf(obj.toString());
		}

		@Override
		public Object ifCharacter(Object obj) {
			if(obj==null || obj.toString().length()==0)
				return EMPTY_STRING;
			return obj.toString().charAt(0);
		}

		@Override
		public Object ifDouble(Object obj) {
			if(obj==null || obj.toString().length()==0)
				return 0.0d;
			try {
				return Double.valueOf(obj.toString());
			} catch (Exception e) {
			}
			return 0.0d;
		}

		@Override
		public Object ifFloat(Object obj) {
			if(obj==null || obj.toString().length()==0)
				return 0.0f;
			try {
				return Float.valueOf(obj.toString());
			} catch (Exception e) {
			}
			return 0.0f;
		}

		@Override
		public Object ifInteger(Object obj) {
			if(obj==null || obj.toString().length()==0)
				return 0;
			try {
				return Integer.valueOf(obj.toString());
			} catch (Exception e) {
			}
			return 0;
		}

		@Override
		public Object ifShort(Object obj) {
			if(obj==null || obj.toString().length()==0)
				return (short)0;
			try {
				return Short.valueOf(obj.toString());
			} catch (Exception e) {
			}
			return (short)0;
		}

		@Override
		public Object ifLong(Object obj) {
			if(obj==null || obj.toString().length()==0)
				return 0l;
			try {
				return Long.valueOf(obj.toString());
			} catch (Exception e) {
			}
			return 0l;
		}

		@Override
		public Object ifString(Object obj) {
			if(obj==null)
				return EMPTY_STRING;
			return obj.toString();
		}

		@Override
		public Object ifNotBaseType(Object obj) {
			return obj.toString();
		}
		

		public Object other(Object obj, Class<?> toType) {
			Object result = null;
			if(Date.class.isAssignableFrom(toType)){
				result = DateUtil.parse(obj.toString());
			}else{
				result = super.other(obj, toType);
			}
			return result;
		}
		
	};
	
	public static final double MB_SIZE = 1000.0*1000.0;
	public static final double KB_SIZE = 1000.0;
	private static final boolean debug = true;
	

	public static boolean isPrimitive(Class<?> clazz){
		return clazz!=null && clazz.isPrimitive();
	}
	public static boolean isBaseType(Class<?> clazz){
		return BASE_CLASS.contains(clazz);
	}
	public static boolean isBaseTypeObject(Object obj){
		if(obj==null)
			return false;
		return BASE_CLASS.contains(obj.getClass());
	}
	
	public static boolean isTimeClass(Class clazz){
		if(clazz==null)
			return false;
		return Date.class.isAssignableFrom(clazz) || Calendar.class.isAssignableFrom(clazz);
	}
	
	public static boolean isTimeObject(Object obj){
		if(obj==null)
			return false;
		Class clazz = obj.getClass();
		return Date.class.isAssignableFrom(clazz) || Calendar.class.isAssignableFrom(clazz);
	}
	
	public static boolean isMap(Object obj){
		if(obj==null)
			return false;
		Class clazz = getObjectClass(obj);
		return Map.class.isAssignableFrom(clazz);
	}
	
	public static boolean isMapClass(Class clazz){
		if(clazz==null)
			return false;
		return Map.class.isAssignableFrom(clazz);
	}
	
	public static boolean isCollectionClass(Class clazz){
		if(clazz==null)
			return false;
		return Collection.class.isAssignableFrom(clazz);
	}
	
	public static boolean isArrayClass(Class clazz){
		if(clazz==null)
			return false;
		return clazz.isArray();
	}
	
	public static Class getObjectClass(Object obj){
		Class clazz = null;
		if(obj instanceof Class)
			clazz = (Class) obj;
		else
			clazz = obj.getClass();
		return clazz;
	}
	
	public static Throwable getCauseServiceException(Throwable e){
		Throwable se ;
		if(!(e instanceof BaseException) && e.getCause()!=null){
			se = getCauseServiceException(e.getCause());
		}else{
			se = e;
		}
		return se;
	}
	
	public static Throwable getFinalCauseException(Throwable e){
		Throwable se = e;
		while(se.getCause()!=null){
			se = se.getCause();
		}
		return se;
	}
	
	public static ServiceException asServiceException(Exception e){
		return asServiceException(null, e);
	}
	
	public static ServiceException asServiceException(String msg, Exception e){
		if(e instanceof ServiceException)
			return (ServiceException) e;
		ServiceException se = null;
		if(msg==null)
			se = new ServiceException(e);
		else
			se = new ServiceException(msg, e);
		return se;
	}

	public static void throwServiceException(Exception e){
		throwServiceException(null, e);
	}

	public static void throwServiceException(String msg){
		throwServiceException(msg, null);
	}

	public static void throwServiceException(String msg, Exception e){
		throw asServiceException(msg, e);
	}

	public static void throwBaseException(Exception e){
		throwBaseException(null, e);
	}
	public static void throwBaseException(String msg){
		throw new BaseException(msg);
	}
	public static void throwBaseException(String msg, Exception e){
		throw asBaseException(msg, e);
	}
	public static BaseException asBaseException(Exception e){
		return asBaseException(null, e);
	}
	public static BaseException asBaseException(String msg){
		return asBaseException(msg, null);
	}
	
	public static BaseException asBaseException(String msg, Exception e){
		if(e==null)
			return new BaseException(msg);
		else if(e instanceof BaseException)
			return (BaseException) e;
		
		BaseException se = null;
		if(msg==null)
			se = new BaseException(e.getMessage(), e);
		else
			se = new BaseException(msg, e);
		return se;
	}

	public static void throwIfNull(Object value, String msg){
		if(value==null)
			throwBaseException(msg);
	}

	
	public static <T> T block(Block block, Object...objects){
		T result = null;
		try {
			result = block.execute(objects);
		} catch (Exception e) {
			throw asBaseException("block error: "+e.getMessage(), e);
		}
		return result;
	}

	public static void print(String str){
		print(true, str);
	}
	
	public static void print(boolean print, String str){
		if(print)
			System.out.print(str);
	}

	public static void println(String str, Object...objects){
		println(true, str, objects);
	}

	public static void debug(String str, Object...objects){
		str = "[DEBUG] " + str;
		println(debug, str, objects);
	}
	
	private static Expression PRINT_SE = ExpressionFacotry.newExpression("${", "}");
	

	public static void printlnNamedArgs(String str, Object...objects){
		toStringWith(true, str, true, objects);
	}

	public static void comment(String str, Object...objects){
		comment(true, str, objects);
	}
	
	public static void comment(boolean print, String str, Object...objects){
		if(!print)
			return ;
		toStringWith(print, str, false, objects);
	}
	
	public static void println(boolean print, String str, Object...objects){
		toStringWith(print, str, false, objects);
	}
	
	public static String toString(String str, Object...objects){
		return toStringWith(false, str, false, objects);
	}
	
	public static String toStringWith(boolean print, String str, boolean named, Object...objects){
		StringBuilder sb = new StringBuilder();
		if(objects==null){
			sb.append(str).append("NULL");
			return sb.toString();
		}
		if(PRINT_SE.isExpresstion(str)){
			Object context = null;
			if(named){
				context = M.c(objects);
			}else{
				context = L.aslist(objects);
			}
			String rs = PRINT_SE.parseByProvider(str, context);
			sb.append(rs);
		}else{
			sb.append(str);
			for(Object obj : objects){
				sb.append("\t"+toString(obj));
			}
		}
		if(print){
			System.out.println(DateUtil.nowString()+" : "+sb.toString());
		}
		return sb.toString();
	}
	

	public static String toString(Throwable e, boolean detail){
		Assert.notNull(e);
		
		if(!detail){
			return e.getMessage();
		}
		StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        String result = "";
        try {
            e.printStackTrace(pw);
            result = sw.toString();
        }
        finally {
            try {
                sw.close();
                pw.close();
            } catch (IOException ioe) {
                // ignore
            }
        }
        return result;
	}

	public static String toString(Object obj){
		if(obj==null)
			return "NULL";
		String result = "";
		if(isBaseType(obj.getClass())){
			result += obj;
		}else if(obj instanceof Class){
			result += ((Class)obj).getName();
		}else if(obj instanceof Date){
			result += DateUtil.formatDateTime((Date)obj);
		}else if(obj instanceof String){
			result += obj.toString();
		}else if(obj instanceof Number){
			result += ((Number)obj).toString();
		}else if(obj instanceof Map){
			result += ((Map)obj).toString();
		}else if(obj instanceof Exception){
			result = toString((Exception)obj, true);
		}else if(obj instanceof Collection){
			Collection<?> list = CUtils.toCollection(obj);
			if(isEmpty(list))
				return "[]";
			/*if(list.iterator().next().getClass().isArray()){
				List<String> strs = LangUtils.newArrayList(list.size());
				for(Object o : list){
					strs.add(toString(o));
				}
				result += "["+StringUtils.join(strs, ", ")+"]";
			}else{
				result += "["+StringUtils.join(list, ", ")+"]";
			}*/
			List<String> strs = LangUtils.newArrayList(list.size());
			for(Object o : list){
				strs.add(toString(o));
			}
			result += "["+StringUtils.join(strs, ", ")+"]";
		}else if(obj.getClass().isArray()){
			List<?> list = CUtils.tolist(obj, false);
			result += "["+StringUtils.join(list, ", ")+"]";
		}else if(obj.getClass()==Object.class){
			return obj.toString();
		}else{
			Map props = ReflectUtils.toMap(obj);
			result += props.toString();
		}
		return result;
	}
	
	public static String newString(byte[] bytes, String charset){
		String result = null;
		try {
			if(StringUtils.isBlank(charset))
				result = new String(bytes);
			else
				result = new String(bytes, charset);
		} catch (UnsupportedEncodingException e) {
			throwBaseException(e);
		}
		return result;
	}
	
	public static String changeCharset(String str, String fromCharset, String toCharset){
		String result = null;
		Assert.notNull(str);
		try {
			result = new String(str.getBytes(fromCharset), toCharset);
		} catch (UnsupportedEncodingException e) {
			throwBaseException(e);
		}
		return result;
	}
	

	public static byte[] getBytes(String source){
		return getBytes(source, null);
	}
	
	public static byte[] getBytes(String source, String charset){
		byte[] result = null;
		try {
			if(StringUtils.isBlank(charset))
				result = source.getBytes();
			else
				result = source.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throwBaseException(e);
		}
		return result;
	}
	

	private static final String HEX_CHAR = "0123456789ABCDEF";

	public static String toHex(byte[] bytes){
		StringBuilder buf = new StringBuilder();
		for(byte b : bytes){
			buf.append(HEX_CHAR.charAt((b >>> 4 & 0xf)));//high
			buf.append(HEX_CHAR.charAt((b & 0xf)));//low
		}
		return buf.toString();
	}
	

	public static byte[] hex2Bytes(String str){
		byte[] bytes = null;
		char[] chars = str.toCharArray();

		int numb1;
		int numb2;
		for(int i=0; i<chars.length; i=i+2){
			numb1 = HEX_CHAR.indexOf(chars[i]) << 4  & 0xf0 ;
			numb2 = HEX_CHAR.indexOf(chars[i+1]) & 0xf;
			bytes = org.apache.commons.lang.ArrayUtils.add(bytes, (byte)((numb1 | numb2) & 0xff));
		}
		return bytes;
	}
	
	public static boolean isHexString(String str){
		char[] chars = str.toCharArray();
		int index = -1;
		for(char ch : chars){
			index = HEX_CHAR.indexOf(ch);
			if(index==-1)
				return false;
		}
		return true;
	}
	

	public static String append(Object... strings) {
		return appendWith(false, strings);
	}
	
	public static String repeatString(int count, String op) {
		if(count<1)
			return "";
		StringBuilder str = new StringBuilder();
		for(int i=0; i<count; i++){
			str.append(op);
		}
		return str.toString();
	}
	
	public static String appendNotBlank(Object... strings) {
		return appendWith(true, strings);
	}

	public static String appendWith(boolean ignoreBlank, Object... strings) {
		if (strings == null || strings.length == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		for (Object str : strings){
			if(ignoreBlank){
				if(str==null || StringUtils.isBlank(str.toString()))
					continue;
			}
			sb.append(str);
		}
		return sb.toString();
	}
	

	
	public static int size(Object obj){
		return size(obj, false);
	}
	
	public static int size(Object obj, boolean reflectSizeMethod){
		if(obj==null)
			return 0;
		if(obj instanceof Collection){
			return ((Collection)obj).size();
		}else if(obj instanceof Map){
			return ((Map)obj).size();
		}else if(obj.getClass().isArray()){
			return Array.getLength(obj);
		}else {
			if(reflectSizeMethod){
				Method m = ReflectUtils.findMethod(true, obj.getClass(), "size");
				if(m!=null)
					return (Integer)ReflectUtils.invokeMethod(m, obj);
			}
			return 1;
		}
	}

	public static boolean isMultiple(Object obj){
		if(obj==null)
			return false;
		if(obj instanceof Collection){
			return true;
		}else if(obj.getClass().isArray()){
			return true;
		}else {
			return false;
		}
	}
	
	public static boolean isMultipleAndNotEmpty(Object obj){
		if(obj==null)
			return false;
		if(obj instanceof Collection){
			return !isEmpty((Collection)obj);
		}else if(obj.getClass().isArray()){
			return !isEmpty((Object[])obj);
		}else {
			return false;
		}
	}
	
	public static boolean isMultipleObjectClass(Class clazz){
		return isCollectionClass(clazz) || isArrayClass(clazz);
	}
	
	public static boolean isArray(Object obj){
		if(obj==null)
			return false;
		return obj.getClass().isArray();
	}
	
	public static Object judgeType(Object obj, TypeJudge executor){
		Assert.notNull(obj);
		Class type = obj.getClass();
		return judgeType(obj, type, executor);
	}
	
	/********
	 * 
	 * @param obj source object
	 * @param type target class
	 * @param executor
	 * @return
	 */
	public static Object judgeType(Object obj, Class type, TypeJudge executor){
		Object result = executor.all(obj);
		if(Collection.class.isAssignableFrom(type)){
			if(List.class.isAssignableFrom(type)){
				result = executor.ifList(obj);
			}else {
				result = executor.ifCollection(obj);
			}
		}else if(Map.class.isAssignableFrom(type)){
			result = executor.ifMap(obj);
		}else if(type.isArray()){
			result = executor.ifArray(obj);
		}else{
			result = executor.other(obj, type);
		}
		return result;
	}
	
	public static <T> T getFirst(Object obj){
		if(obj==null)
			return null;
		return (T)judgeType(obj, FirstObject);
	}
	
	public static List emptyIfNull(List list){
		if(list==null)
			return Collections.EMPTY_LIST;
		return list;
	}
	
	public static <T> T notNullValue(Object obj, Class<T> type){
		if(obj!=null)
			return (T)obj;
		return (T)judgeType(obj, type, NotNullObject);
	}
	
	/*
	public static Object getValue(Object root, String expression){
		try {
			return Ognl.getValue(expression, root);
		} catch (OgnlException e) {
			logger.error("get value error : [expression=" + expression + ", root="+root.getClass()+"]", e);
		} catch (Exception e){
			String msg = "get value error : [expression=" + expression + ", root="+root.getClass()+", msg="+e.getMessage()+"]";
			throw new ServiceException(msg, e);
		}
		return null;
	}
	
	public static void setValue(Object root, String expression, Object value){
		try {
			Ognl.setValue(expression, root, value);
		} catch (OgnlException e) {
			logger.error("set value error : [expression=" + expression + ", root="+root.getClass()+"]", e);
		}
	}*/
	
	public static Map asMap(Object... params) {
		return CUtils.asMap(params);
	}
	
	
	public static <T> List<T> asList(Object array) {
//		return L.exclude(array);
//		return L.tolist(array, true);
		return CUtils.tolist(array, true, CUtils.NULL_LIST);
	}

	public static <T> List<T> asList(Object array, boolean trimNull) {
		return L.trimAndexcludeTheClassElement(trimNull, array);
	}

	public static String getRadomString(int length) {
		  char[] result = new char[length]; 
		  
		  for (int i = 0, j = 59; i < length; ++i, --j) { 
			  int take = (int) (Math.random() * j); 
			  result[i] = takeArr[take]; 
			  char m = takeArr[j - 1]; 
			  takeArr[j - 1] = takeArr[take]; 
			  takeArr[take] = m; 
		   } 
		  
		  return new String(result);
	}
	
	public static void printMemory(){
		printMemory("");
	}
	
	private static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance();
	
	public static void printMemory(String unit){
		long total = Runtime.getRuntime().totalMemory();
		long free = Runtime.getRuntime().freeMemory();
		long used = total-free;
		if("mb".equalsIgnoreCase(unit)){
			total = Math.round(total/MB_SIZE);
			free = Math.round(free/MB_SIZE);
			used = Math.round(used/MB_SIZE);
		}else if("kb".equalsIgnoreCase(unit)){
			total = Math.round(total/KB_SIZE);
			free = Math.round(free/KB_SIZE);
			used = Math.round(used/KB_SIZE);
		}else{
			unit = "b";
		}
		println("system memory status (unit:${0}) { total: ${1} , free: ${2} , used: ${3} }", unit, NUMBER_FORMAT.format(total), NUMBER_FORMAT.format(free), NUMBER_FORMAT.format(used));
	}
	
	public static void closeIO(Closeable io){
		try {
			if(io!=null)
				io.close();
		} catch (IOException e) {
			throwBaseException("close io error: " + e.getMessage(), e);
		}
	}
	

	public static boolean hasNotElement(Object obj){
		return !hasElement(obj);
	}
	
	public static boolean hasElement(Object obj){
		if(obj==null)
			return false;
		else if(String.class.isAssignableFrom(obj.getClass())){
			return StringUtils.isNotBlank(obj.toString());
		}else if(Collection.class.isAssignableFrom(obj.getClass())){
			return hasElement((Collection)obj);
		}else if(obj.getClass().isArray()){
			return ArrayUtils.hasElement((Object[])obj);
		}else if(Map.class.isAssignableFrom(obj.getClass())){
			return hasElement((Map)obj);
		}
		return true;
	}
	
	public static boolean hasElement(Map map){
		return M.hasElement(map);
	}
	
	public static boolean isEmpty(Map map){
		return !M.hasElement(map);
	}
	
	public static boolean isNotEmpty(Map map){
		return M.hasElement(map);
	}
	
	public static boolean hasElement(Collection col){
		if(col==null || col.isEmpty())
			return false;
		for(Object e : col){
			if(e!=null)//hasElement(e)
				return true;
		}
		return false;
	}
	
	public static boolean isEmpty(Collection col){
		return (col==null || col.isEmpty());
	}


	public static boolean isEmpty(Object[] arrays){
		return (arrays==null || arrays.length==0);
	}
	
	public static boolean isNullOrEmptyObject(Object object){
		if(object==null)
			return true;
		if(String.class.isInstance(object)){
			return StringUtils.isBlank((String)object);
		}if(object.getClass().isArray()){
			return isEmpty((Object[])object);
		}else if(Collection.class.isInstance(object)){
			return isEmpty((Collection)object);
		}else if(Map.class.isInstance(object)){
			return isEmpty((Map)object);
		}
		return false;
	}
	
	public static boolean isNotEmpty(Collection col){
		return !isEmpty(col);
	}
	
	public static boolean equals(Object obj1, Object obj2){
		if(obj1==null && obj2==null)
			return true;
		else if(obj1!=null)
			return obj1.equals(obj2);
		else
			return obj2.equals(obj1);
	}
	
	public static BufferedReader asBufferedReader(InputStream in){
		return FileUtils.asBufferedReader(in);
	}
	
	private static int getBeanOrder(Object obj){
		if(obj==null)
			return 0;
		BeanOrder bo = obj.getClass().getAnnotation(BeanOrder.class);
		return bo==null?0:bo.value();
	}
	
	public static <T> void asc(List<T> list){
		Collections.sort(list, AscBeanOrderComparator);
	}
	
	public static <T> void desc(List<T> list){
		Collections.sort(list, DescBeanOrderComparator);
	}
	
	public static <T> void asc(T[] array){
		Arrays.sort(array, AscBeanOrderComparator);
	}
	
	public static <T> void desc(T[] array){
		Arrays.sort(array, DescBeanOrderComparator);
	}
	
	public static void await(int seconds){
		try {
			Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
		} catch (InterruptedException e) {
			LangUtils.throwBaseException("await error : " + e.getMessage(), e);
		}
	}

	public static boolean isIterable(Object object) {
        if (object == null) {
            return false;
        }

        if (object instanceof Map) {
            return true;
        } else if (object instanceof Iterable) {
            return true;
        } else if (object.getClass().isArray()) {
            return true;
        } else if (object instanceof Enumeration) {
            return true;
        } else if (object instanceof Iterator) {
            return true;
        } else {
            return false;
        }
    }

    public static Iterator convertIterator(Object value) {
        Iterator iterator;

        if (value instanceof Iterator) {
            return (Iterator) value;
        }

        if (value instanceof Map) {
            value = ((Map) value).entrySet();
        }

        if (value == null) {
            return null;
        }

        if (value instanceof Iterable) {
            iterator = ((Iterable) value).iterator();
        } else if (value.getClass().isArray()) {
            //need ability to support primitives; therefore, cannot
            //use Object[] casting.
            ArrayList list = new ArrayList(Array.getLength(value));

            for (int j = 0; j < Array.getLength(value); j++) {
                list.add(Array.get(value, j));
            }

            iterator = list.iterator();
        } else if (value instanceof Enumeration) {
            Enumeration enumeration = (Enumeration) value;
            ArrayList list = new ArrayList();

            while (enumeration.hasMoreElements()) {
                list.add(enumeration.nextElement());
            }

            iterator = list.iterator();
        } else {
            List list = new ArrayList(1);
            list.add(value);
            iterator = list.iterator();
        }

        return iterator;
    }
    
    public static <T> T fetchNotNull(T...args){
    	Assert.notNull(args);
    	T val = null;
    	for(T obj : args){
    		if(obj instanceof String){
    			if(StringUtils.isNotBlank((String)obj)){
    				val = obj;
    				break;
    			}
    		}else{
    			if(obj!=null){
    				val = obj;
    				break;
    			}
    		}
    	}
    	return val;
    }
    

	public static void filterMap(Map map, String...patterns){
		Assert.notNull(map);
		if(LangUtils.isEmpty(patterns))
			return ;
		Set keys = new HashSet(map.keySet());
		for(Object key : keys){
			if(StringUtils.matchPrefix(key.toString(), patterns)){
				map.remove(key);
			}
		}
		return ;
	}
	
	public static <T> T strCastTo(String str, Class<T> toType){
		return (T)judgeType(str, toType, STRING_CAST_TYPEJUDGE);
	}
	
	public static <T> T tryCastTo(Object val, Class<T> toType){
		Assert.notNull(toType);
		T reVal = null;
		if(!toType.isInstance(val)){
			if(val instanceof String){
				reVal = strCastTo(val.toString(), toType);
			}
		}
		if(reVal==null){
			reVal = (T) val;
		}
		return reVal;
	}
	
	/*********
	 * map contains a value which instance of this clazz
	 * @param model
	 * @param clazz
	 * @return
	 */
	public static boolean isMapContainsInstance(Map<String, Object> model, Class<?> clazz){
		if(clazz==null)
			return false;
		for(Object val : model.values()){
			if(clazz.isInstance(val))
				return true;
		}
		return false;
	}
	
	public static void remove(Collection<?> collection, int start, int end){
		Assert.notEmpty(collection);
		if(end>collection.size())
			end = collection.size();
		Iterator<?> it = collection.iterator();
//		Object val = null;
		for(int i=0; it.hasNext(); i++){
			if(i>=end)
				break;
			it.next();
			if(i>=start){
				it.remove();
			}
		}
	}
	
	public static Map<String, String> toMap(Properties prop){
		Assert.notNull(prop);
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<String> keys = (Enumeration<String>)prop.propertyNames();
		String name = null;
		while(keys.hasMoreElements()){
			name = keys.nextElement();
			map.put(name, prop.getProperty(name));
		}
		return map;
	}
	
	public static Map mapListToMap(List<? extends Map> datas, String keyName, String valueName){
		if(datas==null)
			return null;
		Map map = new NonCaseMap();
		for(Map row : datas){
			map.put(row.get(keyName), row.get(valueName));
		}
		return map;
	}
	
	public static <T> List<T> asListWithType(Class<T> clazz, Object...objects){
		if(LangUtils.isEmpty(objects))
			return Collections.EMPTY_LIST;
		List<T> list = new ArrayList<T>(objects.length);
		for(Object obj : objects){
			if(clazz.isInstance(obj))
				list.add((T)obj);
		}
		return list;
	}
	
	public static <T> Collection<T> stripNull(Collection<T> collections){
		return L.stripNull(collections);
	}
	

	public static <T> List<T> newArrayList(){
		return CUtils.newArrayList(-1);
	}
	
	public static <T> List<T> newArrayList(int size){
		return CUtils.newArrayList(size);
	}
	
	public static <T> List<T> newList(T...objs){
		return CUtils.newList(objs);
	}
	
	public static <T> List<T> newArrayList(T...objs){
		return CUtils.newList(objs);
	}

	public static <T> HashSet<T> newHashSet(){
		return CUtils.newHashSet();
	}

	public static <K, V> Map<K, V> newMap(){
		return CUtils.newMap();
	}
	
	public static <K, V> Map<K, V> newHashMap(Object... params){
		Map<K, V> map = CUtils.newMap();
		if(isEmpty(params))
			return map;
		CUtils.arrayIntoMap(map, params);
		return map;
	}

	public static <K, V> Map<K, V> newHashMap(int size){
		return CUtils.newHashMap(size);
	}

	public static BusinessException asBusinessException(Exception e){
		return asBusinessException(null, e);
	}
	
	public static BusinessException asBusinessException(String msg, Exception e){
		if(e==null)
			return new BusinessException(msg);
		else if(e instanceof BaseException)
			return (BusinessException) e;
		
		BusinessException se = null;
		if(msg==null)
			se = new BusinessException(e.getMessage(), e);
		else
			se = new BusinessException(msg, e);
		return se;
	}
	
	public static String getBaseExceptonCode(Exception e){
		if(ExceptionCodeMark.class.isInstance(e)){
			return ((ExceptionCodeMark)e).getCode();
		}
		return EMPTY_STRING;
	}
	
	public static boolean isError(Exception e, String errorCode){
		String code = getBaseExceptonCode(e);
		return code.equals(errorCode);
	}
	
	public static boolean matche(boolean cache, String regex, String str){
		Pattern pattern = null;
		if(cache){
			pattern = REGEX_CACHE.get(regex);
			if(pattern==null){
				pattern = Pattern.compile(regex);
				REGEX_CACHE.put(regex, pattern);
			}
		}else{
			pattern = Pattern.compile(regex);
		}
		return pattern.matcher(str).matches();
	}
	
	public static String generateToken(String... strs) {
		String s = MyUtils.append(strs);
		s = MDFactory.MD5.encrypt(s + new Date().getTime() + getRadomString(6));
		return s;
	}

	public static int sum(int... counts){
		int total = 0;
		for(int c : counts){
			total += c;
		}
		return total;
	}

	public static long sumNumbers(Number[] counts){
		long total = 0;
		for(Number c : counts){
			total += c.longValue();
		}
		return total;
	}
	
	public static boolean isWord(String str){
		return AWORD.matcher(str).matches();
	}
	
}
