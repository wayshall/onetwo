<#assign datagridName="dataGrid"/>
<#assign modulePath="/${_globalConfig.getModuleName()}/${_tableContext.tableNameWithoutPrefix}"/>

package com.yooyo.zhiyetong.web.resourcemgr;

import org.onetwo.boot.core.web.controller.AbstractBaseController;
import org.onetwo.boot.core.web.controller.DateInitBinder;
import org.onetwo.boot.plugins.permission.annotation.ByFunctionClass;
import org.onetwo.boot.plugins.permission.annotation.ByMenuClass;
import org.onetwo.common.spring.web.mvc.JsonWrapper;
import org.onetwo.easyui.EasyPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yooyo.zhiyetong.entity.Estate;
import com.yooyo.zhiyetong.entity.EstateExtEntity;
import com.yooyo.zhiyetong.service.impl.EstateServiceImpl;
import com.yooyo.zhiyetong.utils.Zhiyetong.ConfigMgr.DictMgr;
import com.yooyo.zhiyetong.utils.Zhiyetong.ResourceMgr.EstateMgr;

@Controller
@RequestMapping("/resourcemgr/estate")
public class EstateController extends AbstractBaseController implements DateInitBinder {

    @Autowired
    private EstateServiceImpl estateServiceImpl;
    
    
    @ByMenuClass(EstateMgr.class)
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView index(EasyPage<EstateExtEntity> page){
        if(isPageOnleyRequest()){
            return mv("/resourcemgr/estate-index");
        }
        estateServiceImpl.findPage(page);
        return mv("/resourcemgr/estate-index", "estatePage", page, JsonWrapper.wrap(page));
    }
    
    @ByFunctionClass(DictMgr.class)
    @RequestMapping(method=RequestMethod.POST)
    public ModelAndView create(Estate estate){
        estateServiceImpl.save(estate);
        return messageMv("保存成功！");
    }
    @ByFunctionClass(DictMgr.class)
    @RequestMapping(value="{code}", method=RequestMethod.GET)
    public ModelAndView show(@PathVariable("code") Long id){
        Estate estate = estateServiceImpl.findByPrimaryKey(id);
        return mv("/configmgr/dictionary-edit", "estate", estate, JsonWrapper.wrap(estate));
    }
    
    @ByFunctionClass(DictMgr.class)
    @RequestMapping(value="{id}", method=RequestMethod.PUT)
    public ModelAndView update(@PathVariable("id") Long id, Estate estate){
        estate.setId(id);
        estateServiceImpl.update(estate);
        return messageMv("更新成功！");
    }
    
    
    @ByFunctionClass(DictMgr.class)
    @RequestMapping(method=RequestMethod.DELETE)
    public ModelAndView deleteBatch(Long[] ids){
        estateServiceImpl.deleteByPrimaryKeys(ids);
        return messageMv("删除成功！");
    }
}
