package org.onetwo.plugins.admin.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.onetwo.common.utils.StringUtils;
import org.onetwo.dbm.jpa.BaseEntity;
import org.onetwo.plugins.admin.utils.Enums.CommonStatus;

@SuppressWarnings("serial")
@Entity
@Table(name="admin_role")
@Data
@EqualsAndHashCode(callSuper=true)
public class AdminRole extends BaseEntity{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String status;

    private String remark;


    private String appCode;
    

    public String getStatusName(){
    	if(StringUtils.isBlank(status))
    		return "";
    	return CommonStatus.valueOf(status).getLabel();
    }
}