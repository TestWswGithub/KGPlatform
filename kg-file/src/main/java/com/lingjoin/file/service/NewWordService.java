package com.lingjoin.file.service;



import com.lingjoin.file.entity.NewWord;

import java.util.Date;
import java.util.List;

public interface NewWordService {


    Integer batchSave(List<NewWord> list);

    List<NewWord> nwRank(List<String> uuids);
    List<NewWord> listNewWordPage(Integer start,
                                  Integer pageSize,
                                  Date startDate,
                                  Date endDate,
                                  Integer uid,
                                  String time,
                                  String likeName);
    Integer totalCount(Date startDate,
                       Date endDate,
                       Integer uid,
                       String time,
                       String likeName);


}
