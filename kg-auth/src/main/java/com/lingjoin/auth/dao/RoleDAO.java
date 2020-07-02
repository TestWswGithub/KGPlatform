package com.lingjoin.auth.dao;

import com.lingjoin.auth.entity.Role;
import com.lingjoin.auth.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleDAO {


    /**
     * 角色下拉框  用户权限之内所能分配的角色。
     * @param uid
     * @return
     */
    List<Role> selectUsersRole(Integer uid);

    void deleteRole(Integer roleId);

    List<User> selectRoleUser(Integer roleId);


    Integer insertRole(Role role);

    void updateRoleName(@Param("roleId") Integer roleId, @Param("roleName") String roleName);

    String selectRoleName(Integer roleId);


}
