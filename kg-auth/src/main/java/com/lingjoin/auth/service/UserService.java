package com.lingjoin.auth.service;

import com.lingjoin.auth.entity.Dept;
import com.lingjoin.auth.entity.User;

import java.util.List;

public interface UserService {


    /**
     *
     *
     *  新增角色
     *  只有拥有创建角色权限才能创建
     *
     *
     * @param user
     * @return
     */
    Integer save(User user);

    /**
     *
     * 查看用户名是否存在
     *
     *
     * @param account
     * @return
     */

    boolean exist(String account);

    /**
     *
     * 目前还未加盐// todo 需要加盐
     *
     * @param account
     * @param password
     * @return
     */
    User login(String account,String password);
    List<Dept> selectAllDept();
    List<User> selectDeptManagers();
    List<User>selectSubUsers(Integer deptId);

    /**
     *
     * 展示用户所管理的下属（即拥有创建账号权限的用户所创建的子账号）
     *
     * @param creatorId
     * @return
     */
    List<User>listManagedUsers(Integer creatorId);

    /**
     *
     * 展示所有用户，一级管理员也就是系统自带的超级管理员账号拥有此权限。
     *
     * @return
     */
    List<User>listAllUsers();
    void deleteUser(Integer uid);
    User selectByUid(Integer uid);
    void updateUser(User user);


}
