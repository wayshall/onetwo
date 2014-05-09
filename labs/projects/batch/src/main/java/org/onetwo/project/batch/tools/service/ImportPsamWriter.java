package org.onetwo.project.batch.tools.service;

import java.util.List;

import javax.annotation.Resource;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.project.batch.tools.entity.PsamEntity;
import org.slf4j.Logger;
import org.springframework.batch.item.ItemWriter;
import org.springframework.transaction.annotation.Transactional;

public class ImportPsamWriter implements ItemWriter<PsamEntity>{
	private static final Logger logger = MyLoggerFactory.getLogger(ImportPsamWriter.class);
	
	@Resource
	private BaseEntityManager baseEntityManager;

	@Transactional
	@Override
	public void write(List<? extends PsamEntity> items) throws Exception {
		LangUtils.println("psam baseEntityManager: ${0}", baseEntityManager);
		LangUtils.println("psam list: ${0}", items);
		for(PsamEntity psam : items){
			baseEntityManager.persist(psam);
			logger.info("import psam: " + psam.getId());
		}
	}
	
	

}
