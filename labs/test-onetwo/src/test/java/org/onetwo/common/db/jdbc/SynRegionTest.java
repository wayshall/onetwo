package org.onetwo.common.db.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.onetwo.common.db.wheel.DatabaseManager;
import org.onetwo.common.db.wheel.DefaultDatabaseManage;
import org.onetwo.common.db.wheel.JDBC;
import org.onetwo.common.db.wheel.JDao;
import org.onetwo.common.db.wheel.EntityTableInfoBuilder.TMeta;

public class SynRegionTest {
	static JDao mysql;
	static JDao oracle;

	@BeforeClass
	public static void beforeClass(){
		DatabaseManager dm = new DefaultDatabaseManage();
		dm.setDbconfigPath("/org/onetwo/common/db/jdbc/mysql_test.properties");
		mysql = JDBC.createJDao(dm);
		
		dm = new DefaultDatabaseManage();
		dm.setDbconfigPath("/org/onetwo/common/db/jdbc/oracle_test.properties");
		oracle = JDBC.createJDao(dm);
		
		JDBC.init(dm);
	}
	
	@Test
	public void test(){
		Map<String, Object> linkage = new HashMap<String, Object>();
		linkage.put(":table", "v9_linkage");
		linkage.put(TMeta.pk, "linkageid");
		linkage.put(":linkageid", Long.class);
		linkage.put(":name", String.class);
		linkage.put(":parentid", Long.class);
		
		Map<String, Object> region = new HashMap<String, Object>();
		region.put(":table", "bm_region");
		region.put(TMeta.pk, "id");
		region.put(":id", Long.class);
		region.put(":ccode", String.class);
		region.put(":cname", String.class);
		region.put(":language", String.class);
		
		Map<String, Object> linkRegion = new HashMap<String, Object>();
		linkRegion.put(":table", "linkage_region");
		linkRegion.put(TMeta.pk, "id");
		linkRegion.put(":id", Long.class);
		linkRegion.put(":linkage_id", String.class);
		linkRegion.put(":region_id", String.class);
		
		Map<String, Object> query = new HashMap<String, Object>();
		query.put(":entity_meta", linkage);
//		query.put("name", "沿河土家族自治县");
//		linkage.put("parentid", 4593);
		List<Map<String, Object>> list = mysql.findByExample(query);
		int index = 0;
		for(Map map : list){
			Map<String, Object> regionCause = new HashMap<String, Object>();
			regionCause.put(TMeta.entity_meta, region);
			regionCause.put("cname", map.get("name"));
//			Map oregion = oracle.findByExample(regionCause);
			
//			oracle.findByExample(object);
			Map<String, Object> lr = new HashMap<String, Object>();
			lr.put(TMeta.entity_meta, linkRegion);
			lr.put("linkage_id", map.get("linkageid"));
			lr.put("region_id", map.get("linkageid"));
			System.out.println("map["+index+"]: " + map);
			index++;
		}
	}

}
