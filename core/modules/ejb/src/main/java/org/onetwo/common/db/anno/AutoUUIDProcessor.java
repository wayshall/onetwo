package org.onetwo.common.db.anno;

import java.beans.PropertyDescriptor;

import org.onetwo.biz.utils.CodeGenUtils;
import org.onetwo.common.db.DashReplacement;
import org.onetwo.common.db.EntityMonitorAction;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.annotation.AbstractAnnotationProcessor;
import org.onetwo.common.utils.annotation.AnnoContext;

public class AutoUUIDProcessor extends AbstractAnnotationProcessor<EntityMonitorAction> {

	public static final String DASH = "-";
	
	public AutoUUIDProcessor(EntityMonitorAction... action) {
		super(action);
	} 

	public void doAnnotation(AnnoContext context) {
		AutoUUID autoUUID = (AutoUUID)context.getAnnotation(); 
		/*if(!autoUUID.on().equals(context.getEventAction()))
			return ;*/
		if(!ArrayUtils.contains(autoUUID.on(), context.getEventAction())){
			return ;
		}
		PropertyDescriptor p = context.getAnnoIn();
		String val = (String)ReflectUtils.invokeMethod(p.getReadMethod(), context.getSrcObject());
		if(StringUtils.isBlank(val)){
			String uuid = CodeGenUtils.randomUUID();
			String rep = autoUUID.dashReplace();
			if(!DashReplacement.None.equals(rep)){
				uuid = uuid.replace(DASH, rep);
			}
			if(p.getWriteMethod()!=null){
				ReflectUtils.invokeMethod(p.getWriteMethod(), context.getSrcObject(), uuid);
			}else{
				String wm = ReflectUtils.getWriteMethodName(p.getName());
				ReflectUtils.invokeMethod(wm, context.getSrcObject(), uuid);
			}
		}
	}

}
