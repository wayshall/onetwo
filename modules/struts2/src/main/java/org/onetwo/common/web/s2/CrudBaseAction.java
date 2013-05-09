package org.onetwo.common.web.s2;

import java.io.Serializable;

import org.onetwo.common.db.IdEntity;
import org.onetwo.common.utils.Page;

/****
 * 增删改查的action基类
 * @author weishao
 *
 * @param <T>
 */
@SuppressWarnings( { "serial", "unchecked" })
public abstract class CrudBaseAction<T extends IdEntity, PK extends Serializable> extends BaseAction {

	//protected ComIdsVO comId;
	
	protected PK id;

	protected boolean create;

	protected T model;
	
	protected Page page = new Page();

	public CrudBaseAction(){
	}
	
	/***
	 * 子类覆盖此方法
	 */
	protected void initModel(){
	}	
	/**
	 * 删除
	 * 
	 * @author weishao.zeng
	 * @return
	 */
	public String delete()throws Exception {
		throw new UnsupportedOperationException("请覆盖delete操作！class :" + this.getClass().getName());
	}

	public String edit()throws Exception {
		throw new UnsupportedOperationException("请覆盖edit操作！class :" + this.getClass().getName());
	}
	
	/**
	 * 保存
	 * 
	 * @author weishao.zeng
	 * @return
	 */
	public String save()throws Exception {
		throw new UnsupportedOperationException("请覆盖save操作！class :" + this.getClass().getName());
	}
	/**
	 * 查看
	 * 
	 * @author weishao.zeng
	 * @return
	 */

	public String view()throws Exception {
		throw new UnsupportedOperationException("请覆盖view操作！class :" + this.getClass().getName());
	}
	
	public String list(){
		throw new UnsupportedOperationException("请覆盖list操作！class :" + this.getClass().getName());
	}
	
	/*public CrudService getService(){
		throw new UnsupportedOperationException("请覆盖getService操作！class :" + this.getClass().getName());
	}*/


	public void prepareView() {
		prepareModel();
	}
	public void prepareEdit() {
		prepareModel();
	}

	public void prepareModel() {
		T entity = prepareEntity(id, getModel());
		if (entity != null)
			this.model = entity;
		else {
			create = true;
		}
	}


	/**
	 * 查找实体
	 * 
	 * @author weishao.zeng
	 * @param id
	 * @param model
	 * @return
	 */
	protected T prepareEntity(PK id, T model) {
		PK idValue = null;
		if(id==null || (id instanceof Number && ((Number)id).longValue()<1)){
			if(model!=null)
				idValue = (PK)model.getId();
			else
				idValue = null;
		}else{
			idValue = id;
		}
		
		/*if(idValue==null || (idValue instanceof ComIdsVO && ((ComIdsVO)idValue).getId()==null))
			idValue = this.comId;*/
		
		if(idValue==null)
			return null;
		
		T entity = fetchEntity((PK)idValue);
		return entity;
	}
	
	/****
	 * 这个方法是让子类从数据库中根据ID查找entity的
	 * 子类覆盖这个方法
	 * @param id
	 * @return
	 */
	abstract protected T fetchEntity(PK id);

	public T getModel() {
		if(model==null)
			initModel();
		return model;
	}

	public boolean isCreate() {
		return create;
	}

	public PK getId() {
		return id;
	}

	public void setId(PK id) {
		this.id = id;
	}

	public Page getPage() {
		return page;
	}
	
	public String getDeleteAction(){
		return getActionName()+"!"+DELETE+".do";
	}
	
	public String getEditAction(){
		return getActionName()+"!"+EDIT+".do";
	}
}
