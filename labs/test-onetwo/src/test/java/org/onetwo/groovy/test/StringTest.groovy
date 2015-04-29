package org.onetwo.groovy.test

import org.junit.Assert
import org.junit.Test

class StringTest {
	
	@Test
	def void test(){
		def map = [aa:"bb"]
		println "map: ${map.aa}"
		println "map: ${map.cc}"
		def cc = map.cc
		if(cc){
			println "cc is true"
		}else{
			println "cc is false"
		}
		println "".metaClass.methods*.name.sort().unique()
		
		String[] cmds = ["D:\\mydev\\servers\\apache-tomcat-7.0.57\\bin\\shutdown.bat"] as String[]
//		def proc = Runtime.getRuntime().exec(cmds, System.getenv() as String[], new File("D:\\mydev\\servers\\apache-tomcat-7.0.57\\"))
//		println "set home: ${proc.text}"
//		def proc1 = "cmd /c start set CATALINA_HOME=D:\\mydev\\servers\\apache-tomcat-7.0.57\\bin\\;D:\\mydev\\servers\\apache-tomcat-7.0.57\\bin\\shutdown.bat".execute();
//		"cmd /c start D:\\mydev\\servers\\apache-tomcat-7.0.57\\bin\\tomcat7.exe".execute();
		def proc1 = "D:\\mydev\\servers\\apache-tomcat-7.0.57\\bin\\shutdown.bat".execute(System.getenv() as String[], new File("D:\\mydev\\servers\\apache-tomcat-7.0.57\\"))
		println "set home2: ${proc1.text}"
	}
	
	@Test
	def void testReplace(){
		String str = "plugins/activemq"
		println str.replace("/", "-");
	}
	
	@Test
	def void testSplit(){
		String str = "aa;bb";
		println(str.split(';'))
		Assert.assertEquals("[aa, bb]", str.split(';').toString());
		
		str = "";
		println(str.split(';'))
		Assert.assertEquals("[]", str.split(';').toString());
		
		String str2;
		str2 = str2==null?"":str2.split(';')
		println(str2.split(';'))
		Assert.assertEquals("[]", str2.split(';').toString());
	}
	
	@Test
	def void testToDate(){
		def str = "2014-12-30"
		Date date = Date.parse("yyyy-MM-dd", str)
		println("date: ${date.format('yyyy-MM-dd')}")
		Assert.assertEquals(str, date.format("yyyy-MM-dd"))
	}

}
