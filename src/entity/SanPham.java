package entity;

public class SanPham {
	private String maSP;
	private String tenSP;
	private String maDM;
	private String tenDM;
	private double giaBan;
	private int soLuong;
	private String donVi;
	private String hinhAnh;
	private String trangThai;
	private int maKho;
	public String getMaSP() {
		return maSP;
	}
	
	public SanPham(String maSP, String tenSP, String maDM, String tenDM, double giaBan, int maKho,int soLuong, String donVi,
			String hinhAnh, String trangThai) {
		super();
		this.maSP = maSP;
		this.tenSP = tenSP;
		this.maDM = maDM;
		this.tenDM = tenDM;
		this.giaBan = giaBan;
		this.maKho = maKho;
		this.soLuong = soLuong;
		
		this.donVi = donVi;
		this.hinhAnh = hinhAnh;
		this.trangThai = trangThai;
		
	}

	public int getMaKho() {
		return maKho;
	}

	public void setMaKho(int maKho) {
		this.maKho = maKho;
	}

	public void setMaSP(String maSP) {
		this.maSP = maSP;
	}
	public String getTenSP() {
		return tenSP;
	}
	public void setTenSP(String tenSP) {
		this.tenSP = tenSP;
	}
	public String getTenDM() {
		return tenDM;
	}
	public void setTenDM(String maDM) {
		this.tenDM = tenDM;
	}
	public double getGiaBan() {
		return giaBan;
	}
	public void setGiaBan(double giaBan) {
		this.giaBan = giaBan;
	}
	public int getSoLuong() {
		return soLuong;
	}
	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}
	public String getDonVi() {
		return donVi;
	}
	public void setDonVi(String donVi) {
		this.donVi = donVi;
	}
	public String getHinhAnh() {
		return hinhAnh;
	}
	public void setHinhAnh(String hinhAnh) {
		this.hinhAnh = hinhAnh;
	}
	public String getTrangThai() {
		return trangThai;
	}
	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}
	

	public String getMaDM() {
		return maDM;
	}

	public void setMaDM(String maDM) {
		this.maDM = maDM;
	}

	public SanPham() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
