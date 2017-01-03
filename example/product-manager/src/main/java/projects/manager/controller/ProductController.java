

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

import projects.manager.entity.Product;
import projects.manager.service.impl.ProductServiceImpl;
import projects.manager.utils.Products.SystemMgr.ProductMgr;

@Controller
@RequestMapping("/manager/product")
public class ProductController extends AbstractBaseController implements DateInitBinder {

    @Autowired
    private ProductServiceImpl productServiceImpl;
    
    
    @ByPermissionClass(ProductMgr.class)
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView index(EasyDataGrid<Product> easyPage, Product product){
        return responsePageOrData("/manager/product-index", ()->{
        			Page<Product> page = Page.create(easyPage.getPage(), easyPage.getPageSize());
                    productServiceImpl.findPage(page, product);
                    return EasyDataGrid.create(page);
                });
    }
    
    @ByPermissionClass(ProductMgr.Create.class)
    @RequestMapping(method=RequestMethod.POST)
    public ModelAndView create(Product product){
        productServiceImpl.save(product);
        return messageMv("保存成功！");
    }
    @ByPermissionClass(ProductMgr.class)
    @RequestMapping(value="{id}", method=RequestMethod.GET)
    public ModelAndView show(@PathVariable("id") Long id){
        Product product = productServiceImpl.findById(id).orElse(null);
        return responseData(product);
    }
    
    @ByPermissionClass(ProductMgr.Update.class)
    @RequestMapping(value="{id}", method=RequestMethod.PUT)
    public ModelAndView update(@PathVariable("id") Long id, Product product){
        product.setId(id);
        productServiceImpl.save(product);
        return messageMv("更新成功！");
    }
    
    
    @ByPermissionClass(ProductMgr.Delete.class)
    @RequestMapping(method=RequestMethod.DELETE)
    public ModelAndView deleteBatch(Long[] ids){
        productServiceImpl.removeByIds(ids);
        return messageMv("删除成功！");
    }
}