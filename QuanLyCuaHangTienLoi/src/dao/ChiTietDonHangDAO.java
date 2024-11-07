package dao;
import java.sql.Connection;  
import java.sql.PreparedStatement;  
import java.sql.ResultSet;  
import java.sql.SQLException;

import connectSQL.KetNoiSQL;  

public class ChiTietDonHangDAO {  
    private Connection conn; // Giả sử bạn đã có kết nối cơ sở dữ liệu  

    // Constructor để khởi tạo kết nối  
    public ChiTietDonHangDAO() {  
        conn = KetNoiSQL.getInstance().getConnection();
    }  

    // Phương thức countByMaSP  
    public int countByMaSP(String maSP) {  
        int count = 0;  
        String sql = "SELECT COUNT(*) FROM ChiTietDonHang WHERE MaSP = ?";  
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {  
            pstmt.setString(1, maSP);  
            ResultSet rs = pstmt.executeQuery();  
            
            if (rs.next()) {  
                count = rs.getInt(1);  
            }  
        } catch (SQLException e) {  
            e.printStackTrace(); // Ghi lại lỗi (nếu có)  
        }  
        
        return count;  
    }  

    // Bạn có thể thêm các phương thức khác tại đây, ví dụ: delete, getAll,...  
}  