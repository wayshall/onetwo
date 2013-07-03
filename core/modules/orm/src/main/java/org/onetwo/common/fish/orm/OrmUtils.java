package org.onetwo.common.fish.orm;

import java.util.Collections;
import java.util.List;

import org.onetwo.common.fish.annotation.JFishFieldListeners;
import org.onetwo.common.fish.event.JFishEntityFieldListener;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;

public abstract class OrmUtils {

	public static List<JFishEntityFieldListener> initJFishEntityFieldListeners(JFishFieldListeners listenersAnntation){
		List<JFishEntityFieldListener> fieldListeners = Collections.EMPTY_LIST;
		if(listenersAnntation!=null){
			fieldListeners = LangUtils.newArrayList();
			Class<? extends JFishEntityFieldListener>[] flClasses = listenersAnntation.value();
			for(Class<? extends JFishEntityFieldListener> flClass : flClasses){
				if(flClass==null)
					continue;
				JFishEntityFieldListener fl = ReflectUtils.newInstance(flClass);
				fieldListeners.add(fl);
			}
		}
		return fieldListeners;
	}
	
	private OrmUtils(){
	}
}
