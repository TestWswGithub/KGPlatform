package com.lingjoin.file.service.impl;


import com.lingjoin.file.dao.NewWordDAO;
import com.lingjoin.file.entity.NewWord;
import com.lingjoin.file.service.NewWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class NewWordService_impl implements NewWordService {
    @Autowired
    private NewWordDAO newWordDAO;

    @Override
    public List<NewWord> listNewWordPage(Integer start, Integer pageSize, Date startDate, Date endDate, Integer uid, String time, String likeName) {
        return newWordDAO.selectNWPage(start,pageSize,startDate,endDate,uid,time,likeName);
    }
    @Override
    public Integer totalCount(Date startDate, Date endDate, Integer uid, String time,String likeName) {
        return newWordDAO.totalCount(startDate,endDate,uid,time,likeName);
    }
    @Override
    public Integer batchSave(List<NewWord> list) {
        return newWordDAO.batchInsert(list);
    }

    @Override
    public List<NewWord> nwRank(List<String> uuids) {
        return newWordDAO.nwRank(uuids);
    }


}
