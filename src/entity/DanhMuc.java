package entity;

public class DanhMuc {
	private String maDM;
	private String tenDM;
	public String getMaDM() {
		return maDM;
	}
	public void setMaDM(String maDM) {
		this.maDM = maDM;
	}
	public String getTenDM() {
		return tenDM;
	}
	public void setTenDM(String tenDM) {
		this.tenDM = tenDM;
	}
	public DanhMuc(String maDM, String tenDM) {
		super();
		this.maDM = maDM;
		this.tenDM = tenDM;
	}
	public DanhMuc() {
		super();
		// TODO Auto-generated constructor stub
	}
	

}
