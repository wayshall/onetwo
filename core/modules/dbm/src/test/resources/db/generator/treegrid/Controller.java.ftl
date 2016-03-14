<#assign modulePath="/${_globalConfig.getModuleName()}/${_tableContext.tableNameWithoutPrefix}"/>

package com.yooyo.zhiyetong.web.configmgr;

import java.util.List;

import javax.annotation.Resource;

import org.onetwo.boot.core.web.controller.AbstractBaseController;
import org.onetwo.boot.core.web.controller.DateInitBinder;
import org.onetwo.boot.plugins.permission.annotation.ByFunctionClass;
import org.onetwo.boot.plugins.permission.annotation.ByMenuClass;
import org.onetwo.common.spring.web.mvc.JsonWrapper;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.map.MappableMap;
import org.onetwo.easyui.EasyModel;
import org.onetwo.easyui.EasyPage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.Page;
import com.yooyo.zhiyetong.entity.DataDictionary;
import com.yooyo.zhiyetong.service.impl.DictionaryServiceImpl;
import com.yooyo.zhiyetong.utils.Zhiyetong.ConfigMgr.DictMgr;

@Controller
@RequestMapping("/configmgr/dictionary")
public class DictionaryController extends AbstractBaseController implements DateInitBinder {
    
    @Resource
    private DictionaryServiceImpl dictionaryServiceImpl;
    
    @ByMenuClass(DictMgr.class)
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView index(EasyPage<MappableMap> page, String parentCode){
        if(isNoneRequestExt()){
            return mv("/configmgr/dictionary-index");
        }
        Page<DataDictionary> rows = dictionaryServiceImpl.findPage(page, parentCode);
        List<MappableMap> districtMaps = EasyModel.newTreeBuilder(DataDictionary.class)
                .id("code")
                .text("name")
                .isStateOpen((src, mapping)->false)
                .build(rows);
        page.setRows(districtMaps);
        return mv("/configmgr/dictionary-index", "page", page, JsonWrapper.wrap(page));
    }
    
    @ByFunctionClass(DictMgr.class)
    @RequestMapping(value="children", method=RequestMethod.GET)
    public ModelAndView children(String parentCode){
        List<DataDictionary> datalist = dictionaryServiceImpl.findChildren(parentCode);
        List<MappableMap> districtMaps = EasyModel.newTreeBuilder(DataDictionary.class)
                                                .id("code")
                                                .text("name")
                                                .isStateOpen((src, mapping)->StringUtils.isBlank(src.getParentCode()))
                                                .build(datalist);
        return mv("/configmgr/dictionary-index", "datalist", datalist, JsonWrapper.wrap(districtMaps));
    }
    
    @ByFunctionClass(DictMgr.class)
    @RequestMapping(method=RequestMethod.POST)
    public ModelAndView create(DataDictionary dictionary){
        dictionaryServiceImpl.save(dictionary);
        return messageMv("保存成功！");
    }
    
    @ByFunctionClass(DictMgr.class)
    @RequestMapping(value="{code}", method=RequestMethod.GET)
    public ModelAndView show(@PathVariable("code") String code){
        DataDictionary dictionary = dictionaryServiceImpl.findByPrimaryKey(code);
        return mv("/configmgr/dictionary-edit", "dictionary", dictionary, JsonWrapper.wrap(dictionary));
    }
    
    @ByFunctionClass(DictMgr.class)
    @RequestMapping(value="{code}", method=RequestMethod.PUT)
    public ModelAndView update(@PathVariable("code") String code, DataDictionary dictionary){
        dictionary.setCode(code);
        dictionaryServiceImpl.update(dictionary);
        return messageMv("更新成功！");
    }
    
    @ByFunctionClass(DictMgr.class)
    @RequestMapping(value="{code}", method=RequestMethod.DELETE)
    public ModelAndView delete(@PathVariable("code") String code){
        dictionaryServiceImpl.deleteByPrimaryKey(code);
        return messageMv("删除成功！");
    }
    
    @ByFunctionClass(DictMgr.class)
    @RequestMapping(method=RequestMethod.DELETE)
    public ModelAndView deleteBatch(String[] code){
        dictionaryServiceImpl.deleteByPrimaryKeys(code);
        return messageMv("删除成功！");
    }

}
