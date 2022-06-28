/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emart.dao;

import emart.dbutil.DBConnection;
import emart.pojo.ProductsPojo;
import emart.pojo.UserProfile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Mausam Maheshkar
 */
public class OrderDAO {
    public static String getNextOrderId()throws SQLException
    {
        Connection conn = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("Select max(order_id) from orders");
        rs.next();
        String orderId = rs.getString(1);
        if(orderId==null)
            return "O-101";
        int orderNo = Integer.parseInt(orderId.substring(2));
        orderNo++;
        return "O-"+orderNo;
    }
    
    public static boolean addOrder(ArrayList <ProductsPojo> al,String ordId)throws SQLException
    {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("insert into orders values(?,?,?,?)");
        int count = 0;
        for(ProductsPojo p:al)
        {
            ps.setString(1, ordId);
            ps.setString(2, p.getProductId());
            ps.setInt(3, p.getQuantity());
            ps.setString(4, UserProfile.getUserid());
            count = count + ps.executeUpdate();
        }
        return count == al.size();
    }
    
    public static Set<String> getAllOrderId()throws SQLException
    {
        Connection conn = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select order_id from orders order by order_id");
        Set <String> ordAllId = new HashSet<>();
        while(rs.next())
        {
            ordAllId.add(rs.getString(1));
        }
        return ordAllId;
    }
    
    public static List<ProductsPojo> getOrderDetails(String orderId)throws SQLException
    {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("select p_id,quantity from orders where order_id=?");
        ps.setString(1, orderId);
        ResultSet rs = ps.executeQuery();
        
        List<ProductsPojo> productsList = new ArrayList<>();
        while(rs.next())
        {
            ProductsPojo p = new ProductsPojo();
            p.setProductId(rs.getString(1));
            p.setQuantity(rs.getInt(2));
            productsList.add(p);
        }
        return productsList;
    }
}
