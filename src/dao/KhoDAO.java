package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import connectSQL.KetNoiSQL;
import entity.Kho;

public class KhoDAO {
    private Connection connection;
    
    
    
   

    public KhoDAO() {
        // Lấy connection từ KetNoiSQL
        this.connection = KetNoiSQL.getInstance().getConnection();
    }
    private void initializeConnection() {
        if (this.connection == null) { // Kiểm tra nếu connection chưa được khởi tạo
            try {
                // Thay đổi URL, username, password theo thông tin của bạn
                this.connection = DriverManager.getConnection("jdbc:your_database_url", "username", "password");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

	public List<Kho> getAllKho() throws SQLException {
        List<Kho> khoList = new ArrayList<>();
        String query = "SELECT * FROM Kho";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        while (rs.next()) {
            int maKho = rs.getInt("MaKho");
            String tenKho = rs.getString("TenKho");
            khoList.add(new Kho(maKho, tenKho));
        }

        return khoList;
    }

    public void addKho(Kho kho) throws SQLException {
        String query = "INSERT INTO Kho (TenKho) VALUES (?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, kho.getTenKho());
        statement.executeUpdate();
    }

    public void updateKho(Kho kho) throws SQLException {
        String query = "UPDATE Kho SET TenKho = ? WHERE MaKho = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, kho.getTenKho());
        statement.setInt(2, kho.getMaKho());
        statement.executeUpdate();
    }
    public void deleteKho(int maKho) throws SQLException {
        // 1. Xóa các sản phẩm liên quan đến kho
        String deleteProductsSQL = "DELETE FROM SanPham WHERE MaKho = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteProductsSQL)) {
            stmt.setInt(1, maKho);
            stmt.executeUpdate();
        }

        // 2. Xóa kho sau khi các sản phẩm đã được xóa
        String deleteKhoSQL = "DELETE FROM Kho WHERE MaKho = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteKhoSQL)) {
            stmt.setInt(1, maKho);
            stmt.executeUpdate();
        }
    }

        }
 
