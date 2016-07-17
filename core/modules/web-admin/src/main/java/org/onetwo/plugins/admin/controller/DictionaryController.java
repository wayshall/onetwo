package org.onetwo.plugins.admin.controller;

import java.util.List;

import javax.annotation.Resource;

import org.onetwo.boot.core.web.controller.DateInitBinder;
import org.onetwo.common.spring.web.mvc.utils.DataWrapper;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.map.MappableMap;
import org.onetwo.easyui.EasyModel;
import org.onetwo.easyui.EasyPage;
import org.onetwo.ext.permission.api.annotation.ByPermissionClass;
import org.onetwo.plugins.admin.AdminModule.DictMgr;
import org.onetwo.plugins.admin.entity.DataDictionary;
import org.onetwo.plugins.admin.service.impl.DictionaryServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/dictionary")
public class DictionaryController extends WebAdminBaseController implements DateInitBinder {
	
	@Resource
	private DictionaryServiceImpl dictionaryServiceImpl;
	
	@ByPermissionClass(DictMgr.class)
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView index(Page<DataDictionary> page, String parentCode){
		return responsePageOrData("dictionary-index", ()->{
				dictionaryServiceImpl.findPage(page, parentCode);
				List<MappableMap> districtMaps = EasyModel.newTreeBuilder(DataDictionary.class)
						.mapId("code")
						.mapText("name")
						.mapIsStateOpen(src->false)
						.build(page.getResult());
				EasyPage<MappableMap> easyPage = EasyPage.create(districtMaps, page);
				return easyPage;
			}
		);
	}
	
	@ByPermissionClass(DictMgr.class)
	@RequestMapping(value="children", method=RequestMethod.GET)
	public ModelAndView children(String parentCode){
		List<DataDictionary> datalist = dictionaryServiceImpl.findChildren(parentCode);
		List<MappableMap> districtMaps = EasyModel.newTreeBuilder(DataDictionary.class)
												.mapId("code")
												.mapText("name")
												.mapIsStateOpen((src)->StringUtils.isBlank(src.getParentCode()))
												.build(datalist);
		return mv("dictionary-index", "datalist", datalist, DataWrapper.wrap(districtMaps));
	}
	
	@ByPermissionClass(DictMgr.class)
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView create(DataDictionary dictionary){
		dictionaryServiceImpl.save(dictionary);
		return messageMv("保存成功！");
	}
	
	@ByPermissionClass(DictMgr.class)
	@RequestMapping(value="{code}", method=RequestMethod.GET)
	public ModelAndView show(@PathVariable("code") String code){
		DataDictionary dictionary = dictionaryServiceImpl.findByPrimaryKey(code);
		MappableMap dictMap = EasyModel.newSimpleBuilder(DataDictionary.class)
				.addMapping("valid", src->{
					return src.getValid()==null?"false":src.getValid().toString();
				})
				.build(dictionary);
		return responseData(dictMap);
	}
	
	@ByPermissionClass(DictMgr.class)
	@RequestMapping(value="{code}", method=RequestMethod.PUT)
	public ModelAndView update(@PathVariable("code") String code, DataDictionary dictionary){
		dictionary.setCode(code);
		dictionaryServiceImpl.update(dictionary);
		return messageMv("更新成功！");
	}
	
	@ByPermissionClass(DictMgr.class)
	@RequestMapping(value="{code}", method=RequestMethod.DELETE)
	public ModelAndView delete(@PathVariable("code") String code){
		dictionaryServiceImpl.deleteByPrimaryKey(code);
		return messageMv("删除成功！");
	}
	
	@ByPermissionClass(DictMgr.class)
	@RequestMapping(method=RequestMethod.DELETE)
	public ModelAndView deleteBatch(String[] code){
		dictionaryServiceImpl.deleteByPrimaryKeys(code);
		return messageMv("批量删除成功！");
	}


	//open
	/***
	 * 返回easyui下拉框数据形式
	 * @param parentCode
	 * @param valueField
	 * @return
	 */
	@RequestMapping(value="combobox/{parentCode}", method=RequestMethod.GET)
	@ResponseBody
	public Object combobox(@PathVariable("parentCode")String parentCode, String valueField){
		/*if(StringUtils.isBlank(parentCode))
			throw new ServiceException("parentCode不能为空!");*/
		
		if(StringUtils.isBlank(valueField)){
			valueField = "value";
		}
		List<DataDictionary> datalist = dictionaryServiceImpl.findChildren(parentCode);
		List<MappableMap> districtMaps = EasyModel.newComboBoxBuilder(DataDictionary.class)
												.specifyMappedFields()
												.mapText("name")
												.mapValue(valueField)
//												.mapSelected(src->src==empty)
												.build(datalist);
		return districtMaps;
	}
}
