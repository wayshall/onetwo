package org.onetwo.common.date;
/**
 * @author weishao zeng
 * <br/>
 */

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;

import org.junit.Test;

public class DatesTest {
	
	@Test
	public void testLocalTime() {
		LocalTime time = LocalTime.parse("11:30");
		LocalTime time2 = LocalTime.parse("11:29");
		assertThat(time).isAfter(time2);
	}

}
