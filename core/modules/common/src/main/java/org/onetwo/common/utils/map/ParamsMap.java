package org.onetwo.common.utils.map;

import java.util.Comparator;

import org.apache.commons.collections.map.MultiValueMap;
import org.onetwo.common.utils.ParamUtils;

public class ParamsMap extends MultiValueMap {

	public String toParamString(){
		return toParamString(null);
	}
	@SuppressWarnings("unchecked")
	public String toParamString(Comparator<? super String> comparator){
		return ParamUtils.toParamString(this, comparator);
	}
}
