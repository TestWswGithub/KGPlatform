package com.lingjoin.auth.service;

import com.lingjoin.auth.entity.Role;
import com.lingjoin.auth.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleService {


    /**
     * 角色下拉框  用户权限之内所能分配的角色。
     * @param uid
     * @return
     */
    List<Role> listUsersRole(Integer uid);


    /**
     *
     *新建角色，但是也要把角色与操作进行绑定
     *
     * @param role
     * @return
     */
    Integer addRole(Role role);
    void deleteRole(Integer roleId);
    void updateRoleName( Integer roleId,  String roleName);
    String selectRoleName(Integer roleId);

    List<User>roleHasUsers(Integer roleId);



}
