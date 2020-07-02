package com.lingjoin.auth.dao;

import com.lingjoin.auth.entity.Operator;
import com.lingjoin.auth.entity.RoleOperaMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OperaDAO {


    Integer roleOperaBatchInsert(List<RoleOperaMap> list);
    List<Operator> selectUsersOpera(Integer uid);

    List<Integer> selectRoleOpera(Integer roleId);

    void batchDelete(@Param("roleId") Integer roleId,@Param("list") List<Integer> list);



}
