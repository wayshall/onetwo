package org.onetwo.common.ws;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("serial")
@XmlRootElement( name="WSResult" )
@XmlAccessorType
public class WSResult implements Serializable {
	
	public static final String MSG_SUCCESS = "执行成功！";
	public static final String MSG_FAIL = "执行失败！";

	/****
	 * 执行是否成功
	 */
	private Boolean success;

	/****
	 * 执行返回的信息
	 */
	private String message;
    
	/****
	 * 执行返回的业务结果代码
	 */
	private String resultCode;
    
    public WSResult(){
    	this(true);
    }

    public WSResult(Boolean success){
    	this.success = success;
    	this.message = "";
    	this.resultCode = "";
    }

    @XmlElement(name = "success", required = true)
	public Boolean isSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result
				+ ((resultCode == null) ? 0 : resultCode.hashCode());
		result = prime * result + (success ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WSResult other = (WSResult) obj;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (resultCode == null) {
			if (other.resultCode != null)
				return false;
		} else if (!resultCode.equals(other.resultCode))
			return false;
		if (success != other.success)
			return false;
		return true;
	}

	public String toString(){
		String str = "success:"+success+", message:"+message+", resultCode:"+resultCode;
		return str;
	}
}
