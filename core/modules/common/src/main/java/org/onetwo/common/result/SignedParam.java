package org.onetwo.common.result;

import java.util.List;

import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.StringUtils;


abstract public class SignedParam {
	
	private String signKey;
	private ParamSigner signer;
	

	public SignedParam() {
	}
	public SignedParam(ParamSigner signer) {
		super();
		this.signer = signer;
	}

	public boolean isValidParams(){
		if(signer==null)
			return true;
		return signer.checkSignkey(getSignKey(), getSourceKey());
	}
	
	protected String getSourceKey(){
		List<String> pnames = ReflectUtils.getIntro(this.getClass()).getPropertyNames(SignedIgnore.class);
		StringBuilder str = new StringBuilder();
		for(String p : pnames){
			Object val = ReflectUtils.getProperty(this, p);
			str.append(StringUtils.emptyIfNull(val));
		}
		return str.toString();
	}

	public String getSignKey() {
		return signKey;
	}

	public void setSignKey(String signKey) {
		this.signKey = signKey;
	}



}
