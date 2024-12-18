package dao;

import java.sql.*;
import java.util.ArrayList;

import connectSQL.KetNoiSQL;
import entity.NhanVien;

import java.text.SimpleDateFormat;

public class NhanVienDAO {
    private Connection conn;
    
    public NhanVienDAO() {
        conn = KetNoiSQL.getInstance().getConnection();
    }
    
    // Lấy danh sách nhân viên
    public ArrayList<NhanVien> getList() {
        ArrayList<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("MaNV"));
                nv.setHoTen(rs.getString("HoTen"));
                nv.setGioiTinh(rs.getString("GioiTinh"));
                nv.setNgaySinh(rs.getDate("NgaySinh"));
                nv.setSdt(rs.getString("SDT"));
                nv.setDiaChi(rs.getString("DiaChi"));
                nv.setChucVu(rs.getString("ChucVu"));
                list.add(nv);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Thêm nhân viên mới
    public boolean add(NhanVien nv) {
        String sql = "INSERT INTO NhanVien (MaNV, HoTen, GioiTinh, NgaySinh, SDT, DiaChi, ChucVu) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nv.getMaNV());
            ps.setString(2, nv.getHoTen());
            ps.setString(3, nv.getGioiTinh());
            ps.setDate(4, new java.sql.Date(nv.getNgaySinh().getTime()));
            ps.setString(5, nv.getSdt());
            ps.setString(6, nv.getDiaChi());
            ps.setString(7, nv.getChucVu());
            
            return ps.executeUpdate() > 0;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Cập nhật thông tin nhân viên
    public boolean update(NhanVien nv) {
        String sql = "UPDATE NhanVien SET HoTen=?, GioiTinh=?, NgaySinh=?, SDT=?, DiaChi=?, ChucVu=? WHERE MaNV=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nv.getHoTen());
            ps.setString(2, nv.getGioiTinh());
            ps.setDate(3, new java.sql.Date(nv.getNgaySinh().getTime()));
            ps.setString(4, nv.getSdt());
            ps.setString(5, nv.getDiaChi());
            ps.setString(6, nv.getChucVu());
            ps.setString(7, nv.getMaNV());
            
            return ps.executeUpdate() > 0;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Xóa nhân viên
    public boolean delete(String maNV) {
        String sql = "DELETE FROM NhanVien WHERE MaNV=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, maNV);
            return ps.executeUpdate() > 0;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Tìm kiếm nhân viên
    public ArrayList<NhanVien> search(String keyword) {
        ArrayList<NhanVien> list = new ArrayList<>();
        String sql = """
            SELECT * FROM NhanVien 
            WHERE MaNV LIKE ? OR HoTen LIKE ? OR SDT LIKE ? OR DiaChi LIKE ? OR ChucVu LIKE ?
        """;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            String searchKey = "%" + keyword + "%";
            for(int i = 1; i <= 5; i++) {
                ps.setString(i, searchKey);
            }
            
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("MaNV"));
                nv.setHoTen(rs.getString("HoTen"));
                nv.setGioiTinh(rs.getString("GioiTinh"));
                nv.setNgaySinh(rs.getDate("NgaySinh"));
                nv.setSdt(rs.getString("SDT"));
                nv.setDiaChi(rs.getString("DiaChi"));
                nv.setChucVu(rs.getString("ChucVu"));
                list.add(nv);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Tạo mã nhân viên mới
    public String generateNewId() {
        String sql = "SELECT TOP 1 MaNV FROM NhanVien ORDER BY MaNV DESC";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                String lastId = rs.getString("MaNV");
                int number = Integer.parseInt(lastId.substring(2)) + 1;
                return String.format("NV%03d", number);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return "NV001";
    }
}