package org.onetwo.boot.func.signature;

import java.util.Arrays;
import java.util.List;

import org.onetwo.common.reflect.BeanToMapConvertor;
import org.onetwo.common.spring.utils.EnhanceBeanToMapConvertor.EnhanceBeanToMapBuilder;
import org.onetwo.common.utils.DataSigner.DefaultDataSigner;
import org.onetwo.common.utils.StringUtils;

import com.google.common.collect.Lists;

/**
 * @author weishao zeng
 * <br/>
 */
public class SimpleDataSigner extends DefaultDataSigner {

	protected BeanToMapConvertor getBeanToMapConvertor(String... excludeProperties){
		List<String> excludes = Lists.newArrayList();
		excludes.add(BaseSignableRequest.FIELD_SIGNKEY);
		excludes.add(BaseSignableRequest.FIELD_TIMESTAMP);
		excludes.add(StringUtils.capitalize(BaseSignableRequest.FIELD_SIGNKEY));
		excludes.add(StringUtils.capitalize(BaseSignableRequest.FIELD_TIMESTAMP));
		excludes.addAll(Arrays.asList(excludeProperties));
		BeanToMapConvertor convertor = EnhanceBeanToMapBuilder.enhanceBuilder()
															.enableJsonPropertyAnnotation()
															.excludeProperties(excludes.toArray(new String[0]))
															.enableFieldNameAnnotation()
//															.propertyAcceptor((p, v)->v!=null)
															.build();
		return convertor;
	}
}
