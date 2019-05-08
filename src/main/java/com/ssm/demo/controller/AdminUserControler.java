package com.ssm.demo.controller;

import com.ssm.demo.common.Constants;
import com.ssm.demo.common.Result;
import com.ssm.demo.common.ResultGenerator;
import com.ssm.demo.controller.annotation.TokenToUser;
import com.ssm.demo.entity.AdminUser;
import com.ssm.demo.service.AdminUserService;
import com.ssm.demo.utils.DateUtil;
import com.ssm.demo.utils.FileUtil;
import com.ssm.demo.utils.PageUtil;
import com.ssm.demo.utils.PoiUtil;
import org.apache.commons.collections4.CollectionUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class AdminUserControler {
    @Autowired
    private AdminUserService adminUserService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody AdminUser user) {
        Result result = ResultGenerator.genFailResult("登录失败");
        if (StringUtils.isEmpty(user.getUserName()) || StringUtils.isEmpty(user.getPassword())) {
            result.setMessage("请填写登录信息！");
        }
        AdminUser loginUser = adminUserService.updateTokenAndLogin(user.getUserName(), user.getPassword());
        if (loginUser != null) {
            result = ResultGenerator.genSuccessResult(loginUser);
        }
        return result;
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "参数异常！");
        }
        //查询列表数据
        PageUtil pageUtil = new PageUtil(params);
        return ResultGenerator.genSuccessResult(adminUserService.getAdminUserPage(pageUtil));
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public Result save(@RequestBody AdminUser user, @TokenToUser AdminUser loginUser) {
        if (loginUser == null) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_NOT_LOGIN, "未登录！");
        }
        if (StringUtils.isEmpty(user.getUserName()) || StringUtils.isEmpty(user.getPassword())) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "参数异常！");
        }
        AdminUser tempUser = adminUserService.selectByUserName(user.getUserName());
        if (tempUser != null) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "用户已存在勿重复添加！");
        }
        if ("admin".endsWith(user.getUserName().trim())) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "不能添加admin用户！");
        }
        if (adminUserService.save(user) > 0) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("添加失败");
        }
    }

    /**
     * 修改
     */
    @RequestMapping("/updatePassword")
    public Result update(@RequestBody AdminUser user, @TokenToUser AdminUser loginUser) {
        if (loginUser == null) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_NOT_LOGIN, "未登录！");
        }
        if (StringUtils.isEmpty(user.getPassword())) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "请输入密码！");
        }
        AdminUser tempUser = adminUserService.selectById(user.getId());
        if (tempUser == null) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "无此用户！");
        }
        if ("admin".endsWith(tempUser.getUserName().trim())) {
            return ResultGenerator.genErrorResult(Constants.RESULT_CODE_PARAM_ERROR, "不能修改admin用户！");
        }
        tempUser.setPassword(user.getPassword());
        if (adminUserService.updatePassword(user) > 0) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("添加失败");
        }
    }

    /**
     * 批量导入用户 V1
     *
     * @param multipartFile
     * @return
     */
    @RequestMapping(value = "/importV1", method = RequestMethod.POST)
    public Result saveByExcelFileV1(@RequestParam("file") MultipartFile multipartFile) {
        File file = FileUtil.convertMultipartFileToFile(multipartFile);
        if (file == null) {
            return ResultGenerator.genFailResult("导入失败");
        }
        int importResult = adminUserService.importUsersByExcelFile(file);
        if (importResult > 0) {
            Result result = ResultGenerator.genSuccessResult();
            result.setData(importResult);
            return result;
        } else {
            return ResultGenerator.genFailResult("导入失败");
        }
    }

    /**
     * 批量导入用户V2
     * <p>
     * 批量导入用户(根据文件url导入)
     */
    @RequestMapping(value = "/importV2", method = RequestMethod.POST)
    public Result saveByExcelFileV2(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) {
            return ResultGenerator.genFailResult("fileUrl不能为空");
        }
        File file = FileUtil.downloadFile(fileUrl);
        if (file == null) {
            return ResultGenerator.genFailResult("文件不存在");
        }
        int importResult = adminUserService.importUsersByExcelFile(file);
        if (importResult > 0) {
            Result result = ResultGenerator.genSuccessResult();
            result.setData(importResult);
            return result;
        } else {
            return ResultGenerator.genFailResult("导入失败");
        }
    }

    /**
     * 文件导出
     */
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public void exportUsers(HttpServletRequest request, HttpServletResponse response) {
        List<AdminUser> userList = adminUserService.getUsersForExport();
        //单元格表头
        String[] excelHeader = {"用户id", "用户名", "账号状态", "添加时间"};
        //字段名称
        String[] fileds = {"userId", "userName", "status", "createTime"};
        //单元格宽度内容格式
        int[] formats = {4, 2, 1, 1};
        //单元格宽度
        int[] widths = {256 * 14, 512 * 14, 256 * 14, 512 * 14};
        try {
            List<Map<String, Object>> excelData = new ArrayList<Map<String, Object>>();
            if (CollectionUtils.isNotEmpty(userList)) {
                for (AdminUser user : userList) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("userId", user.getId());
                    map.put("userName", user.getUserName());
                    map.put("status", user.getIsDeleted() == 0 ? "正常账号" : "废弃账号");
                    map.put("createTime", DateUtil.getDateString(user.getCreateTime()));
                    excelData.add(map);
                }
            }
            String excelName = "用户数据_" + System.currentTimeMillis();
            PoiUtil.exportFile(excelName, excelHeader, fileds, formats, widths, excelData, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
