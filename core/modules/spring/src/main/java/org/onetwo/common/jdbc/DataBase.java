package org.onetwo.common.jdbc;


public enum DataBase {
	MySQL("mysql"),
	Oracle("oracle"),
	Sqlserver("sqlserver"),
	H2("h2"),
	Access("access");
//	Unknow("unknow");
	
	private String name;
	
	DataBase(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public boolean isOracle(){
		return this.equals(Oracle);
	}
	
	public boolean isMySQL(){
		return this.equals(MySQL);
	}
	
	public static DataBase of(String name){
		name = name.toLowerCase();
		for(DataBase db : values()){
			if(name.indexOf(db.name)!=-1){
				return db;
			}
		}
		throw new IllegalArgumentException("Unknown Database");
	}
	
}
