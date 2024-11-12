package dao;

import entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connectSQL.KetNoiSQL; // Lớp kết nối SQL bạn đã có

public class UserDAO {

    private Connection connection;

    // Constructor để khởi tạo kết nối từ lớp KetNoiSQL
    public UserDAO() {
        connection = KetNoiSQL.getInstance().getConnection(); // Sử dụng phương thức getConnection() từ KetNoiSQL
    }

    // Phương thức lấy thông tin tài khoản từ cơ sở dữ liệu theo username
    public User getUserByUsername(String username) {
        String sql = "SELECT TaiKhoan.TenDangNhap, TaiKhoan.MatKhau, NhanVien.MaNV, NhanVien.HoTen, NhanVien.GioiTinh "
                   + "FROM TaiKhoan "
                   + "INNER JOIN NhanVien ON TaiKhoan.MaNV = NhanVien.MaNV "
                   + "WHERE TaiKhoan.TenDangNhap = ?"; // Truy vấn lấy thông tin người dùng theo username từ bảng TaiKhoan và NhanVien
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username); // Gán giá trị cho username
            ResultSet resultSet = preparedStatement.executeQuery(); // Thực thi câu truy vấn

            if (resultSet.next()) {
                // Nếu tìm thấy tài khoản, trả về đối tượng User
                User user = new User(); // Khởi tạo đối tượng User
                user.setUsername(resultSet.getString("TenDangNhap"));
                user.setPassword(resultSet.getString("MatKhau"));
                user.setMaNV(resultSet.getString("MaNV"));
                user.setHoTen(resultSet.getString("HoTen"));
                user.setGioiTinh(resultSet.getString("GioiTinh"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // In ra lỗi nếu có
        }
        return null; // Trả về null nếu không tìm thấy tài khoản
    }

    // Phương thức kiểm tra thông tin đăng nhập (so sánh username và password)
    public boolean authenticate(User user) {
        User dbUser = getUserByUsername(user.getUsername()); // Lấy tài khoản từ cơ sở dữ liệu
        if (dbUser != null) {
            return dbUser.getPassword().equals(user.getPassword()); // Kiểm tra mật khẩu
        }
        return false; // Nếu không tìm thấy tài khoản hoặc mật khẩu không đúng
    }
}
