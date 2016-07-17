package org.onetwo.plugins.admin.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@SuppressWarnings("serial")
@Entity
@Table(name="data_dictionary")
@Data
public class DataDictionary implements Serializable {
	@Id
    private String code;
    private String name;
    private String value;
    private String parentCode;
    
    @Column(name="IS_VALID")
    private Boolean valid;
    @Column(name="is_Enum_Value")
    private Boolean enumValue;
    private Integer sort;
    private String remark;
    private Date createAt;
    private Date updateAt;
}