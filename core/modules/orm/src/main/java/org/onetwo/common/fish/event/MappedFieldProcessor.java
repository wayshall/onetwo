package org.onetwo.common.fish.event;

import org.onetwo.common.fish.orm.JFishMappedField;

public interface MappedFieldProcessor<T extends JFishMappedField> {
	void execute(T field);
}
