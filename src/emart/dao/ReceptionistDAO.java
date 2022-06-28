/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emart.dao;

import emart.dbutil.DBConnection;
import emart.pojo.ReceptionistPojo;
import emart.pojo.UserPojo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Mausam Maheshkar
 */
public class ReceptionistDAO {
    public static Map<String,String> getNonRegisteredReceptionists()throws SQLException
    {
        Connection conn = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select empid,empname from employees where job='Receptionist' and empid not in (select empid from users where usertype='Receptionist')");
        HashMap <String,String> receptionistList = new HashMap<>();
        while(rs.next())
        {
            String id = rs.getString(1);
            String name = rs.getString(2);
            receptionistList.put(id, name);
        }
        return receptionistList;
    }
    
    public static boolean addReceptionist(UserPojo u)throws SQLException
    {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("insert into users values(?,?,?,?,?)");
        ps.setString(1, u.getUserid());
        ps.setString(2, u.getEmpid());
        ps.setString(3, u.getPassword());
        ps.setString(4, u.getUsertype());
        ps.setString(5, u.getUsername());
        int result = ps.executeUpdate();
        return result==1;
    }
    
    public static List<ReceptionistPojo > getAllReceptionistDetails()throws SQLException
    {
        Connection conn = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select users.empid,empname,userid,job,salary from users,employees where usertype='Receptionist' and users.empid = employees.empid");
        ArrayList<ReceptionistPojo> al = new ArrayList<>();
        while(rs.next())
        {
            ReceptionistPojo recep = new ReceptionistPojo();
            recep.setEmpid(rs.getString(1));
            recep.setEmpname(rs.getString(2));
            recep.setUserid(rs.getString(3));
            recep.setJob(rs.getString(4));
            recep.setSalary(rs.getDouble(5));
            al.add(recep);
        }
        return al;
    }
    
    public static Map<String,String> getAllReceptionistId()throws SQLException
    {
        Connection conn = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select userid,username from users where usertype='Receptionist' order by userid ");
        HashMap<String,String> receptionistList = new HashMap<>();
        while(rs.next())
        {
            String id = rs.getString(1);
            String name = rs.getString(2);
            receptionistList.put(id, name);
        }
        return receptionistList;
    }
    
    public static boolean updatePassword(String userid,String password)throws SQLException
    {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("update users set password=? where userid=?");
        ps.setString(1, password);
        ps.setString(2, userid);
        
        int result = ps.executeUpdate();
        return result==1;
    }
    
    public static List<String> getAllReceptionistUserId()throws SQLException
    {
        Connection conn  = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("Select userid from users where usertype='Receptionist'");
        List <String> receptionistUserId = new ArrayList<>();
        while(rs.next())
        {
            receptionistUserId.add(rs.getString(1));
        }
        return receptionistUserId;
    }
    
    public static boolean deleteReceptionist(String userid)throws SQLException
    {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("delete from users where userid=?");
        ps.setString(1, userid);
        int x = ps.executeUpdate();
        return x==1;
    }
    
    public static Set<String> getAllReceptionistOrderId(String userId)throws SQLException
    {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("select order_id from orders where userid=?");
        ps.setString(1, userId);
        ResultSet rs = ps.executeQuery();
        
        Set<String> ordIdSet = new HashSet<>();
        while(rs.next())
        {
            ordIdSet.add(rs.getString(1));
        }
        return ordIdSet;
    }
}
