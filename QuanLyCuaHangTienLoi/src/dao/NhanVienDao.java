package dao;

import entity.NhanVien;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Connect.DatabaseConnection;

public class NhanVienDao {
    private Connection connection;

    // Constructor kết nối với cơ sở dữ liệu
    public NhanVienDao() throws SQLException {
        this.connection = DatabaseConnection.getConnection(); // Giả sử bạn đã có lớp DatabaseConnection
    }

    // Phương thức thêm một nhân viên mới
    public void createNhanVien(NhanVien nhanVien) {
        String query = "INSERT INTO nhanvien (maNV, hoTen, gioiTinh, ngaySinh, soDienThoai, diaChi, chucVu) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nhanVien.getMaNV());
            statement.setString(2, nhanVien.getHoTen());
            statement.setString(3, nhanVien.getGioiTinh());
            statement.setDate(4, new java.sql.Date(nhanVien.getNgaySinh().getTime()));
            statement.setString(5, nhanVien.getSoDienThoai());
            statement.setString(6, nhanVien.getDiaChi());
            statement.setString(7, nhanVien.getChucVu());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Phương thức lấy thông tin nhân viên theo mã nhân viên
    public NhanVien getNhanVienByMaNV(String maNV) {
        String query = "SELECT * FROM nhanvien WHERE maNV = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, maNV);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new NhanVien(
                    resultSet.getString("maNV"),
                    resultSet.getString("hoTen"),
                    resultSet.getString("gioiTinh"),
                    resultSet.getDate("ngaySinh"),
                    resultSet.getString("soDienThoai"),
                    resultSet.getString("diaChi"),
                    resultSet.getString("chucVu")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Phương thức cập nhật thông tin nhân viên
    public void updateNhanVien(NhanVien nhanVien) {
        String query = "UPDATE nhanvien SET hoTen = ?, gioiTinh = ?, ngaySinh = ?, soDienThoai = ?, diaChi = ?, chucVu = ? WHERE maNV = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nhanVien.getHoTen());
            statement.setString(2, nhanVien.getGioiTinh());
            statement.setDate(3, new java.sql.Date(nhanVien.getNgaySinh().getTime()));
            statement.setString(4, nhanVien.getSoDienThoai());
            statement.setString(5, nhanVien.getDiaChi());
            statement.setString(6, nhanVien.getChucVu());
            statement.setString(7, nhanVien.getMaNV());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Phương thức xóa nhân viên theo mã nhân viên
    public void deleteNhanVien(String maNV) {
        String query = "DELETE FROM nhanvien WHERE maNV = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, maNV);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Phương thức lấy tất cả nhân viên
    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> nhanVienList = new ArrayList<>();
        String query = "SELECT * FROM nhanvien";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                nhanVienList.add(new NhanVien(
                    resultSet.getString("maNV"),
                    resultSet.getString("hoTen"),
                    resultSet.getString("gioiTinh"),
                    resultSet.getDate("ngaySinh"),
                    resultSet.getString("soDienThoai"),
                    resultSet.getString("diaChi"),
                    resultSet.getString("chucVu")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nhanVienList;
    }
}
