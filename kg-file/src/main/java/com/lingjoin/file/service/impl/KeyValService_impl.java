package com.lingjoin.file.service.impl;



import com.lingjoin.file.dao.KeyValDAO;
import com.lingjoin.file.entity.KeyVal;
import com.lingjoin.file.service.KeyValService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class KeyValService_impl implements KeyValService {


    @Autowired
    private KeyValDAO keyValDAO;





    @Override
    public Integer batchSave(List<KeyVal> list) {
        return keyValDAO.batchInsert(list);
    }

    @Override
    public List<KeyVal> keyValPage(Integer start, Integer pageSize, Integer uid, String uuid, String likeName) {
        return keyValDAO.keyValPage(start,pageSize,uid,uuid,likeName);
    }

    @Override
    public Integer totalCount(Integer uid,  String uuid, String likeName) {
        return keyValDAO.totalCount(uid,uuid,likeName);
    }

    @Override
    public List<KeyVal> cdkv(Integer start, Integer pageSize, Integer uid, String uuid, String likeName) {
        return keyValDAO.cdkv(start,pageSize,uid,uuid,likeName);
    }

    @Override
    public List<KeyVal> eqandcn(List<String> uuids) {
        return keyValDAO.eqandcn(uuids);
    }
}
