package com.ssm.demo.service;

import com.ssm.demo.entity.AdminUser;

public interface AdminUserService {
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
