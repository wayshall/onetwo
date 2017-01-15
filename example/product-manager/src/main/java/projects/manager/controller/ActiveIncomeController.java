

package projects.manager.controller;

import org.onetwo.boot.core.web.controller.AbstractBaseController;
import org.onetwo.boot.core.web.controller.DateInitBinder;
import org.onetwo.common.utils.Page;
import org.onetwo.easyui.EasyDataGrid;
import org.onetwo.ext.permission.api.annotation.ByPermissionClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import projects.manager.service.impl.ProductActiveServiceImpl;
import projects.manager.utils.Products.SystemMgr.ActiveMgr;
import projects.manager.vo.ActiveIncomeStatisVo;
import projects.manager.vo.LoginUserInfo;


@Controller
@RequestMapping("/manager/activeIncome")
public class ActiveIncomeController extends AbstractBaseController implements DateInitBinder {

    @Autowired
    private ProductActiveServiceImpl productActiveServiceImpl;

    @ByPermissionClass(ActiveMgr.ActiveIncomeView.class)
    @RequestMapping(value="statis", method=RequestMethod.GET)
    public ModelAndView statis(EasyDataGrid<ActiveIncomeStatisVo> easyPage){
        return responsePageOrData("/manager/active-income-statis", ()->{
        			Page<ActiveIncomeStatisVo> page = Page.create(easyPage.getPage(), easyPage.getPageSize());
        			LoginUserInfo loginUser = getCurrentLoginUser(LoginUserInfo.class);
        			
                    productActiveServiceImpl.statisActiveIncomeByGroup(page, loginUser);
                    EasyDataGrid<ActiveIncomeStatisVo> epage = EasyDataGrid.create(page);
                    epage.addFooter(productActiveServiceImpl.statisActiveIncomeTotal(loginUser));
                    return epage;
                });
    }
}