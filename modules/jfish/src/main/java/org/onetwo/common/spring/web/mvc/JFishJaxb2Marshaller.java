package org.onetwo.common.spring.web.mvc;

import java.util.List;

import org.onetwo.common.spring.utils.JFishResourcesScanner;
import org.onetwo.common.spring.utils.JaxbClassFilter;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

public class JFishJaxb2Marshaller extends Jaxb2Marshaller {
	
	private JFishResourcesScanner scanner = new JFishResourcesScanner();
	

	public void setXmlBasePackage(String xmlBasePackage) {
		if(StringUtils.isBlank(xmlBasePackage))
			return ;

		String[] packagesToScan = StringUtils.split(xmlBasePackage, ",");
		this.setClassesToBeBoundByBasePackages(packagesToScan);
	}


	public void setClassesToBeBoundByBasePackages(String[] packagesToScan) {
		String pagePackage = Page.class.getPackage().getName();
		packagesToScan = (String[])ArrayUtils.add(packagesToScan, pagePackage);
		
		List<Class<?>> classesToBound = scanner.scan(JaxbClassFilter.Instance, packagesToScan);
		if(BaseSiteConfig.inst().isDev()){
			for(Class<?> cls : classesToBound){
				logger.info("load jaxb class : " + cls);
			}
		} 
		super.setClassesToBeBound(classesToBound.toArray(new Class[classesToBound.size()]));
	}
	
}
