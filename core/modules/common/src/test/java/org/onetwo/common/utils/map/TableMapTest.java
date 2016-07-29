package org.onetwo.common.utils.map;

import org.junit.Assert;
import org.junit.Test;

public class TableMapTest {

	@Test
	public void test(){
		TableMap<Integer, String, String> table = new TableMap<>();
		Integer rowNumber = 1245;
		table.put(rowNumber, "clumn1", "data1");
		table.put(rowNumber, "clumn2", "data2");
		table.put(rowNumber, "clumn3", "data4");
		Assert.assertEquals(1, table.rowSize());
		Assert.assertEquals(3, table.size());
		Assert.assertTrue(table.contains(rowNumber, "clumn1"));
		Assert.assertTrue(table.containsColumn("clumn1"));
		Assert.assertTrue(table.containsRow(rowNumber));
		
		Assert.assertEquals(table.getByRC(rowNumber, "clumn1"), table.getByCR("clumn1", rowNumber));
	}
}
