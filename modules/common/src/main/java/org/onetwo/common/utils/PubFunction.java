

package org.onetwo.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
/**
 * @Description
 * 
 * 公共常用的一些方法
 */
@Deprecated
public class PubFunction {
	
	
	/**
	 * 将date转换成timestamp
	 *
	 * @param date
	 * @return
	 *
	 * @author waa
	 * @since 1.0
	 */
	public static Timestamp translateDate(Date date)
	{
		if (date == null)
			return null;

		return new Timestamp(date.getTime());
	}
	
	/**
	 * 取当前Date
	 * @return
	 */
	public static Date getCurDate()
	{
		GregorianCalendar tGC = new GregorianCalendar();
		return tGC.getTime();
	}
	
	
	/**
	 * 取当前java.sql.Date
	 * @return
	 */
/*	public static java.sql.Date getCurSqlDate()
	{
		return  new java.sql.Date((new java.util.Date()).getTime());
	}*/
	
	/**
	 * 把字符串转Date
	 * @param year 年
	 * @param month 月
	 * @param day  日
	 * @return
	 * @throws Exception
	 */
	public static Date stringToDate(String year,String month,String day) throws Exception
	{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = year.trim()+"-"+month.trim()+"-"+day.trim();        
        return sdf.parse(dateStr);
		
	}
	
	/**
	 * 把字符串转Date
	 * @param day  日
	 * @return
	 * @throws Exception
	 */
	public static Date stringToDate(String dateStr) throws Exception
	{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(dateStr);
	}
	
	/**
	 *  取当前字符串类型的日期 格式：2005-01-01
	 * @return
	 */
	public static String getCurrentDateString()
	{
		GregorianCalendar tGC = new GregorianCalendar();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(tGC.getTime());
	}
	
	public static String getPreDateString()
	{
		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date());
		int month=cal.get(Calendar.MONTH)+1;
		int year=cal.get(Calendar.YEAR);
		int day=cal.get(Calendar.DAY_OF_MONTH);
		if(day<=7)
		{
			if(month <= 1)
			{
				year = year-1;
				month = month + 11;
			}else{
				month = month-1;
			}
			day = day + 20;
		}else{
			day = day - 7;
		}
		return String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day);
	}
	
	/**
	 * 将当前时间以字符串格式返回 例如：2006-08-17 17:22:00
	 * @return 返回"yyyy-MM-dd HH:mm:ss"格式的时间字符串
	 */
	public static String getCurDateTimeString(){
		Date now = getCurDate();		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(now);
	}
	
	public static String getDateTimeString(){
		Date now = getCurDate();		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(now);
	}
	
	public static String getDateString(){
		Date now = getCurDate();		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		return sdf.format(now);
	}
	
	/**
	 * 将指定时间以字符串格式返回 例如：2006-08-17
	 * @return 返回"yyyy-MM-dd"格式的时间字符串
	 */
	public static String getDateString(Date date){
		if(date==null) return "";
 		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}
	
	/**
	 * 取出12个月的集合
	 * @return
	 */
	public static List getAllMonth()
	{
		
		List month = new ArrayList();
		
		for(int i=1;i<13;i++)
		{
			if(i<10)
			{
			month.add("0"+i);
			}else{
			 month.add(String.valueOf(i));
			}
		}
		
		return month;
	}
	
	/**
	 * 取出31天的集合
	 * @return
	 */
	public static List getAllDate()
	{
		
		List date = new ArrayList();
		
		for(int i=1;i<32;i++)
		{
			date.add(String.valueOf(i));
		}
		
		return date;
	}
	
	/**
	 * 取出2000年到现在的年份集合
	 * @return
	 */
	public static List  getFrom2000Year()
	{
		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date());
		int nowyear=cal.get(Calendar.YEAR);
		
		List year = new ArrayList();
		
		for(int i=nowyear;i>=2000;i--)
		{
			year.add(String.valueOf(i));
		}
		
		return year;
		
	}
	
	
	/**
	 * 返回分隔字符串
	 */
    public static String[] strToArray(String aSource, String aFlag)
	{
		int tArrayLen = intStrMember(aSource, aFlag);
		String[] returnArray = new String[tArrayLen];		
		int tReturnAryLen = 0;
		int tFlagPos = 0;

		tFlagPos = aSource.indexOf(aFlag);
		while (aSource.length() > 0)
		{
			if (tFlagPos == -1)
			{
				returnArray[tReturnAryLen] = aSource;
				aSource = "";
			}
			else
			{
				returnArray[tReturnAryLen] = aSource.substring(0, tFlagPos);
			}
			aSource = aSource.substring(tFlagPos + aFlag.length());
			tFlagPos = aSource.indexOf(aFlag);
			tReturnAryLen = tReturnAryLen + 1;
		}
		return returnArray;
	}

	/**
	 * 取分隔字符串,返回List
	 */
	public static List<String> strToList(String aSource, String aFlag)
	{
		ArrayList<String> tIndex = new ArrayList<String>();
		if (aSource == null)
		{
			return tIndex;
		}

		int tFlagPos = 0;

		tFlagPos = aSource.indexOf(aFlag);
		while (aSource.length() > 0)
		{
			if (tFlagPos == -1)
			{
				tIndex.add(aSource);
				aSource = "";
			}
			else
			{
				tIndex.add(aSource.substring(0, tFlagPos));
				aSource = aSource.substring(tFlagPos + aFlag.length());
			}

			tFlagPos = aSource.indexOf(aFlag);			
		}
		return tIndex;
	}
	
	/**
	 * 字符串aFlag在字符串aSource中出现的次数
	 *@return 字符串aFlag在字符串aSource中出现的次数
	 */
	 public static int intStrMember(String aSource, String aFlag)
	{		
		int tReturnAryLen = 0;
		int tFlagPos = 0;
		tFlagPos = aSource.indexOf(aFlag);
		while (aSource.length() > 0)
		{
			tReturnAryLen = tReturnAryLen + 1;
			if (tFlagPos == -1)
			{
				aSource = "";
			}
			else
			{
				aSource = aSource.substring(tFlagPos + aFlag.length());
			}
			tFlagPos = aSource.indexOf(aFlag);
		}
		return tReturnAryLen;
	}
	
	/**
	 * 由数组产生条件子句
	 *
	 * 如调用arrayToString([1,2]),返回"1,2"
	 *
	 * @param strings
	 * @return	字符串，该函数不会返回null
	 */
	public static String arrayToString(String[] strings)
	{
		return arrayToString(strings, ",");
	}

	/**
	 * 将字符串数组转化为单个字符串的形式
	 *
	 * @param strings
	 * @param seperater
	 * @return	字符串，该函数不会返回null
	 */
	public static String arrayToString(String[] strings, String seperater)
	{
		if (strings == null || strings.length <= 0)
			return "";

		StringBuffer result = new StringBuffer();

		for (int i = 0; i < strings.length; i++)
		{
			if (i != 0)
				result.append(seperater);

			result.append(strings[i]);
		}

		return result.toString();
	}

	
	/**
	 * 取随机字符串
	 * @param length 字符串长度
	 * @return
	 */
	public static String getRadomString(int length)
	{
		
		  char[] result = new char[length]; 
		  
		  char[] takeArr = {  '1', '2', '3', '4', '5', '6', '7', 
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 
			'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 
			'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 
			'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 
			'Y', 'Z' }; 
		  
		  for (int i = 0, j = 59; i < length; ++i, --j) { 
			  int take = (int) (Math.random() * j); 
			  result[i] = takeArr[take]; 
			  char m = takeArr[j - 1]; 
			  takeArr[j - 1] = takeArr[take]; 
			  takeArr[take] = m; 
		   } 
		  
		  return new String(result);


		
	}
	
	
	/**
	 * 取随机数字串
	 * @param length 数字长度
	 * @return
	 */
	public static String getRadomNumber(int length)
	{
		
		  char[] result = new char[length]; 
		  
		  char[] takeArr = { '0', '1', '2', '3', '4', '5', '6', '7','8', '9'}; 
		  
		  for (int i = 0, j = 10; i < length; ++i, --j) { 
			  int take = (int) (Math.random() * j); 
			  result[i] = takeArr[take]; 
			  char m = takeArr[j - 1]; 
			  takeArr[j - 1] = takeArr[take]; 
			  takeArr[take] = m; 
		   } 
		  
		  return new String(result);


		
	}
	
	/**
	 * 用base64给url编码
	 *
	 * @param url
	 * @return
	 *	
	 * @since 1.0
	 */
	public static String urlEncode(String url)
	{
		if (url == null)
			return null;

		BASE64Encoder encoder = new BASE64Encoder();
		String tar = encoder.encode(url.getBytes());

		// 将所有"="替换成"_"
		while (tar.indexOf('=') >= 0)
			tar = tar.replace('=', '_');

		while (tar.indexOf('\r') >= 0)
			tar = tar.replace('\r', '[');

		while (tar.indexOf('\n') >= 0)
			tar = tar.replace('\n', ']');

		return tar;
	}

	/**
	 * 用base64给url解码
	 *
	 * @param url
	 * @return
	 *	
	 * @since 1.0
	 */
	public static String urlDecode(String url)
	{
		if (url == null)
			return null;

		BASE64Decoder decoder = new BASE64Decoder();

		// 将所有"_"替换回"="
		while (url.indexOf('_') >= 0)
			url = url.replace('_', '=');

		while (url.indexOf('[') >= 0)
			url = url.replace('[', '\r');

		while (url.indexOf(']') >= 0)
			url = url.replace(']', '\n');

		try
		{
			byte[] b = decoder.decodeBuffer(url);
			return new String(b);
		}
		catch (IOException e)
		{
			return null;
		}
	}
	

	/**
	 * 将str里的old_str替换成new_str
	 * @param str
	 * @param old_str
	 * @param new_str
	 * @return
	 */
	public static String strReplace(String str, String old_str, String new_str)
	{
		String tmpstr = str;
		if ("".equals(new_str) || "null".equals(new_str) || new_str == null)
		{
			new_str = "";
		}
		if ("".equals(old_str) || "null".equals(old_str) || old_str == null)
		{
			old_str = "";
		}
		if ("".equals(str) || "null".equals(str) || str == null)
		{
			str = "";
		}

		int found_pos = tmpstr.indexOf(old_str);
		while (found_pos >= 0)
		{
			tmpstr = tmpstr.substring(0, found_pos) + new_str + tmpstr.substring(found_pos + old_str.length(), tmpstr.length());
			found_pos = tmpstr.indexOf(old_str, found_pos + new_str.length());
		}
		return tmpstr;

	}

	
	
	/**
	 * 将 s 进行 BASE64 编码
	 */
	public static String getBASE64(String s) {
	  if (s == null) return null;
	  return (new sun.misc.BASE64Encoder()).encode( s.getBytes() );
	}


	/**
	 *  将 BASE64 编码的字符串 s 进行解码
	 */
	public static String getFromBASE64(String s) {
	  if (s == null) return null;
	  BASE64Decoder decoder = new BASE64Decoder();
	  try {
	    byte[] b = decoder.decodeBuffer(s);
	    return new String(b);
	  } catch (Exception e) {
	    return null;
	  }
	} 

	/**
	 * URL中文编码
	 * @param url
	 * @return
	 * @throws Exception
	 */
     public static String urlGBKEncode(String url)throws Exception
     {
    	 return (java.net.URLEncoder.encode(url, "GBK"));
     }


    /**
     * 获取文件扩展名
     * @param filepath
     * @return
     */
    public static String  getFileExtensionName(String filepath)
    {
    	if(filepath.indexOf('.')!=-1)
    		return filepath.substring(filepath.lastIndexOf(".") + 1, filepath.length());
        return "";
    }
    
    
    /**
     * copy 文件
     * @param from
     * @param to
     * @return
     */
	  public static boolean copyFile(String from,String to){

	    File fromFile,toFile;
	    fromFile = new File(from);
	    toFile = new File(to);
	    FileInputStream fis = null;
	    FileOutputStream fos = null;

	    try{
	      toFile.createNewFile();
	      fis = new FileInputStream(fromFile);
	      fos = new FileOutputStream(toFile);
	      int bytesRead;
	      byte[] buf = new byte[4 * 1024];  // 4K buffer
	      while((bytesRead=fis.read(buf))!=-1){
	        fos.write(buf,0,bytesRead);
	      }
	      fos.flush();
	      fos.close();
	      fis.close();
	    }catch(IOException e){
	      return false;
	    }
	    return true;

	  }  

		/**
		 * @Description 操作系统信息
		 * 
		 */
		public static class System {

			public static final String Agent_Windows98 = "windows 98";

			public static final String Agent_Windowsnt = "windows nt 4.0";

			public static final String Agent_Windows2000 = "windows nt 5.0";

			public static final String Agent_Windowsxp = "windows nt 5.1";

			public static final String Agent_Windows2003 = "windows nt 5.2";
			
			public static final String Agent_WindowsVista = "windows nt 6.0";
			
			public static final String Agent_Windows7 = "windows nt 6.1";

			public static final String Agent_SunOS = "sunos";

			public static final String Agent_Linux = "linux";

			public static final String windows98 = "Windows 98";

			public static final String windowsnt = "Windows nt";

			public static final String windows2000 = "Windows 2000";

			public static final String windowsxp = "Windows xp";

			public static final String windows2003 = "Womdows 2003";
			
			public static final String windowsvista= "Womdows Vista";
			
			public static final String windows7 = "Womdows 7";

			public static final String sunos = "SunOS";

			public static final String linux = "Linux";

			public static final String other = "Other";

		}

		/**
		 * 
		 * @Description 浏览器信息
		 */
		public static class Browser {

			public static final String Agent_IE4 = "msie 4.0";

			public static final String Agent_IE5 = "msie 5.0";

			public static final String Agent_IE6 = "msie 6.0";
			
			public static final String Agent_IE7 = "msie 7.0";
			
			public static final String Agent_IE8 = "msie 8.0";

			public static final String Agent_Firefox = "firefox";

			public static final String Agent_Netscape = "mozilla/5.0";

			public static final String IE4 = "MSIE 4.0";

			public static final String IE5 = "MSIE 5.0";

			public static final String IE6 = "MSIE 6.0";
			
			public static final String IE7 = "MSIE 7.0";
			
			public static final String IE8 = "MSIE 8.0";

			public static final String Firefox = "Firefox";

			public static final String Netscape = "Netscape";

			public static final String Other = "Other";

		}
		
		/**
		 * 通过User-Agent 取到操作系统的信息
		 * @param userAgent
		 * @return
		 * @throws Exception
		 */
		public static String getSystemByAgent(String userAgent){
		   if(userAgent.indexOf(System.Agent_Windowsxp)!=-1)
		   {
			   return System.windowsxp;
		   }else if(userAgent.indexOf(System.Agent_WindowsVista)!=-1)
		    {
		    	return System.windowsvista;
		    }else if(userAgent.indexOf(System.Agent_Windows7)!=-1)
		    {
		    	return System.windows7;
		    }	   
		    else if(userAgent.indexOf(System.Agent_Windows2003)!=-1)
		    {
		    	  return System.windows2003;
		     }
		    else if(userAgent.indexOf(System.Agent_Windows2000)!=-1)
		    {
		    	return System.windows2000;
		    }		     
		      else if(userAgent.indexOf(System.Agent_Windows98)!=-1)
		        {
		        	return System.windows98;
		        }
		        else if(userAgent.indexOf(System.Agent_Windowsnt)!=-1)
		        {
		        	return System.windowsnt;
		        }
		        else if(userAgent.indexOf(System.Agent_Linux)!=-1)
		        {
		        	return System.linux;
		        }
		        else if(userAgent.indexOf(System.Agent_SunOS)!=-1)
		        {
		        	return System.sunos;
		        }else{
		        	return System.other;
		        }
		}
		
		/**
		 * 通过User-Agent 取到 浏览器的信息
		 * @param userAgent
		 * @return
		 * @throws Exception
		 */
		public static String getBrowerByAgent(String userAgent){
			if(userAgent.indexOf(Browser.Agent_IE5)!=-1)
			{
				return Browser.IE5;	
			}
			else if(userAgent.indexOf(Browser.Agent_IE6)!=-1)
			{
				return  Browser.IE6;
			}
			else if(userAgent.indexOf(Browser.Agent_IE7)!=-1)
			{
				return  Browser.IE7;
			}
			else if(userAgent.indexOf(Browser.Agent_IE8)!=-1)
			{
				return  Browser.IE8;
			}
			else if(userAgent.indexOf(Browser.Agent_Firefox)!=-1)
			{
				return  Browser.Firefox;
			}
			else if(userAgent.indexOf(Browser.Agent_Netscape)!=-1)
			{
				return  Browser.Netscape;
			}
			else if(userAgent.indexOf(Browser.Agent_IE4)!=-1)
			{
				return  Browser.Agent_IE4;
			}
			else {
				return  Browser.Other; 
			}
			
		}
		
		
		public static Long cacuIp(String ip) {
			long srIP = 0 ;
			String[] aIp = ip.split("\\.");
			
			if(aIp.length!=4){
				return srIP;
			}
			
			for(int i=0; i<4;i++){
				
				srIP = (long)(srIP+(Integer.parseInt(aIp[i])*(Math.pow(256,(3-i)))));
				
			}
			
			return srIP;
		}
		
	
		/**
		 * 清理nginx的缓存
		 * @param urls
		 * @return
		 */
	  public static String nginxPurge(String urls)
	  {
		  String logs="";
		  if(urls==null||"".equals(urls.trim())) 
		  {
			  logs=" urls 不能为空 ";
			  return logs;
		  }else{
                      urls = urls.replaceAll("\\\\", "");
                      urls = urls.replaceAll("\"", "");
                      urls = urls.replaceAll("\'", "");
                      urls = urls.replaceAll("$", "");
                      urls = urls.replaceAll("|", "");                      
                      urls = urls.replaceAll(">", "");
                      urls = urls.replaceAll("<", "");
                      urls = urls.replaceAll("&&", "");
                      urls = urls.replaceAll(" ", "");
                      
			  urls=urls.replaceAll("\r", "");
			  urls=urls.replaceAll("\n", "");			  
		  }
		 
		  String [] url=urls.split(";");
		  for(int i=0;i<url.length;i++)
		  {
			if(url[i]==null||"".equals(url[i].trim()))
			  {
				continue;  
			  }
			  
			logs=logs+" <br/> "+url[i];
			if(!url[i].startsWith("http://"))
			{
				logs=logs+" 处理失败，原因： url必须http://开头； ";
				continue;
			}
			
            try {
				
				Runtime.getRuntime().exec( "/usr/local/bin/purge " + url[i]);
				logs=logs+" 处理成功； ";
				
			} catch (Exception ex) {
				logs=logs+" 处理失败, 原因："+ex.getMessage()+" ；";
			}			  
		  }
		  
		  return logs;		  
	  }
	  
}
