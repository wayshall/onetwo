package org.onetwo.plugins.admin.entity;

import java.util.Date;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.hibernate.validator.constraints.NotBlank;
import org.onetwo.boot.utils.ImageUrlJsonSerializer;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.dbm.jpa.BaseEntity;
import org.onetwo.plugins.admin.utils.DataUtils;
import org.onetwo.plugins.admin.utils.Enums.UserStatus;
import org.onetwo.plugins.admin.utils.WebConstant.DictKeys;
import org.onetwo.plugins.admin.utils.WebConstant.ValidGroup.ValidWhenNew;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name="admin_user")
@Data
@EqualsAndHashCode(callSuper=true)
@SuppressWarnings("serial")
public class AdminUser extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
	
	@NotBlank(groups=ValidWhenNew.class)
    private String userName;

    private String nickName;

	@NotBlank(groups={ValidWhenNew.class})
    private String password;

    private String email;

    private String mobile;

    private String gender;

    private String status;

    private Date birthday;

    private String appCode;

	@JsonSerialize(using = ImageUrlJsonSerializer.class)
    private String avatar;
    
    public String getGenderName(){
    	if(StringUtils.isBlank(gender))
    		return "";
    	Optional<DataDictionary> data = DataUtils.getDictionaryCachingService().findByValue(DictKeys.SEX, gender);
    	return data.isPresent()?data.get().getName():"";
    }
    
    public String getStatusName(){
    	if(StringUtils.isBlank(status))
    		return "";
    	return UserStatus.of(status).getLabel();
    }

}