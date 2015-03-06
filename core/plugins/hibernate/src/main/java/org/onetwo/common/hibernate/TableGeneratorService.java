package org.onetwo.common.hibernate;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface TableGeneratorService {

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public long generatedValue(String seqName);
	public long generatedValue(String seqName, int cacheSize);

}