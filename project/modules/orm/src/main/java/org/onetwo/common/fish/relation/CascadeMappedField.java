package org.onetwo.common.fish.relation;

import org.onetwo.common.fish.orm.JFishMappedEntry;
import org.onetwo.common.fish.orm.JFishMappedField;

public interface CascadeMappedField extends JFishMappedField {
	
	public JFishMappedEntry getCascadeEntry();

	public void setCascadeEntry(JFishMappedEntry manySideEntry);
	
	public JoinTableMapper getJoinTableMapper();

	public JoinColumnMapper getJoinColumnMapper();
	
	public void setJoinTableMapper(JoinTableMapper joinTableMapper);

	public void setJoinColumnMapper(JoinColumnMapper joinColumnMapper);
	

}