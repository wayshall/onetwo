package org.onetwo.plugins.admin.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name="admin_role")
@Data
@EqualsAndHashCode
public class AdminRole {
    private Long id;

    private String name;

    private String status;

    private String remark;

    private Date createAt;

    private Date updateAt;

    private String appCode;
}