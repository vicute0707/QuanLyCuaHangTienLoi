package entity;

import javax.swing.ImageIcon;

public class SanPham {
    private String maSP;       // Mã sản phẩm
    private ImageIcon hinhAnh; // Hình ảnh sản phẩm
    private String tenSP;      // Tên sản phẩm
    private String danhMuc;    // Danh mục sản phẩm
    private String giaBan;      // Giá bán
    private int tonKho;        // Tồn kho
    private String donVi;      // Đơn vị
    private String trangThai;   // Trạng thái

    // Constructor
    public SanPham(String maSP, ImageIcon hinhAnh, String tenSP, String danhMuc, 
                   String giaBan, int tonKho, String donVi, String trangThai) {
        this.maSP = maSP;
        this.hinhAnh = hinhAnh;
        this.tenSP = tenSP;
        this.danhMuc = danhMuc;
        this.giaBan = giaBan;
        this.tonKho = tonKho;
        this.donVi = donVi;
        this.trangThai = trangThai;
    }

    // Getters and Setters
    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public ImageIcon getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(ImageIcon hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getDanhMuc() {
        return danhMuc;
    }

    public void setDanhMuc(String danhMuc) {
        this.danhMuc = danhMuc;
    }

    public String getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(String giaBan) {
        this.giaBan = giaBan;
    }

    public int getTonKho() {
        return tonKho;
    }

    public void setTonKho(int tonKho) {
        this.tonKho = tonKho;
    }

    public String getDonVi() {
        return donVi;
    }

    public void setDonVi(String donVi) {
        this.donVi = donVi;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}