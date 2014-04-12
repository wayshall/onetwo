package org.onetwo.common.spring.sql;

public interface FileSqlParser<T extends JFishNamedFileQueryInfo> {
	public void initialize();
	public String parse(String name, Object context);

}