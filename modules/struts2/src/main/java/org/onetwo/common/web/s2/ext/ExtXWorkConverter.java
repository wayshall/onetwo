package org.onetwo.common.web.s2.ext;

import com.opensymphony.xwork2.conversion.impl.XWorkBasicConverter;
import com.opensymphony.xwork2.conversion.impl.XWorkConverter;
import com.opensymphony.xwork2.inject.Inject;

@SuppressWarnings("unchecked")
public class ExtXWorkConverter extends XWorkConverter {

    @Inject(ExtXWorkBasicConverter.BEAN_NAME)
	public void setDefaultTypeConverter(XWorkBasicConverter conv) {
		super.setDefaultTypeConverter(conv);
	}
	
}
