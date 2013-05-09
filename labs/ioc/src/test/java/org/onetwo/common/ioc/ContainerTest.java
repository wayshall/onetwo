package org.onetwo.common.ioc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.appbase.BaseModule;
import org.onetwo.common.cache.CacheModule;
import org.onetwo.common.db.sqlext.SQLDialet;
import org.onetwo.common.db.sqlext.SQLSymbolManager;
import org.onetwo.common.eav.core.EavModule;
import org.onetwo.common.ejb.jpa.JpaSQLSymbolManager;
import org.onetwo.common.ejb.jpa.openjpa.OpenJpaClassMetaManagerImpl;
import org.onetwo.common.ioc.AbstractBFModule;
import org.onetwo.common.ioc.BFModule;
import org.onetwo.common.ioc.Container;
import org.onetwo.common.ioc.ObjectBinder;
import org.onetwo.common.ioc.SimpleContainerFactory;
import org.onetwo.common.ioc.Valuer;
import org.onetwo.common.ioc.test.AuthTestService;
import org.onetwo.common.ioc.test.CacheService;
import org.onetwo.common.ioc.test.CacheServiceImpl;
import org.onetwo.common.ioc.test.ContainerAwareServiceImpl;
import org.onetwo.common.ioc.test.FlushCacheService;
import org.onetwo.common.ioc.test.FlushCacheServiceImpl;
import org.onetwo.common.ioc.test.MapTestServiceImpl;
import org.onetwo.common.ioc.test.RoleTestSerivce;
import org.onetwo.common.ioc.test.RoleTestSerivceImpl;
import org.onetwo.common.ioc.test.TestInterceptor;
import org.onetwo.common.ioc.test.TestOrderInterceptor;
import org.onetwo.common.ioc.test.TimeCounterService;
import org.onetwo.common.ioc.test.TimeCounterServiceImpl;
import org.onetwo.common.ioc.test.UserTestService;
import org.onetwo.common.ioc.test.UserTestServiceImpl;
import org.onetwo.common.utils.AppConstant;

@SuppressWarnings("unchecked")
public class ContainerTest {
	
	private Container container;
	
	protected UserTestService userTestService1 = new UserTestServiceImpl();
	protected UserTestService userTestService2 = new UserTestServiceImpl();

	String map_key1 = "map_key1";
	String map_value1 = "map_value1";
	String map_key2 = "map_key2";
	String map_value2 = "map_value2";
	
	
	String map_name = "map";
	String map_test_service_name = "mapTestService";
	
	@Before
	public void init(){
		container = SimpleContainerFactory.create(OpenJpaClassMetaManagerImpl.class);
		BFModule module = new AbstractBFModule(){

			@Override
			public void build(ObjectBinder binder) {
				binder.bind(UserTestServiceImpl.class);
				binder.bind(RoleTestSerivceImpl.class);
				binder.bind(CacheServiceImpl.class);
				binder.bind(FlushCacheServiceImpl.class);
				binder.bind(AuthTestService.class);
				binder.bind(TimeCounterServiceImpl.class);
				binder.bind(ContainerAwareServiceImpl.class);
				binder.bind(MapTestServiceImpl.class);
				binder.bind("testClass", Valuer.val(UserTestServiceImpl.class));

				binder.map(map_name).map(map_key1, map_value1).map(map_key2, map_value2).bindit();
				
				binder.map(map_test_service_name).map("field1", UserTestServiceImpl.class).map("field2", UserTestServiceImpl.class).bindit();
				
				/*Map<String, UserTestService> map = new LinkedHashMap<String, UserTestService>();
				map.put("field1", userTestService1);
				map.put("field2", userTestService2);
				
				binder.bindAsMap(map_test_service_name, map);*/
			}
			
		};
		List<BFModule> list = new ArrayList<BFModule>();
		BaseModule baseModel = new BaseModule();
		list.add(baseModel);
		
		BFModule eavModule = new EavModule();
		list.add(eavModule);
		
		BFModule cache = new CacheModule();
		list.add(cache);
		
		list.add(module);
		container.build(list.toArray(new BFModule[list.size()]));
	}
	

	
	@Test
	public void testBaseModel(){
		SQLSymbolManager symbol = container.getObject(JpaSQLSymbolManager.class);
		SQLDialet dialet = container.getObject(AppConstant.DIALET_JPA);
		Assert.assertEquals(symbol.getSqlDialet(), dialet);

		SQLSymbolManager defaultSymbol = (SQLSymbolManager)container.getObject(AppConstant.SYMBOL_JDBC);
		SQLDialet jdbcDialet = container.getObject(AppConstant.DIALET_JDBC);
		Assert.assertEquals(defaultSymbol.getSqlDialet(), jdbcDialet);
	}
	
	@Test
	public void testInject(){
		RoleTestSerivce u1 = container.getObject(RoleTestSerivceImpl.class);
		RoleTestSerivce u2 = container.getObject(RoleTestSerivceImpl.class);
		Assert.assertEquals(u1, u2);
		Assert.assertEquals(u1.getClassSimpleName(), TestInterceptor.class.getSimpleName());
	}
	
	@Test
	public void testInterceptors(){
		UserTestService u1 = container.getObject(UserTestServiceImpl.class);
		Assert.assertTrue(TestInterceptor.class.getSimpleName().equals(u1.outString()));
		Assert.assertFalse(TestOrderInterceptor.class.getSimpleName().equals(u1.outString()));
	}
	
	@Test
	public void testMap(){
		Map map = container.getObject(map_name);
		Assert.assertTrue(map_value1.equals(map.get(map_key1)));
		Assert.assertTrue(map_value2.equals(map.get(map_key2)));
		
		MapTestServiceImpl mapService = container.getObject(MapTestServiceImpl.class);
		Assert.assertTrue(map_value1.equals(mapService.getMap().get(map_key1)));
		Assert.assertTrue(map_value2.equals(mapService.getMap().get(map_key2)));
		Assert.assertTrue(UserTestServiceImpl.class.equals(mapService.getMapClass()));
		
		Map map2 = container.getObject(map_test_service_name);
		Assert.assertTrue(map2.equals(mapService.getUserMap()));
	}
	
	@Test
	public void testEav(){
		List listners =(List)container.getObject(EavModule.LISTENER_SAVE_OR_UPDATE_KEY);
		Assert.assertTrue(listners.size()>0);
	}
	
	@Test
	public void testCache(){
		CacheService cacheService = container.getObject(CacheServiceImpl.class);
		String data1 = "just test~~~";
		String expectResult = data1+"--not--cache-result--"+CacheServiceImpl.count;
		String actualResult = cacheService.getString(data1);
		Assert.assertEquals(expectResult, actualResult);

		String expectResult2 = data1+"--not--cache-result--"+CacheServiceImpl.count;
		String actualResult2 = cacheService.getString(data1);
		Assert.assertFalse(actualResult2.equals(expectResult2));
		Assert.assertTrue(actualResult2.equals(expectResult));
		
		try {
			Thread.sleep((CacheService.expireTime+1)*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		String actualResult3 = cacheService.getString(data1);
		String expectResult3 = data1+"--not--cache-result--"+CacheServiceImpl.count;
		Assert.assertFalse(actualResult3.equals(expectResult3));
		Assert.assertTrue(actualResult3.equals(expectResult2));
		
		FlushCacheService flushCacheService = container.getObject(FlushCacheServiceImpl.class);
		flushCacheService.setGroup("");
		
		actualResult3 = cacheService.getString(data1);
		Assert.assertTrue(actualResult3.equals(expectResult3));
		Assert.assertFalse(actualResult3.equals(expectResult2));
	}
	

	
	@Test
	public void testLongTimeCache(){
		TimeCounterService timeConterService = container.getObject(TimeCounterServiceImpl.class);
		timeConterService.getTimeMethod(2);
		timeConterService.getTimeMethod(2);
	}
	
	@Test
	public void testContainerAware(){
		ContainerAwareServiceImpl timeConterService = container.getObject(ContainerAwareServiceImpl.class);
		Assert.assertTrue(container.equals(timeConterService.findContainer()));
	}

}
