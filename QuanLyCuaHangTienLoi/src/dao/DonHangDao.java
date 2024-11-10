package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Connect.DatabaseConnection;
import entity.*;

public class DonHangDao {
    private Connection connection;

    // Constructor kết nối với cơ sở dữ liệu
    public DonHangDao() throws SQLException {
        this.connection = DatabaseConnection.getConnection(); // Giả sử bạn đã có lớp DatabaseConnection
    }

    // Phương thức thêm một đơn hàng mới
    public void createDonHang(DonHangEntity donHang) {
        String query = "INSERT INTO donhang (maDonHang, ngayTao, khachHang, soDienThoai, nhanVien, tongTien, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, donHang.getMaDonHang());
            statement.setDate(2, donHang.getNgayTao());
            statement.setString(3, donHang.getKhachHang());
            statement.setString(4, donHang.getSoDienThoai());
            statement.setString(5, donHang.getNhanVien());
            statement.setDouble(6, donHang.getTongTien());
            statement.setString(7, donHang.getTrangThai());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Phương thức lấy thông tin đơn hàng theo mã đơn hàng
    public DonHangEntity getDonHangByMaDonHang(String maDonHang) {
        String query = "SELECT * FROM donhang WHERE maDonHang = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, maDonHang);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new DonHangEntity(
                    resultSet.getString("maDonHang"),
                    resultSet.getDate("ngayTao"),
                    resultSet.getString("khachHang"),
                    resultSet.getString("soDienThoai"),
                    resultSet.getString("nhanVien"),
                    resultSet.getDouble("tongTien"),
                    resultSet.getString("trangThai")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Phương thức cập nhật thông tin đơn hàng
    public void updateDonHang(DonHangEntity donHang) {
        String query = "UPDATE donhang SET ngayTao = ?, khachHang = ?, soDienThoai = ?, nhanVien = ?, tongTien = ?, trangThai = ? WHERE maDonHang = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, donHang.getNgayTao());
            statement.setString(2, donHang.getKhachHang());
            statement.setString(3, donHang.getSoDienThoai());
            statement.setString(4, donHang.getNhanVien());
            statement.setDouble(5, donHang.getTongTien());
            statement.setString(6, donHang.getTrangThai());
            statement.setString(7, donHang.getMaDonHang());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Phương thức xóa đơn hàng theo mã đơn hàng
    public void deleteDonHang(String maDonHang) {
        String query = "DELETE FROM donhang WHERE maDonHang = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, maDonHang);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Phương thức lấy tất cả đơn hàng
    public List<DonHangEntity> getAllDonHang() {
        List<DonHangEntity> donHangList = new ArrayList<>();
        String query = "SELECT * FROM donhang";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                donHangList.add(new DonHangEntity(
                    resultSet.getString("maDonHang"),
                    resultSet.getDate("ngayTao"),
                    resultSet.getString("khachHang"),
                    resultSet.getString("soDienThoai"),
                    resultSet.getString("nhanVien"),
                    resultSet.getDouble("tongTien"),
                    resultSet.getString("trangThai")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return donHangList;
    }
}
