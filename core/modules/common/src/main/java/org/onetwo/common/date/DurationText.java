package org.onetwo.common.date;

import java.time.Duration;
import java.util.List;

import com.google.common.collect.Lists;

public class DurationText {
	
	public static DurationText New(){
		return new DurationText();
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
//				.sorted()
				.filter(map->duration.compareTo(map.duration)<0)
				.findFirst()
				.orElse(defaultMapping);
//		System.out.println("match:" + matchMapping.duration.toMinutes());
//		System.out.println("target duration:" + duration.toMinutes());
//		Duration du = matchMapping.duration.minus(duration);
		return matchMapping.textFormater.format(duration);
	}
	
	public static interface MessageFormater {
		String format(Duration du);
	}

	static class DurationTextMapping {
		final private Duration duration;
		final private MessageFormater textFormater;
		public DurationTextMapping(Duration duration, MessageFormater text) {
			super();
			this.duration = duration;
			this.textFormater = text;
		}
	}
}
