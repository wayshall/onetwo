package org.onetwo.common.ioc.inject;

import org.onetwo.common.ioc.InnerContainer;
import org.onetwo.common.ioc.ObjectInfo;

public interface InjectAnnotationParser {

	public boolean containsAnnotation(InjectMeta meta);

	public ObjectInfo parse(InjectMeta meta, InnerContainer container);

}