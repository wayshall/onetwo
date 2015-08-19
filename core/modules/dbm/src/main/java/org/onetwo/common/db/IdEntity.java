package org.onetwo.common.db;

import java.io.Serializable;

public interface IdEntity<PK extends Serializable> extends Serializable{
 
	abstract public PK getId();
}
