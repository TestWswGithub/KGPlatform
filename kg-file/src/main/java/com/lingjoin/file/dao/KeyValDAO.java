package com.lingjoin.file.dao;

import com.lingjoin.file.entity.KeyVal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface KeyValDAO {
    Integer batchInsert(List<KeyVal> list);

    List<KeyVal> keyValPage(@Param("start") Integer start,
                            @Param("pageSize") Integer pageSize,
                            @Param("uid") Integer uid,
                            @Param("uuid") String uuid,
                            @Param("likeName") String likeName);

    Integer totalCount(@Param("uid") Integer uid,
                       @Param("uuid") String uuid,
                       @Param("likeName") String likeName);

    List<KeyVal> cdkv(@Param("start") Integer start,
                      @Param("pageSize") Integer pageSize,
                      @Param("uid") Integer uid,
                      @Param("uuid") String uuid,
                      @Param("likeName") String likeName);


    List<KeyVal> eqandcn(List<String> uuids);


}
