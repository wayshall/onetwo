package org.onetwo.plugins.dq;


public class DynamicQueryHandlerTest {

	/*private HibernateNamedSqlFileManager fileManager;
	ParserContext parserContext;
	private HibernateFileQueryManagerImpl mananger;
	
	private DqEntityManager be;
	DataQueryHolderForTest dqholder = new DataQueryHolderForTest();
	private JdbcDao jdao;
	private DqNamedJdbcTemplate template = new DqNamedJdbcTemplate();
	
	@Before
	public void setup(){
		StringTemplateLoaderFileSqlParser<HibernateNamedInfo> p = new StringTemplateLoaderFileSqlParser<HibernateNamedInfo>();
		this.fileManager = new HibernateNamedSqlFileManager(DataBase.Oracle, false, HibernateNamedInfo.class, p);
		fileManager.build();
		
		be = new DqEntityManager();
		be.setDqholder(dqholder);
		mananger = new HibernateFileQueryManagerImpl(DataBase.Oracle, false, null);
		mananger.initQeuryFactory(be);
		be.setFileNamedQueryFactory(mananger);
		
		template.setDqholder(dqholder);
		
		jdao = new JdbcDao(){
			public NamedJdbcTemplate getNamedParameterJdbcTemplate() {
				  return template;
			}
		};
	}
	

	@Test
	public void testDqh(){
		DynamicQueryHandler dqh = new DynamicQueryHandler(be, null, jdao, TestQueryInterface.class);
		TestQueryInterface i = (TestQueryInterface)dqh.getQueryObject();
		Page<TestBean> page = new Page<TestBean>();
		i.findPage(page, "page");
		System.out.println("sql: " + dqholder.getSql());
		Assert.assertEquals("select count(*) from admin_user", dqholder.getSql());
		
		List<TestBean> datas = LangUtils.newArrayList();
		TestBean data = new TestBean();
		data.setUserName("userName1");
		data.setAge(1);
		datas.add(data);
		
		data = new TestBean();
		data.setUserName("userName3");
		data.setAge(2);
		datas.add(data);

		i.batchInsert(datas);
		Assert.assertEquals("insert admin_user(user_name, age) values (:userName, :age);", dqholder.getSql());
		Assert.assertEquals("[{age=1, userName=userName1}, {age=2, userName=userName3}]", LangUtils.toString(dqholder.getBatchValues()));
		
		i.batchInsertWithNamed(datas);
		Assert.assertEquals("insert admin_user(user_name, age) values (:userName, :age);", dqholder.getSql());
		Assert.assertEquals("[{data.age=1, data.userName=userName1}, {data.age=2, data.userName=userName3}]", LangUtils.toString(dqholder.getBatchValues()));
	}*/
	
}
