package org.onetwo.plugins.fmtag.directive;

import java.util.Map;

import org.onetwo.common.fish.exception.JFishException;
import org.onetwo.common.ftl.directive.DirectivesUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.fmtag.service.JFishDataDelegateService;

import freemarker.core.Environment;
import freemarker.ext.beans.BeanModel;


@SuppressWarnings("rawtypes")
public class EntityGridDirective extends DataGridDirective {

	public static final String DIRECTIVE_NAME = "egrid";
	
	public static final String PARAMS_ENTITY = "entity";
	
	
	public EntityGridDirective(){
	}
	
//	@SuppressWarnings("unchecked")
	protected void doBeforeRenderInnerBody(DataGrid dg, Environment env, Map params){
//		if(dg.page==null || dg.page.getResult().isEmpty())
//			return ;
		String excludeFieldsStr = DirectivesUtils.getParameterByString(params, "excludeFields", "");
		String[] excludeFields = StringUtils.split(excludeFieldsStr, ",");
		String includeFieldsStr = DirectivesUtils.getParameterByString(params, "includeFields", "");
		String[] includeFields = StringUtils.split(includeFieldsStr, ",");
		String searchFieldsStr = DirectivesUtils.getParameterByString(params, "searchFields", "");
		String[] searchFields = StringUtils.split(searchFieldsStr, ",");
		
		JFishDataDelegateService jdataDelegate = getHighestBean(JFishDataDelegateService.class);
		BeanModel bm = (BeanModel)DirectivesUtils.getVariable(env, "entityClass", true);
		Class<?> eclass = null;
		if(bm!=null){
			eclass = (Class<?>) bm.getWrappedObject();
		}else{
			if(!dg.page.getResult().isEmpty())
				eclass = dg.page.getResult().get(0).getClass();
			else
				throw new JFishException("no entity class");
		}
		
		//TODO
//		JFishMappedEntry entry = jdataDelegate.getJFishMappedEntry(eclass.getName());
//		
//		doBeforeBuildEntryField(entry, dg);
//		Collection<AbstractMappedField> fields = entry.getFields(JFishMappedFieldType.FIELD);
//		boolean showable = false;
//		for(JFishMappedField field : fields){
//			showable = ArrayUtils.contains(includeFields, field.getName()) || !ArrayUtils.contains(excludeFields, field.getName());
//			if(!showable)
//				continue;
//			DataField dfield = this.createDataField(field);
//			if(!dfield.isShowInGrid())
//				continue;
//			if(ArrayUtils.contains(searchFields, field.getName())){
//				dfield.search = true;
//			}
//			
//			DataRow row = dg.getIteratorRow();
//			row.addField(dfield);
//		}
//		Collections.sort(dg.getIteratorRow().getFields());
	}
	
	//TODO
//	protected DataField createDataField(JFishMappedField field){
//		return new DataField(field.getName(), field.getLabel());
//	}
//	
//	protected void doBeforeBuildEntryField(JFishMappedEntry entry, DataGrid dg){
//	}

	@Override
	public String getDirectiveName() {
		return DIRECTIVE_NAME;
	}

}
