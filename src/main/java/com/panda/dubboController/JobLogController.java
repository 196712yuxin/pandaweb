package com.panda.dubboController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.panda.common.utils.poi.ExcelUtil;
import com.panda.framework.aspectj.lang.annotation.Log;
import com.panda.framework.aspectj.lang.enums.BusinessType;
import com.panda.framework.web.controller.BaseController;
import com.panda.framework.web.domain.AjaxResult;
import com.panda.framework.web.page.TableDataInfo;
import com.panda.project.dubboService.IJobLogDubboService;
import com.panda.project.monitor.job.domain.JobLog;
import com.panda.project.monitor.job.service.IJobLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 调度日志操作处理
 * 
 * @author panda
 */
@Controller
@RequestMapping("/monitor/jobLog")
public class JobLogController extends BaseController
{
    private String prefix = "monitor/job";

    @Reference(version = "0.0.0")
    private IJobLogDubboService jobLogService;

    @RequiresPermissions("monitor:job:view")
    @GetMapping()
    public String jobLog()
    {
        return prefix + "/jobLog";
    }

    @RequiresPermissions("monitor:job:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(JobLog jobLog)
    {
        startPage();
        List<JobLog> list = jobLogService.selectJobLogList(jobLog);
        return getDataTable(list);
    }

    @Log(title = "调度日志", businessType = BusinessType.EXPORT)
    @RequiresPermissions("monitor:job:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(JobLog jobLog)
    {
        List<JobLog> list = jobLogService.selectJobLogList(jobLog);
        ExcelUtil<JobLog> util = new ExcelUtil<JobLog>(JobLog.class);
        return util.exportExcel(list, "jobLog");
    }

    @Log(title = "调度日志", businessType = BusinessType.DELETE)
    @RequiresPermissions("monitor:job:remove")
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(jobLogService.deleteJobLogByIds(ids));
    }
    
    @Log(title = "调度日志", businessType = BusinessType.CLEAN)
    @RequiresPermissions("monitor:job:remove")
    @PostMapping("/clean")
    @ResponseBody
    public AjaxResult clean()
    {
        jobLogService.cleanJobLog();
        return success();
    }
}
