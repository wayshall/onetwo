package org.onetwo.project.batch.tools.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.onetwo.project.batch.tools.entity.PsamEntity;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

//@Component
public class PsamReader implements ItemReader<PsamEntity>{

	@Resource
	private PsamService psamService;
	private Iterator<PsamEntity> psamIterator;
	private Map<Object, Object> params;
	
	@PostConstruct
	public void loadDatas(){
		List<PsamEntity> psamList = this.psamService.findByParams(params);
		this.psamIterator = psamList.iterator();
	}

	public PsamEntity read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		if(this.psamIterator.hasNext()){
			return this.psamIterator.next();
		}else{
			return null;
		}
	}

	public Map<Object, Object> getParams() {
		return params;
	}

	public void setParams(Map<Object, Object> params) {
		this.params = params;
	}
	
	
}
