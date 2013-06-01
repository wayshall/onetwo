package org.onetwo.common.ioc;


abstract public class AbstractBFModule implements BFModule, ContainerAware{
	
	protected Container container;

	public void setContainer(Container container) { 
		this.container = container;
	}

	public Container getContainer() {
		return container;
	}
	
}
