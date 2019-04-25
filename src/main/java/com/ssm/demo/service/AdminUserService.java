package com.ssm.demo.service;

import com.ssm.demo.entity.AdminUser;
import com.ssm.demo.utiles.PageResult;
import com.ssm.demo.utiles.PageUtil;

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
}
