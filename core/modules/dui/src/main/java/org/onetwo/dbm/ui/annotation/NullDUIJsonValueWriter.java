package org.onetwo.dbm.ui.annotation;

import org.onetwo.dbm.ui.meta.DUIFieldMeta;
import org.onetwo.dbm.ui.spi.DUIJsonValueWriter;

import com.fasterxml.jackson.core.JsonGenerator;

/**
 * @author weishao zeng <br/>
 */
final public class NullDUIJsonValueWriter implements DUIJsonValueWriter<Object> {

	@Override
	public void write(Object value, DUIFieldMeta field, JsonGenerator jgen) {
		throw new UnsupportedOperationException();
	}

}
