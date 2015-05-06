package org.onetwo.plugins.fmtagext.directive;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.onetwo.common.fish.exception.JFishException;
import org.onetwo.common.ftl.directive.DirectivesUtils;
import org.onetwo.common.jfishdb.orm.AbstractMappedField;
import org.onetwo.common.jfishdb.orm.JFishMappedEntry;
import org.onetwo.common.jfishdb.orm.JFishMappedField;
import org.onetwo.common.jfishdb.orm.JFishMappedFieldType;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.fmtag.directive.DataField;
import org.onetwo.plugins.fmtag.directive.DataGrid;
import org.onetwo.plugins.fmtag.directive.DataGridDirective;
import org.onetwo.plugins.fmtag.directive.DataRow;
import org.onetwo.plugins.fmtagext.service.JFishDataDelegateService;

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
			if(!dg.getPage().getResult().isEmpty())
				eclass = dg.getPage().getResult().get(0).getClass();
			else
				throw new JFishException("no entity class");
		}
		
		JFishMappedEntry entry = jdataDelegate.getJFishMappedEntry(eclass.getName());
		
		doBeforeBuildEntryField(entry, dg);
//		jdataDelegate.findPage(entry.getEntityClass(), dg.page);
		Collection<AbstractMappedField> fields = entry.getFields(JFishMappedFieldType.FIELD);
		boolean showable = false;
		for(JFishMappedField field : fields){
			showable = ArrayUtils.contains(includeFields, field.getName()) || !ArrayUtils.contains(excludeFields, field.getName());
			if(!showable)
				continue;
			DataField dfield = this.createDataField(field);
			if(!dfield.isShowInGrid())
				continue;
			if(ArrayUtils.contains(searchFields, field.getName())){
				dfield.setSearch(true);
			}
//			dg.addIteratorField(dfield);
			
			DataRow row = dg.getIteratorRow();
//			dfield.setShowOrder(row.getFields().size()+1);
			row.addField(dfield);
		}
		Collections.sort(dg.getIteratorRow().getFields());
	}
	
	protected DataField createDataField(JFishMappedField field){
		return new DataField(field.getName(), field.getLabel());
	}
	
	protected void doBeforeBuildEntryField(JFishMappedEntry entry, DataGrid dg){
	}

	@Override
	public String getDirectiveName() {
		return DIRECTIVE_NAME;
	}

}
