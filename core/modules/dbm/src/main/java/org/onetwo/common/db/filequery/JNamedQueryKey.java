package org.onetwo.common.db.filequery;

public enum JNamedQueryKey {

	ParserContext,
	ResultClass,
	ASC,
	DESC;
//	countClass;
	
	public static JNamedQueryKey ofKey(Object qkey){
		if(JNamedQueryKey.class.isInstance(qkey))
			return (JNamedQueryKey)qkey;
		String key = qkey.toString();
		if(!key.startsWith(":"))
			return null;
		String keyStr = key.substring(1);
		JNamedQueryKey queryKey = null;
		try {
			queryKey = JNamedQueryKey.valueOf(keyStr);
		} catch (Exception e) {
			System.out.println("no JNamedQueryKey: " + key);
		}
		return queryKey;
	}
}
