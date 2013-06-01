package org.onetwo.common.ioc;

import org.onetwo.common.ioc.inject.InjectAnnotationParser;

public interface InjectProcessor {
	
	public void setInnerContainer(InnerContainer innerContainer);
	
	public InjectProcessor addAnnotationParser(InjectAnnotationParser parser);

	public void inject(Object object);

}