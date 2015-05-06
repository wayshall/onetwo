package org.onetwo.common.jfishdb.event;

import org.onetwo.common.jfishdb.orm.JFishMappedField;

public interface MappedFieldProcessor<T extends JFishMappedField> {
	void execute(T field);
}
