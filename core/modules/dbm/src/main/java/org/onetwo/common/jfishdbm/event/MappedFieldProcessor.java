package org.onetwo.common.jfishdbm.event;

import org.onetwo.common.jfishdbm.mapping.DbmMappedField;

public interface MappedFieldProcessor<T extends DbmMappedField> {
	void execute(T field);
}
