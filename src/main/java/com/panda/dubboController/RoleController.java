package com.panda.dubboController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.panda.common.utils.poi.ExcelUtil;
import com.panda.framework.aspectj.lang.annotation.Log;
import com.panda.framework.aspectj.lang.enums.BusinessType;
import com.panda.framework.web.controller.BaseController;
import com.panda.framework.web.domain.AjaxResult;
import com.panda.framework.web.page.TableDataInfo;
import com.panda.project.dubboService.IRoleDubboService;
import com.panda.project.system.role.domain.Role;
import com.panda.project.system.role.service.IRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色信息
 * 
 * @author panda
 */
@Controller
@RequestMapping("/system/role")
public class RoleController extends BaseController
{
    private String prefix = "system/role";

    @Reference(version = "0.0.0")
    private IRoleDubboService roleService;

    @RequiresPermissions("system:role:view")
    @GetMapping()
    public String role()
    {
        return prefix + "/role";
    }

    @RequiresPermissions("system:role:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Role role)
    {
        startPage();
        List<Role> list = roleService.selectRoleList(role);
        return getDataTable(list);
    }

    @Log(title = "角色管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:role:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Role role)
    {
        List<Role> list = roleService.selectRoleList(role);
        ExcelUtil<Role> util = new ExcelUtil<Role>(Role.class);
        return util.exportExcel(list, "role");
    }

    /**
     * 新增角色
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存角色
     */
    @RequiresPermissions("system:role:add")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public AjaxResult addSave(Role role)
    {
        return toAjax(roleService.insertRole(role));

    }

    /**
     * 修改角色
     */
    @GetMapping("/edit/{roleId}")
    public String edit(@PathVariable("roleId") Long roleId, ModelMap mmap)
    {
        mmap.put("role", roleService.selectRoleById(roleId));
        return prefix + "/edit";
    }

    /**
     * 修改保存角色
     */
    @RequiresPermissions("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public AjaxResult editSave(Role role)
    {
        return toAjax(roleService.updateRole(role));
    }

    /**
     * 新增数据权限
     */
    @GetMapping("/rule/{roleId}")
    public String rule(@PathVariable("roleId") Long roleId, ModelMap mmap)
    {
        mmap.put("role", roleService.selectRoleById(roleId));
        return prefix + "/rule";
    }

    /**
     * 修改保存数据权限
     */
    @RequiresPermissions("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PostMapping("/rule")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public AjaxResult ruleSave(Role role)
    {
        return toAjax(roleService.updateRule(role));
    }

    @RequiresPermissions("system:role:remove")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        try
        {
            return toAjax(roleService.deleteRoleByIds(ids));
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }

    /**
     * 校验角色名称
     */
    @PostMapping("/checkRoleNameUnique")
    @ResponseBody
    public String checkRoleNameUnique(Role role)
    {
        return roleService.checkRoleNameUnique(role);
    }

    /**
     * 校验角色权限
     */
    @PostMapping("/checkRoleKeyUnique")
    @ResponseBody
    public String checkRoleKeyUnique(Role role)
    {
        return roleService.checkRoleKeyUnique(role);
    }

    /**
     * 选择菜单树
     */
    @GetMapping("/selectMenuTree")
    public String selectMenuTree()
    {
        return prefix + "/tree";
    }
}