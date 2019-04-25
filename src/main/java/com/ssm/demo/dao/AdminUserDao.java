package com.ssm.demo.dao;

import com.ssm.demo.entity.AdminUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AdminUserDao {

    /**
     * 根据参数查询用户列表
     *
     * @param param
     * @return
     */
    List<AdminUser> findAdminUsers(Map param);

    /**
     * 查询用户总数
     *
     * @param param
     * @return
     */
    int getTotalAdminUser(Map param);

    /**
     * 根据登录名和密码获取用户记录
     * @param userName
     * @param passwordMD5
     * @return
     */
    AdminUser getAdminUserByUserNameAndPassword(@Param("userName")String userName,@Param("passwordMD5")String passwordMD5);
    /**
     * 根据userToken获取用户记录
     *
     * @return
     */
    AdminUser getAdminUserByToken(String userToken);

    /**
     * 更新用户token值
     *
     * @param userId
     * @param newToken
     * @return
     */
    int updateUserToken(@Param("userId") Long userId, @Param("newToken") String newToken);
}
