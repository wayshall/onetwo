package org.onetwo.common.rest.vo;

/**********
 * addCallback params: {itemordersno=201304191425107370561672, verifydate=2013-04-19 14:30:25, userkey=486888a9311a4fa4a3fbb74d28658389, rd=14:30, return_status=RETURN_SUCCESS}
 * @author wayshall
 *
 */
public class CallbackParams {
	public static final String SUCCESS = "RETURN_SUCCESS";
	
	private String itemordersno;
	private String verifydate;
	private String userkey;
	private String rd;
	private String return_status;
	
	public boolean isSucceed(){
		return SUCCESS.equalsIgnoreCase(return_status);
	}

	public String getItemordersno() {
		return itemordersno;
	}
	
	public boolean isSuccess(){
		return SUCCESS.equals(return_status);
	}

	public void setItemordersno(String itemordersno) {
		this.itemordersno = itemordersno;
	}

	public String getVerifydate() {
		return verifydate;
	}

	public void setVerifydate(String verifydate) {
		this.verifydate = verifydate;
	}

	public String getUserkey() {
		return userkey;
	}

	public void setUserkey(String userkey) {
		this.userkey = userkey;
	}

	public String getRd() {
		return rd;
	}

	public void setRd(String rd) {
		this.rd = rd;
	}

	public String getReturn_status() {
		return return_status;
	}

	public void setReturn_status(String return_status) {
		this.return_status = return_status;
	}
	
	public String toString(){
		return "itemordersno:"+itemordersno+", userkey:"+userkey+", return_status:"+return_status;
	}

}
