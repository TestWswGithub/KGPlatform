package com.lingjoin.graph.dao;

import com.lingjoin.graph.entity.Tuple;

import java.util.List;

public interface TupleDAO {

    Integer batchInsert(List<Tuple> list);


    List<Tuple> tuplePage(Integer flag);

    List<Tuple> selectBySource(String source);


    Integer totalEntries();
    List<Tuple> selectNodeSource(String nodeName);
    List<Tuple> tuples2SingleDoc(String uuid);
    List<Tuple> relativeInfo(String nodeName);
    Tuple nodeDataSrc(String nodeName);


}
