package org.onetwo.common.spring.dozer;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.TypeDefinition;
import org.dozer.loader.api.TypeMappingBuilder;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;

public class PropertyUnderlineMappingBuilder extends BeanMappingBuilder {
	public static final String UNDERLINE = "_";

	private List<DozerClassMapper> mappers;
	
	
	public PropertyUnderlineMappingBuilder(List<DozerClassMapper> mappers) {
		super();
		this.mappers = mappers;
	}


	@Override
	protected void configure() {
		for(DozerClassMapper mapper : mappers){
			mapDozerClassMapper(mapper);
		}
	}
	
	protected void mapDozerClassMapper(DozerClassMapper mapper){
		TypeMappingBuilder builder = mapping(buildTypeDefinitionA(mapper), buildTypeDefinitionB(mapper));
		Map<String, String> mfields = mappingFields(mapper);
		for(Entry<String, String> entry : mfields.entrySet()){
			builder.fields(entry.getKey(), entry.getValue());
		}
	}
	
	protected TypeDefinition buildTypeDefinitionA(DozerClassMapper mapper){
		TypeDefinition a = new TypeDefinition(mapper.getClassa());
		buildProperties(a, mapper);
		return a;
	}
	
	protected TypeDefinition buildTypeDefinitionB(DozerClassMapper mapper){
		TypeDefinition b = new TypeDefinition(mapper.getClassb());
		buildProperties(b, mapper);
		return b;
	}
	
	protected void buildProperties(TypeDefinition type, DozerClassMapper mapper){
		type.mapNull(mapper.isMapNull());
		type.mapEmptyString(mapper.isMapEmpty());
	}
	
	protected Map<String, String> mappingFields(DozerClassMapper mapper){
		Map<String, String> mappingFields = LangUtils.newHashMap();
		Collection<String> srcFields = ReflectUtils.desribPropertiesName(mapper.getClassa(), Set.class);
		if(mapper.getClassa()==mapper.getClassb()){
			for(String field : srcFields)
				mappingFields.put(field, field);
			return mappingFields;
		}
		
		Collection<String> desctFields = ReflectUtils.desribPropertiesName(mapper.getClassb(), Set.class);
//		Collection<String> mapFields = srcFields.size()<desctFields.size()?srcFields:desctFields;
		for(String fname : srcFields){
			if(desctFields.contains(fname)){
				mappingFields.put(fname, fname);
				
			}else if(fname.contains(UNDERLINE)){
				String destname = StringUtils.toPropertyName(fname);
				if(desctFields.contains(destname)){
					mappingFields.put(fname, destname);
				}
			}else if(StringUtils.hasUpper(fname)){
				String destname = StringUtils.convert2UnderLineName(fname, UNDERLINE);
				if(desctFields.contains(destname)){
					mappingFields.put(fname, destname);
				}
			}
		}
		
		return mappingFields;
	}

}
