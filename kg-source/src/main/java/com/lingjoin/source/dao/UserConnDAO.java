package com.lingjoin.source.dao;

import com.lingjoin.source.entity.UsersConn;

import java.util.List;

public interface UserConnDAO {

    Integer insert(UsersConn usersConn);

    Integer batchInsert(List<UsersConn> list);


}
