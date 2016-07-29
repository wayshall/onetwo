	package org.onetwo.common.jfishdbm.event;

import java.util.Collection;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.RegisterManager;
import org.onetwo.common.utils.map.CollectionMap;

public class JFishdbEventListenerManager implements RegisterManager<JFishEventAction, Collection<JFishEventListener>> /*DbEventListenerManager*/ {

//	private Map<JFishEventAction, ?> registerMap = ArrayListMultimap.create();
	private static final JFishEventListener[] EMPTY_LISTENERS = new JFishEventListener[]{};
	private CollectionMap<JFishEventAction, JFishEventListener> registerMap = CollectionMap.newListMap();
	
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
	public CollectionMap<JFishEventAction, JFishEventListener> getRegister() {
		return registerMap;
	}
	
	public JFishdbEventListenerManager registerListeners(JFishEventAction action, JFishEventListener...eventListeners){
		registerMap.putElements(action, eventListeners);
		return this;
	}
	
	public JFishEventListener[] getListeners(JFishEventAction action){
		Collection<JFishEventListener> listenerList = getRegistered(action);
		if(LangUtils.isEmpty(listenerList)){
			return EMPTY_LISTENERS;
		}
		return listenerList.toArray(new JFishEventListener[listenerList.size()]);
	}
	
}
	