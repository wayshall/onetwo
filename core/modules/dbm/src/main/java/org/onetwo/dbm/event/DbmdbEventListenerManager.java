	package org.onetwo.dbm.event;

import java.util.Collection;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.RegisterManager;
import org.onetwo.common.utils.map.CollectionMap;

public class DbmdbEventListenerManager implements RegisterManager<DbmEventAction, Collection<DbmEventListener>> /*DbEventListenerManager*/ {

//	private Map<JFishEventAction, ?> registerMap = ArrayListMultimap.create();
	private static final DbmEventListener[] EMPTY_LISTENERS = new DbmEventListener[]{};
	private CollectionMap<DbmEventAction, DbmEventListener> registerMap = CollectionMap.newListMap();
	
	/*protected JFishInsertOrUpdateListener[] insertOrUpdateEventListeners;
	protected InsertEventListener[] insertEventListeners;
	protected InsertEventListener[] batchInsertEventListeners;
	protected UpdateEventListener[] batchUpdateEventListeners;
	protected UpdateEventListener[] updateEventListeners;
	protected JFishDeleteEventListener[] deleteEventListeners;
	protected JFishFindEventListener[] findEventListeners;
	protected JFishExtQueryEventListener[] extQueryEventListeners;*/

//	protected JFishEventListener[] saveRefEventListeners;
//	protected JFishEventListener[] dropRefEventListeners;
	

	@Override
	public CollectionMap<DbmEventAction, DbmEventListener> getRegister() {
		return registerMap;
	}
	
	public DbmdbEventListenerManager registerListeners(DbmEventAction action, DbmEventListener...eventListeners){
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
	