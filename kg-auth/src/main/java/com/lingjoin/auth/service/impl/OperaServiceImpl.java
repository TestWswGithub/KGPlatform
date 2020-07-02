package com.lingjoin.auth.service.impl;

import com.lingjoin.auth.dao.OperaDAO;
import com.lingjoin.auth.entity.Operator;
import com.lingjoin.auth.entity.RoleOperaMap;
import com.lingjoin.auth.service.OperaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.transaction.annotation.Propagation.*;

@Service
public class OperaServiceImpl implements OperaService {
    @Autowired
    private OperaDAO operaDAO;

    @Override
    @Transactional(propagation = SUPPORTS,readOnly = true)
    public List<Operator> listUsersOpera(Integer uid) {
        return operaDAO.selectUsersOpera(uid);
    }

    @Override
    @Transactional(propagation = SUPPORTS,readOnly = true)
    public List<Integer> listRoleOperaIds(Integer roleId) {
        return operaDAO.selectRoleOpera(roleId);
    }

    @Override
    @Transactional
    public Integer roleOperaBatchInsert(List<RoleOperaMap> list) {
        return operaDAO.roleOperaBatchInsert(list);
    }

    @Override
    @Transactional
    public void batchDelete(Integer roleId, List<Integer> list) {
        operaDAO.batchDelete(roleId,list);
    }
}
