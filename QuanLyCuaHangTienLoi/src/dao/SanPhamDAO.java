package dao;

import entity.SanPham;
import connectSQL.KetNoiSQL;

import java.sql.*;
import java.util.ArrayList;


	public class SanPhamDAO {
        private Connection conn;
        
        public SanPhamDAO() {
            conn = KetNoiSQL.getInstance().getConnection();
        }
        
        public ArrayList<SanPham> getList() {
            ArrayList<SanPham> list = new ArrayList<>();
            String sql = """
                SELECT * FROM SanPham
            """;
            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                
                while(rs.next()) {
                    SanPham sp = new SanPham();
                    sp.setMaSP(rs.getString("MaSP"));
                    sp.setTenSP(rs.getString("TenSP")); 
                    sp.setMaDM(rs.getString("MaDM"));
                   // Set tên danh mục
                    sp.setGiaBan(rs.getDouble("GiaBan"));
                    sp.setSoLuong(rs.getInt("SoLuong"));
                    sp.setDonVi(rs.getString("DonVi")); 
                    sp.setHinhAnh(rs.getString("HinhAnh"));
                    sp.setTrangThai(rs.getString("TrangThai"));
                    list.add(sp);
                }
            } catch(SQLException e) {
                e.printStackTrace();
            }
            return list;
        }
        public SanPham getById(String maSP) {
            SanPham sp = null;
            String sql = "SELECT sp.*, dm.TenDM FROM SanPham sp " +
                        "JOIN DanhMuc dm ON sp.MaDM = dm.MaDM " +
                        "WHERE MaSP = ?";
                        
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, maSP);
                ResultSet rs = ps.executeQuery();
                
                if(rs.next()) {
                    sp = new SanPham();
                    sp.setMaSP(rs.getString("MaSP"));
                    sp.setTenSP(rs.getString("TenSP"));
                    sp.setMaDM(rs.getString("MaDM")); 
                    sp.setTenDM(rs.getString("TenDM"));
                    sp.setGiaBan(rs.getDouble("GiaBan"));
                    sp.setSoLuong(rs.getInt("SoLuong"));
                    sp.setDonVi(rs.getString("DonVi")); 
                    sp.setHinhAnh(rs.getString("HinhAnh"));
                    sp.setTrangThai(rs.getString("TrangThai"));
                }
            } catch(SQLException e) {
                e.printStackTrace();
            }
            return sp;
        }
        public ArrayList<SanPham> getByCategory(String tenDM) {
            ArrayList<SanPham> list = new ArrayList<>();
            String sql = """
                SELECT sp.*, dm.TenDM 
                FROM SanPham sp 
                JOIN DanhMuc dm ON sp.MaDM = dm.MaDM
            """;
            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, tenDM);
                
                ResultSet rs = ps.executeQuery();
                while(rs.next()) {
                    SanPham sp = new SanPham();
                    sp.setMaSP(rs.getString("MaSP"));
                    sp.setTenSP(rs.getString("TenSP"));
                    sp.setMaDM(rs.getString("MaDM"));
                    sp.setTenDM(rs.getString("TenDM")); // Set tên danh mục
                    sp.setGiaBan(rs.getDouble("GiaBan"));
                    sp.setSoLuong(rs.getInt("SoLuong"));
                    sp.setDonVi(rs.getString("DonVi"));
                    sp.setHinhAnh(rs.getString("HinhAnh"));
                    sp.setTrangThai(rs.getString("TrangThai"));
                    list.add(sp);
                }
            } catch(SQLException e) {
                e.printStackTrace();
            }
            return list;
        }
        
        // Thêm sản phẩm mới

        public boolean add(SanPham sp) {
            String sql = "INSERT INTO SanPham(MaSP, TenSP, MaDM, GiaBan, SoLuong, DonVi, HinhAnh, TrangThai) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, sp.getMaSP());
                ps.setString(2, sp.getTenSP());
                ps.setString(3, sp.getMaDM()); // Lấy mã danh mục từ tên
                ps.setDouble(4, sp.getGiaBan());
                ps.setInt(5, sp.getSoLuong());
                ps.setString(6, sp.getDonVi());
                ps.setString(7, sp.getHinhAnh());
                ps.setString(8, sp.getTrangThai());
                
                return ps.executeUpdate() > 0;
            } catch(SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        
        public boolean update(SanPham sp) {  
            String sql = "UPDATE SanPham SET TenSP = ?, MaDM = ?, GiaBan = ?, SoLuong = ?, DonVi = ?, HinhAnh = ?, TrangThai = ? WHERE MaSP = ?";  
            try (
                 PreparedStatement preparedStatement = conn.prepareStatement(sql)) {  
                
                // Gán giá trị cho các tham số  
                preparedStatement.setString(1, sp.getTenSP());  
                preparedStatement.setString(2, sp.getMaDM());  
                preparedStatement.setDouble(3, sp.getGiaBan());  
                preparedStatement.setInt(4, sp.getSoLuong());  
                preparedStatement.setString(5, sp.getDonVi());  
                preparedStatement.setString(6, sp.getHinhAnh());  
                preparedStatement.setString(7, sp.getTrangThai());  
                preparedStatement.setString(8, sp.getMaSP());  
                
                // Thực hiện cập nhật  
                return preparedStatement.executeUpdate() > 0;  
                
            } catch (SQLException e) {  
                e.printStackTrace();  
                return false;  
            }  
        }
        
        // Xóa sản phẩm
        public boolean delete(String maSP) {
            String sql = "DELETE FROM SanPham WHERE MaSP=?";
            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, maSP);
                return ps.executeUpdate() > 0;
            } catch(SQLException e) {
                e.printStackTrace();
            }
            return false;
        }
        
        // Tìm kiếm sản phẩm
        public ArrayList<SanPham> search(String keyword) {
            ArrayList<SanPham> list = new ArrayList<>();
            String sql = """
                SELECT sp.*, dm.TenDM 
                FROM SanPham sp 
                JOIN DanhMuc dm ON sp.MaDM = dm.MaDM
                WHERE sp.TenSP LIKE ? 
                OR dm.TenDM LIKE ?
            """;
            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, "%" + keyword + "%");
                ps.setString(2, "%" + keyword + "%");
                
                ResultSet rs = ps.executeQuery();
                while(rs.next()) {
                    SanPham sp = new SanPham();
                    sp.setMaSP(rs.getString("MaSP"));
                    sp.setTenSP(rs.getString("TenSP"));
                    sp.setMaDM(rs.getString("MaDM")); 
                    sp.setTenDM(rs.getString("TenDM")); // Set tên danh mục
                    sp.setGiaBan(rs.getDouble("GiaBan"));
                    sp.setSoLuong(rs.getInt("SoLuong"));
                    sp.setDonVi(rs.getString("DonVi"));
                    sp.setHinhAnh(rs.getString("HinhAnh"));
                    sp.setTrangThai(rs.getString("TrangThai"));
                    list.add(sp);
                }
            } catch(SQLException e) {
                e.printStackTrace();
            }
            return list;
        }
        
 
        
        // Lấy mã danh mục từ tên
        private String getMaDM(String tenDM) {
            String maDM = null;
            String sql = "SELECT MaDM FROM DanhMuc WHERE TenDM = ?";
            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, tenDM);
                ResultSet rs = ps.executeQuery();
                if(rs.next()) {
                    maDM = rs.getString("MaDM");
                }
            } catch(SQLException e) {
                e.printStackTrace();
            }
            return maDM;
        }
        
        // Tự động tạo mã sản phẩm mới
        public String generateNewId() {
            String sql = "SELECT MAX(CAST(SUBSTRING(MaSP, 3, LEN(MaSP)) AS INT)) AS MaxID FROM SanPham";
            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                if(rs.next()) {
                    int maxId = rs.getInt("MaxID");
                    return String.format("SP%03d", maxId + 1);
                }
            } catch(SQLException e) {
                e.printStackTrace();
            }
            return "SP001";
        }
        
        // Cập nhật trạng thái theo số lượng
        public void updateStatus(String maSP) {
            String sql = """
                UPDATE SanPham SET TrangThai = 
                CASE 
                    WHEN SoLuong = 0 THEN N'Hết hàng'
                    WHEN SoLuong <= 10 THEN N'Sắp hết'
                    ELSE N'Còn hàng'
                END
                WHERE MaSP = ?
            """;
            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, maSP);
                ps.executeUpdate();
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
    }
