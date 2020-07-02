package com.lingjoin.auth.service.impl;

import com.lingjoin.auth.dao.RoleDAO;
import com.lingjoin.auth.entity.Role;
import com.lingjoin.auth.entity.User;
import com.lingjoin.auth.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {


    @Autowired
    private RoleDAO roleDAO;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public List<Role> listUsersRole(Integer uid) {
        return roleDAO.selectUsersRole(uid);
    }

    @Override
    @Transactional
    public Integer addRole(Role role) {
        return roleDAO.insertRole(role);
    }

    @Override
    @Transactional
    public void deleteRole(Integer roleId) {
        roleDAO.deleteRole(roleId);
    }

    @Override
    @Transactional
    public void updateRoleName(Integer roleId, String roleName) {
        roleDAO.updateRoleName(roleId,roleName);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public String selectRoleName(Integer roleId) {
        return roleDAO.selectRoleName(roleId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public List<User> roleHasUsers(Integer roleId) {
        return roleDAO.selectRoleUser(roleId);
    }
}
