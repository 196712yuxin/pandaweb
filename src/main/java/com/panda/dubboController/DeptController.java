package com.panda.dubboController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.panda.common.utils.StringUtils;
import com.panda.framework.aspectj.lang.annotation.Log;
import com.panda.framework.aspectj.lang.enums.BusinessType;
import com.panda.framework.web.controller.BaseController;
import com.panda.framework.web.domain.AjaxResult;
import com.panda.project.dubboService.IDeptDubboService;
import com.panda.project.system.dept.domain.Dept;
import com.panda.project.system.dept.service.IDeptService;
import com.panda.project.system.role.domain.Role;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 部门信息
 * 
 * @author panda
 */
@Controller
@RequestMapping("/system/dept")
public class DeptController extends BaseController
{
    private String prefix = "system/dept";

    @Reference(version = "0.0.0")
    private IDeptDubboService deptService;

    @RequiresPermissions("system:dept:view")
    @GetMapping()
    public String dept()
    {
        return prefix + "/dept";
    }

    @RequiresPermissions("system:dept:list")
    @GetMapping("/list")
    @ResponseBody
    public List<Dept> list(Dept dept)
    {
        List<Dept> deptList = deptService.selectDeptList(dept);
        return deptList;
    }

    /**
     * 新增部门
     */
    @GetMapping("/add/{parentId}")
    public String add(@PathVariable("parentId") Long parentId, ModelMap mmap)
    {
        mmap.put("dept", deptService.selectDeptById(parentId));
        return prefix + "/add";
    }

    /**
     * 新增保存部门
     */
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @RequiresPermissions("system:dept:add")
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Dept dept)
    {
        return toAjax(deptService.insertDept(dept));
    }

    /**
     * 修改
     */
    @GetMapping("/edit/{deptId}")
    public String edit(@PathVariable("deptId") Long deptId, ModelMap mmap)
    {
        Dept dept = deptService.selectDeptById(deptId);
        if (StringUtils.isNotNull(dept) && 100L == deptId)
        {
            dept.setParentName("无");
        }
        mmap.put("dept", dept);
        return prefix + "/edit";
    }

    /**
     * 保存
     */
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:dept:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Dept dept)
    {
        return toAjax(deptService.updateDept(dept));
    }

    /**
     * 删除
     */
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("system:dept:remove")
    @PostMapping("/remove/{deptId}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("deptId") Long deptId)
    {
        if (deptService.selectDeptCount(deptId) > 0)
        {
            return error(1, "存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId))
        {
            return error(1, "部门存在用户,不允许删除");
        }
        return toAjax(deptService.deleteDeptById(deptId));
    }

    /**
     * 校验部门名称
     */
    @PostMapping("/checkDeptNameUnique")
    @ResponseBody
    public String checkDeptNameUnique(Dept dept)
    {
        return deptService.checkDeptNameUnique(dept);
    }

    /**
     * 选择部门树
     */
    @GetMapping("/selectDeptTree/{deptId}")
    public String selectDeptTree(@PathVariable("deptId") Long deptId, ModelMap mmap)
    {
        mmap.put("dept", deptService.selectDeptById(deptId));
        return prefix + "/tree";
    }

    /**
     * 加载部门列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Map<String, Object>> treeData()
    {
        List<Map<String, Object>> tree = deptService.selectDeptTree();
        return tree;
    }

    /**
     * 加载角色部门（数据权限）列表树
     */
    @GetMapping("/roleDeptTreeData")
    @ResponseBody
    public List<Map<String, Object>> deptTreeData(Role role)
    {
        List<Map<String, Object>> tree = deptService.roleDeptTreeData(role);
        return tree;
    }
}
