package com.lingjoin.web.controller;


import com.alibaba.fastjson.JSON;

import com.lingjoin.auth.entity.Operator;
import com.lingjoin.auth.entity.Role;
import com.lingjoin.auth.entity.RoleOperaMap;
import com.lingjoin.auth.entity.User;
import com.lingjoin.auth.service.OperaService;
import com.lingjoin.auth.service.RoleService;
import com.lingjoin.auth.service.UserService;
import com.lingjoin.common.anonation.LoginRequired;
import com.lingjoin.common.util.JWTUtil;
import com.lingjoin.common.util.ReturnModel;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RequestMapping(value = "/auth", produces = "application/json;charset=UTF-8")
@RestController
//跨域问题仅仅是联调测试时出现的问题，在联调测试完成后，应该删掉此注解
@CrossOrigin(origins = "null",allowCredentials = "true")
public class AuthController {
    //redis中用户token的key的前缀    key应该为 前缀+用户id   例：usertokenuid2  usertokenuid1
    private static final String REDIS_TOKEN_KEY_PREFIX = "usertokenuid";

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    @Autowired
    private OperaService operaService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public String login(String account, String password, HttpServletResponse response) {
        logger.info("account:" + account);

        //todo 目前密码还未进行加密，后期需要替换，还需要加盐
        logger.info("password:" + password);


        ReturnModel model = new ReturnModel(null, "", 0);
        User loginUser = userService.login(account, password);

        if (loginUser == null) {
            model.setMessage("请检查用户名密码是否正确");
            model.setData(account);
            model.setStatus(1);
            return JSON.toJSONString(model);
        }

        logger.info("loginuser:" + loginUser);

        //登录成功之后生成用户密钥
        String secret = UUID.randomUUID().toString();
        logger.info("用户私钥：" + secret);
        String userTokenKey = REDIS_TOKEN_KEY_PREFIX + loginUser.getId();
        //将此密钥存储子redis中
        stringRedisTemplate.opsForValue().set(userTokenKey, secret, 60L, TimeUnit.MINUTES);
        //根据密钥以及用户生成token
        String authToken = JWTUtil.generalTokenWithDefaultAlg(loginUser, secret);

        logger.info("authtoken:" + authToken);
        //响应头添加Authorization属性
        response.addHeader("Authorization", authToken);

        model.setData(authToken);
        model.setMessage("登陆成功");
        return JSON.toJSONString(model);

    }

    /**
     *
     * 展示用户所拥有的权限内的 可以进行的操作
     * 每个用户不用
     * 例如超级管理员可以进行的权限是所有的operator
     * 有的角色只有查看的权限
     * 有的角色有创建账号和删除账号的权限
     *
     * @return
     */
    @RequestMapping("/operaList")
    public String operaList(HttpServletRequest request){
        ReturnModel model = new ReturnModel(null, "", 0);
//        String token = request.getHeader("Authorization");
//        String[] split = token.split("\\.");
//        User user = JSON.parseObject(new String(Base64.decodeBase64(split[1])), User.class);
//        Integer uid = user.getId();
        Integer uid = 1;
        List<Operator> operators = operaService.listUsersOpera(uid);
        model.setData(operators);
        return JSON.toJSONString(model);
    }


    @RequestMapping("/role_has_opers")
    public String roleHasOpers(HttpServletRequest request,Integer roleId){

        ReturnModel model = new ReturnModel(null, "", 0);
//        String token = request.getHeader("Authorization");
//        String[] split = token.split("\\.");
//        User user = JSON.parseObject(new String(Base64.decodeBase64(split[1])), User.class);
//        Integer uid = user.getId();
        Integer uid = 1;
        //todo 验证登录用户是否有修改权限
        HashMap<String, Object> dataMap = new HashMap<>();
        List<Operator> operators = operaService.listUsersOpera(uid);
        dataMap.put("allOpers",operators);
        List<Integer> roleHasOpersIds = operaService.listRoleOperaIds(roleId);
        dataMap.put("roleHasOpersId",roleHasOpersIds);
        String roleName = roleService.selectRoleName(roleId);
        dataMap.put("roleName",roleName);

        model.setData(dataMap);

        return JSON.toJSONString(model);
    }




    @RequestMapping("/updateRole")
    public String updateRole(HttpServletRequest request,String roleName,Integer roleId,@RequestParam("opers[]") Integer[] opers){
        ReturnModel model = new ReturnModel(null, "", 0);
//        String token = request.getHeader("Authorization");
//        String[] split = token.split("\\.");
//        User user = JSON.parseObject(new String(Base64.decodeBase64(split[1])), User.class);
//        Integer uid = user.getId();
        Integer uid = 1;
        //todo 验证登录用户是否有修改权限

        List<Integer> operatorIds = operaService.listRoleOperaIds(roleId);
        List<Integer> postOperas = Arrays.asList(opers);
        ArrayList<Integer> addOpers = new ArrayList<>();
        ArrayList<Integer> minusOpers = new ArrayList<>();


        //由于是复选框修改  先判断减少了哪些操作，再判断增加了哪些操作


        //减少了哪些操作
        for (int i=0;i<operatorIds.size();i++) if (!postOperas.contains(operatorIds.get(i))) minusOpers.add(operatorIds.get(i));

        //增加了哪些操作
        for (int i=0;i<postOperas.size();i++) if (!operatorIds.contains(postOperas.get(i))) addOpers.add(postOperas.get(i));




        if (addOpers.size()!=0){
            ArrayList<RoleOperaMap> addRoleOperaMaps = new ArrayList<>();
            for (int i=0;i<addOpers.size();i++) addRoleOperaMaps.add(new RoleOperaMap(null,roleId,addOpers.get(i)));
            operaService.roleOperaBatchInsert(addRoleOperaMaps);
        }



        if (minusOpers.size()!=0) operaService.batchDelete(roleId,minusOpers);

        roleService.updateRoleName(roleId,roleName);


        model.setMessage("修改成功");
        return JSON.toJSONString(model);


    }













    @RequestMapping("/deleteRole")
    public String deleteRole(HttpServletRequest request,Integer roleId){

        ReturnModel model = new ReturnModel(null, "", 0);
//        String token = request.getHeader("Authorization");
//        String[] split = token.split("\\.");
//        User user = JSON.parseObject(new String(Base64.decodeBase64(split[1])), User.class);
//        Integer uid = user.getId();
        Integer uid = 1;
        //todo 验证登录用户是否有删除权限

        List<User> users = roleService.roleHasUsers(roleId);


        //如果该角色下用户数不为0  则提醒
        if (users.size()!=0){
            model.setMessage("目前该角色用户数为"+users.size()+",请确保该角色下用户数量为0");
            model.setStatus(1);
            return JSON.toJSONString(model);
        }
        roleService.deleteRole(roleId);

        model.setMessage("删除成功");
        return JSON.toJSONString(model);


    }


    /**
     * 添加角色的接口。添加角色的同时要对应存储所被赋予的操作权限
     *
     * @param request
     * @param roleName
     * @param opers
     * @return
     */

    @RequestMapping("/addrole")
    public String addRole(HttpServletRequest request,String roleName,@RequestParam("opers[]") Integer[] opers){
        System.out.println("roleName"+roleName);
        System.out.println(Arrays.toString(opers));
        ReturnModel model = new ReturnModel(null, "", 0);
//        String token = request.getHeader("Authorization");
//        String[] split = token.split("\\.");
//        User user = JSON.parseObject(new String(Base64.decodeBase64(split[1])), User.class);
//        Integer uid = user.getId();
        Integer uid = 1;
        //todo 在真正进行对应的操作之前应该先确认此登录用户是否有进行此操作的权限。
        Role role = new Role(null, roleName, 0, uid);
        Integer integer = roleService.addRole(role);
        System.out.println("integer"+integer);
        if (integer==0) {
            model.setMessage("创建角色失败，请检查数据库");
            model.setStatus(1);
            return JSON.toJSONString(model);
        }
        ArrayList<RoleOperaMap> list = new ArrayList<>();
        for (int i=0;i<opers.length;i++) list.add(new RoleOperaMap(null,role.getId(),opers[i]));
        operaService.roleOperaBatchInsert(list);
        model.setMessage("添加角色成功");
        return JSON.toJSONString(model);
    }




    /**
     *
     * 角色下拉框
     *
     * 展示用户权限范围内所能赋予的角色。
     *
     * @param request
     * @return
     */
    @RequestMapping("/roleList")
    @LoginRequired
    public String usersRole(HttpServletRequest request){
        ReturnModel model = new ReturnModel(null, "", 0);
//        String token = request.getHeader("Authorization");
//        String[] split = token.split("\\.");
//        User user = JSON.parseObject(new String(Base64.decodeBase64(split[1])), User.class);
//        Integer uid = user.getId();
        Integer uid = 1;
        List<Role> roles = roleService.listUsersRole(uid);
        model.setData(roles);
        return JSON.toJSONString(model);
    }


    /**
     *
     * 添加账号接口，注意验证用户是否有添加账号的权限
     *
     * @param request
     * @param name
     * @param account
     * @param password
     * @param userType
     * @return
     */

    @RequestMapping("/adduser")
    @LoginRequired
    public String add(HttpServletRequest request, String name, String account, String password, Integer userType) {
        ReturnModel model = new ReturnModel(null, "", 0);
//        String token = request.getHeader("Authorization");
//        String[] split = token.split("\\.");
//        User parentUser = JSON.parseObject(new String(Base64.decodeBase64(split[1])), User.class);
//        Integer parentUid = parentUser.getId();
        Integer parentUid = 1;
        User user = new User(null, name, account, password, parentUid, userType);
        boolean exist = userService.exist(account);
        if (exist) {
            model.setMessage("用户名已经存在");
            model.setStatus(1);
            model.setData(user);
            return JSON.toJSONString(model);
        }
        userService.save(user);
        model.setMessage("添加成功");
        return JSON.toJSONString(model);
    }







    @RequestMapping("/logout")
    @LoginRequired
    public String logout(HttpServletRequest request) {

        ReturnModel model = new ReturnModel(null, "", 0);
        String secret = UUID.randomUUID().toString();
        String token = request.getHeader("Authorization");
        String[] split = token.split("\\.");
        User user = JSON.parseObject(new String(Base64.decodeBase64(split[1])), User.class);
        String userTokenKey = REDIS_TOKEN_KEY_PREFIX + user.getId();
        stringRedisTemplate.opsForValue().set(userTokenKey, secret, 60L, TimeUnit.MINUTES);
        model.setMessage("登出成功");
        return JSON.toJSONString(model);

    }

}
