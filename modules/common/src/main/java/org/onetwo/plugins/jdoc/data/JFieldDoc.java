package org.onetwo.plugins.jdoc.data;

import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;

public class JFieldDoc {

	private JClassDoc classDoc;
	private String name;
	private boolean required;
	private String typeName;
	private String description;
	private Class<?> typeClass;

	public JFieldDoc(JClassDoc classDoc) {
		super();
		this.classDoc = classDoc;
	}

	public JClassDoc getClassDoc() {
		return classDoc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		if (StringUtils.isNotBlank(typeName)) {
			this.typeClass = ReflectUtils.loadClass(typeName, false);
		}
		this.typeName = typeName;
	}

	public Class<?> getTypeClass() {
		return typeClass;
	}

	public void setTypeClass(Class<?> typeClass) {
		this.typeClass = typeClass;
	}

	public boolean isTypeLink() {
		return getTypeClass() != null;
	}

}
