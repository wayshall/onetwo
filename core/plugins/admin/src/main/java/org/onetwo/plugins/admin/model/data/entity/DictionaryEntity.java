package org.onetwo.plugins.admin.model.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.onetwo.common.hibernate.TimestampBaseEntity;
import org.onetwo.plugins.admin.utils.WebConstant;


/*****
 * 
 * @Entity
 */
@SuppressWarnings("serial")
@Entity
@Table(name="DATA_DICTIONARY")
@TableGenerator(table=WebConstant.SEQ_TABLE_NAME, pkColumnName="GEN_NAME",valueColumnName="GEN_VALUE", pkColumnValue="SEQ_DATA_DICTIONARY", allocationSize=50, initialValue=50000, name="DictionaryEntityGenerator")
public class DictionaryEntity extends TimestampBaseEntity {
	
	/*****
	 * 
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="DictionaryEntityGenerator")
	protected Long id;
  
	/*****
	 * 
	 */
	@NotBlank
	@Size(min=1, max=50)
	protected String code;
  
	/*****
	 * 
	 */
	@NotBlank
	@Size(min=1, max=20)
	protected String name;
  
	/*****
	 * 
	 */
	@Size(max=20)
	protected String value;
  
	/*****
	 * 
	 */
	@Enumerated(EnumType.STRING)
	protected DicType dictType;
  
	/*****
	 * 
	 */
	protected Long typeId;
  
	/*****
	 * 
	 */
	@Column(name="IS_VALID")
	protected Boolean valid;
	@Column(name="IS_ENUM_VALUE")
	protected Boolean enumValue;
  
	/*****
	 * 
	 */
	@Digits(integer=5, fraction=0)
	protected int sort;
  
	/*****
	 * 
	 */
	@Size(max=1000)
	protected String remark;
  
  
	public DictionaryEntity(){
	}
	
	
	/*****
	 * 
	 * @return
	 */
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	/*****
	 * 
	 * @return
	 */
	public String getCode() {
		return this.code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	/*****
	 * 
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	/*****
	 * 
	 * @return
	 */
	public String getValue() {
		return this.value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	
	public DicType getDictType() {
		return dictType;
	}


	public void setDictType(DicType dictType) {
		this.dictType = dictType;
	}


	/*****
	 * 
	 * @return
	 */
	public Long getTypeId() {
		return this.typeId;
	}
	
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}
	
	/*****
	 * 
	 * @return
	 */
	public int getSort() {
		return this.sort;
	}
	
	public void setSort(int sort) {
		this.sort = sort;
	}
	
	/*****
	 * 
	 * @return
	 */
	public String getRemark() {
		return this.remark;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	public Boolean getValid() {
		return valid;
	}




	public Boolean getEnumValue() {
		return enumValue;
	}


	public void setEnumValue(Boolean enumValue) {
		this.enumValue = enumValue;
	}




	public static enum DicType {
		TYPE("字典类型"),
		DATA("字典数据");
		
		private final String name;

		private DicType(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
	
	
}