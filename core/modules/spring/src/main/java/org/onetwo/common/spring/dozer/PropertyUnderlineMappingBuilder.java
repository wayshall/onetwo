package org.onetwo.common.spring.dozer;

import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.TypeDefinition;
import org.dozer.loader.api.TypeMappingBuilder;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.LangUtils;
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
		for(Class<?> classb : mapper.getClassb()){
			if(classb==Object.class)
				classb = mapper.getClassa();
			TypeMappingBuilder builder = mapping(buildTypeDefinitionA(mapper), buildTypeDefinitionB(mapper, classb));
			Map<String, String> mfields = mappingFields(mapper.getClassa(), classb, mapper.getFieldSplit());
			for(Entry<String, String> entry : mfields.entrySet()){
				builder.fields(entry.getKey(), entry.getValue());
			}
		}
	}
	
	protected TypeDefinition buildTypeDefinitionA(DozerClassMapper mapper){
		TypeDefinition a = new TypeDefinition(mapper.getClassa());
		buildProperties(a, mapper);
		return a;
	}
	
	protected TypeDefinition buildTypeDefinitionB(DozerClassMapper mapper, Class<?> classb){
		TypeDefinition b = new TypeDefinition(classb);
		buildProperties(b, mapper);
		return b;
	}
	
	protected void buildProperties(TypeDefinition type, DozerClassMapper mapper){
		type.mapNull(mapper.isMapNull());
		type.mapEmptyString(mapper.isMapEmpty());
	}
	
	private boolean ignoreProperty(PropertyDescriptor prop){
		return prop.getWriteMethod()==null;
	}
	
	protected Map<String, String> mappingFields(Class<?> classa, Class<?> classb, String fieldSplit){
		Map<String, String> mappingFields = LangUtils.newHashMap();
		PropertyDescriptor[] srcProps = ReflectUtils.desribProperties(classa);
		if(classa==classb){
			for(PropertyDescriptor prop : srcProps){
				if(ignoreProperty(prop))
					continue;
				mappingFields.put(prop.getName(), prop.getName());
			}
			return mappingFields;
		}
		
		boolean isFieldSplit = StringUtils.isNotBlank(fieldSplit);
		Collection<String> desctFields = ReflectUtils.desribPropertiesName(classb, Set.class);
//		Collection<String> mapFields = srcFields.size()<desctFields.size()?srcFields:desctFields;
		for(PropertyDescriptor prop : srcProps){
			if(ignoreProperty(prop))
				continue;
			String fname = prop.getName();
			if(!isFieldSplit){
				if(desctFields.contains(fname)){
					mappingFields.put(fname, fname);
				}
			}else{
				if(fname.contains(fieldSplit)){
					String destname = StringUtils.toPropertyName(fname);
					if(desctFields.contains(destname)){
						mappingFields.put(fname, destname);
					}
				}else if(StringUtils.hasUpper(fname)){
					String destname = StringUtils.convertWithSeperator(fname, fieldSplit);
					if(desctFields.contains(destname)){
						mappingFields.put(fname, destname);
					}
				}else{
					if(desctFields.contains(fname)){
						mappingFields.put(fname, fname);
					}
				}
			}
		}
		
		return mappingFields;
	}

}
