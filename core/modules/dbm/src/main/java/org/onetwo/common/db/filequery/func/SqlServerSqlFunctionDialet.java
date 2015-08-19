package org.onetwo.common.db.filequery.func;

import java.util.List;

import org.onetwo.common.db.filequery.AbstractSqlFunctionDialet;
import org.onetwo.common.utils.Assert;

public class SqlServerSqlFunctionDialet extends AbstractSqlFunctionDialet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1816774293840285087L;

	public SqlServerSqlFunctionDialet(){
		register("to_date", new SQLFunctionGenerator() {
			
			@Override
			public String render(List<String> arguments) {
				Assert.sizeEqual(arguments, 2);
				return "convert(datetime, '"+arguments.get(0)+"')";
			}
		})
		.register("to_char", new SQLFunctionGenerator() {
			
			@Override
			public String render(List<String> arguments) {
				Assert.sizeEqual(arguments, 1);
				return "cast("+arguments.get(0)+" as varchar)";
			}
		});
	}
}
