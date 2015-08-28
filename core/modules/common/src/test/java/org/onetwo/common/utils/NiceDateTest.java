package org.onetwo.common.utils;

import org.junit.Test;
import org.onetwo.common.date.NiceDate;


public class NiceDateTest {

	@Test
	public void testNiceDateText(){
		NiceDate date = NiceDate.New((map)->{
			map.map(TimeUnit.minis(0), TimeUnit.minis(1), "刚刚");
			map.map(TimeUnit.minis(1), TimeUnit.minis(60), "{time}分钟前");
		});
		NiceDate date2 = NiceDate.New().nextMinute(-1);
		String timeText = date2.awayFrom(date).getFriendlyTimeText();
	}
}
