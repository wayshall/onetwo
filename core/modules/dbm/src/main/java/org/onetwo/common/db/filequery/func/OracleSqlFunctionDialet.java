package org.onetwo.common.db.filequery.func;

import java.util.List;

import org.onetwo.common.db.filequery.AbstractSqlFunctionDialet;
import org.onetwo.common.utils.Assert;

public class OracleSqlFunctionDialet extends AbstractSqlFunctionDialet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4598241408116438659L;

	public OracleSqlFunctionDialet(){
		register("to_date", new SQLFunctionGenerator() {
			
			@Override
			public String render(List<String> arguments) {
				Assert.sizeEqual(arguments, 2);
				//to_date('${date}','yyyy-MM-DD') 
				return "to_date('"+arguments.get(0)+"', '"+arguments.get(1)+"')";
			}
		})
		.register("to_char", new SQLFunctionGenerator() {
			
			@Override
			public String render(List<String> arguments) {
				Assert.sizeEqual(arguments, 1);
				//to_char(rtcs.cardno)
				return "to_char("+arguments.get(0)+")";
			}
		});
	}
}
