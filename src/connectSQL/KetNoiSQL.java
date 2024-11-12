package connectSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class KetNoiSQL {
	private static KetNoiSQL instance;
	private Connection connection;

	public KetNoiSQL() {
		// Chuỗi kết nối sử dụng tài khoản SQL Server Authentication
		String url = "jdbc:sqlserver://localhost;databaseName=QuanLyCuaHangTienLoi1;trustServerCertificate=true;";
		String user = "sa";
		String password = "123123";
		try {
			connection = DriverManager.getConnection(url, user, password);
			System.out.println("Đăng nhập thành công");
		} catch (SQLException e1) {
			System.out.println("Đăng nhập thất bại");
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
