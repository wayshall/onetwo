package stemplate.model;

import java.util.Date;

import javax.persistence.Column;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.onetwo.common.db.IBaseEntity;
import org.onetwo.common.utils.xml.jaxb.DateAdapter;

@SuppressWarnings("serial")
abstract public class BaseEntity implements IBaseEntity{
 
	protected Date createTime;
	
	protected Date lastUpdateTime;

	@Column(name="CREATE_TIME")
	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name="LAST_UPDATE_TIME")
	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

}
	
	