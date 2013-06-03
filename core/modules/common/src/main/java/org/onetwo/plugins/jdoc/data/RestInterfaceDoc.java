package org.onetwo.plugins.jdoc.data;



public class RestInterfaceDoc extends JMethodDoc {
	
	public RestInterfaceDoc(JClassDoc classDoc) {
		super(classDoc);
	}

	/***
	 * 接口地址
	 */
	private String url;
	
	/***
	 * http方法
	 */
	private String httpMethod;
	

	public String getKey(){
		return this.url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		if(httpMethod.indexOf("RequestMethod.GET")!=-1){
			this.httpMethod = "get";
		}else if(httpMethod.indexOf("RequestMethod.POST")!=-1){
			this.httpMethod = "post";
		}else{
			this.httpMethod = httpMethod;
		}
	}

	
}
