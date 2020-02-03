package org.onetwo.dbm.ui.json;

import java.io.IOException;

import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.dbm.ui.meta.DUIFieldMeta;
import org.onetwo.dbm.ui.spi.DUIJsonValueWriter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author weishao zeng
 * <br/>
 */
public class ObjectNodeToStringValueWriter implements DUIJsonValueWriter<ObjectNode> {

	@Override
	public void write(ObjectNode value, DUIFieldMeta field, JsonGenerator jgen) throws IOException {
		jgen.writeString(JsonMapper.toJsonString(value));
	}

}
