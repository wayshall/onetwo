package org.onetwo.dbm.ui.json;

import java.io.IOException;
import java.util.Optional;

import org.onetwo.common.spring.Springs;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.dbm.ui.core.DefaultUISelectDataProviderService;
import org.onetwo.dbm.ui.meta.DUIEntityMeta;
import org.onetwo.dbm.ui.meta.DUIFieldMeta;
import org.onetwo.dbm.ui.meta.DUIFieldMeta.DUISelectMeta;
import org.onetwo.dbm.ui.spi.DUIMetaManager;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class DUISelectFieldJsonSerializer extends JsonSerializer<Object> {
	
    @Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider serializers) throws IOException, JsonProcessingException {
    	if (value==null) {
			return ;
		}
    	
    	jgen.writeString(value.toString());
		
		Object object = jgen.getOutputContext().getCurrentValue();
		String fieldName = jgen.getOutputContext().getCurrentName();
		if (object==null || fieldName==null) {
			return ;
		}
		
		Optional<DUIEntityMeta> meta = getDUIMetaManager().find(object.getClass());
		if (!meta.isPresent()) {
			return ;
		} 
		
		DUIFieldMeta field = meta.get().getField(fieldName);
		if (StringUtils.isNotBlank(field.getListField()) && field.getSelect()!=null) {
			DUISelectMeta uiselect = field.getSelect();
			Object label = getUISelectDataProviderService().getListValue(uiselect, value);
			jgen.writeStringField(field.getListField(), label==null?"":label.toString());
		}
    }

	protected DUIMetaManager getDUIMetaManager() {
		return Springs.getInstance().getBean(DUIMetaManager.class);
	}
	
	protected DefaultUISelectDataProviderService getUISelectDataProviderService() {
		return Springs.getInstance().getBean(DefaultUISelectDataProviderService.class);
	}

}
