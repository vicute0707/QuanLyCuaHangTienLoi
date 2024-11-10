package dao;

import entity.BanHangEntity;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Connect.DatabaseConnection;

public class BanHangDao {
    private Connection connection;

    // Constructor kết nối với cơ sở dữ liệu
    public BanHangDao() throws SQLException {
        this.connection = DatabaseConnection.getConnection(); // Giả sử bạn đã có lớp DatabaseConnection
    }

    // Phương thức thêm một giao dịch bán hàng mới
    public void createBanHang(BanHangEntity banHang) {
        String query = "INSERT INTO banhang (staffName, customerName, customerPhone, customerAddress, date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, banHang.getStaffName());
            statement.setString(2, banHang.getCustomerName());
            statement.setString(3, banHang.getCustomerPhone());
            statement.setString(4, banHang.getCustomerAddress());
            statement.setDate(5, new java.sql.Date(banHang.getDate().getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Phương thức lấy thông tin giao dịch bán hàng theo ID (giả sử có ID trong bảng)
    public BanHangEntity getBanHangById(int id) {
        String query = "SELECT * FROM banhang WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new BanHangEntity(
                    resultSet.getString("staffName"),
                    resultSet.getString("customerName"),
                    resultSet.getString("customerPhone"),
                    resultSet.getString("customerAddress"),
                    resultSet.getDate("date")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Phương thức cập nhật thông tin giao dịch bán hàng
    public void updateBanHang(BanHangEntity banHang) {
        String query = "UPDATE banhang SET staffName = ?, customerName = ?, customerPhone = ?, customerAddress = ?, date = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, banHang.getStaffName());
            statement.setString(2, banHang.getCustomerName());
            statement.setString(3, banHang.getCustomerPhone());
            statement.setString(4, banHang.getCustomerAddress());
            statement.setDate(5, new java.sql.Date(banHang.getDate().getTime()));
            statement.setString(6, banHang.getId()); 
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Phương thức xóa giao dịch bán hàng
    public void deleteBanHang(int id) {
        String query = "DELETE FROM banhang WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Phương thức lấy tất cả giao dịch bán hàng
    public List<BanHangEntity> getAllBanHang() {
        List<BanHangEntity> banHangList = new ArrayList<>();
        String query = "SELECT * FROM banhang";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                banHangList.add(new BanHangEntity(
                    resultSet.getString("staffName"),
                    resultSet.getString("customerName"),
                    resultSet.getString("customerPhone"),
                    resultSet.getString("customerAddress"),
                    resultSet.getDate("date")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return banHangList;
    }
}
