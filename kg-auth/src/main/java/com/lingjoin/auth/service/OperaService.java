package com.lingjoin.auth.service;

import com.lingjoin.auth.entity.Operator;
import com.lingjoin.auth.entity.RoleOperaMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OperaService {

    /**
     *
     * 展示登录的用户对应权限所拥有的操作
     *
     * @param uid
     * @return
     */
    List<Operator> listUsersOpera(Integer uid);

    /**
     *
     * 展示对应角色权限下已经拥有的操作
     *
     */

    List<Integer> listRoleOperaIds(Integer roleId);


    Integer roleOperaBatchInsert(List<RoleOperaMap> list);


    void batchDelete(Integer roleId,List<Integer> list);

}
