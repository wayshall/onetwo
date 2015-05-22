package org.onetwo.common.jfishdbm.utils;

import java.util.Collections;
import java.util.List;

import org.onetwo.common.jfishdbm.annotation.JFishFieldListeners;
import org.onetwo.common.jfishdbm.event.JFishEntityFieldListener;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;

final public class JFishdbUtils {
	private JFishdbUtils(){
	}
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
}
