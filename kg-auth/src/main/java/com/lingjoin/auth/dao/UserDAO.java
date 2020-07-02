package com.lingjoin.auth.dao;

import com.lingjoin.auth.entity.Dept;
import com.lingjoin.auth.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDAO {
    /**
     *
     * 新增角色
     * 只有拥有创建角色权限才能创建
     *
     * @param user
     * @return
     */
    Integer insert(User user);

    /**
     *
     * 通过登录用  //todo 目前还未加盐，
     * @param account
     * @param password
     * @return
     */
    User selectByAccPwd(@Param("account") String account, @Param("password") String password);

    /**
     * 查看用户名是否存在
     *
     * @param account
     * @return
     */
    User selectByUsername(String account);
    List<Dept> selectAllDept();
    List<User> selectDeptManagers();
    List<User>selectSubUsers(Integer deptId);
    List<User>selectManagedUsers(Integer creatorId);
    List<User>selectAllUsers();

}
