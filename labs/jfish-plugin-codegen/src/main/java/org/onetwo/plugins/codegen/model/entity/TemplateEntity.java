package org.onetwo.plugins.codegen.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.onetwo.common.fish.jpa.BaseEntity;
import org.onetwo.plugins.fmtag.JFieldShowable;
import org.onetwo.plugins.fmtag.annotation.JEntryViewMeta;
import org.onetwo.plugins.fmtagext.annotation.JFieldView;

@SuppressWarnings("serial")
@Entity
@Table(name = "codegen_template")
@SequenceGenerator(name = "TemplateEntityGenerator", sequenceName = "SEQ_codegen_template")
@JEntryViewMeta(label="模板")
public class TemplateEntity extends BaseEntity {

	private Long id;
	private String name;
	private String packageName;
	private String fileNamePostfix;
	private String filePostfix;
	private String content;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TemplateEntityGenerator")
	@Column(name = "id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@JFieldView(label="模板名称", order=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="package_name")
	@JFieldView(label="生成代码所在的包名")
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	@Column(name="file_name_postfix")
	@JFieldView(label="生成的文件名后缀")
	public String getFileNamePostfix() {
		return fileNamePostfix;
	}

	public void setFileNamePostfix(String fileNamePostfix) {
		this.fileNamePostfix = fileNamePostfix;
	}

	@Column(name="file_postfix")
	@JFieldView(label="生成的文件后缀")
	public String getFilePostfix() {
		return filePostfix;
	}

	public void setFilePostfix(String filePostfix) {
		this.filePostfix = filePostfix;
	}

	@JFieldView(label="模板内容", showable={JFieldShowable.create, JFieldShowable.update, JFieldShowable.show})
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
