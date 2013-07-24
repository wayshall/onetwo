package qy.iccard.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

/*****
 * 可以在这里扩展更多公用的查询方法
 * @author way
 *
 * @param <T>
 * @param <ID>
 */
abstract public class BaseDao<T, ID extends Serializable> {

	@Resource
	private SessionFactory sessionFactory;
	private Class<T> entityClass;
	
	public BaseDao(Class<T> entityClass){
		//这里可以改为泛型获取
		this.entityClass = entityClass;
	}
	
	protected Session getSession(){
		return sessionFactory.getCurrentSession();
	}
	
	public T findById(ID id){
		return (T)getSession().get(entityClass, id);
	}

	public List<T> findAll(){
		String queryString = "select ent from " + entityClass.getName() + " ent";
		return getSession().createQuery(queryString).list();
	}
}
