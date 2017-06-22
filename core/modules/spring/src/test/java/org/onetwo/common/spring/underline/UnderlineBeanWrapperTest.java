package org.onetwo.common.spring.underline;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.spring.copier.Cloneable;
import org.onetwo.common.spring.copier.ConvertToCamelProperty;
import org.onetwo.common.spring.copier.UnderlineBeanWrapper;
import org.onetwo.common.spring.underline.CopyUtilsTest.CapitalBean2;

public class UnderlineBeanWrapperTest {

	@Test
	public void testExtBeanWrapper(){
		UnderlineAnnotationBean srcBean = new UnderlineAnnotationBean();
		UnderlineBeanWrapper bw = new UnderlineBeanWrapper(srcBean);

		String userName = "testValue";
		long id = 11;
		Date createTime = new Date();
		bw.setPropertyValue("id", id);
		bw.setPropertyValue("user_name", userName);
		bw.setPropertyValue("create_time", createTime);
		Assert.assertEquals(id, srcBean.getId());
		Assert.assertEquals(userName, srcBean.getUserName());
		Assert.assertEquals(createTime, srcBean.getCreateTime());
		
	}
	
	@ConvertToCamelProperty
	public static class UnderlineAnnotationBean {
		private long id;
		private String userName;
		private Date birthday;
		private Date createTime;
		
		@Cloneable
		private List<CapitalBean2> datas;
		
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
		
	}
}
