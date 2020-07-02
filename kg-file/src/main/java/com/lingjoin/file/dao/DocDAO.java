package com.lingjoin.file.dao;

import com.lingjoin.file.entity.Doc;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface DocDAO {

    Integer batchInsert(List<Doc> list);

    Integer insert(Doc doc);


    Doc selectByUUID(@Param("uid") Integer uid, @Param("uuid") String uuid);



    List<Doc> selectDocPage(@Param("start") Integer start,
                            @Param("pageSize") Integer pageSize,
                            @Param("startDate") Date startDate,
                            @Param("endDate") Date endDate,
                            @Param("status") String status,
                            @Param("uid") Integer uid,
                            @Param("docName") String docName);
    Integer totalCount(@Param("start") Integer start,
                       @Param("pageSize") Integer pageSize,
                       @Param("startDate") Date startDate,
                       @Param("endDate") Date endDate,
                       @Param("status") String status,
                       @Param("uid") Integer uid,
                       @Param("docName") String docName);

    void delete(Integer id);
    void batchDelete(List ids);

}
