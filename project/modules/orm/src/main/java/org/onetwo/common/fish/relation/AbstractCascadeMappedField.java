package org.onetwo.common.fish.relation;

import org.onetwo.common.fish.orm.AbstractMappedField;
import org.onetwo.common.fish.orm.JFishMappedEntry;
import org.onetwo.common.utils.JFishProperty;


public class AbstractCascadeMappedField extends AbstractMappedField implements CascadeMappedField {

	protected JFishMappedEntry cascadeEntry;//(@JoinColumn)
	
	private JoinTableMapper joinTableMapper;
	private JoinColumnMapper joinColumnMapper;
	
	public AbstractCascadeMappedField(JFishMappedEntry entry, JFishProperty prop){
		super(entry, prop);
	}
	
	public AbstractCascadeMappedField(JFishMappedEntry entry, JFishProperty prop, JoinTableMapper joinTableMapper, JoinColumnMapper joinColumnMapper){
		super(entry, prop);
		this.joinTableMapper = joinTableMapper;
		this.joinColumnMapper = joinColumnMapper;
	}

	
	@Override
	public JFishMappedEntry getCascadeEntry() {
		return cascadeEntry;
	}

	@Override
	public void setCascadeEntry(JFishMappedEntry manySideEntry) {
		this.checkFreezing("setCascadeEntry");
		this.cascadeEntry = manySideEntry;
	}

	public JoinTableMapper getJoinTableMapper() {
		return joinTableMapper;
	}

	public JoinColumnMapper getJoinColumnMapper() {
		return joinColumnMapper;
	}

	public void setJoinTableMapper(JoinTableMapper joinTableMapper) {
		this.joinTableMapper = joinTableMapper;
	}

	public void setJoinColumnMapper(JoinColumnMapper joinColumnMapper) {
		this.joinColumnMapper = joinColumnMapper;
	}

	@Override
	public boolean isJoinTableField(){
		return joinTableMapper!=null;
	}
}
