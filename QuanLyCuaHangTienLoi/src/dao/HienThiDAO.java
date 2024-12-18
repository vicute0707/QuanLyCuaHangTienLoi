package dao;

import java.sql.*;
import java.util.*;

import connectSQL.KetNoiSQL;
public class HienThiDAO {
    private Connection conn;
    
    public HienThiDAO() {
        conn = KetNoiSQL.getInstance().getConnection();
    }
    
    public HashMap<String, String[]> getThongKeTrangChu() {
        HashMap<String, String[]> thongKe = new HashMap<>();
        
        try {
            // 1. Thống kê doanh thu
            String sqlDoanhThu = """
                SELECT TOP 2
                    MONTH(NgayTao) as Thang,
                    SUM(TongTien) as DoanhThu
                FROM DonHang 
                WHERE TrangThai = N'Hoàn thành'
                GROUP BY MONTH(NgayTao)
                ORDER BY Thang DESC
            """;
            PreparedStatement psDoanhThu = conn.prepareStatement(sqlDoanhThu);
            ResultSet rsDoanhThu = psDoanhThu.executeQuery();
            
            double doanhThuHienTai = 0;
            double doanhThuTruoc = 0;
            if(rsDoanhThu.next()) {
                doanhThuHienTai = rsDoanhThu.getDouble("DoanhThu");
                if(rsDoanhThu.next()) {
                    doanhThuTruoc = rsDoanhThu.getDouble("DoanhThu");
                }
            }
            
            double phanTramDT = 0;
            if(doanhThuTruoc > 0) {
                phanTramDT = ((doanhThuHienTai - doanhThuTruoc) / doanhThuTruoc) * 100;
            }
            
            String[] dataDoanhThu = {
                String.format("%,.0f", doanhThuHienTai),
                (phanTramDT >= 0 ? "↑ " : "↓ ") + String.format("%.1f", Math.abs(phanTramDT)) + "%"
            };
            thongKe.put("DoanhThu", dataDoanhThu);
            
            // 2. Thống kê đơn hàng
            String sqlDonHang = "SELECT COUNT(MaHD) as TongDon FROM DonHang";
            PreparedStatement psDonHang = conn.prepareStatement(sqlDonHang);
            ResultSet rsDonHang = psDonHang.executeQuery();
            if(rsDonHang.next()) {
                String[] dataDonHang = {
                    rsDonHang.getString("TongDon"),
                    "↑ 8.3%"
                };
                thongKe.put("DonHang", dataDonHang);
            }
            
            // 3. Thống kê nhân viên
            String sqlNhanVien = "SELECT COUNT(MaNV) as TongNV FROM NhanVien";
            PreparedStatement psNhanVien = conn.prepareStatement(sqlNhanVien);
            ResultSet rsNhanVien = psNhanVien.executeQuery();
            if(rsNhanVien.next()) {
                String[] dataNhanVien = {
                    rsNhanVien.getString("TongNV"),
                    "↑ 15.0%"
                };
                thongKe.put("NhanVien", dataNhanVien);
            }
            
            // 4. Thống kê sản phẩm
            String sqlSanPham = "SELECT COUNT(MaSP) as TongSP FROM SanPham";
            PreparedStatement psSanPham = conn.prepareStatement(sqlSanPham);
            ResultSet rsSanPham = psSanPham.executeQuery();
            if(rsSanPham.next()) {
                String[] dataSanPham = {
                    rsSanPham.getString("TongSP"),
                    "↑ 5.0%"
                };
                thongKe.put("SanPham", dataSanPham);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return thongKe;
    }
}