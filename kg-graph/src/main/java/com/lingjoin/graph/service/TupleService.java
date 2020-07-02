package com.lingjoin.graph.service;

import com.lingjoin.graph.entity.Tuple;

import java.util.List;

public interface TupleService {

    Integer batchInsert(String jsonResult,Integer uid ,String sources);

    void generateGraphfromKGB(Integer start);
    List<Tuple> selectNodeSource(String nodeName);
    List<Tuple> selectBySource(String source);
    Integer totalEntries();
    List<Tuple> tuples2SingleDoc(String uuid);
    List<Tuple> relativeInfo(String nodeName);
    Tuple nodeDataSrc(String nodeName);

}
