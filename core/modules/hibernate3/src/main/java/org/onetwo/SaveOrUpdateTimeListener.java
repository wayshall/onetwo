package org.onetwo;

import java.util.Date;

import org.hibernate.event.SaveOrUpdateEvent;
import org.hibernate.event.SaveOrUpdateEventListener;
import org.onetwo.common.db.IBaseEntity;

/****
 * 扩展hibernate的，自动更新实体的创建时间和最后更新时间
 * 
 * @author weishao
 *
 */
@SuppressWarnings({"unchecked", "serial"})
public class SaveOrUpdateTimeListener implements SaveOrUpdateEventListener{
	
	public void onSaveOrUpdate(SaveOrUpdateEvent event) {
		final Object object = event.getObject();
		if (object instanceof IBaseEntity) {
			IBaseEntity entity = (IBaseEntity) object;
			if (entity.getCreateTime() == null)
				entity.setCreateTime(new Date());
			entity.setLastUpdateTime(new Date());
		}
	}
}
