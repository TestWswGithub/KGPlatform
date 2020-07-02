package com.lingjoin.source.service.impl;

import com.lingjoin.source.dao.UserConnDAO;
import com.lingjoin.source.entity.UsersConn;
import com.lingjoin.source.service.UsersConnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UsersConnService_impl implements UsersConnService {
    @Autowired
    private UserConnDAO userConnDAO;


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Integer save(UsersConn usersConn) {
        return userConnDAO.insert(usersConn);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Integer batchSave(List<UsersConn> list) {
        return userConnDAO.batchInsert(list);
    }
}
