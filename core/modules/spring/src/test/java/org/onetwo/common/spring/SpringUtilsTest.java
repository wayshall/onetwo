package org.onetwo.common.spring;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.onetwo.common.annotation.FieldName;
import org.onetwo.common.date.DateUtils;
import org.onetwo.common.reflect.Intro;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;

public class SpringUtilsTest {
	
	@Test
	public void testGetConstructorNames(){
		String[] strs = SpringUtils.getConstructorNames(TestBean.class, 1);
		System.out.println("names:"+Arrays.asList(strs));
		assertThat(strs).contains("name");
	}
	
	@Test
	public void testNewPathResource() throws Exception{
		Resource res = SpringUtils.newPathResource("classpath:/org");
		assertThat(res).isInstanceOf(ClassPathResource.class);
		System.out.println("path:"+res.getFile());
		
		res = SpringUtils.newPathResource("D:/tmp");
		assertThat(res).isInstanceOf(FileSystemResource.class);
		System.out.println("path:"+res.getFile());
	}

	@Test
	public void testToMap(){
		TestBean test = new TestBean();
		test.setUserName("testUserName");
		test.setBirthday(DateUtils.parse("2017-01-05 11:11:11"));
		test.getAttrs().put("aa", "bb");
		TestBean test2 = new TestBean();
		test2.setUserName("testUserName2");
		test.getAttrs().put("cc", test2);
		test.setName("testRealName");
		Map<String, Object> map = SpringUtils.toFlatMap(test);
		System.out.println("map:"+map);
		assertThat(map.size()).isEqualTo(5);
		assertThat(map.get("birthday")).isEqualTo("2017-01-05");
		assertThat(map.get("realName")).isEqualTo("testRealName");
	}
	@Test
	public void testEnumToString(){
		TestBean test = new TestBean();
		test.setUserName("testUserName");
		test.setBirthday(DateUtils.parse("2017-01-05 11:11:11"));
		test.setStatus(TestUserStatus.NORMAL);
		
		String status = SpringUtils.getFormattingConversionService().convert(TestUserStatus.NORMAL, String.class);
		System.out.println("status: " + status);
		assertThat(status).isEqualTo(TestUserStatus.NORMAL.name());
	}
	
	@Test
	public void test() throws ParseException {
		DateFormatTest t = new DateFormatTest();
		Field field = Intro.wrap(DateFormatTest.class).getField("date");
		Date date = SpringUtils.parseDate("2020-12-09", field);
		assertThat(date).isEqualTo(DateUtils.parse("2020-12-09"));
	}

	static public class DateFormatTest {
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		Date date;
	}

	
	public static class TestBean {
		private String userName;
	    @DateTimeFormat(pattern= DateUtils.DATE_ONLY)
		private Date birthday;
	    private Map<String, Object> attrs = new HashMap<String, Object>();
	    
	    @FieldName("realName")
	    private String name;
	    
	    private TestUserStatus status;

		public TestBean() {
			super();
		}

		public TestBean(String name) {
			super();
			this.userName = name;
		}
		
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public Date getBirthday() {
			return birthday;
		}
		public void setBirthday(Date birthday) {
			this.birthday = birthday;
		}
		public Map<String, Object> getAttrs() {
			return attrs;
		}
		public void setAttrs(Map<String, Object> attrs) {
			this.attrs = attrs;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public TestUserStatus getStatus() {
			return status;
		}

		public void setStatus(TestUserStatus status) {
			this.status = status;
		}
	}
	
	public static enum TestUserStatus {
		NORMAL,
		DISABLED;
	}
}
