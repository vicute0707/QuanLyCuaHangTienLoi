package entity;

public class Kho {
    private int maKho;
    private String tenKho;

    public Kho(int maKho, String tenKho) {
        this.maKho = maKho;
        this.tenKho = tenKho;
    }

    public Kho() {
    }

    public int getMaKho() {
        return maKho;
    }

    public void setMaKho(int maKho) {
        this.maKho = maKho;
    }

    public String getTenKho() {
        return tenKho;
    }

    public void setTenKho(String tenKho) {
        this.tenKho = tenKho;
    }
}
