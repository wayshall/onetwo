package org.onetwo.plugins.admin.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.hibernate.validator.constraints.NotBlank;
import org.onetwo.plugins.admin.utils.WebConstant.ValidGroup.ValidWhenEdit;
import org.onetwo.plugins.admin.utils.WebConstant.ValidGroup.ValidWhenNew;


/*****
 * 用户表
 * @Entity
 */
@SuppressWarnings("serial")
@Entity
@Table(name="admin_application")
@Data
@EqualsAndHashCode
public class AdminApplication implements Serializable {

	@Id
	@NotBlank
	@Size(min=1, max=20, groups={Default.class, ValidWhenNew.class, ValidWhenEdit.class})
	private String code;
	@NotBlank
	@Size(min=1, max=100, groups={Default.class, ValidWhenNew.class, ValidWhenEdit.class})
	private String name;

    private String baseUrl;

    private Date createAt;

    private Date updateAt;
	
}