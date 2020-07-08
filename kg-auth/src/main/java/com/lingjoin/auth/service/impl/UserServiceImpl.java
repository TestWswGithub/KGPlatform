package com.lingjoin.auth.service.impl;

import com.lingjoin.auth.dao.UserDAO;
import com.lingjoin.auth.entity.Dept;
import com.lingjoin.auth.entity.User;
import com.lingjoin.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {



    @Autowired
    private UserDAO userDAO;

    @Override
    @Transactional
    public Integer save(User user) {
        return userDAO.insert(user);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public boolean exist(String account) {
        return userDAO.selectByUsername(account)!=null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public User login(String account, String password) {
        return userDAO.selectByAccPwd(account,password);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public List<Dept> selectAllDept() {
        return userDAO.selectAllDept();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public List<User> selectDeptManagers() {
        return userDAO.selectDeptManagers();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public List<User> selectSubUsers(Integer deptId) {
        return userDAO.selectSubUsers(deptId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public List<User> listManagedUsers(Integer creatorId) {
        return userDAO.selectManagedUsers(creatorId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public List<User> listAllUsers() {
        return userDAO.selectAllUsers();
    }

    @Override
    @Transactional
    public void deleteUser(Integer uid) {
        userDAO.deleteUser(uid);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public User selectByUid(Integer uid) {
        return userDAO.selectByUid(uid);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        userDAO.updateUser(user);
    }

}
