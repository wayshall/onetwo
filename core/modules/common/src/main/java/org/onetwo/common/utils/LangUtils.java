package org.onetwo.common.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
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
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.regex.Pattern;

import org.onetwo.common.annotation.BeanOrder;
import org.onetwo.common.convert.Types;
import org.onetwo.common.date.DateUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.BusinessException;
import org.onetwo.common.exception.ExceptionCodeMark;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.expr.Expression;
import org.onetwo.common.expr.ExpressionFacotry;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.md.Hashs;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.func.ArgsReturnableClosure;
import org.onetwo.common.utils.map.CaseInsensitiveMap;

@SuppressWarnings({"rawtypes", "unchecked"})
public class LangUtils {

//	private static final Logger logger = LoggerFactory.getLogger(LangUtils.class); 

	public static final String UTF8 = "utf-8";
	public static final Pattern DIGIT = Pattern.compile("^[0-9]+$");
	public static final Pattern AWORD = Pattern.compile("^\\w+$", Pattern.CASE_INSENSITIVE);
	public static final String EMPTY_STRING = "";
	public static final Object EMPTY_OBJECT = new Object();
	public static final Object[] EMPTY_ARRAY = new Object[0];
	public static final String[] EMPTY_STRING_ARRAY = new String[0];
	public static final Class[] Empty_CLASSES = new Class[0];
	
	private final static Map<String, Pattern> REGEX_CACHE = new ConcurrentHashMap<String, Pattern>(); 

	public static final Consoler CONSOLE;

	private static final Collection<Class<?>> BASE_CLASS;
	private static final Collection<Class<?>> SIMPLE_CLASS;

	static {
		CONSOLE = Consoler.create(asBufferedReader(System.in));
		
		
		Set<Class<?>> cls = new HashSet<Class<?>>();
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
		BASE_CLASS = Collections.unmodifiableSet(cls);
		
		Set<Class<?>> simples = new HashSet<Class<?>>(cls);
		simples.add(String.class);
		simples.add(Date.class);
		simples.add(Calendar.class);
		simples.add(Number.class);
		
		SIMPLE_CLASS = Collections.unmodifiableSet(simples);
	}
	
	public static final char[] WORD_CHARS = {  '1', '2', '3', '4', '5', '6', '7', 
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
		public Object ifIterable(Iterable<?> obj) {
			Iterator<?> it = obj.iterator();
			if(it.hasNext()){
				return it.next();
			}
			return null;
		}

		@Override
		public Object ifList(List list) {
			if(list.isEmpty())
				return null;
			return list.get(0);
		}
		
		@Override
		public Object ifArray(Object[] array) {
			return array.length==0?null:array[0];
		}

		@Override
		public Object ifCollection(Collection col) {
			if(col.isEmpty())
				return null;
			return col.iterator().next();
		}

		public Object other(Object obj, Class<?> type) {
			return obj;
		}

		@Override
		public Object ifMap(Map<?, ?> obj) {
			if(LangUtils.isEmpty(obj))
				return null;
			return obj.entrySet().iterator().next().getValue();
		}

		@Override
		public Object all(Object obj) {
			return obj;
		}
		
	};
	
	public static TypeJudge NotNullObject = new TypeJudgeAdapter(){

		@Override
		public Object ifArray(Object[] array) {
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
		public Object ifCollection(Collection<?> obj) {
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
		public Object ifList(List<?> obj) {
			return Collections.EMPTY_LIST;
		}

		@Override
		public Object ifLong(Object obj) {
			return Long.valueOf(0);
		}

		@Override
		public Object ifMap(Map<?, ?> obj) {
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

	/*public static final TypeJudgeAdapter STRING_CAST_TYPEJUDGE = new TypeJudgeAdapter(){

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
		
	};*/
	
	public static final double MB_SIZE = 1000.0*1000.0;
	public static final double KB_SIZE = 1000.0;
	private static final boolean debug = true;
	

	public static Collection<Class<?>> getBaseTypeClass(){
		return BASE_CLASS;
	}
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
	public static boolean isSimpleType(Class<?> clazz){
		return SIMPLE_CLASS.contains(clazz);
	}
	public static boolean isSimpleTypeObject(Object obj){
		if(obj==null)
			return false;
		return SIMPLE_CLASS.contains(obj.getClass());
	}
	
	public static Collection<Class<?>> getSimpleClass() {
		return SIMPLE_CLASS;
	}
	public static boolean isTimeClass(Class<?> clazz){
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
	
	public static boolean isIterableClass(Class clazz){
		if(clazz==null)
			return false;
		return Iterable.class.isAssignableFrom(clazz);
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
	
	public static <T extends Throwable> T getCauseException(Throwable e, Class<T> root){
		Throwable se = e;
		while(se.getCause()!=null){
			se = se.getCause();
			if(root.isInstance(se)){
				return (T)se;
			}
		}
		return null;
	}
	
	/***
	 * 获取第一个不是jfish框架自定义异常的cause异常
	 * @param e
	 * @return
	 */
	public static Throwable getFirstNotJFishThrowable(Throwable e){
		Assert.notNull(e);
		Throwable se = e;
		while((se = se.getCause())!=null){
			if(!isJFishThrowable(se))
				return se;
		}
		return se;
	}
	
	public static boolean isJFishThrowable(Throwable t){
		return BaseException.class.isInstance(t) || ExceptionCodeMark.class.isInstance(t);
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
	
	public static BaseException asBaseException(String msg, Throwable e){
		if(e==null){
			return new BaseException(msg);
		}else if(e instanceof BaseException){
			return (BaseException) e;
		}
		
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

	
	public static <T> T block(ArgsReturnableClosure block, Object...objects){
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
				context = CUtils.asMap(objects);
			}else{
				context = CUtils.tolist(objects, true);
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
			System.out.println(DateUtils.nowString()+" : "+sb.toString());
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
		StringBuilder result = new StringBuilder("");
		if(isBaseType(obj.getClass())){
			result.append(obj);
		}else if(obj instanceof Class){
			result.append(((Class)obj).getName());
		}else if(obj instanceof Date){
			result.append(DateUtils.formatDateTime((Date)obj));
		}else if(obj instanceof String){
			result.append(obj);
		}else if(obj instanceof Number){
			result.append(((Number)obj).toString());
		}else if(obj instanceof Map){
//			result += ((Map)obj).toString();
			result.append("{");
			int index = 0;
			for(Entry<Object, Object> entry : ((Map<Object, Object>)obj).entrySet()){
				if(index!=0)
					result.append(", ");
				result.append(toString(entry.getKey())).append(":").append(toString(entry.getValue()));
				index++;
			}
			result.append("}");
		}else if(obj instanceof Exception){
			result.append(toString((Exception)obj, true));
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
			result.append("[").append(StringUtils.join(strs, ", ")).append("]");
		}else if(obj.getClass().isArray()){
			List<?> list = CUtils.tolist(obj, false);
			result.append("[").append(StringUtils.join(list, ", ")).append("]");
		}else if(obj.getClass()==Object.class){
			return obj.toString();
		}else{
			Map props = ReflectUtils.toMap(obj);
			result.append(props.toString());
		}
		return result.toString();
	}
	

	
	public static String newString(byte[] bytes){
		return newString(bytes, UTF8);
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
	

	public static String changeCharset(String str, String toCharset){
		return changeCharset(str, null, toCharset);
	}
	public static String changeCharset(String str, String fromCharset, String toCharset){
		String result = null;
		Assert.notNull(str);
		try {
			if(StringUtils.isBlank(fromCharset)){
				result = new String(str.getBytes(), toCharset);
			}else{
				result = new String(str.getBytes(fromCharset), toCharset);
			}
		} catch (UnsupportedEncodingException e) {
			throwBaseException(e);
		}
		return result;
	}
	

	public static byte[] getBytes(String source){
		return getBytes(source, UTF8);
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
			//a byte contains 8 bit, every 4 bit can indecated by a hex number, in other words, a byte(8 bit) can indecated by 2 hex number
			buf.append(HEX_CHAR.charAt((b >>> 4 & 0xf)));//high. symbol >>> is unsigned right shift 
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
			//every 2 hex number indecate a byte
			numb1 = HEX_CHAR.indexOf(chars[i]) << 4  & 0xf0 ;//first hex number is high
			numb2 = HEX_CHAR.indexOf(chars[i+1]) & 0xf;
			bytes = ArrayUtils.addAll(bytes, (byte)((numb1 | numb2) & 0xff));
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
		StringBuilder str = new StringBuilder(count*op.length());
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
	
	public static String strings(String... strings) {
		if (strings == null || strings.length == 0)
			return "";
		int size = 0;
		for(String str : strings){
			if(str==null)
				continue;
			size += str.length();
		}
		StringBuilder sb = new StringBuilder(size);
		for (String str : strings){
			if(str==null)
				continue;
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
		}else if(obj instanceof CharSequence){
			return ((CharSequence)obj).length();
		}else if(obj instanceof Map){
			return ((Map)obj).size();
		}else if(obj instanceof File){
			return (int)FileUtils.size((File)obj);
		}else if(obj.getClass().isArray()){
			return Array.getLength(obj);
		}else {
			if(reflectSizeMethod){
				Method m = ReflectUtils.findMethod(true, obj.getClass(), "size");
				if(m!=null)
					return (Integer)ReflectUtils.invokeMethod(m, obj);
			}
			return 1;
//			throw new UnsupportedOperationException("can not get size of object : " + obj);
		}
	}
	

	public static boolean isBlank(Object obj){
		if(obj==null)
			return true;
		if(obj instanceof Iterable){
			for(Object o : (Iterable)obj){
				if(o!=null)
					return false;
			}
		}else if(obj instanceof CharSequence){
			return StringUtils.isBlank(obj.toString());
		}else if(obj instanceof Map){
			for(Object o : ((Map)obj).values()){
				if(o!=null)
					return false;
			}
		}else if(obj.getClass().isArray()){
			int size = Array.getLength(obj);
			for(int i=0; i<size; i++){
				if(Array.get(obj, i)!=null)
					return false;
			}
		}else {
			return false;
		}
		return true;
	}

	public static boolean isMultiple(Object obj){
		if(obj==null)
			return false;
		if(obj instanceof Iterable){
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
		if(obj instanceof Iterable){
//			return !isEmpty((Collection)obj);
			Iterator<?> it = ((Iterable)obj).iterator();
			return it.hasNext();
		}else if(obj.getClass().isArray()){
			return !isEmpty((Object[])obj);
		}else {
			return false;
		}
	}
	
	public static boolean isMultipleObjectClass(Class clazz){
		return isIterableClass(clazz) || isArrayClass(clazz);
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
		if(Iterable.class.isAssignableFrom(type)){
			result = executor.ifIterable((Iterable)obj);
		}else if(Collection.class.isAssignableFrom(type)){
			if(List.class.isAssignableFrom(type)){
				result = executor.ifList((List)obj);
			}else {
				result = executor.ifCollection((Collection)obj);
			}
		}else if(Map.class.isAssignableFrom(type)){
			result = executor.ifMap((Map)obj);
		}else if(type.isArray()){
			/*Class<?> ctype = type.getComponentType();
			if(ctype.isPrimitive()){
				result = executor.ifPrimitiveArray(obj, ctype);
			}else{
				result = executor.ifArray((Object[])obj);
			}*/
			result = executor.ifArray((Object[])obj);
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
	
	public static <T> Optional<T> getFirstOptional(Object obj){
		if(obj==null)
			return Optional.empty();
		return Optional.ofNullable((T)judgeType(obj, FirstObject));
	}
	
	public static <T> T getFirstOrCreate(Object obj, Class<T> clazz){
		if(obj==null)
			return ReflectUtils.newInstance(clazz);
		Object val = (T)judgeType(obj, FirstObject);
		return (T)(val==null?ReflectUtils.newInstance(clazz):val);
	}

	public static <T> List<T> emptyIfNull(List<T> list){
		return list==null?Collections.EMPTY_LIST:list;
	}
	public static <K, V> Map<K, V> emptyIfNull(Map<K, V> map){
		return map==null?Collections.EMPTY_MAP:map;
	}
	
	public static List defIfEmpty(List list, List def){
		return emptyIfNull(list).isEmpty()?def:list;
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
		return CUtils.trimAndexcludeTheClassElement(trimNull, array);
	}

	public static String getRadomString(int length) {
		  char[] result = new char[length];
		  char[] takeArr = WORD_CHARS.clone();
		  for (int i = 0, j = takeArr.length; i < length; ++i, --j) { 
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
		println(statisticsMemory(unit));
	}

	public static String statisticsMemory(String unit){
		unit = unit.toLowerCase();
		long total = Runtime.getRuntime().totalMemory();
		long free = Runtime.getRuntime().freeMemory();
		long used = total-free;
		if("mb".equals(unit)){
			total = Math.round(total/MB_SIZE);
			free = Math.round(free/MB_SIZE);
			used = Math.round(used/MB_SIZE);
		}else if("kb".equals(unit)){
			total = Math.round(total/KB_SIZE);
			free = Math.round(free/KB_SIZE);
			used = Math.round(used/KB_SIZE);
		}else{
			unit = "b";
		}
		return toString("system memory status (unit:${0}) { total: ${1} , free: ${2} , used: ${3} }", unit, NUMBER_FORMAT.format(total), NUMBER_FORMAT.format(free), NUMBER_FORMAT.format(used));
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
		else if(CharSequence.class.isAssignableFrom(obj.getClass())){
			return StringUtils.isNotBlank(obj.toString());
		}else if(Collection.class.isAssignableFrom(obj.getClass())){
			return hasElement((Collection)obj);
		}else if(obj.getClass().isArray()){
			if(obj.getClass().getComponentType().isPrimitive()){
				return Array.getLength(obj)!=0;
			}else {
				return hasElement((Object[])obj);
			}
		}else if(Map.class.isAssignableFrom(obj.getClass())){
			return hasElement((Map)obj);
		}
		return true;
	}
	
	public static boolean isEmpty(Object obj){
		return size(obj)==0;
	}
	

	public static boolean hasElement(Object[] obj){
		if(isEmpty(obj))
			return false;
		for(Object o : (Object[])obj){
			if(o!=null)//hasElement(e)
				return true;
		}
		return false;
	}
	
	public static boolean hasElement(Map map){
		return !isEmpty(map);
	}
	
	public static boolean isEmpty(Map map){
		return map==null || map.isEmpty();
	}
	
	public static boolean isNotEmpty(Map map){
		return !isEmpty(map);
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
		return obj1==obj2 || (obj1!=null && obj2!=null && obj1.equals(obj2));
	}
	public static boolean equalsIgnoreCase(String str1, String str2){
		return str1==str2 || (str1!=null && str2!=null && str1.equalsIgnoreCase(str2));
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
	
	public static void await(long seconds){
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
		if(List.class.isAssignableFrom(toType)){
			return (T)Types.asList(str, String.class);
		}else if(toType.isArray()){
			return (T)Types.asArray(str, String.class);
		}else{
			return Types.convertValue(str, toType);
		}
	}
	
	public static <T> T tryCastTo(Object val, Class<T> toType){
		return Types.convertValue(val, toType);
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
		Map map = new CaseInsensitiveMap();
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
		return CUtils.stripNull(collections);
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
		Object[] objs = strs;
		String s = appendNotBlank(objs);
		s = Hashs.MD5.hash(s + System.currentTimeMillis() + getRadomString(6));
		return s;
	}
	
	public static boolean isWord(String str){
		return AWORD.matcher(str).matches();
	}
	
	public static Object formatValue(Object value, String dataFormat){
		Object actualValue;
		if(value instanceof Date){
			if(StringUtils.isBlank(dataFormat))
				dataFormat = DateUtils.DATE_TIME;
			actualValue = DateUtils.format(dataFormat, (Date)value);
		}else if(value instanceof Number && dataFormat != null) {
			NumberFormat nf = new DecimalFormat(dataFormat);
			nf.setRoundingMode(RoundingMode.HALF_UP);
			actualValue = nf.format(value);
		}else{
			actualValue = value;
		}
		return actualValue;
	}

	public static String format(Number num, String pattern) {
		NumberFormat format = new DecimalFormat(pattern);
		return format.format(num);
	}
	
	public static String format(Number num) {
		return new DecimalFormat("0.00").format(num);
	}

	/*****
	 * 填充字符串，如果s的长度少于taotalLength，则在左边填充(aleng-s.length)个append
	 * @param s 
	 * @param taotalLength
	 * @param append
	 * @return
	 */
	public static String padLeft(String s, int taotalLength, String append){
		return pad(s, taotalLength, append.charAt(0), true);
	}
	public static String padRight(String s, int taotalLength, String append){
		return pad(s, taotalLength, append.charAt(0), false);
	}
	
	public static String pad(String s, int taotalLength, char append, boolean padLeft){
		Assert.isTrue(taotalLength>0, "total length must be >0");
		if(s==null){
			return s;
		}
		int length = Math.abs(taotalLength);
		if(s.length()==length)
			return s;
		StringBuilder str = new StringBuilder(s);
		if(str.length()<length){
			int lack = length-str.length();
			char[] appendChars = new char[lack];
			Arrays.fill(appendChars, append);
			if(padLeft){
				str.insert(0, appendChars);
			}else{
				str.append(appendChars);
			}
		}else{
			if(taotalLength>0)
				str.delete(length, str.length());
			else
				str.delete(0, str.length()-length);
		}
		return str.toString();
	}
	
	public static String fixedLengthString(String str, int length, String padString){
		if(StringUtils.isBlank(str))
			return padLeft("", length, padString);
		if(str.length()>=length){
			return str.substring(str.length()-length, str.length());
		}else{
			return padLeft(str, length, padString);
		}
	}
	

	public static Object safeGetValue(Object elemetns, int index){
		return safeGetValue(elemetns, index, null);
	}
	
	public static Object safeGetValue(Object elemetns, int index, Object def){
		if(elemetns==null)
			return def;
		if(elemetns.getClass().isArray()){
			Object[] array = (Object[]) elemetns;
			if(index>=array.length){
				return def;
			}
			return array[index];
		}else if(List.class.isInstance(elemetns)){
			List<?> list = (List) elemetns;
			if(index>=list.size())
				return def;
			return list.get(index);
		}else{
			throw new UnsupportedOperationException("unsupported type: "+ elemetns.getClass());
		}
	}
	
	public static Long hexToLong(String hexStr){
		return Long.parseLong(hexStr, 16);
	}
	
	public static String decToHexString(String decStr){
		return Long.toHexString(Types.convertValue(decStr, Long.class));
	}
	
	public static String decToHexString(String decStr, int length){
		String str = Long.toHexString(Types.convertValue(decStr, Long.class));
		return LangUtils.padLeft(str, length, "0");
	}
	
	public static String decToRadixString(String decStr, int radix, int length){
		String str = Long.toString(Types.convertValue(decStr, Long.class), radix);
		return LangUtils.padLeft(str, length, "0");
	}
	
	public static String encodeUrl(String url){
		try {
			return URLEncoder.encode(url, UTF8);
		} catch (UnsupportedEncodingException e) {
			throw new BaseException("Unsupported Encoding", e);
		}
	}
	
	public static String decodeUrl(String url){
		try {
			return URLDecoder.decode(url, UTF8);
		} catch (UnsupportedEncodingException e) {
			throw new BaseException("Unsupported Encoding", e);
		}
	}
	
	public static boolean isDigitString(String str){
		return DIGIT.matcher(str).matches();
	}
	
	public static <T> T cast(Object obj, Class<T> clazz){
		return clazz.cast(obj);
	}
	
	public static Object firstNotNull(Object...objects){
		for(Object obj : objects)
			if(obj!=null)
				return obj;
		return null;
	}
	
	public static <T> T[] makeGenericArray(Class<T> cls, int length){
		return (T[])Array.newInstance(cls, length);
	}
	
	public static void lockAction(Lock lock, Runnable action){
		lock.lock();
		try {
			action.run();
		} finally{
			lock.unlock();
		}
	}
	

	public static boolean isNumberObject(Object val){
		if(val==null)
			return false;
		return isNumberType(val.getClass());
	}
	public static boolean isNumberType(Class<?> clazz){
		if(clazz==null)
			return false;
		if(clazz.isPrimitive()){
			return int.class==clazz || long.class==clazz || short.class==clazz || float.class==clazz || double.class==clazz;
		}
		return Number.class.isAssignableFrom(clazz);
	}
	
	public static boolean isIntegralType(Class<?> clazz){
		if(clazz==null)
			return false;
		if(clazz.isPrimitive()){
			return int.class==clazz || long.class==clazz || short.class==clazz;
		}
		return Integer.class==clazz || Long.class==clazz || Short.class==clazz;
	}
	
	public static Optional<String> getPid() {
		try {
			String jvmName = ManagementFactory.getRuntimeMXBean().getName();
			return Optional.ofNullable(jvmName.split("@")[0]);
		}
		catch (Throwable ex) {
			return Optional.empty();
		}
	}
}
