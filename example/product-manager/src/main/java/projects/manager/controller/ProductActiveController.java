

package projects.manager.controller;

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

import projects.manager.entity.ProductActive;
import projects.manager.service.impl.ProductActiveServiceImpl;
import projects.manager.utils.Products.SystemMgr.ActiveMgr;
import projects.manager.vo.ProductActiveStatisVo;


@Controller
@RequestMapping("/manager/productActive")
public class ProductActiveController extends AbstractBaseController implements DateInitBinder {

    @Autowired
    private ProductActiveServiceImpl productActiveServiceImpl;
    
    
    @ByPermissionClass(ActiveMgr.List.class)
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView index(EasyDataGrid<ProductActive> easyPage, ProductActive productActive){
        return responsePageOrData("/manager/product-active-index", ()->{
        			Page<ProductActive> page = Page.create(easyPage.getPage(), easyPage.getPageSize());
                    productActiveServiceImpl.findPage(page, productActive);
                    return EasyDataGrid.create(page);
                });
    }
    
    @ByPermissionClass(ActiveMgr.Create.class)
    @RequestMapping(method=RequestMethod.POST)
    public ModelAndView create(ProductActive productActive){
        productActiveServiceImpl.save(productActive);
        return messageMv("保存成功！");
    }
    @ByPermissionClass(ActiveMgr.class)
    @RequestMapping(value="{id}", method=RequestMethod.GET)
    public ModelAndView show(@PathVariable("id") Long id){
        ProductActive productActive = productActiveServiceImpl.findById(id);
        return responseData(productActive);
    }
    
    @ByPermissionClass(ActiveMgr.Update.class)
    @RequestMapping(value="{id}", method=RequestMethod.PUT)
    public ModelAndView update(@PathVariable("id") Long id, ProductActive productActive){
        productActive.setId(id);
        productActiveServiceImpl.update(productActive);
        return messageMv("更新成功！");
    }
    
    
    @ByPermissionClass(ActiveMgr.Delete.class)
    @RequestMapping(method=RequestMethod.DELETE)
    public ModelAndView deleteBatch(Long[] ids){
        productActiveServiceImpl.removeByIds(ids);
        return messageMv("删除成功！");
    }
    

    @ByPermissionClass(ActiveMgr.Statis.class)
    @RequestMapping(value="statis", method=RequestMethod.GET)
    public ModelAndView statis(EasyDataGrid<ProductActiveStatisVo> easyPage){
        return responsePageOrData("/manager/product-active-statis", ()->{
        			Page<ProductActiveStatisVo> page = Page.create(easyPage.getPage(), easyPage.getPageSize());
                    productActiveServiceImpl.statisActiveByDatePage(page);
                    EasyDataGrid<ProductActiveStatisVo> epage = EasyDataGrid.create(page);
                    epage.addFooter(productActiveServiceImpl.statisActive());
                    return epage;
                });
    }
}