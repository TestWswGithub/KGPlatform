package com.lingjoin.source.service;


import com.lingjoin.source.entity.UsersConn;

import java.util.List;

public interface UsersConnService {

    Integer save(UsersConn usersConn);
    Integer batchSave(List<UsersConn> list);

}
