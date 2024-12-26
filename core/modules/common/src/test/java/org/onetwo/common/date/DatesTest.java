package org.onetwo.common.date;
/**
 * @author weishao zeng
 * <br/>
 */

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Test;

public class DatesTest {
	
	@Test
	public void testLocalTime() {
		LocalTime time = LocalTime.parse("11:30");
		LocalTime time2 = LocalTime.parse("11:29");
		assertThat(time).isAfter(time2);
		
		// 设置时间为20:00:00
        LocalTime specificTime = LocalTime.of(20, 0, 0);
        // 合并日期和时间
        LocalDateTime dateTime = LocalDateTime.of(LocalDate.of(2024, 12, 26), specificTime);
        LocalDateTime yeserdate = dateTime.minusDays(1);
        System.out.println("yeserdate: " + Dates.toDate(yeserdate).toLocaleString());
        System.out.println("datetime: " + Dates.toDate(dateTime).toLocaleString());
        assertThat(Dates.toDate(yeserdate)).isEqualTo(NiceDate.New("2024-12-25 20:00:00").getTime());
        assertThat(Dates.toDate(dateTime)).isEqualTo(NiceDate.New("2024-12-26 20:00:00").getTime());
	}

}
