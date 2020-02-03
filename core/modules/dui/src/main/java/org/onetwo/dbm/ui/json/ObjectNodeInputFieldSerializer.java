package org.onetwo.dbm.ui.json;

import java.util.Optional;

import org.onetwo.dbm.ui.meta.DUIFieldMeta;
import org.onetwo.dbm.ui.spi.DUIJsonValueWriter;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author weishao zeng
 * <br/>
 */
@SuppressWarnings("serial")
public class ObjectNodeInputFieldSerializer extends CustomerWriterBaseSerializer<ObjectNode> {

    public ObjectNodeInputFieldSerializer() {
        super(ObjectNode.class);
    }

	@SuppressWarnings("unchecked")
	protected Optional<DUIJsonValueWriter<ObjectNode>> getDUIJsonValueWriter(DUIFieldMeta field) {
		if (field.getInput().hasValueWriter()) {
			DUIJsonValueWriter<ObjectNode> transfer = getDUIJsonValueWriter((Class<? extends DUIJsonValueWriter<ObjectNode>>)field.getInput().getValueWriter());
			return Optional.of(transfer);
		} 
		return Optional.empty();
	}
    
	
	@JsonSerialize(using=ObjectNodeInputFieldSerializer.class)
	public static class ObjectNodeMixin {
		
	}
}
