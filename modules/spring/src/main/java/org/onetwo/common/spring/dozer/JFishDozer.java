package org.onetwo.common.spring.dozer;

import java.util.List;
import java.util.Properties;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.bean.BeanDuplicator;

public class JFishDozer extends BeanDuplicator {

	private PropertyUnderlineMappingBuilder builder;
	
	public JFishDozer(List<DozerClassMapper> dozerClasses, Properties props) {
		super(props);
		if(LangUtils.isNotEmpty(dozerClasses)){
			this.builder = new PropertyUnderlineMappingBuilder(dozerClasses);
		}
	}

	@Override
	public void initDozer() {
		if(builder!=null){
			getDozer().addMapping(builder);
		}
	}

}
