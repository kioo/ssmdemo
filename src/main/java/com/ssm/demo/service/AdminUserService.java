package com.ssm.demo.service;

import com.ssm.demo.entity.AdminUser;
import com.ssm.demo.utils.PageResult;
import com.ssm.demo.utils.PageUtil;

import java.io.File;
import java.util.List;

public interface AdminUserService {

    PageResult getAdminUserPage(PageUtil pageUtil);
    /**
     * 登陆功能
     *
     * @return
     */
    AdminUser updateTokenAndLogin(String userName, String password);

    /**
     * 根据userToken获取用户记录
     *
     * @return
     */
    AdminUser getAdminUserByToken(String userToken);

    /**
     * 根据用户名获取用户记录
     *
     * @return
     */
    AdminUser selectByUserName(String userName);
    /**
     * 根据id获取用户记录
     *
     * @return
     */
    AdminUser selectById(Long id);
    /**
     * 新增用户记录
     *
     * @return
     */
    int save(AdminUser user);

    /**
     * 修改密码
     *
     * @return
     */
    int updatePassword(AdminUser user);

    int importUsersByExcelFile(File file);

    /**
     * 获取导出数据
     *
     * @return
     */
    List<AdminUser> getUsersForExport();
}
