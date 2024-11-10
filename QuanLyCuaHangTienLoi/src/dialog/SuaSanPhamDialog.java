package dialog;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import style.RoundedBorder;

public class SuaSanPhamDialog extends JDialog {
	// Copy các thuộc tính constants từ ThemSanPhamDialog
	private static final Color PRIMARY = new Color(255, 255, 255);
	private static final Color SECONDARY = new Color(243, 244, 246);
	private static final Color ACCENT = new Color(37, 99, 235);
	private static final Color ACCENT_HOVER = new Color(29, 78, 216);
	private static final Color TEXT = new Color(17, 24, 39);
	private static final Color TEXT_SECONDARY = new Color(107, 114, 128);
	private static final Color BORDER = new Color(229, 231, 235);

	private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);
	private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
	private static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);

	private static final int FIELD_HEIGHT = 36;
	private static final int BUTTON_HEIGHT = 36;

	// Components
	private JTextField txtMaSP;
	private JTextField txtTenSP;
	private JComboBox<String> cmbDanhMuc;
	private JTextField txtDonViTinh;
	private JTextField txtGiaBan;
	private JTextField txtSoLuong;
	private JPanel pnlImagePreview;
	private JLabel lblImagePath;
	private byte[] selectedImage;

	// Constructor nhận thông tin sản phẩm từ table
	public SuaSanPhamDialog(JTable table, int selectedRow) {
		setTitle("Sửa Sản Phẩm");
		setSize(600, 650);
		setLocationRelativeTo(null);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		initComponents();
		loadProductData(table, selectedRow);
	}

	private void initComponents() {
		// Main container
		JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
		mainPanel.setBackground(PRIMARY);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		// Content wrapper
		JPanel contentWrapper = new JPanel();
		contentWrapper.setLayout(new BoxLayout(contentWrapper, BoxLayout.Y_AXIS));
		contentWrapper.setBackground(PRIMARY);
		contentWrapper.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

		// Header Section
		JLabel lblTitle = new JLabel("Thêm sản phẩm mới");
		lblTitle.setFont(TITLE_FONT);
		lblTitle.setForeground(TEXT);
		lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

		contentWrapper.add(lblTitle);
		contentWrapper.add(Box.createVerticalStrut(20));

		// Form Section
		contentWrapper.add(createFormSection("THÔNG TIN SẢN PHẨM"));
		contentWrapper.add(Box.createVerticalStrut(20));
		contentWrapper.add(createPriceQuantitySection("GIÁ BÁN & SỐ LƯỢNG"));
		contentWrapper.add(Box.createVerticalStrut(20));
		contentWrapper.add(createImageSection("HÌNH ẢNH SẢN PHẨM"));

		// Add contentWrapper to a ScrollPane
		JScrollPane scrollPane = new JScrollPane(contentWrapper);
		scrollPane.setBorder(null);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);

		// Button Panel
		JPanel buttonPanel = createButtonPanel();

		// Add to main panel
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		setContentPane(mainPanel);
	}
	private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setBackground(PRIMARY);
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER));

        JButton btnCancel = createStyledButton("Hủy", Color.WHITE, TEXT, false);
        JButton btnSave = createStyledButton("Lưu", ACCENT, Color.BLUE, true);

        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> saveProduct());

        panel.add(btnCancel);
        panel.add(btnSave);

        return panel;
    }
    private JButton createStyledButton(String text, Color bg, Color fg, boolean isAccent) {
        JButton button = new JButton(text);
        button.setFont(BODY_FONT);
        button.setForeground(fg);
        button.setBackground(bg);
        button.setPreferredSize(new Dimension(100, BUTTON_HEIGHT));
        button.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(6, isAccent ? bg : BORDER),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(isAccent ? ACCENT_HOVER : SECONDARY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bg);
            }
        });

        return button;
    }
	private JPanel createImageSection(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(PRIMARY);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                HEADER_FONT,
                TEXT
            ),
            BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Image preview panel
        pnlImagePreview = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(SECONDARY);
                g.fillRect(0, 0, getWidth(), getHeight());
                if (selectedImage == null) {
                    g.setColor(TEXT_SECONDARY);
                    g.drawString("Chưa có ảnh", 20, getHeight()/2);
                }
            }
        };
        pnlImagePreview.setPreferredSize(new Dimension(120, 120));
        panel.add(pnlImagePreview, gbc);

        // Upload button
        gbc.gridx = 1;
        JButton btnUpload = createStyledButton("Chọn ảnh", ACCENT, Color.blue, true);
        btnUpload.addActionListener(e -> selectImage());
        panel.add(btnUpload, gbc);

        // Image path label
        gbc.gridx = 2;
        gbc.weightx = 1.0;
        lblImagePath = new JLabel("Chưa chọn ảnh");
        lblImagePath.setForeground(TEXT_SECONDARY);
        panel.add(lblImagePath, gbc);

        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }
	private void selectImage() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getPath();
            lblImagePath.setText(path);
            // TODO: Load and display image preview
        }
    }
	private JPanel createPriceQuantitySection(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(PRIMARY);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                HEADER_FONT,
                TEXT
            ),
            BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Giá bán
        addFormField(panel, gbc, 0, "Giá bán:", 
            txtGiaBan = createStyledTextField("Nhập giá bán"));

        // Số lượng
        addFormField(panel, gbc, 1, "Số lượng:", 
            txtSoLuong = createStyledTextField("Nhập số lượng"));

        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

	private JPanel createFormSection(String title) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBackground(PRIMARY);
		panel.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BORDER), title,
								TitledBorder.LEFT, TitledBorder.TOP, HEADER_FONT, TEXT),
						BorderFactory.createEmptyBorder(15, 10, 15, 10)));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);

		// Mã sản phẩm
		addFormField(panel, gbc, 0, "Mã sản phẩm:", txtMaSP = createStyledTextField("Nhập mã sản phẩm"));

		// Tên sản phẩm
		addFormField(panel, gbc, 1, "Tên sản phẩm:", txtTenSP = createStyledTextField("Nhập tên sản phẩm"));

		// Danh mục
		String[] categories = { "Chọn danh mục", "Nước giải khát", "Thực phẩm", "Bánh kẹo", "Đồ dùng" };
		cmbDanhMuc = new JComboBox<>(categories);
		styleComboBox(cmbDanhMuc);
		addFormField(panel, gbc, 2, "Danh mục:", cmbDanhMuc);

		// Đơn vị tính
		addFormField(panel, gbc, 3, "Đơn vị tính:", txtDonViTinh = createStyledTextField("Nhập đơn vị tính"));

		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		return panel;
	}

	private JTextField createStyledTextField(String placeholder) {
		JTextField field = new JTextField();
		field.setPreferredSize(new Dimension(200, FIELD_HEIGHT));
		styleTextField(field, placeholder);
		return field;
	}
	 private void styleTextField(JTextField field, String placeholder) {
	        field.setFont(BODY_FONT);
	        field.setBorder(BorderFactory.createCompoundBorder(
	            new RoundedBorder(6, BORDER),
	            BorderFactory.createEmptyBorder(5, 15, 5, 15)
	        ));

	        field.setForeground(TEXT_SECONDARY);
	        field.setText(placeholder);

	        field.addFocusListener(new FocusAdapter() {
	            @Override
	            public void focusGained(FocusEvent e) {
	                if (field.getText().equals(placeholder)) {
	                    field.setText("");
	                    field.setForeground(TEXT);
	                }
	            }

	            @Override
	            public void focusLost(FocusEvent e) {
	                if (field.getText().isEmpty()) {
	                    field.setForeground(TEXT_SECONDARY);
	                    field.setText(placeholder);
	                }
	            }
	        });
	    }
	private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(BODY_FONT);
        comboBox.setPreferredSize(new Dimension(200, FIELD_HEIGHT));
        comboBox.setBorder(new RoundedBorder(6, BORDER));
        comboBox.setBackground(PRIMARY);
    }

	private void addFormField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent field) {
		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.weightx = 0;

		JLabel label = new JLabel(labelText);
		label.setFont(BODY_FONT);
		label.setForeground(TEXT);
		panel.add(label, gbc);

		gbc.gridx = 1;
		gbc.weightx = 1.0;
		panel.add(field, gbc);
	}

	// Phần initComponents() giữ nguyên như ThemSanPhamDialog

	// Thêm phương thức để load dữ liệu sản phẩm từ table vào form
	private void loadProductData(JTable table, int row) {
		// Lấy dữ liệu từ table
		String maSP = table.getValueAt(row, 0).toString();
		String tenSP = table.getValueAt(row, 2).toString();
		String danhMuc = table.getValueAt(row, 3).toString();
		String giaBan = table.getValueAt(row, 4).toString().replace("đ", "").replace(",", "").trim();
		String tonKho = table.getValueAt(row, 5).toString();
		String donVi = table.getValueAt(row, 6).toString();
		Icon anhSP = (Icon) table.getValueAt(row, 1);

		// Set dữ liệu vào các components
		txtMaSP.setText(maSP);
		txtMaSP.setEditable(false); // Không cho phép sửa mã SP
		txtMaSP.setForeground(TEXT);

		txtTenSP.setText(tenSP);
		txtTenSP.setForeground(TEXT);

		cmbDanhMuc.setSelectedItem(danhMuc);

		txtDonViTinh.setText(donVi);
		txtDonViTinh.setForeground(TEXT);

		txtGiaBan.setText(giaBan);
		txtGiaBan.setForeground(TEXT);

		txtSoLuong.setText(tonKho);
		txtSoLuong.setForeground(TEXT);

		// Hiển thị ảnh sản phẩm
		if (anhSP instanceof ImageIcon) {
			ImageIcon icon = (ImageIcon) anhSP;
			// Tạo một JLabel để hiển thị ảnh trong pnlImagePreview
			JLabel lblImage = new JLabel(icon);
			pnlImagePreview.removeAll();
			pnlImagePreview.add(lblImage);
			pnlImagePreview.revalidate();
			pnlImagePreview.repaint();
			lblImagePath.setText("Đã có ảnh");
		}
	}

	// Override phương thức saveProduct để xử lý cập nhật
	private void saveProduct() {
		// Kiểm tra dữ liệu đầu vào
		if (validateInput()) {
			try {
				// Lấy dữ liệu từ form
				String maSP = txtMaSP.getText();
				String tenSP = txtTenSP.getText();
				String danhMuc = cmbDanhMuc.getSelectedItem().toString();
				String donVi = txtDonViTinh.getText();
				double giaBan = Double.parseDouble(txtGiaBan.getText());
				int soLuong = Integer.parseInt(txtSoLuong.getText());

				// TODO: Xử lý cập nhật vào database

				JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thành công!", "Thông báo",
						JOptionPane.INFORMATION_MESSAGE);

				dispose();

			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số!", "Lỗi",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private boolean validateInput() {
		// Kiểm tra tên sản phẩm
		if (txtTenSP.getText().trim().isEmpty()) {
			showError("Vui lòng nhập tên sản phẩm!");
			return false;
		}

		// Kiểm tra danh mục
		if (cmbDanhMuc.getSelectedIndex() == 0) {
			showError("Vui lòng chọn danh mục!");
			return false;
		}

		// Kiểm tra đơn vị tính
		if (txtDonViTinh.getText().trim().isEmpty()) {
			showError("Vui lòng nhập đơn vị tính!");
			return false;
		}

		// Kiểm tra giá bán
		try {
			double giaBan = Double.parseDouble(txtGiaBan.getText().trim());
			if (giaBan <= 0) {
				showError("Giá bán phải lớn hơn 0!");
				return false;
			}
		} catch (NumberFormatException e) {
			showError("Vui lòng nhập giá bán hợp lệ!");
			return false;
		}

		// Kiểm tra số lượng
		try {
			int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
			if (soLuong < 0) {
				showError("Số lượng không được âm!");
				return false;
			}
		} catch (NumberFormatException e) {
			showError("Vui lòng nhập số lượng hợp lệ!");
			return false;
		}

		return true;
	}

	private void showError(String message) {
		JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
	}
}
