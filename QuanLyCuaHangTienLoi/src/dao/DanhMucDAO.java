package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connectSQL.KetNoiSQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DanhMucDAO {
    private Connection conn;
    
    public DanhMucDAO() {
        conn = KetNoiSQL.getInstance().getConnection();
    }
    
    // Lấy tất cả danh mục
    public ArrayList<String> getAllDanhMuc() {
        ArrayList<String> dsDanhMuc = new ArrayList<>();
        String sql = "SELECT TenDM FROM DanhMuc ORDER BY MaDM";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            dsDanhMuc.add("Tất cả");  // Thêm option mặc định
            while(rs.next()) {
                dsDanhMuc.add(rs.getString("TenDM"));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return dsDanhMuc;
    }
    
    // Lấy tên danh mục theo mã
    public String getTenDanhMuc(String maDM) {
        String sql = "SELECT TenDM FROM DanhMuc WHERE MaDM = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, maDM);
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()) {
                return rs.getString("TenDM");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Lấy mã danh mục theo tên
    public String getMaDanhMuc(String tenDM) {
        String sql = "SELECT MaDM FROM DanhMuc WHERE TenDM = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, tenDM);
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()) {
                return rs.getString("MaDM");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Lấy HashMap chứa cặp Mã-Tên danh mục
    public HashMap<String, String> getDanhMucMap() {
        HashMap<String, String> map = new HashMap<>();
        String sql = "SELECT MaDM, TenDM FROM DanhMuc";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                map.put(rs.getString("MaDM"), rs.getString("TenDM"));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return map;
    }
    
    // Thêm danh mục mới
    public boolean add(String maDM, String tenDM) {
        String sql = "INSERT INTO DanhMuc VALUES (?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, maDM);
            ps.setString(2, tenDM);
            return ps.executeUpdate() > 0;
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Sửa tên danh mục
    public boolean update(String maDM, String tenDMMoi) {
        String sql = "UPDATE DanhMuc SET TenDM = ? WHERE MaDM = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, tenDMMoi);
            ps.setString(2, maDM);
            return ps.executeUpdate() > 0;
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Xóa danh mục
    public boolean delete(String maDM) {
        String sql = "DELETE FROM DanhMuc WHERE MaDM = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, maDM);
            return ps.executeUpdate() > 0;
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Kiểm tra danh mục tồn tại
    public boolean exists(String maDM) {
        String sql = "SELECT COUNT(*) FROM DanhMuc WHERE MaDM = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, maDM);
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Tự động sinh mã danh mục mới
    public String generateNewId() {
        String sql = "SELECT TOP 1 MaDM FROM DanhMuc ORDER BY MaDM DESC";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()) {
                String lastId = rs.getString("MaDM");
                int number = Integer.parseInt(lastId.substring(2)) + 1;
                return String.format("DM%02d", number);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return "DM01";
    }
}
