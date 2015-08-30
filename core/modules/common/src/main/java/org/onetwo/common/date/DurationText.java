package org.onetwo.common.date;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;

public class DurationText {
	
	public static DurationText New(){
		return new DurationText();
	}
	
	public static final DurationText createSimpleChineseDurationText(){
		return DurationText.New()
							.map(Duration.ofMinutes(1), "刚刚")
							.map(Duration.ofHours(24), du->du.toHours()+"小时前")
							.map(Duration.ofMinutes(60), du->du.toMinutes()+"分钟前")
							.other(du->du.toDays()+"天前");
	}
	private List<DurationTextMapping> mappings = Lists.newArrayList();
	private DurationTextMapping defaultMapping;
	
	public DurationText map(Duration duration, String text){
		mappings.add(new DurationTextMapping(duration, d->text));
		return this;
	}
	
	public DurationText map(Duration duration, MessageFormater formater){
		mappings.add(new DurationTextMapping(duration, formater));
		return this;
	}
	
	public DurationText other(MessageFormater formater){
		defaultMapping = new DurationTextMapping(null, formater);
		return this;
	}
	
	public String getText(Duration duration){
		DurationTextMapping matchMapping = mappings.stream()
				.sorted()
				.filter(map->duration.compareTo(map.duration)<0)
				.findFirst()
				.orElse(defaultMapping);
//		System.out.println("match:" + matchMapping.duration.toMinutes());
//		System.out.println("target duration:" + duration.toMinutes());
//		Duration du = matchMapping.duration.minus(duration);
		return matchMapping.textFormater.format(duration);
	}
	
	/****
	 * date 距离 unitDate 的文字描述
	 * @param fromDate
	 * @param unitDate
	 * @return
	 */
	public String getText(LocalDateTime fromDate, LocalDateTime unitDate){
		Duration duration = Duration.ofSeconds(fromDate.until(unitDate, ChronoUnit.SECONDS));
		String text = getText(duration);
		return text;
	}
	public String getText(Date fromDate, Date unitDate){
		return getText(Dates.toLocalDateTime(fromDate), Dates.toLocalDateTime(unitDate));
	}
	
	/***
	 * fromDate 距离 当前时间 的文字描述
	 * @param fromDate
	 * @return
	 */
	public String getText(LocalDateTime fromDate){
		return getText(fromDate, LocalDateTime.now());
	}
	public String getText(Date fromDate){
		return getText(Dates.toLocalDateTime(fromDate), LocalDateTime.now());
	}
	
	public static interface MessageFormater {
		String format(Duration du);
	}

	static class DurationTextMapping implements Comparable<DurationTextMapping>{
		final private Duration duration;
		final private MessageFormater textFormater;
		public DurationTextMapping(Duration duration, MessageFormater text) {
			super();
			this.duration = duration;
			this.textFormater = text;
		}
		@Override
		public int compareTo(DurationTextMapping o) {
			if(duration==null && o.duration==null)
				return 0;
			if(duration==null){
				return -1;
			}else if(o.duration==null){
				return 1;
			}else{
				return duration.compareTo(o.duration);
			}
		}
		
		
	}
}
