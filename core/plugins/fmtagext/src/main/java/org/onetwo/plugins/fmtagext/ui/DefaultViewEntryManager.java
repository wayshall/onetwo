package org.onetwo.plugins.fmtagext.ui;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

import org.onetwo.common.utils.Intro;
import org.onetwo.common.utils.JFishFieldInfoImpl;
import org.onetwo.common.utils.JFishProperty;
import org.onetwo.common.utils.JFishPropertyInfoImpl;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.fmtag.annotation.JEntryViewMeta;
import org.onetwo.plugins.fmtagext.annotation.JFieldView;

public class DefaultViewEntryManager {

	private Map<Class<?>, ViewEntry> viewEntries = LangUtils.newHashMap();
	
	public synchronized ViewEntry getViewEntry(Class<?> viewClass){
		ViewEntry viewEntry = viewEntries.get(viewClass);
		if(viewEntry==null){
			viewEntry = buildViewEntry(viewClass);
			viewEntries.put(viewClass, viewEntry);
		}
		return viewEntry;
	}
	
	protected ViewEntry buildViewEntry(Class<?> viewClass){
		JEntryViewMeta viewMeta = viewClass.getAnnotation(JEntryViewMeta.class);
		if(viewMeta==null)
			return null;
		
		ViewEntry viewEntry = new ViewEntry(viewClass);
		viewEntry.setTitle(viewMeta.label());
		
		Intro<?> wrapper = Intro.wrap(viewClass);
		if(viewMeta.byProperty()){
			Collection<PropertyDescriptor> props = wrapper.getProperties();
			for(PropertyDescriptor prop : props){
				JFieldView jfv = prop.getReadMethod().getAnnotation(JFieldView.class);
				if(jfv==null)
					continue;
				JFishProperty jp = new JFishPropertyInfoImpl(wrapper, prop);
				JFieldViewObject fieldViewObj = new JFieldViewObject(jp, jfv);
				viewEntry.addField(fieldViewObj);
			}
		}else{
			Collection<Field> fields = wrapper.getFields();
			for(Field field : fields){
				JFieldView jfv = field.getAnnotation(JFieldView.class);
				if(jfv==null)
					continue;
				JFishProperty jp = new JFishFieldInfoImpl(wrapper, field);
				JFieldViewObject fieldViewObj = new JFieldViewObject(jp, jfv);
				viewEntry.addField(fieldViewObj);
			}
		}
		viewEntry.sortFields();
		return viewEntry;
	}
}
