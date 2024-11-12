package view;

import dao.UserDAO;
import entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Form_DN extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private UserDAO userDAO;

    public Form_DN() {
        userDAO = new UserDAO();
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblTitle = new JLabel("Đăng Nhập");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblTitle, gbc);

        JLabel lblUsername = new JLabel("Tên đăng nhập:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(lblUsername, gbc);

        txtUsername = new JTextField(15);
        gbc.gridx = 1;
        add(txtUsername, gbc);

        JLabel lblPassword = new JLabel("Mật khẩu:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(lblPassword, gbc);

        txtPassword = new JPasswordField(15);
        gbc.gridx = 1;
        add(txtPassword, gbc);

        btnLogin = new JButton("Đăng Nhập");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnLogin, gbc);

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword());

                User user = new User(username, password);

                // Tạo đối tượng UserDAO để kiểm tra đăng nhập
                UserDAO userDAO = new UserDAO();

                // Kiểm tra thông tin đăng nhập
                if (userDAO.authenticate(user)) {
                    // Đăng nhập thành công
                    JOptionPane.showMessageDialog(Form_DN.this, "Đăng nhập thành công!");

                    // Lưu thông tin người dùng vào session (có thể sử dụng cho các mục đích khác)
                    String maNV = user.getMaNV();
                    String hoTen = user.getHoTen();
                    String gioiTinh = user.getGioiTinh();

                    // Đóng form đăng nhập và mở form chính
                    JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(Form_DN.this);
                    mainFrame.dispose();

                    // Mở form chính
                    Form_GiaoDienChinh mainInterface = new Form_GiaoDienChinh();
                    mainInterface.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    mainInterface.setSize(800, 600);
                    mainInterface.setLocationRelativeTo(null);
                    mainInterface.setVisible(true);
                } else {
                    // Thông báo lỗi nếu đăng nhập thất bại
                    JOptionPane.showMessageDialog(Form_DN.this, "Tên đăng nhập hoặc mật khẩu không đúng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Login Form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.add(new Form_DN());
        frame.setVisible(true);
        
        
        
        SwingUtilities.invokeLater(() -> {
			Form_DN app = new Form_DN();
			app.setVisible(true);
		});
	}
        
        
    
}
