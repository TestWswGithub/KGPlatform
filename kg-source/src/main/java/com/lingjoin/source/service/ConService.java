package com.lingjoin.source.service;


import com.lingjoin.common.util.Page;
import com.lingjoin.source.entity.Connection;
import com.lingjoin.source.entity.CorpusEntry;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ConService {

    boolean testMysqlCon(String ip, String port, String database, String username, String password);

    Integer saveConn(Connection connection,Integer uid);

    boolean exist(String database, String table, String field, String conn);

    List<Connection> selectUsersConns(Integer start,
                                      Integer pageSize,
                                      Date startDate,
                                      Date endDate,
                                      Integer uid,
                                      String type);

    Integer usersConnsTotalCount(Date startDate,
                                 Date endDate,
                                 Integer uid,
                                 String type);

    List<Connection> selectUsersAllCorpusConns(Integer uid);

    Connection selectConnById(Integer id);


    Page listCorpusEntries(Integer conid, Integer pageNum, Integer pageSize);

    List<Connection> selectUsersAllKnowledgeConns(Integer uid);

    Page listKnowledgeEntries(Integer conid, Integer pageNum, Integer pageSize);

    String getCropus(Integer conid,Integer rowId);

    boolean exist(String host,
                  Integer port,
                  String database,
                  String table,
                  String connType);

}
