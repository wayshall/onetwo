package org.onetwo.dbm.ui.json;

import java.io.IOException;
import java.util.Optional;

import org.onetwo.common.spring.Springs;
import org.onetwo.dbm.ui.meta.DUIEntityMeta;
import org.onetwo.dbm.ui.meta.DUIFieldMeta;
import org.onetwo.dbm.ui.spi.DUIJsonValueWriter;
import org.onetwo.dbm.ui.spi.DUIMetaManager;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * @author weishao zeng
 * <br/>
 */
@SuppressWarnings("serial")
public class ObjectNodeSerializer extends StdSerializer<ObjectNode> {

    public ObjectNodeSerializer() {
        super(ObjectNode.class);
    }
    
	@Override
	public void serialize(ObjectNode value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
		if (value==null) {
			return ;
		}
		
		Object object = jgen.getOutputContext().getCurrentValue();
		String fieldName = jgen.getOutputContext().getCurrentName();
		if (object==null || fieldName==null) {
			return ;
		}
		
		Optional<DUIEntityMeta> meta = getDUIMetaManager().find(object.getClass());
		if (meta.isPresent()) {
			DUIFieldMeta field = meta.get().getField(fieldName);
			if (field.getInput().hasValueWriter()) {
				DUIJsonValueWriter transfer = Springs.getInstance().getBean(field.getInput().getValueWriter());
				transfer.write(value, field, jgen);
			} else {
				writeObject(jgen, value);
			}
		} else {
			writeObject(jgen, value);
		}
		
	}
	
	private void writeObject(JsonGenerator jgen, Object value) throws IOException {
		jgen.writeStartObject();
		jgen.writeObject(value);
		jgen.writeEndObject();
	}

	protected DUIMetaManager getDUIMetaManager() {
		return Springs.getInstance().getBean(DUIMetaManager.class);
	}
	
	@JsonSerialize(using=ObjectNodeSerializer.class)
	public static class ObjectNodeMixin {
		
	}
}
