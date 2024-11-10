package entity;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DonHangEntity {
    private String maDonHang;
    private Date ngayTao;
    private String khachHang;
    private String soDienThoai;
    private String nhanVien;
    private double tongTien;
    private String trangThai;

    public DonHangEntity(String maDonHang, Date ngayTao, String khachHang, String soDienThoai, String nhanVien, double tongTien, String trangThai) {
        this.maDonHang = maDonHang;
        this.ngayTao = ngayTao;
        this.khachHang = khachHang;
        this.soDienThoai = soDienThoai;
        this.nhanVien = nhanVien;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
    }

    // Getters and Setters
    public String getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(String maDonHang) {
        this.maDonHang = maDonHang;
    }

    public Date getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }

    public String getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(String khachHang) {
        this.khachHang = khachHang;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(String nhanVien) {
        this.nhanVien = nhanVien;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    // Method to convert DonHangEntity to Object array for table model
    public Object[] toObjectArray() {
        return new Object[]{maDonHang, ngayTao, khachHang, soDienThoai, nhanVien, tongTien, trangThai};
    }

    // Method to convert List of DonHangEntity to List of Object arrays for table model
    public static List<Object[]> toObjectArrayList(List<DonHangEntity> donHangEntities) {
        List<Object[]> objectArrayList = new ArrayList<>();
        for (DonHangEntity donHangEntity : donHangEntities) {
            objectArrayList.add(donHangEntity.toObjectArray());
        }
        return objectArrayList;
    }
}