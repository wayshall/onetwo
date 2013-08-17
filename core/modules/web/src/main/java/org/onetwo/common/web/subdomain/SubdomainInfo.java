package org.onetwo.common.web.subdomain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/****
 * 处理二级域名所需要用到的数据封装
 * @author weishao
 *
 */
public class SubdomainInfo{

	private Long organId;
	private String language;
	private String submain;
	private Long id;
	
	private boolean defaultSubdomain;

	public SubdomainInfo() {
	}

	public Long getOrganId() {
		return organId;
	}

	public void setOrganId(Long organId) {
		this.organId = organId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getSubmain() {
		return submain;
	}

	public void setSubmain(String submain) {
		this.submain = submain;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isDefaultSubdomain() {
		return defaultSubdomain;
	}

	public void setDefaultSubdomain(boolean defaultSubdomain) {
		this.defaultSubdomain = defaultSubdomain;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof SubdomainInfo))
			return false;
		SubdomainInfo o = (SubdomainInfo) obj;
		return new EqualsBuilder().append(id, o.getId()).append(language, o.getLanguage()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(id).append(language).toHashCode();
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("[id: ").append(this.id).append(", ");
		sb.append("organId: ").append(this.organId).append(", ");
		sb.append("language: ").append(this.language).append(", ");
		sb.append("submain: ").append(this.submain).append(", ");
		sb.append("defaultSubdomain: ").append(this.defaultSubdomain).append(" ]");
		return sb.toString();
	}

}
