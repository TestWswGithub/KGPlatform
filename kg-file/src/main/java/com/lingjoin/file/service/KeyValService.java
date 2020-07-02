package com.lingjoin.file.service;



import com.lingjoin.file.entity.KeyVal;

import java.util.List;

public interface KeyValService {

    Integer batchSave(List<KeyVal> list);

    List<KeyVal> keyValPage(Integer start,
                            Integer pageSize,
                            Integer uid,

                            String uuid,
                            String likeName);

    Integer totalCount(Integer uid,

                       String uuid,
                       String likeName);

    List<KeyVal> cdkv(Integer start,
                      Integer pageSize,
                      Integer uid,
                      String uuid,
                      String likeName);

    List<KeyVal> eqandcn(List<String> uuids);



}
