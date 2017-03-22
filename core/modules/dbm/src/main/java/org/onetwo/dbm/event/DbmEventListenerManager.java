	package org.onetwo.dbm.event;

import java.util.Collection;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.RegisterManager;
import org.onetwo.common.utils.map.CollectionMap;

public class DbmEventListenerManager implements RegisterManager<DbmEventAction, Collection<DbmEventListener>> /*DbEventListenerManager*/ {

//	private Map<JFishEventAction, ?> registerMap = ArrayListMultimap.create();
	private static final DbmEventListener[] EMPTY_LISTENERS = new DbmEventListener[]{};
	private CollectionMap<DbmEventAction, DbmEventListener> registerMap = CollectionMap.newListMap();
	

	@Override
	public CollectionMap<DbmEventAction, DbmEventListener> getRegister() {
		return registerMap;
	}
	
	/****
	 * 覆盖已存在的listerner
	 */
	public DbmEventListenerManager register(DbmEventAction action, Collection<DbmEventListener> eventListeners){
		getRegister().put(action, eventListeners);
		return this;
	}
	
	/****
	 * 不覆盖已存在的listerner
	 * @param action
	 * @param eventListeners
	 * @return
	 */
	public DbmEventListenerManager registerListeners(DbmEventAction action, DbmEventListener...eventListeners){
		registerMap.putElements(action, eventListeners);
		return this;
	}
	
	public DbmEventListener[] getListeners(DbmEventAction action){
		Collection<DbmEventListener> listenerList = getRegistered(action);
		if(LangUtils.isEmpty(listenerList)){
			return EMPTY_LISTENERS;
		}
		return listenerList.toArray(new DbmEventListener[listenerList.size()]);
	}
	
}
	