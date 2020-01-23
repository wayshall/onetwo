package org.onetwo.dbm.ui.json;

import java.io.IOException;

import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.dbm.ui.meta.DUIFieldMeta;
import org.onetwo.dbm.ui.spi.DUIJsonValueWriter;

import com.fasterxml.jackson.core.JsonGenerator;

/**
 * @author weishao zeng
 * <br/>
 */
public class StringValueWriter implements DUIJsonValueWriter {

	@Override
	public void write(Object value, DUIFieldMeta field, JsonGenerator jgen) throws IOException {
		jgen.writeString(JsonMapper.toJsonString(value));
	}

}
