package com.lingjoin.file.dao;

import com.lingjoin.file.entity.NewWord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface NewWordDAO {


    Integer batchInsert(List<NewWord> list);

    List<NewWord> nwRank(List<String> uuids);
    List<NewWord> selectNWPage(@Param("start") Integer start,
                               @Param("pageSize") Integer pageSize,
                               @Param("startDate") Date startDate,
                               @Param("endDate") Date endDate,
                               @Param("uid") Integer uid,
                               @Param("time") String time,
                               @Param("likeword") String likeword);

    Integer totalCount(@Param("startDate") Date startDate,
                       @Param("endDate") Date endDate,
                       @Param("uid") Integer uid,
                       @Param("time") String time,
                       @Param("likeword") String likeword);

}
