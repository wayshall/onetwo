package org.onetwo.common.ioc.test;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.ioc.Container;
import org.onetwo.common.ioc.annotation.AfterPropertiesSet;
import org.onetwo.common.ioc.annotation.Inject;


public class ContainerAwareServiceImpl  {

	@Inject
	private Container container;
	
	public ContainerAwareServiceImpl(){
		System.out.println("ContainerAwareServiceImpl");
	}
	
	@AfterPropertiesSet
	public void init(){
		System.out.println("container @AfterPropertiesSet : " + container);
		if(container==null)
			throw new ServiceException("no container!");
	}

	public Container findContainer(){
		return container;
	}

	public void setContainer(Container container) {
		this.container = container;
	}

}
