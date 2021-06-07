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
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * @author weishao zeng
 * <br/>
 */
@SuppressWarnings("serial")
abstract public class CustomerWriterBaseSerializer<T> extends StdSerializer<T> {

    public CustomerWriterBaseSerializer(Class<T> clazz) {
        super(clazz);
    }
    
	@Override
	public void serialize(T value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
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
			Optional<DUIJsonValueWriter<T>> transfer = getDUIJsonValueWriter(field);
			if (transfer.isPresent()) {
				transfer.get().write(value, field, jgen);
			} else {
				writeObject(jgen, value);
			}
		} else {
			writeObject(jgen, value);
		}
		
	}
	
	abstract protected Optional<DUIJsonValueWriter<T>> getDUIJsonValueWriter(DUIFieldMeta field);
	
	protected void writeObject(JsonGenerator jgen, T value) throws IOException {
		jgen.writeStartObject();
		jgen.writeObject(value);
		jgen.writeEndObject();
	}

	protected DUIMetaManager getDUIMetaManager() {
		return Springs.getInstance().getBean(DUIMetaManager.class);
	}

	protected <W extends DUIJsonValueWriter<T>> W getDUIJsonValueWriter(Class<W> writerClass) {
		return writerClass.cast(Springs.getInstance().getBean(writerClass));
	}
	
}
