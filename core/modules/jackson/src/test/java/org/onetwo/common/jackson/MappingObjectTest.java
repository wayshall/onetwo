package org.onetwo.common.jackson;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import org.onetwo.common.utils.ReflectUtils;


@SuppressWarnings("serial")
public class MappingObjectTest extends HashMap<String, Object>{
	
//	final private Object sourceObject;
//	final private List<MappingInfo> mappingInfos = new ArrayList<>();

	public MappingObjectTest() {
	    super();
    }
	
	public MappingObjectTest from(Object sourceObject){
	    PropertyDescriptor[] props = ReflectUtils.desribProperties(sourceObject.getClass());
	    Stream.of(props).forEach(p->{
	    	Object value = ReflectUtils.getProperty(sourceObject, p);
	    	if(value==null)
	    		return ;
	    	super.put(p.getName(), value);
	    });
	    return this;
	}
	
	public MappingBuilder mapFrom(Object sourceObject){
	    return new MappingBuilder(this, sourceObject);
	}
	
	public static class StaticMappingBuilder {
		final private List<MappingInfo> mappingInfos = new ArrayList<>();
		public StaticMappingBuilder() {
        }
		
		public StaticMappingBuilder mapping(String sourceName, String targetName){
			this.mappingInfos.add(new MappingInfo(sourceName, targetName));
			return this;
		}

		
		public MappingObjectTest bindValues(MappingObjectTest mappingObject,Object sourceObject){
		    mappingInfos.stream().forEach(maping->{
		    	Object value = ReflectUtils.getProperty(sourceObject, maping.sourceName);
		    	mappingObject.put(maping.targetName, value);
		    });
		    return mappingObject;
		}
		
	}
	
	static class MappingBuilder {
		final private Object sourceObject;
		final private List<MappingInfo> mappingInfos = new ArrayList<>();
		final private MappingObjectTest mappingObject;
		public MappingBuilder(MappingObjectTest mappingObject, Object sourceObject) {
	        super();
	        this.sourceObject = sourceObject;
	        this.mappingObject = mappingObject;
        }
		
		public MappingBuilder mapping(String sourceName, String targetName){
			this.mappingInfos.add(new MappingInfo(sourceName, targetName));
			return this;
		}

		
		public MappingObjectTest bindValues(){
		    mappingInfos.stream().forEach(maping->{
		    	Object value = ReflectUtils.getProperty(sourceObject, maping.sourceName);
		    	mappingObject.put(maping.targetName, value);
		    });
		    return mappingObject;
		}
		
	}
	
	static class MappingInfo {
		final private String sourceName;
		final private String targetName;
		public MappingInfo(String sourceName, String targetName) {
	        super();
	        this.sourceName = sourceName;
	        this.targetName = targetName;
        }
		
	}
	

}
