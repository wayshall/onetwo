package org.onetwo.common.spring.underline;

import org.onetwo.common.utils.StringUtils;

public class SeperatorNamedConvertor implements PropertyNameConvertor {
    public static final String SEPERATOR_UNDERLINE = "_";
    public static final PropertyNameConvertor UNDERLINE_CONVERTOR = new SeperatorNamedConvertor(SEPERATOR_UNDERLINE);
    
    public final String seperator;
    

	public SeperatorNamedConvertor(String seperator) {
		super();
		this.seperator = seperator;
	}

	@Override
	public String convert(String targetPropertyName) {
		if(targetPropertyName.contains(seperator)){
			return StringUtils.toPropertyName(targetPropertyName);
		}else if(StringUtils.hasUpper(targetPropertyName)){
			return StringUtils.convertWithSeperator(targetPropertyName, seperator);
		}
		return targetPropertyName;
	}

}
