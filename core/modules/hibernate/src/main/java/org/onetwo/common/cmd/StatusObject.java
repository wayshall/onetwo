package org.onetwo.common.cmd;

import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;

public class StatusObject {
	
	public static final float MBASE = 1024 * 1024;
	
	private Statistics hbnStatistics;
	
	public StatusObject(SessionFactory sf){
		this.hbnStatistics = sf.getStatistics();
	}
	
	public float getTotalMemory(){
		return parse2Mb(Runtime.getRuntime().totalMemory());
	}
	
	public float getFreeMemory(){
		return parse2Mb(Runtime.getRuntime().freeMemory());
	}
	
	public float getMaxMemory(){
		return parse2Mb(Runtime.getRuntime().maxMemory());
	}
	
	protected float parse2Mb(long size){
		return Float.parseFloat(String.valueOf(size))/MBASE;
	}

	public Statistics getHbnStatistics() {
		return hbnStatistics;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("========================= status start ===============================\n");
		sb.append("TotalMemory: ").append(this.getTotalMemory()).append("\n");
		sb.append("FreeMemory: ").append(this.getFreeMemory()).append("\n");
		sb.append("MaxMemory: ").append(this.getMaxMemory()).append("\n");
		if(getHbnStatistics()!=null)
			sb.append("HbnStatistics: ").append(this.getHbnStatistics().toString()).append("\n");
		sb.append("=========================  status end  ===============================\n");
		return sb.toString();
	}

}
