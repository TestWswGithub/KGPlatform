package com.lingjoin.source.service.impl;


import com.lingjoin.common.util.Page;
import com.lingjoin.source.dao.ConnDAO;
import com.lingjoin.source.entity.CorpusEntry;
import com.lingjoin.source.entity.KnowledgeEntry;
import com.lingjoin.source.service.ConService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class ConService_impl implements ConService {


    @Autowired
    private ConnDAO connDAO;

    @Override
    public boolean testMysqlCon(String ip, String port, String database, String username, String password) {
        String url = "jdbc:mysql://" + ip + ":" + port + "/" + database + "?serverTimezone=GMT&characterEncoding=utf8&useSSL=false";
        Connection conn = null;

        try {
            //1.加载驱动程序
            Class.forName("com.mysql.cj.jdbc.Driver");
            //2. 获得数据库连接
            conn = DriverManager.getConnection(url, username, password);
            if (conn == null) return false;
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            //节约资源，关闭连接
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public Integer saveConn(com.lingjoin.source.entity.Connection connection) {
        return connDAO.insert(connection);
    }

    @Override
    public boolean exist(String database, String table, String field, String conn) {
        return connDAO.selectBycon(database, table, field, conn) != null;
    }

    @Override
    public List<com.lingjoin.source.entity.Connection> selectUsersConns(Integer start, Integer pageSize, Date startDate, Date endDate, Integer uid, String type) {
        return connDAO.selectUsersConns(start, pageSize, startDate, endDate, uid, type);
    }

    @Override
    public Integer usersConnsTotalCount(Date startDate, Date endDate, Integer uid, String type) {
        return connDAO.usersConnsTotalCount(startDate, endDate, uid, type);
    }

    @Override
    public List<com.lingjoin.source.entity.Connection> selectUsersAllCorpusConns(Integer uid) {
        return connDAO.selectUsersAllCorpusConns(uid);
    }

    @Override
    public com.lingjoin.source.entity.Connection selectConnById(Integer id) {
        return connDAO.selectConnById(id);
    }

    @Override
    public Page listCorpusEntries(Integer conid, Integer pageNum, Integer pageSize) {

        com.lingjoin.source.entity.Connection connection = selectConnById(conid);


        String url = connection.getConn();
        String username = connection.getUser();
        String password = connection.getPassword();
        String table = connection.getTable();
        String field = connection.getField();
        String markField = connection.getMarkField();
        String pageSQL = "select " + markField + "," + field + " from " + table + " limit " + (pageNum - 1) * pageSize + "," + pageSize;
        System.out.println("pageSQL=====>" + pageSQL);
        String countSQL = "select count(*) from " + table;
        System.out.println(countSQL);
        PreparedStatement pstmt = null;
        Connection con = null;
        ResultSet rs = null;
        Page page=null;
        ArrayList<CorpusEntry> corpusEntries = new ArrayList<>();
        try {
            con = DriverManager.getConnection(url, username, password);
            System.out.println("=====>con"+con);
            pstmt = con.prepareStatement(countSQL);
            rs = pstmt.executeQuery();
            System.out.println("===============>"+rs);
            int count= 0;
            while (rs.next()) {
                 count = rs.getInt(1);
            }
            System.out.println("====>count"+count);
            page=new Page(count,pageSize);
            pstmt = con.prepareStatement(pageSQL);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                int i = rs.getInt(1);
                String string = rs.getString(2);
                corpusEntries.add(new CorpusEntry(i, string));
            }
            page.setPageList(corpusEntries);
        } catch (SQLException se) {
            System.out.println("第三方数据库连接失败！");
            se.printStackTrace();
        } finally {
            if (rs != null) {   // 关闭记录集
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pstmt != null) {   // 关闭声明
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {  // 关闭连接对象
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }


        }


        return page;
    }

    @Override
    public Page listKnowledgeEntries(Integer conid, Integer pageNum, Integer pageSize) {

        com.lingjoin.source.entity.Connection connection = selectConnById(conid);


        String url = connection.getConn();
        String username = connection.getUser();
        String password = connection.getPassword();
        String table = connection.getTable();
        String field = connection.getField();
        String headField = connection.getHeadField();
        String relField = connection.getRelField();
        String tailField = connection.getTailField();
        String markField = connection.getMarkField();
        String pageSQL = "select " + markField + "," + headField + "," + relField + "," + tailField + " from " + table + " limit " + (pageNum - 1) * pageSize + "," + pageSize;
        System.out.println("pageSQL=====>" + pageSQL);
        String countSQL = "select count(*) from " + table;
        System.out.println(countSQL);
        PreparedStatement pstmt = null;
        Connection con = null;
        ResultSet rs = null;
        Page page=null;
        ArrayList<KnowledgeEntry> knowledgeEntries = new ArrayList<>();
        try {
            con = DriverManager.getConnection(url, username, password);
            pstmt = con.prepareStatement(countSQL);
            rs = pstmt.executeQuery();
            int count= 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            page=new Page(count,pageSize);

            pstmt = con.prepareStatement(pageSQL);
            rs = pstmt.executeQuery();


            while (rs.next()) {
                int i = rs.getInt(1);
                String head = rs.getString(2);
                String rel = rs.getString(3);
                String tail = rs.getString(4);
                knowledgeEntries.add(new KnowledgeEntry(i, head,rel,tail));
            }
            page.setPageList(knowledgeEntries);
        } catch (SQLException se) {
            System.out.println("第三方数据库连接失败！");
            se.printStackTrace();
        } finally {
            if (rs != null) {   // 关闭记录集
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pstmt != null) {   // 关闭声明
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {  // 关闭连接对象
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }


        }


        return page;
    }


    @Override
    public List<com.lingjoin.source.entity.Connection> selectUsersAllKnowledgeConns(Integer uid) {
        return connDAO.selectUsersAllKnowledgeConns(uid);
    }


}
