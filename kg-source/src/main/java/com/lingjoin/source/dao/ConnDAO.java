package com.lingjoin.source.dao;

import com.lingjoin.source.entity.Connection;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ConnDAO {

    Integer insert(Connection connection);


    Connection selectBycon(@Param("database") String database,
                           @Param("table") String table,
                           @Param("field") String field,
                           @Param("conn") String conn);

    List<Connection> selectUsersConns(@Param("start") Integer start,
                                      @Param("pageSize") Integer pageSize,
                                      @Param("startDate") Date startDate,
                                      @Param("endDate") Date endDate,
                                      @Param("uid")Integer uid,
                                      @Param("type")String type);
    Integer usersConnsTotalCount(@Param("startDate") Date startDate,
                         @Param("endDate") Date endDate,
                         @Param("uid")Integer uid,
                         @Param("type")String type);

    List<Connection> selectUsersAllCorpusConns(Integer uid);
    Connection selectConnById(Integer id);
    List<Connection> selectAllConns();
    List<Connection> selectUsersAllKnowledgeConns(Integer uid);

    Connection selectByIpPortDatabaseTableConType(@Param("host") String host,
                                                  @Param("port") Integer port,
                                                  @Param("database") String database,
                                                  @Param("table") String table,
                                                  @Param("connType") String connType);


}
