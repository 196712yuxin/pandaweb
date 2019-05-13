package com.panda.dubboController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.panda.framework.config.PandaConfig;
import com.panda.framework.web.controller.BaseController;
import com.panda.project.dubboService.IMenuDubboService;
import com.panda.project.system.menu.domain.Menu;
import com.panda.project.system.menu.service.IMenuService;
import com.panda.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 首页 业务处理
 * 
 * @author panda
 */
@Controller
public class IndexController extends BaseController
{
    @Reference(version = "0.0.0")
    private IMenuDubboService menuService;

    @Autowired
    private PandaConfig pandaConfig;

    // 系统首页
    @GetMapping("/index")
    public String index(ModelMap mmap)
    {
        // 取身份信息
        User user = getSysUser();
        // 根据用户id取出菜单
        List<Menu> menus = menuService.selectMenusByUser(user);
        mmap.put("menus", menus);
        mmap.put("user", user);
        mmap.put("copyrightYear", pandaConfig.getCopyrightYear());
        return "index";
    }

    // 系统介绍
    @GetMapping("/system/main")
    public String main(ModelMap mmap)
    {
        mmap.put("version", pandaConfig.getVersion());
        return "main";
    }
}
