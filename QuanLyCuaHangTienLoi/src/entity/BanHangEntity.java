package entity;

import java.util.Date;

public class BanHangEntity {
    private String staffName;
    private String customerName;
    private String customerPhone;
    private String customerAddress;
    private Date date;
    private String id;
    // Các thuộc tính khác có thể bổ sung tại đây

    public BanHangEntity(String staffName, String customerName, String customerPhone, String customerAddress, Date date) {
        this.staffName = staffName;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerAddress = customerAddress;
        this.date = date;
        this.id = id;
    }

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}