package connectSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class KetNoiSQL {
	private String connectionString;
	private static KetNoiSQL instance;
	private Connection connection;

	public KetNoiSQL() {
		// Chuỗi kết nối sử dụng Windows Authentication
		String url = "jdbc:sqlserver://DESKTOP-RE9R3MQ\\VI;databaseName=QuanLyCuaHangTienLoi; trustServerCertificate=true;integratedSecurity=true;";
		try {

			connection = DriverManager.getConnection(url);
			System.out.println("Danh nhap thanh cong");
		} catch (SQLException e1) {
			System.out.println("Dang nhap that bai");
			e1.printStackTrace();
		}
	}

	public synchronized static KetNoiSQL getInstance() {
		if (instance == null)
			instance = new KetNoiSQL();
		return instance;
	}

	public Connection getConnection() {
		return connection;
	}

}