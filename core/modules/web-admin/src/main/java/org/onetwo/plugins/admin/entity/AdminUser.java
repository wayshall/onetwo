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
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.admin.utils.DataUtils;
import org.onetwo.plugins.admin.utils.Enums.UserStatus;
import org.onetwo.plugins.admin.utils.WebConstant.DictKeys;
import org.onetwo.plugins.admin.utils.WebConstant.ValidGroup.ValidWhenNew;

@Entity
@Table(name="admin_user")
@Data
@EqualsAndHashCode
public class AdminUser {
	
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

    private Date createAt;

    private Date updateAt;

    private String appCode;
    
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