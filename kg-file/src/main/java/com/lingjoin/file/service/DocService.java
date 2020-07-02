package com.lingjoin.file.service;



import com.lingjoin.file.entity.Doc;

import java.util.Date;
import java.util.List;

public interface DocService {


    Integer batchSave(List<Doc> list);

    Integer save(Doc doc);

    Doc queryByUUID(Integer uid, String uuid);

    List<Doc> listDocPage(Integer start,
                          Integer pageSize,
                          Date startDate,
                          Date endDate,
                          String status,
                          Integer uid,
                          String docName);

    Integer totalCount(Integer start,
                       Integer pageSize,
                       Date startDate,
                       Date endDate,
                       String status,
                       Integer uid,
                       String docName);


    void delDoc(Integer id);
    void batchDelDoc(List list);


}
