package org.onetwo.plugins.admin.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name="admin_user")
@Data
@EqualsAndHashCode
public class AdminUser {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String userName;

    private String nickName;

    private String password;

    private String email;

    private String mobile;

    private String gender;

    private String status;

    private Date birthday;

    private Date createAt;

    private Date updateAt;

    private String appCode;

}