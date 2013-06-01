package org.onetwo.common.db.parser;

import org.onetwo.common.lexer.AbstractParser;
import org.onetwo.common.lexer.StringSourceReader;
import org.onetwo.common.utils.ArrayUtils;

public class JFishSqlParser extends AbstractParser<SqlTokenKey> implements SqlParser {

	private SqlTokenKey[] keywords;
	private SqlTokenKey[] operators;
	private boolean debug;
	
	public JFishSqlParser(String sql) {
		super(new SqlLexer(new StringSourceReader(sql)));
		keywords = getSqlLexer().getKeyWords().getKeyWordTokenAsArray();
		keywords = (SqlTokenKey[])ArrayUtils.add(keywords, SqlTokenKey.EOF);
		
		operators = getSqlLexer().getOperators().getKeyWordTokenAsArray();
	}
	
	public SqlLexer getSqlLexer(){
		return (SqlLexer)lexer;
	}
	
	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
		this.lexer.setDebug(true);
	}

	protected void addJTokenValuesToStatments(SqlStatment statment, JTokenValueCollection<SqlTokenKey> jtokens){
		if(!jtokens.isEmpty()){
			statment.addSqlObject(new SqlJTokenValuesObjectImpl(jtokens.clone()));
			jtokens.clear();
		}
	}
	
	public SqlStatment parse(){
		SqlStatment statment = new SqlStatment();
		lexer.nextToken();
		
//		boolean createCollection = true;
		SqlObject sqlObj = null;
//		int lastKeywordPos = -1;
//		int sqlObjectCount = 0;
		
		JTokenValueCollection<SqlTokenKey> jtokens = new JTokenValueCollection<SqlTokenKey>();
		SqlTokenKey token = null;
		boolean afterWhere = false;
		while(true){
			token = lexer.getToken();
			if(tokenIs(SqlTokenKey.EOF) || tokenIs(SqlTokenKey.SEMI)){
				this.addJTokenValuesToStatments(statment, jtokens);
				break;
			}

			/*if(tokenIs(SqlTokenKey.WHERE)){
				System.out.println("test");
			}*/
			if(ArrayUtils.contains(operators, token)){
				JTokenValue<SqlTokenKey> op = new JTokenValue<SqlTokenKey>(token, stringValue());
				this.throwIfNoNextToken();
				if(tokenIs(SqlTokenKey.LPARENT)){//sub
					/*jtokens.addJTokenValue(op);
					jtokens.addJTokenValue(lexer.getToken(), stringValue());
					this.throwIfNoNextToken();*/

					JTokenValue<SqlTokenKey> lparent = new JTokenValue<SqlTokenKey>(lexer.getToken(), stringValue());
					
					this.throwIfNoNextToken();
					if(tokenIs(SqlTokenKey.SELECT)){
						this.addJTokenValuesToStatments(statment, jtokens);
						sqlObj = new SqlKeywordObject(op);
						statment.addSqlObject(sqlObj);
						jtokens.addJTokenValue(lparent);
					}else{
						JTokenValueCollection<SqlTokenKey> rightIds = nextAllTokensUntil(keywords);
						rightIds.addJTokenValue(0, lparent);
						sqlObj = createSqlInfixCondition(jtokens, token, rightIds);
						statment.addSqlObject(sqlObj);
						
					}
				}else{
					JTokenValueCollection<SqlTokenKey> rightIds = nextAllTokensUntil(keywords);
					
					//(m.login_code=:loginCode or m.mobile=:loginCode or m.email=:email)
					if(jtokens!=null && !jtokens.isEmpty() && SqlTokenKey.LPARENT==jtokens.getFirst().getToken() && !jtokens.contains(SqlTokenKey.RPARENT)){
						statment.addSqlObject(new SqlSymbolObject(jtokens.remove(0)));
						
						sqlObj = createSqlInfixCondition(jtokens, token, rightIds);
						statment.addSqlObject(sqlObj);
						
					}else if(rightIds!=null && !rightIds.isEmpty() && SqlTokenKey.RPARENT==rightIds.getLast().getToken() && !rightIds.contains(SqlTokenKey.LPARENT)){
						sqlObj = createSqlInfixCondition(jtokens, token, rightIds);
						statment.addSqlObject(sqlObj);
						
						statment.addSqlObject(new SqlSymbolObject(rightIds.removeLast()));
					}else{
						sqlObj = createSqlInfixCondition(jtokens, token, rightIds);
						statment.addSqlObject(sqlObj);
					}
				}
				continue;
			}else if(ArrayUtils.contains(keywords, token)){
				this.addJTokenValuesToStatments(statment, jtokens);
				
				sqlObj = new SqlKeywordObject(token, stringValue());
				if(tokenIs(SqlTokenKey.WHERE)){
					afterWhere = true;
				}
				
			}else if(!afterWhere && tokenIsSqlVarSymbol()){
				/*JTokenValue<SqlTokenKey> op = new JTokenValue<SqlTokenKey>(token, stringValue());
				throwIfNoNextToken();
				if(tokenIsOneOf(SqlTokenKey.COMMA, SqlTokenKey.RPARENT)){
					
				}*/
				this.addJTokenValuesToStatments(statment, jtokens);
				String var = stringValue();
				sqlObj = new SqlVarObjectImpl(var);
				
			}else{
				sqlObj = null;
			}
			
//			sqlObjectCount++;
			if(sqlObj!=null){
				statment.addSqlObject(sqlObj);
			}else{
				jtokens.addJTokenValue(token, stringValue());
			}
//			System.out.println("statment:"+statment.toSql());
			this.lexer.nextToken();
			
		}
//		System.out.println("statment:"+statment.toSql());
		return statment;
	}
	
	private SqlObject createSqlInfixCondition(JTokenValueCollection<SqlTokenKey> jtokens, SqlTokenKey token, JTokenValueCollection<SqlTokenKey> rightIds){
		SqlObject sqlObj = null;
		
		if(jtokens.contains(SqlTokenKey.VARNAME) || jtokens.contains(SqlTokenKey.QUESTION)){
			SqlInfixVarConditionExpr cond = new SqlInfixVarConditionExpr(jtokens.clone(), token, rightIds, false);
			sqlObj = cond;
		}else if(rightIds.contains(SqlTokenKey.VARNAME) || rightIds.contains(SqlTokenKey.QUESTION)){
			sqlObj = new SqlInfixVarConditionExpr(jtokens.clone(), token, rightIds, true);
		}else{
			sqlObj = new SqlInfixConditionExpr(jtokens.clone(), token, rightIds);
		}
		jtokens.clear();
		
		/*statment.addSqlObject(sqlObj);
		sqlObj = new SqlObjectImpl(stringValue());*/
		
		return sqlObj;
	}
	
	private boolean tokenIsSqlVarSymbol(){
		return tokenIs(SqlTokenKey.VARNAME) || tokenIs(SqlTokenKey.QUESTION);
	}
	
	/*
	protected void buildWhere(WhereSqlObject where){
		SqlInfixConditionExpr cond = new SqlInfixConditionExpr();
		while(lexer.nextToken()){
			JTokenValueCollection<SqlTokenKey>  leftIds = nextAllTokensUntil(getSqlLexer().getOperators().getKeyWordTokenAsArray());
			cond.setLeft(leftIds);
			cond.setOperator(lexer.getToken());
			this.throwIfNoNextToken();
			JTokenValueCollection<SqlTokenKey>  rightIds = nextAllTokensUntil(getSqlLexer().getOperators().getKeyWordTokenAsArray());
			cond.setRight(rightIds);
			
			where.addCondition(cond);
			
			if(tokenIs(SqlTokenKey.AND)){
				cond = new SqlWordCollection();
			}else if(tokenIs(SqlTokenKey.OR)){
				cond = new SqlOrExpr();
			}else{
				break;
			}
		}
	}*/
	

}
