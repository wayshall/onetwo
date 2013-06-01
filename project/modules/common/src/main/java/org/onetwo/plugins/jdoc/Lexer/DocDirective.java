package org.onetwo.plugins.jdoc.Lexer;

public enum DocDirective {

	PARAM("@param"),
	RETURN("@return"),
	THROWS("@throws");

	private final String name;
	
	DocDirective(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public static final DocDirective getByName(String name){
		for(DocDirective doc : DocDirective.values()){
			if(doc.name.equals(name)){
				return doc;
			}
		}
		return DocDirective.valueOf(name);
	}
	
}
