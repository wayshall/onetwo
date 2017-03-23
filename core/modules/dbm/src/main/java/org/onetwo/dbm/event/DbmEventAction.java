package org.onetwo.dbm.event;

/****
 * 同一个操作可能触发session和jdbc事件
 * @author way
 *
 */
public enum DbmEventAction {
	//for dbm session event action
	insertOrUpdate,
	insert,
	update,
	delete,
	find,
	
	extQuery,
	
	batchInsert,
	batchUpdate
//	saveRef,
//	dropRef
	
	//for jdbc operation event
	/*jdbcAfterUpdate,
	jdbcAfterQuery*/
}
