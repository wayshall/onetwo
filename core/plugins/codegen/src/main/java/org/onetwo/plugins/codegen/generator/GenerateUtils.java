package org.onetwo.plugins.codegen.generator;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.plugins.codegen.db.BaseColumnInfo;
import org.onetwo.plugins.codegen.db.TableInfo;

@SuppressWarnings("unchecked")
public class GenerateUtils {

	private static GenerateUtils instance = new GenerateUtils();
	
	private GenerateUtils(){
	} 
	
	public static GenerateUtils getInstance(){
		return instance;
	}
	
	public static String toPropertyName(String str){
		return toJavaName(str, false);
	}
	
	public static String toClassName(String str){
		return toJavaName(str, true);
	}
	
	public static String capital(String str){
		return toJavaName(str, true);
	}
	
	public static String uncapital(String str){
		return toJavaName(str, false);
	}
	
	public static String toJavaName(String str, boolean isFirstUpper){
		if(str.indexOf('_')==-1){
			str = str.toLowerCase();
			if(isFirstUpper && Character.isLowerCase(str.charAt(0))){
				return str.substring(0,1).toUpperCase()+str.substring(1);
			}else{
				return str;
			}
		}
		char[] chars = str.toCharArray();
		StringBuilder newStr = new StringBuilder();
		boolean needUpper = isFirstUpper;
		for(int i=0; i<chars.length; i++){
			char c = Character.toLowerCase(chars[i]);
			if(needUpper){
				c = Character.toUpperCase(c);
				needUpper = false;
			}
			if(c=='_'){
				needUpper = true;
				continue ;
			}
			newStr.append(c);
		}
		return newStr.toString();
	}
	
	public static Class loadClass(String className){
		Class clazz = null;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new BaseException("no class found : " + className, e);
		}
		return clazz;
	}
	
	public static <T> T newInstance(Class<T> clazz){
		T bean = null;
		try {
			bean = clazz.newInstance();
		} catch (Exception e) {
			throw new BaseException("instance class error : " + clazz, e);
		}
		return bean;
	}
	
	public static Object newInstance(String className){
		return newInstance(loadClass(className));
	}
	
	public static void makeDirs(String path){
		File outDir = new File(path);
		if(outDir.isFile())
			outDir = outDir.getParentFile();
		
		if(!outDir.exists())
			if(!outDir.mkdirs())
				throw new RuntimeException("can't create output dir:"+path);
	}
	
	public static Set<String> importClasses(TableInfo table){
		Set<String> importClasses = new HashSet<String>();
		if(table.getPrimaryKey()!=null)
			addImportClass(table.getPrimaryKey().getJavaType(), importClasses);
		for(BaseColumnInfo column : table.getColumns().values()){
			if(column.getJavaType()==null){
				throw new ServiceException("column["+column.getName()+"]'s javaType is null!");
			}
			addImportClass(column.getJavaType(), importClasses);
			if(column.isDateType()){
				addImportClass(Temporal.class, importClasses);
				addImportClass(TemporalType.class, importClasses);
			}
		}
		return importClasses;
	}
	
	public static void addImportClass(Class clazz, Set<String> importClasses){
		String pack = clazz.getPackage().getName();
		if(!pack.equals("java.lang")){
			importClasses.add(clazz.getName());
		}
	}

	public static void main(String[] args){
		String str = toJavaName("ID", false);
		System.out.println(str);
	}
}
