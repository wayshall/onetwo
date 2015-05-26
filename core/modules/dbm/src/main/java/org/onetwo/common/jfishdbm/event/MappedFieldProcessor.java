package org.onetwo.common.jfishdbm.event;

import org.onetwo.common.jfishdbm.mapping.JFishMappedField;

public interface MappedFieldProcessor<T extends JFishMappedField> {
	void execute(T field);
}
