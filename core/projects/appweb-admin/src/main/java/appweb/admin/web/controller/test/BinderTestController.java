package appweb.admin.web.controller.test;

import java.util.Date;

import org.onetwo.common.spring.underline.ConvertUnderlineProperty;
import org.onetwo.common.spring.underline.UnderlineInitBinder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("binder-test")
public class BinderTestController implements UnderlineInitBinder {

	/****
	 * test url: http://localhost:9080/appweb-admin/binder-test/testInitbinder?user_name=test
	 * @param paramBean
	 * @return
	 */
	@RequestMapping("testInitbinder")
	@ResponseBody
	public Object testInitbinder(CapitalBean paramBean){
		return paramBean;
	}
	

	@ConvertUnderlineProperty
	public static class CapitalBean {
		private long id;
		private String userName;
		private Date birthday;
		private Date createTime;
		
		private CapitalBean2 bean2;
		
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
		public CapitalBean2 getBean2() {
			return bean2;
		}
		public void setBean2(CapitalBean2 bean2) {
			this.bean2 = bean2;
		}
		
	}
	

	@ConvertUnderlineProperty
	public static class CapitalBean2 {
		private long id;
		private String userName;
		private Date birthday;
		private Date createTime;
		
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
		
	}
	
}
