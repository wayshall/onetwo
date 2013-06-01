package org.onetwo.common.web.s2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

/**
 * struts2文件上传的辅助类
 * 
 * @author weishao.zeng
 *
 * @param <T>
 * Apr 10, 2009
 */
public class Attacher {

	// attachment field name must set to uplaod
	protected List<File> attachs = new ArrayList<File>(5);

	protected List<String> attachFileNames = new ArrayList<String>(5);

	protected List<String> attachContentTypes = new ArrayList<String>(5);

	public static interface BeanSettor{
		public void setBean(File file, String fileName, String conetntType);
	}
	
	/**
	 * @param beanSettor
	 * @throws Exception
	 */
	public void attachement(BeanSettor beanSettor){
		if(attachs==null || attachs.isEmpty())
			return ;
		for(int i=0; i<attachs.size() ; i++){
			beanSettor.setBean(attachs.get(i), attachFileNames.get(i), attachContentTypes.get(i));
		}
	}
	
	public int size(){
		return attachs.size();
	}
	
	public boolean isEmpty(){
		return attachs.isEmpty();
	}

	public List<File> getAttach() {
		return attachs;
	}

	public Attacher addFile(File file, String fileName, String conetntType) {
		Assert.notNull(file);
		this.attachs.add(file);
		
		if(StringUtils.isBlank(fileName))
			fileName = file.getName();
		this.attachFileNames.add(fileName);
		
		if(StringUtils.isBlank(conetntType))
			conetntType = "";
		this.attachContentTypes.add(conetntType);
				
		return this;
	}

	public void setAttach(List<File> attach) {
		this.attachs = attach;
	}

	public List<String> getAttachFileName() {
		return attachFileNames;
	}

	public void setAttachFileName(List<String> attachFileName) {
		this.attachFileNames = attachFileName;
	}

	public List<String> getAttachContentType() {
		return attachContentTypes;
	}

	public void setAttachContentType(List<String> attachContentType) {
		this.attachContentTypes = attachContentType;
	}

}
