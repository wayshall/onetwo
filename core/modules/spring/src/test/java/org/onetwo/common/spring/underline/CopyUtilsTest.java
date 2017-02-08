package org.onetwo.common.spring.underline;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.copier.Cloneable;
import org.onetwo.common.spring.copier.CopyUtils;
import org.onetwo.common.spring.copier.CopyUtils.BeanCopierBuilder;
import org.onetwo.common.utils.LangOps;
import org.springframework.beans.ConversionNotSupportedException;

public class CopyUtilsTest {
	
	@Test
	public void testSimple(){
		PropertyDescriptor pd = ReflectUtils.getPropertyDescriptor(new CapitalBean(), "datas");
		boolean rs = pd.getReadMethod().getGenericReturnType() instanceof ParameterizedType;
		System.out.println("rs: " + rs);
		ParameterizedType ptype = (ParameterizedType)pd.getReadMethod().getGenericReturnType();
		System.out.println("type: " + ptype.getActualTypeArguments()[0]);
	}
	
	@Test
	public void testClone(){
		Date now = new Date();
		Date createTime = new Date(now.getTime()+3600000);
		String userName = "testName";
		long id = 111333L;
		
		CapitalBean srcBean = new CapitalBean();
		srcBean.setId(id);
		srcBean.setUserName(userName);
		srcBean.setBirthday(now);
		srcBean.setCreateTime(createTime);
		
		CapitalBean srcBean2 = srcBean.clone();
		Assert.assertTrue(srcBean!=srcBean2);
		Assert.assertEquals(userName, srcBean2.getUserName());
		Assert.assertEquals(id, srcBean2.getId());
		Assert.assertEquals(createTime, srcBean2.getCreateTime());
		Assert.assertEquals(now, srcBean2.getBirthday());
		
		long dataId = 2342332;
		String subName = "subNameTest";
		CapitalBean2 srcData = new CapitalBean2();
		srcData.setId(dataId);
		srcData.setSubName(subName);
		srcData.setCreateTime(createTime);
		
		List<CapitalBean2> datas = new ArrayList<CopyUtilsTest.CapitalBean2>();
		datas.add(srcData);
		srcBean.setDatas(datas);
		
		srcBean2 = srcBean.clone();
		Assert.assertTrue(srcBean2.getDatas()==datas);
		
		srcBean2 = CopyUtils.deepClone(srcBean);
		Assert.assertTrue(srcBean2.getDatas()!=datas);
		CapitalBean2 srcData2 = srcBean2.getDatas().get(0);
		Assert.assertTrue(srcData2!=srcData);
		Assert.assertEquals(srcData.getId(), srcData2.getId());
		Assert.assertEquals(srcData.getSubName(), srcData2.getSubName());
	}

	@Test
	public void testCopy(){
		Date now = new Date();
		Date createTime = new Date(now.getTime()+3600000);
		String userName = "testName";
		long id = 111333L;
		
		CapitalBean srcBean = new CapitalBean();
		srcBean.setId(id);
		srcBean.setUserName(userName);
		srcBean.setBirthday(now);
		srcBean.setCreateTime(createTime);
		
		UnderlineBean target = CopyUtils.copy(UnderlineBean.class, srcBean);
		

		Assert.assertEquals(userName, target.getUser_name());
		Assert.assertEquals(id, target.getId());
		Assert.assertEquals(createTime, target.getCreate_time());
		Assert.assertEquals(now, target.getBirthday());
		
		Assert.assertEquals(srcBean.getId(), target.getId());
		Assert.assertEquals(srcBean.getUserName(), target.getUser_name());
		Assert.assertEquals(srcBean.getBirthday(), target.getBirthday());
		Assert.assertEquals(srcBean.getCreateTime(), target.getCreate_time());
		
		target = new UnderlineBean();
		BeanCopierBuilder.fromObject(srcBean)
						.propertyNameConvertor(CopyUtils.UNDERLINE_CONVERTOR)
						.to(target);  
		Assert.assertEquals(userName, target.getUser_name());
		Assert.assertEquals(id, target.getId());
		Assert.assertEquals(createTime, target.getCreate_time());
		Assert.assertEquals(now, target.getBirthday());
		
		Assert.assertEquals(srcBean.getId(), target.getId());
		Assert.assertEquals(srcBean.getUserName(), target.getUser_name());
		Assert.assertEquals(srcBean.getBirthday(), target.getBirthday());
		Assert.assertEquals(srcBean.getCreateTime(), target.getCreate_time());
		
		
		srcBean = CopyUtils.copy(new CapitalBean(), target);

		Assert.assertEquals(userName, srcBean.getUserName());
		Assert.assertEquals(id, srcBean.getId());
		Assert.assertEquals(createTime, srcBean.getCreateTime());
		Assert.assertEquals(now, srcBean.getBirthday());
		
		Assert.assertEquals(srcBean.getId(), target.getId());
		Assert.assertEquals(srcBean.getUserName(), target.getUser_name());
		Assert.assertEquals(srcBean.getBirthday(), target.getBirthday());
		Assert.assertEquals(srcBean.getCreateTime(), target.getCreate_time());
		
	}
	

	@Test
	public void testCopyIngoreNull(){
		Date now = new Date();
		Date createTime = new Date(now.getTime()+3600000);
		String userName = "testName";
		long id = 111333L;
		
		CapitalBean srcBean = new CapitalBean();
		srcBean.setId(id);
		srcBean.setUserName(userName);
		srcBean.setBirthday(now);
		srcBean.setCreateTime(createTime);
		
		Integer age = 5000;
		CapitalBean target = new CapitalBean();
		target.setAge(age);
		srcBean.setAge(null);
		CopyUtils.copyFrom(srcBean).ignoreNullValue().to(target);
		Assert.assertEquals(srcBean.getId(), target.getId());
		Assert.assertEquals(srcBean.getUserName(), target.getUserName());
		Assert.assertEquals(srcBean.getBirthday(), target.getBirthday());
		Assert.assertEquals(age, target.getAge());

		target = new CapitalBean();
		target.setAge(age);
		srcBean.setAge(null);
		CopyUtils.copyFrom(srcBean).to(target);
		Assert.assertEquals(srcBean.getId(), target.getId());
		Assert.assertEquals(srcBean.getUserName(), target.getUserName());
		Assert.assertEquals(srcBean.getBirthday(), target.getBirthday());
		Assert.assertTrue(target.getAge()==null);
	}


	@Test
	public void testCopyIngoreBlank(){
		Date now = new Date();
		Date createTime = new Date(now.getTime()+3600000);
		String userName = "testName";
		long id = 111333L;
		
		CapitalBean srcBean = new CapitalBean();
		srcBean.setId(id);
		srcBean.setBirthday(now);
		srcBean.setCreateTime(createTime);
		
		Integer age = 5000;
		CapitalBean target = new CapitalBean();
		target.setAge(age);
		target.setUserName(userName);
		srcBean.setUserName("");
		srcBean.setPassword("     ");
		srcBean.setAge(null);
		CopyUtils.copyFrom(srcBean)
				.ignoreBlankString()
				.to(target);
		Assert.assertEquals(srcBean.getId(), target.getId());
		Assert.assertEquals(userName, target.getUserName());
		Assert.assertEquals(srcBean.getBirthday(), target.getBirthday());
		Assert.assertTrue(target.getAge()==null);
		Assert.assertTrue(target.getPassword()==null);
		
		target = new CapitalBean();
		target.setAge(age);
		target.setUserName(userName);
		srcBean.setUserName("");
		srcBean.setPassword("     ");
		srcBean.setAge(null);
		CopyUtils.copyFrom(srcBean)
				.ignoreNullValue()
				.ignoreBlankString()
				.to(target);
		Assert.assertEquals(srcBean.getId(), target.getId());
		Assert.assertEquals(userName, target.getUserName());
		Assert.assertEquals(srcBean.getBirthday(), target.getBirthday());
		Assert.assertEquals(age, target.getAge());
		Assert.assertTrue(target.getPassword()==null);

	}
	

	@Test
	public void testCopyIngoreField(){
		Date now = new Date();
		Date createTime = new Date(now.getTime()+3600000);
		String userName = "testName";
		long id = 111333L;
		
		CapitalBean srcBean = new CapitalBean();
		srcBean.setId(id);
		srcBean.setBirthday(now);
		srcBean.setCreateTime(createTime);
		srcBean.setUserName(userName);
		srcBean.setPassword("password1");
		srcBean.setAge(null);
		
		Integer age = 5000;
		CapitalBean target = new CapitalBean();
		target.setAge(age+1);
		target.setUserName("userName2");
		target.setPassword("password2");
		target.setId(1111);
		CopyUtils.copyFrom(srcBean)
//				.ignoreBlankString()
				.ignoreFields("password")
				.ignoreFields("userName")
				.to(target);
		Assert.assertEquals(srcBean.getId(), target.getId());
		Assert.assertEquals(srcBean.getBirthday(), target.getBirthday());
		Assert.assertTrue(target.getAge()==null);
		Assert.assertTrue(target.getId()==srcBean.getId());
		Assert.assertTrue(srcBean.getPassword()=="password1");
		Assert.assertTrue(target.getPassword()=="password2");
		Assert.assertTrue(srcBean.getUserName()==userName);
		Assert.assertTrue(target.getUserName()=="userName2");
		

		target = new CapitalBean();
		target.setAge(age+1);
		target.setUserName("userName2");
		target.setPassword("password2");
		target.setId(1111);
		CopyUtils.copyFrom(srcBean)
//				.ignoreBlankString()
				.includeFields("password")
				.includeFields("userName")
				.to(target);
		Assert.assertEquals(1111, target.getId());
		Assert.assertTrue(target.getBirthday()==null);
		Assert.assertTrue(target.getAge()==age+1);
		Assert.assertTrue(target.getPassword()=="password1");
		Assert.assertTrue(target.getUserName()==userName);

	}
	
	@Test
	public void testCopyList(){
		Date now = new Date();
		Date createTime = new Date(now.getTime()+3600000);
		String userName = "testName";
		long id = 111333L;
		
		CapitalBean srcBean = new CapitalBean();
		srcBean.setId(id);
		srcBean.setUserName(userName);
		srcBean.setBirthday(now);
		srcBean.setCreateTime(createTime);
		
		long dataId = 2342332;
		String subName = "subNameTest";
		CapitalBean2 srcData = new CapitalBean2();
		srcData.setId(dataId);
		srcData.setSubName(subName);
		srcData.setCreateTime(createTime);
		
		List<CapitalBean2> datas = new ArrayList<CopyUtilsTest.CapitalBean2>();
		datas.add(srcData);
		srcBean.setDatas(datas);
		

		try {
			UnderlineBeanWithoutCloneable underlineBeanWithoutCloneable = CopyUtils.copy(UnderlineBeanWithoutCloneable.class, srcBean);
			Assert.fail("it should be faield");
		} catch (Exception e) {
			Assert.assertTrue(ConversionNotSupportedException.class.isInstance(e));
		}
		
		UnderlineBean target = CopyUtils.copy(UnderlineBean.class, srcBean);

		Assert.assertEquals(userName, target.getUser_name());
		Assert.assertEquals(id, target.getId());
		Assert.assertEquals(createTime, target.getCreate_time());
		Assert.assertEquals(now, target.getBirthday());
		
		Assert.assertEquals(srcBean.getId(), target.getId());
		Assert.assertEquals(srcBean.getUserName(), target.getUser_name());
		Assert.assertEquals(srcBean.getBirthday(), target.getBirthday());
		Assert.assertEquals(srcBean.getCreateTime(), target.getCreate_time());
		
		Assert.assertNotNull(target.getDatas());
		Assert.assertEquals(1, target.getDatas().size());
		UnderlineBean2 targetData = target.getDatas().get(0);
		Assert.assertEquals(dataId, targetData.getId());
		Assert.assertEquals(subName, targetData.getSub_name());
		Assert.assertEquals(createTime, targetData.getCreate_time());
		
		
		srcBean = CopyUtils.copy(new CapitalBean(), target);

		Assert.assertEquals(userName, srcBean.getUserName());
		Assert.assertEquals(id, srcBean.getId());
		Assert.assertEquals(createTime, srcBean.getCreateTime());
		Assert.assertEquals(now, srcBean.getBirthday());
		
		Assert.assertEquals(srcBean.getId(), target.getId());
		Assert.assertEquals(srcBean.getUserName(), target.getUser_name());
		Assert.assertEquals(srcBean.getBirthday(), target.getBirthday());
		Assert.assertEquals(srcBean.getCreateTime(), target.getCreate_time());
	}
	

	
	@Test
	public void testDeepClone(){
		Date now = new Date();
		Date createTime = new Date(now.getTime()+3600000);
		String userName = "testName";
		long id = 111333L;
		
		CapitalBean srcBean = new CapitalBean();
		srcBean.setId(id);
		srcBean.setUserName(userName);
		srcBean.setBirthday(now);
		srcBean.setCreateTime(createTime);
		
		long dataId = 2342332;
		String subName = "subNameTest";
		CapitalBean2 srcData = new CapitalBean2();
		srcData.setId(dataId);
		srcData.setSubName(subName);
		srcData.setCreateTime(createTime);
		
		List<CapitalBean2> datas = new ArrayList<CopyUtilsTest.CapitalBean2>();
		datas.add(srcData);
		srcBean.setDatas(datas);
		
		List<CapitalBean2> datas2 = CopyUtils.deepClones(datas);
		Assert.assertFalse(datas==datas2);
		
		CapitalBean target = CopyUtils.deepClone(srcBean);
		Assert.assertNotEquals(srcBean, target);
		Assert.assertNotEquals(srcBean.getDatas(), target.getDatas());
		
		Assert.assertNotEquals(srcBean.getDatas().get(0), target.getDatas().get(0));
		Assert.assertEquals(srcBean.getId(), target.getId());
		Assert.assertEquals(srcBean.getUserName(), target.getUserName());
		Assert.assertEquals(srcBean.getBirthday(), target.getBirthday());
		

		Assert.assertEquals(srcBean.getDatas().get(0).getId(), target.getDatas().get(0).getId());
		Assert.assertEquals(srcBean.getDatas().get(0).getSubName(), target.getDatas().get(0).getSubName());
		Assert.assertEquals(srcBean.getDatas().get(0).getCreateTime(), target.getDatas().get(0).getCreateTime());

	}
	
//	@Test
	public void testCompare(){
		Date now = new Date();
		Date createTime = new Date(now.getTime()+3600000);
		String userName = "testName";
		long id = 111333L;
		
		CapitalBean srcBean = new CapitalBean();
		srcBean.setId(id);
		srcBean.setUserName(userName);
		srcBean.setBirthday(now);
		srcBean.setCreateTime(createTime);
		
		int count = 100000;
		LangOps.repeatRun("copyFrom", count, ()->{
			CopyUtils.copyFrom(srcBean).toNewInstance();
		});
		
		LangOps.repeatRun("deepClone", count, ()->{
			CopyUtils.deepClone(srcBean);
		});
		
		long dataId = 2342332;
		String subName = "subNameTest";
		CapitalBean2 srcData = new CapitalBean2();
		srcData.setId(dataId);
		srcData.setSubName(subName);
		srcData.setCreateTime(createTime);
		
		List<CapitalBean2> datas = new ArrayList<CopyUtilsTest.CapitalBean2>();
		datas.add(srcData);
		srcBean.setDatas(datas);
		

		LangOps.repeatRun("copyFrom", count, ()->{
			CopyUtils.copyFrom(srcBean).toNewInstance();
		});
		
		LangOps.repeatRun("deepClone", count, ()->{
			CopyUtils.deepClone(srcBean);
		});
	}

	public static class CapitalBean implements Serializable, java.lang.Cloneable {
		private long id;
		private Integer age;
		private String userName;
		private String password;
		private Date birthday;
		private Date createTime;
		
		@Cloneable
		private List<CapitalBean2> datas;
		
		@Override
		public CapitalBean clone()  {
			try {
				return (CapitalBean)super.clone();
			} catch (CloneNotSupportedException e) {
				throw new RuntimeException(e);
			}
		}
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
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
		public Date getCreateTime() {
			return createTime;
		}
		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}
		public List<CapitalBean2> getDatas() {
			return datas;
		}
		public void setDatas(List<CapitalBean2> datas) {
			this.datas = datas;
		}
		public Integer getAge() {
			return age;
		}
		public void setAge(Integer age) {
			this.age = age;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		
	}
	

	public static class CapitalBean2 implements Serializable{
		private long id;
		private String subName;
		private Date createTime;
		
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public Date getCreateTime() {
			return createTime;
		}
		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}
		public String getSubName() {
			return subName;
		}
		public void setSubName(String subName) {
			this.subName = subName;
		}
	}

	public static class UnderlineBean implements Serializable {
		private long id;
		private String user_name;
		private Date birthday;
		private Date create_time;
		
		@Cloneable
		private List<UnderlineBean2> datas;
		
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getUser_name() {
			return user_name;
		}
		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}
		public Date getBirthday() {
			return birthday;
		}
		public void setBirthday(Date birthday) {
			this.birthday = birthday;
		}
		public Date getCreate_time() {
			return create_time;
		}
		public void setCreate_time(Date create_time) {
			this.create_time = create_time;
		}
		public List<UnderlineBean2> getDatas() {
			return datas;
		}
		public void setDatas(List<UnderlineBean2> datas) {
			this.datas = datas;
		}
		
	}
	

	public static class UnderlineBean2 implements Serializable {
		private long id;
		private String sub_name;
		private Date create_time;
		
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public Date getCreate_time() {
			return create_time;
		}
		public void setCreate_time(Date create_time) {
			this.create_time = create_time;
		}
		public String getSub_name() {
			return sub_name;
		}
		public void setSub_name(String sub_name) {
			this.sub_name = sub_name;
		}
	}
	

	public static class UnderlineBeanWithoutCloneable {
		private long id;
		private String user_name;
		private Date birthday;
		private Date create_time;
		
		private List<UnderlineBean2> datas;
		
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getUser_name() {
			return user_name;
		}
		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}
		public Date getBirthday() {
			return birthday;
		}
		public void setBirthday(Date birthday) {
			this.birthday = birthday;
		}
		public Date getCreate_time() {
			return create_time;
		}
		public void setCreate_time(Date create_time) {
			this.create_time = create_time;
		}
		public List<UnderlineBean2> getDatas() {
			return datas;
		}
		public void setDatas(List<UnderlineBean2> datas) {
			this.datas = datas;
		}
		
	}
}
