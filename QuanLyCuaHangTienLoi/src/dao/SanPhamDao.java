package dao;

import entity.SanPham;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

import Connect.DatabaseConnection;

public class SanPhamDao {
    private Connection connection;

    // Constructor kết nối với cơ sở dữ liệu
    public SanPhamDao() throws SQLException {
        this.connection = DatabaseConnection.getConnection(); // Giả sử bạn đã có lớp DatabaseConnection
    }

    // Phương thức thêm một sản phẩm mới
    public void createSanPham(SanPham sanPham) {
        String query = "INSERT INTO sanpham (maSP, hinhAnh, tenSP, danhMuc, giaBan, tonKho, donVi, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, sanPham.getMaSP());
            statement.setBytes(2, convertImageToBytes(sanPham.getHinhAnh())); // Chuyển đổi hình ảnh thành mảng byte
            statement.setString(3, sanPham.getTenSP());
            statement.setString(4, sanPham.getDanhMuc());
            statement.setString(5, sanPham.getGiaBan());
            statement.setInt(6, sanPham.getTonKho());
            statement.setString(7, sanPham.getDonVi());
            statement.setString(8, sanPham.getTrangThai());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Phương thức lấy thông tin sản phẩm theo mã sản phẩm
    public SanPham getSanPhamByMaSP(String maSP) {
        String query = "SELECT * FROM sanpham WHERE maSP = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, maSP);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new SanPham(
                    resultSet.getString("maSP"),
                    new ImageIcon(resultSet.getBytes("hinhAnh")), // Chuyển đổi mảng byte thành ImageIcon
                    resultSet.getString("tenSP"),
                    resultSet.getString("danhMuc"),
                    resultSet.getString("giaBan"),
                    resultSet.getInt("tonKho"),
                    resultSet.getString("donVi"),
                    resultSet.getString("trangThai")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Phương thức cập nhật thông tin sản phẩm
    public void updateSanPham(SanPham sanPham) {
        String query = "UPDATE sanpham SET hinhAnh = ?, tenSP = ?, danhMuc = ?, giaBan = ?, tonKho = ?, donVi = ?, trangThai = ? WHERE maSP = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setBytes(1, convertImageToBytes(sanPham.getHinhAnh())); // Chuyển đổi hình ảnh thành mảng byte
            statement.setString(2, sanPham.getTenSP());
            statement.setString(3, sanPham.getDanhMuc());
            statement.setString(4, sanPham.getGiaBan());
            statement.setInt(5, sanPham.getTonKho());
            statement.setString(6, sanPham.getDonVi());
            statement.setString(7, sanPham.getTrangThai());
            statement.setString(8, sanPham.getMaSP());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Phương thức xóa sản phẩm theo mã sản phẩm
    public void deleteSanPham(String maSP) {
        String query = "DELETE FROM sanpham WHERE maSP = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, maSP);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Phương thức lấy tất cả sản phẩm
    public List<SanPham> getAllSanPham() {
        List<SanPham> sanPhamList = new ArrayList<>();
        String query = "SELECT * FROM sanpham";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                sanPhamList.add(new SanPham(
                    resultSet.getString("maSP"),
                    new ImageIcon(resultSet.getBytes("hinhAnh")),
                    resultSet.getString("tenSP"),
                    resultSet.getString("danhMuc"),
                    resultSet.getString("giaBan"),
                    resultSet.getInt("tonKho"),
                    resultSet.getString("donVi"),
                    resultSet.getString("trangThai")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sanPhamList;
    }
    public List<SanPham> getProductsByCategory(String category) {
        List<SanPham> products = new ArrayList<>();
        String query = "SELECT * FROM sanpham WHERE danhMuc = ? LIMIT 12";

        // Adjust query for 'Tất cả' to retrieve all products regardless of category
        if (category.equals("Tất cả")) {
            query = "SELECT * FROM sanpham LIMIT 12";
        }

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            if (!category.equals("Tất cả")) {
                statement.setString(1, category);
            }
            
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                SanPham product = new SanPham(
                    resultSet.getString("maSP"),
                    new ImageIcon(resultSet.getBytes("hinhAnh")), // Convert byte array to ImageIcon
                    resultSet.getString("tenSP"),
                    resultSet.getString("danhMuc"),
                    resultSet.getString("giaBan"),
                    resultSet.getInt("tonKho"),
                    resultSet.getString("donVi"),
                    resultSet.getString("trangThai")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    // Helper method to convert ImageIcon to byte array
    private byte[] convertImageToBytes(ImageIcon imageIcon) {
        if (imageIcon != null) {
            java.awt.Image image = imageIcon.getImage();
            java.io.ByteArrayOutputStream byteArrayOutputStream = new java.io.ByteArrayOutputStream();
            try {
                javax.imageio.ImageIO.write((java.awt.image.BufferedImage) image, "png", byteArrayOutputStream);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            return byteArrayOutputStream.toByteArray();
        }
        return null;
    }
}
