package com.lingjoin.file.service.impl;



import com.lingjoin.file.dao.DocDAO;
import com.lingjoin.file.entity.Doc;
import com.lingjoin.file.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class DocService_impl implements DocService {

    @Autowired
    private DocDAO docDAO;


    @Override
    @Transactional
    public Integer batchSave(List<Doc> list) {
        return docDAO.batchInsert(list);
    }

    @Override
    public Integer save(Doc doc) {
        return docDAO.insert(doc);
    }

    @Override
    @Transactional(readOnly = true)
    public Doc queryByUUID(Integer uid, String uuid) {
        return docDAO.selectByUUID(uid,uuid);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Doc> listDocPage(Integer start, Integer pageSize, Date startDate, Date endDate, String status, Integer uid, String docName) {
        return docDAO.selectDocPage(start,pageSize,startDate,endDate,status,uid,docName);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer totalCount(Integer start, Integer pageSize, Date startDate, Date endDate, String status, Integer uid, String docName) {
        return docDAO.totalCount(start,pageSize,startDate,endDate,status,uid,docName);
    }

    @Override
    @Transactional
    public void delDoc(Integer id) {

        docDAO.delete(id);

    }

    @Override
    @Transactional
    public void batchDelDoc(List list) {
        docDAO.batchDelete(list);
    }
}
