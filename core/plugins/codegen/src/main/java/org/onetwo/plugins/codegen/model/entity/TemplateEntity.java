package org.onetwo.plugins.codegen.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.onetwo.plugins.fmtag.JFieldShowable;
import org.onetwo.plugins.fmtag.annotation.JEntryViewMeta;
import org.onetwo.plugins.fmtag.annotation.JFieldViewMeta;

@SuppressWarnings("serial")
@Entity
@Table(name = TemplateEntity.TABLE_NAME)
@SequenceGenerator(name = "TemplateEntityGenerator", sequenceName = "SEQ_codegen_template")
@JEntryViewMeta(label="模板")
public class TemplateEntity extends BaseEntity {

	public static final String TABLE_NAME = "codegen_template";

	private Long id;
	private String name;
	private String packageName;
	private String fileNamePostfix;
	private String filePostfix;
	private String filePath;
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

	@JFieldViewMeta(label="模板名称", order=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="package_name")
	@JFieldViewMeta(label="生成代码所在的包名")
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	@Column(name="file_name_postfix")
	@JFieldViewMeta(label="生成的文件名后缀")
	public String getFileNamePostfix() {
		return fileNamePostfix;
	}

	public void setFileNamePostfix(String fileNamePostfix) {
		this.fileNamePostfix = fileNamePostfix;
	}

	@Column(name="file_postfix")
	@JFieldViewMeta(label="生成的文件后缀")
	public String getFilePostfix() {
		return filePostfix;
	}

	public void setFilePostfix(String filePostfix) {
		this.filePostfix = filePostfix;
	}

	@JFieldViewMeta(label="模板内容", showable={JFieldShowable.create, JFieldShowable.update, JFieldShowable.show})
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@JFieldViewMeta(label="模板路径", showable={JFieldShowable.create, JFieldShowable.update, JFieldShowable.show})
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
}
