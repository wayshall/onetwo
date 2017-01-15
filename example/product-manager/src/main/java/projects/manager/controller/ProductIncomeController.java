

package projects.manager.controller;

import java.util.Arrays;

import org.onetwo.boot.core.web.controller.AbstractBaseController;
import org.onetwo.boot.core.web.controller.DateInitBinder;
import org.onetwo.common.utils.Page;
import org.onetwo.easyui.EasyDataGrid;
import org.onetwo.ext.permission.api.annotation.ByPermissionClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import projects.manager.entity.ProductIncome;
import projects.manager.service.impl.ProductIncomeServiceImpl;
import projects.manager.utils.Products.SystemMgr.IncomeMgr;
import projects.manager.vo.BudgetStatisVo;

@Controller
@RequestMapping("/manager/productIncome")
public class ProductIncomeController extends AbstractBaseController implements DateInitBinder {

    @Autowired
    private ProductIncomeServiceImpl productIncomeServiceImpl;
    
    
    @ByPermissionClass(IncomeMgr.List.class)
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView index(EasyDataGrid<ProductIncome> easyPage, ProductIncome productIncome){
        return responsePageOrData("/manager/product-income-index", ()->{
        			Page<ProductIncome> page = Page.create(easyPage.getPage(), easyPage.getPageSize());
                    productIncomeServiceImpl.findPage(page, productIncome);
                    return EasyDataGrid.create(page);
                });
    }
    
    @ByPermissionClass(IncomeMgr.Create.class)
    @RequestMapping(method=RequestMethod.POST)
    public ModelAndView create(ProductIncome productIncome){
        productIncomeServiceImpl.save(productIncome);
        return messageMv("保存成功！");
    }
    @ByPermissionClass(IncomeMgr.class)
    @RequestMapping(value="{id}", method=RequestMethod.GET)
    public ModelAndView show(@PathVariable("id") Long id){
        ProductIncome productIncome = productIncomeServiceImpl.findById(id);
        return responseData(productIncome);
    }
    
    @ByPermissionClass(IncomeMgr.Update.class)
    @RequestMapping(value="{id}", method=RequestMethod.PUT)
    public ModelAndView update(@PathVariable("id") Long id, ProductIncome productIncome){
        productIncome.setId(id);
        productIncomeServiceImpl.save(productIncome);
        return messageMv("更新成功！");
    }
    
    
    @ByPermissionClass(IncomeMgr.Delete.class)
    @RequestMapping(method=RequestMethod.DELETE)
    public ModelAndView deleteBatch(Long[] ids){
        productIncomeServiceImpl.removeByIds(ids);
        return messageMv("删除成功！");
    }
    

    @ByPermissionClass(IncomeMgr.Statis.class)
    @RequestMapping(value="statis", method=RequestMethod.GET)
    public ModelAndView statis(){
        return responsePageOrData("/manager/product-income-statis", ()->{
        			BudgetStatisVo budget = productIncomeServiceImpl.statisSummary();
                    return EasyDataGrid.create(Arrays.asList(budget));
                });
    }
}