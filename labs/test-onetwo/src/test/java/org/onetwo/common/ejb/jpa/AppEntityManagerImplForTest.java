package org.onetwo.common.ejb.jpa;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.sql.SequenceNameManager;

@Stateless
@Local(BaseEntityManager.class)
public class AppEntityManagerImplForTest extends AbstractBaseEntityManager {
	
	private EntityManager entityManager;
	
	private SequenceNameManager sequenceNameManager;
	
	public AppEntityManagerImplForTest(){
		super();
	}

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public void update(Object entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SequenceNameManager getSequenceNameManager() {
		return this.sequenceNameManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void setSequenceNameManager(SequenceNameManager sequenceNameManager) {
		this.sequenceNameManager = sequenceNameManager;
	}

	@Override
	public EntityManager getRawManagerObject() {
		return entityManager;
	}

}
