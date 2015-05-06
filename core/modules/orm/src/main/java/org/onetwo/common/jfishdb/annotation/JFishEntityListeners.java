package org.onetwo.common.jfishdb.annotation;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.common.jfishdb.event.JFishEntityListener;
@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JFishEntityListeners {

	Class<? extends JFishEntityListener>[] value();
	
}
