package view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.*;

import dialog.ChiTietHoaDonDialog;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import style.RoundedBorder;

public class Form_BanHang extends JPanel {
	// Constants
	private static final Color PRIMARY = new Color(255, 255, 255);
	private static final Color SECONDARY = new Color(243, 244, 246);
	private static final Color ACCENT = new Color(37, 99, 235);
	private static final Color ACCENT_HOVER = new Color(29, 78, 216);
	private static final Color BACKGROUND = new Color(249, 250, 251);
	private static final Color BORDER = new Color(229, 231, 235);
	private static final Color TEXT = new Color(17, 24, 39);
	private static final Color TEXT_SECONDARY = new Color(107, 114, 128);
	private static final Color SUCCESS = new Color(16, 185, 129);

	private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
	private static final Font HEADER_FONT = new Font("Segoe UI", Font.PLAIN, 16);
	private static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);
	Font headerFont = new Font("Segoe UI", Font.PLAIN, 14);
	Font cellFont = new Font("Segoe UI", Font.PLAIN, 14);

	// Colors
	Color headerBgColor = new Color(220, 241, 255); // Màu xanh pastel nhẹ cho header
	Color selectedBgColor = new Color(240, 248, 255); // Màu khi select row
	Color alternateRowColor = new Color(250, 252, 255); // Màu cho dòng chẵn

	// Components
	private JTabbedPane productTabs;
	private JTable cartTable;
	private DefaultTableModel cartModel;
	private JLabel lblTotal;
	private JTextField txtSearch;
	private double totalAmount = 0;
	private DecimalFormat currencyFormat;
	private JComboBox<String> cboStaff;
	private JTextField txtCustomerName;
	private JTextField txtCustomerPhone;
	private JTextField txtCustomerAddress;

	public Form_BanHang() {
		setLayout(new BorderLayout(20, 20));
        setBackground(BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        currencyFormat = new DecimalFormat("#,### đ");
        
        initComponents();
	}

	private void initComponents() {
		JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(BACKGROUND);
        
        // Thêm panel thông tin phía trên
        mainPanel.add(createInfoPanel(), BorderLayout.NORTH);
        
        // Main content - split into left (products) and right (cart) panels
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.7);
        splitPane.setBorder(null);
        
        // Left panel - Products
        JPanel leftPanel = createProductsPanel();
        
        // Right panel - Cart
        JPanel rightPanel = createCartPanel();
        
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        
        mainPanel.add(splitPane, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
	}
	 private JPanel createInfoPanel() {
	        JPanel panel = new JPanel(new BorderLayout(20, 0));
	        panel.setBackground(PRIMARY);
	        panel.setBorder(new CompoundBorder(
	            new RoundedBorder(8, BORDER),
	            BorderFactory.createEmptyBorder(15, 15, 15, 15)
	        ));

	        // Left side - Staff info
	        JPanel staffPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
	        staffPanel.setBackground(PRIMARY);
	        
	        JLabel lblStaff = new JLabel("Nhân viên:");
	        lblStaff.setFont(new Font("Segoe UI", Font.BOLD, 14));
	        
	        // Tạo sample data cho combo box
	        String[] staffs = {"Nguyễn Văn A", "Trần Thị B", "Phạm Văn C"};
	        cboStaff = new JComboBox<>(staffs);
	        cboStaff.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	        cboStaff.setPreferredSize(new Dimension(200, 35));
	        styleComboBox(cboStaff);
	        
	        staffPanel.add(lblStaff);
	        staffPanel.add(cboStaff);

	        // Right side - Customer info
	        JPanel customerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
	        customerPanel.setBackground(PRIMARY);
	        
	        // Customer Name
	        JLabel lblCustomerName = new JLabel("Khách hàng:");
	        lblCustomerName.setFont(new Font("Segoe UI", Font.BOLD, 14));
	        txtCustomerName = new JTextField(15);
	        styleTextField(txtCustomerName, "Nhập tên khách hàng");
	        txtCustomerName.setPreferredSize(new Dimension(200, 35));
	        
	        // Customer Phone
	        JLabel lblCustomerPhone = new JLabel("SĐT:");
	        lblCustomerPhone.setFont(new Font("Segoe UI", Font.BOLD, 14));
	        txtCustomerPhone = new JTextField(10);
	        styleTextField(txtCustomerPhone, "Nhập số điện thoại");
	        txtCustomerPhone.setPreferredSize(new Dimension(150, 35));
	        
	        // Customer Address
	        JLabel lblCustomerAddress = new JLabel("Địa chỉ:");
	        lblCustomerAddress.setFont(new Font("Segoe UI", Font.BOLD, 14));
	        txtCustomerAddress = new JTextField(20);
	        styleTextField(txtCustomerAddress, "Nhập địa chỉ");
	        txtCustomerAddress.setPreferredSize(new Dimension(250, 35));
	        
	        customerPanel.add(lblCustomerName);
	        customerPanel.add(txtCustomerName);
	        customerPanel.add(lblCustomerPhone);
	        customerPanel.add(txtCustomerPhone);
	        customerPanel.add(lblCustomerAddress);
	        customerPanel.add(txtCustomerAddress);

	        // Add panels to main info panel
	        panel.add(staffPanel, BorderLayout.WEST);
	        panel.add(customerPanel, BorderLayout.CENTER);

	        return panel;
	    }
	 private void styleComboBox(JComboBox<String> comboBox) {
	        comboBox.setBackground(PRIMARY);
	        comboBox.setBorder(BorderFactory.createCompoundBorder(
	            new RoundedBorder(6, BORDER),
	            BorderFactory.createEmptyBorder(5, 10, 5, 10)
	        ));
	        
	        // Style the dropdown arrow
	        comboBox.setUI(new BasicComboBoxUI() {
	            @Override
	            protected JButton createArrowButton() {
	                JButton button = new JButton();
	                button.setBackground(PRIMARY);
	                button.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
	                button.setIcon(new ImageIcon(getClass().getResource("/icons/arrow-down.png")));
	                return button;
	            }
	        });
	        
	        // Style the dropdown list
	        Object comp = comboBox.getUI().getAccessibleChild(comboBox, 0);
	        if (comp instanceof JPopupMenu) {
	            JPopupMenu popup = (JPopupMenu) comp;
	            popup.setBorder(new RoundedBorder(6, BORDER));
	        }
	    }

	private JPanel createProductsPanel() {
		JPanel panel = new JPanel(new BorderLayout(0, 20));
		panel.setBackground(PRIMARY);
		panel.setBorder(new RoundedBorder(8, BORDER));

		// Search bar at top
		JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
		searchPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		searchPanel.setBackground(PRIMARY);

		txtSearch = new JTextField();
		txtSearch.setPreferredSize(new Dimension(200, 36));
		styleTextField(txtSearch, "Tìm kiếm sản phẩm...");

		searchPanel.add(txtSearch, BorderLayout.CENTER);

		// Products organized in tabs by category
		productTabs = new JTabbedPane();
		productTabs.setFont(BODY_FONT);
		productTabs.setBackground(PRIMARY);

		// Add category tabs
		String[] categories = { "Tất cả", "Nước giải khát", "Thực phẩm", "Bánh kẹo", "Đồ dùng" };
		for (String category : categories) {
			JPanel productGrid = createProductGrid(category);
			productTabs.addTab(category, productGrid);
		}

		panel.add(searchPanel, BorderLayout.NORTH);
		panel.add(productTabs, BorderLayout.CENTER);

		return panel;
	}

	private JPanel createProductGrid(String category) {
		// Main panel to hold all components
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(PRIMARY);

		// Product grid panel
		JPanel gridPanel = new JPanel(new GridLayout(0, 3, 15, 15)); // 3 cột, khoảng cách 15px
		gridPanel.setBackground(PRIMARY);
		gridPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		// Add sample products based on category - giới hạn 12 sản phẩm (3x4)
		if (category.equals("Nước giải khát") || category.equals("Tất cả")) {
			addProductCard(gridPanel, "SP001", "/icons/home.png", "Coca Cola", 12000);
			addProductCard(gridPanel, "SP002", "/icons/home.png", "Pepsi", 12000);
			addProductCard(gridPanel, "SP003", "/icons/home.png", "Fanta", 12000);
			addProductCard(gridPanel, "SP004", "/icons/home.png", "Sprite", 12000);
		}
		if (category.equals("Thực phẩm") || category.equals("Tất cả")) {
			addProductCard(gridPanel, "SP005", "/icons/home.png", "Mì Hảo Hảo", 4000);
			addProductCard(gridPanel, "SP006", "/icons/home.png", "Mì Kokomi", 4500);
			addProductCard(gridPanel, "SP007", "/icons/home.png", "Mì 3 Miền", 4000);
			addProductCard(gridPanel, "SP008", "/icons/home.png", "Mì Gấu Đỏ", 4500);
		}
		if (category.equals("Bánh kẹo") || category.equals("Tất cả")) {
			addProductCard(gridPanel, "SP009", "/icons/home.png", "Bánh Oreo", 10000);
			addProductCard(gridPanel, "SP010", "/icons/home.png", "Kẹo Gấu", 15000);
			addProductCard(gridPanel, "SP011", "/icons/home.png", "Snack Lay's", 12000);
			addProductCard(gridPanel, "SP012", "/icons/home.png", "Bánh Solite", 8000);
		}

		// Create scroll pane
		JScrollPane scrollPane = new JScrollPane(gridPanel);
		scrollPane.setBorder(null);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);

		// Add scroll pane to main panel
		mainPanel.add(scrollPane, BorderLayout.CENTER);

		return mainPanel;
	}

	private void addProductCard(JPanel panel, String productId, String imagePath, String name, double price) {
		JPanel card = new JPanel(new BorderLayout(10, 10));
		card.setBackground(PRIMARY);
		card.setPreferredSize(new Dimension(200, 250)); // Tăng kích thước card
		card.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(8, BORDER),
				BorderFactory.createEmptyBorder(15, 15, 15, 15)));

		// Product image - tăng kích thước ảnh
		ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
		Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
		JLabel lblImage = new JLabel(new ImageIcon(img));
		lblImage.setHorizontalAlignment(JLabel.CENTER);

		// Product info - tăng font chữ
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.setBackground(PRIMARY);

		JLabel lblName = new JLabel(name);
		lblName.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Tăng kích thước font
		lblName.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel lblPrice = new JLabel(currencyFormat.format(price));
		lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 18)); // Tăng kích thước font
		lblPrice.setForeground(ACCENT);
		lblPrice.setAlignmentX(Component.CENTER_ALIGNMENT);

		infoPanel.add(lblName);
		infoPanel.add(Box.createVerticalStrut(10));
		infoPanel.add(lblPrice);
		infoPanel.add(Box.createVerticalStrut(10));

		// Add to cart button - tăng kích thước nút
		JButton btnAdd = createStyledButton("Thêm vào giỏ", ACCENT, Color.BLUE, true);
		btnAdd.setPreferredSize(new Dimension(150, 40));
		btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnAdd.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnAdd.addActionListener(e -> addToCart(productId, name, price));

		// Layout components
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBackground(PRIMARY);
		centerPanel.add(lblImage, BorderLayout.CENTER);
		centerPanel.add(infoPanel, BorderLayout.SOUTH);

		card.add(centerPanel, BorderLayout.CENTER);
		card.add(btnAdd, BorderLayout.SOUTH);

		// Hover effect
		card.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				card.setBackground(SECONDARY);
				centerPanel.setBackground(SECONDARY);
				infoPanel.setBackground(SECONDARY);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				card.setBackground(PRIMARY);
				centerPanel.setBackground(PRIMARY);
				infoPanel.setBackground(PRIMARY);
			}
		});

		panel.add(card);
	}

	private JPanel createCartPanel() {
		JPanel panel = new JPanel(new BorderLayout(0, 20));
		panel.setBackground(PRIMARY);
		panel.setBorder(new RoundedBorder(8, BORDER));

		// Cart table
		String[] columns = { "Mã SP", "Tên SP", "Đơn giá", "Số lượng", "Thành tiền" };
		cartModel = new DefaultTableModel(columns, 0);
		cartTable = new JTable(cartModel);
		cartTable.setBackground(Color.white);
		styleTable(cartTable);

		JScrollPane scrollPane = new JScrollPane(cartTable);
		scrollPane.setBorder(null);

		// Cart summary
		JPanel summaryPanel = new JPanel(new BorderLayout(20, 15));
		summaryPanel.setBackground(PRIMARY);
		summaryPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER),
				BorderFactory.createEmptyBorder(15, 15, 15, 15)));

		// Total amount
		JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		totalPanel.setBackground(PRIMARY);

		JLabel lblTotalText = new JLabel("Tổng tiền:");
		lblTotalText.setFont(HEADER_FONT);
		lblTotal = new JLabel("0 đ");
		lblTotal.setFont(HEADER_FONT);
		lblTotal.setForeground(ACCENT);

		totalPanel.add(lblTotalText);
		totalPanel.add(lblTotal);

		// Action buttons
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		buttonsPanel.setBackground(PRIMARY);

		JButton btnClear = createStyledButton("Xóa tất cả", Color.WHITE, TEXT, false);
		JButton btnCheckout = createStyledButton("Thanh toán", ACCENT, Color.BLUE, true);
		btnCheckout.setPreferredSize(new Dimension(120, 36));

		btnClear.addActionListener(e -> clearCart());
		btnCheckout.addActionListener(e -> checkout());

		buttonsPanel.add(btnClear);
		buttonsPanel.add(btnCheckout);

		summaryPanel.add(totalPanel, BorderLayout.WEST);
		summaryPanel.add(buttonsPanel, BorderLayout.EAST);

		panel.add(scrollPane, BorderLayout.CENTER);
		panel.add(summaryPanel, BorderLayout.SOUTH);
		panel.setBackground(Color.white);
		return panel;
	}

	private void addToCart(String productId, String name, double price) {
		// Check if product already in cart
		for (int i = 0; i < cartModel.getRowCount(); i++) {
			if (cartModel.getValueAt(i, 0).equals(productId)) {
				int quantity = (int) cartModel.getValueAt(i, 3);
				cartModel.setValueAt(quantity + 1, i, 3);
				cartModel.setValueAt(price * (quantity + 1), i, 4);
				updateTotal();
				return;
			}
		}

		// Add new product to cart
		cartModel.addRow(new Object[] { productId, name, price, 1, price });
		updateTotal();
	}

	private void updateTotal() {
		totalAmount = 0;
		for (int i = 0; i < cartModel.getRowCount(); i++) {
			totalAmount += (double) cartModel.getValueAt(i, 4);
		}
		lblTotal.setText(currencyFormat.format(totalAmount));
	}

	private void clearCart() {
		cartModel.setRowCount(0);
		updateTotal();
	}

	private void checkout() {
		if (cartModel.getRowCount() == 0) {
	        JOptionPane.showMessageDialog(this,
	            "Giỏ hàng trống!",
	            "Thông báo",
	            JOptionPane.WARNING_MESSAGE);
	        return;
	    }
	    
	    // Kiểm tra thông tin khách hàng
	    String customerName = txtCustomerName.getText();
	    String customerPhone = txtCustomerPhone.getText();
	    String customerAddress = txtCustomerAddress.getText();
	    String staffName = (String) cboStaff.getSelectedItem();
	    
	    if (customerName.isEmpty() || customerPhone.isEmpty()) {
	        JOptionPane.showMessageDialog(this,
	            "Vui lòng nhập thông tin khách hàng!",
	            "Thông báo",
	            JOptionPane.WARNING_MESSAGE);
	        return;
	    }
	    
	    // Hiển thị dialog chi tiết hóa đơn
	    ChiTietHoaDonDialog dialog = new ChiTietHoaDonDialog(
	        (Frame) SwingUtilities.getWindowAncestor(this),
	        staffName,
	        customerName,
	        customerPhone,
	        customerAddress,
	        cartModel,
	        totalAmount);
	        
	    dialog.setVisible(true);
	    
	    // Sau khi đóng dialog, clear giỏ hàng và thông tin khách hàng
	    clearCart();
	    clearCustomerInfo();
	}
	  private void clearCustomerInfo() {
	        txtCustomerName.setText("");
	        txtCustomerPhone.setText("");
	        txtCustomerAddress.setText("");
	    }

	// Utility methods
	private void styleTextField(JTextField textField, String placeholder) {
		textField.setFont(BODY_FONT);
		textField.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(6, BORDER),
				BorderFactory.createEmptyBorder(5, 15, 5, 15)));

		textField.setForeground(TEXT_SECONDARY);
		textField.setText(placeholder);

		textField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (textField.getText().equals(placeholder)) {
					textField.setText("");
					textField.setForeground(TEXT);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (textField.getText().isEmpty()) {
					textField.setForeground(TEXT_SECONDARY);
					textField.setText(placeholder);
				}
			}
		});
	}

	private void styleTable(JTable table) {

		// Font settings
		Font headerFont = new Font("Segoe UI", Font.BOLD, 14);
		Font cellFont = new Font("Segoe UI", Font.PLAIN, 14);

		// Colors
		Color headerBgColor = new Color(220, 241, 255); // Màu xanh pastel nhẹ cho header
		Color selectedBgColor = new Color(240, 248, 255); // Màu khi select row
		Color alternateRowColor = new Color(250, 252, 255); // Màu cho dòng chẵn

		// Style cho table
		table.setFont(cellFont);
		table.setRowHeight(40); // Tăng chiều cao của row
		table.setShowGrid(true);
		table.setGridColor(new Color(230, 236, 240)); // Màu đường kẻ nhẹ hơn
		table.setSelectionBackground(selectedBgColor);
		table.setSelectionForeground(Color.BLACK);
		table.setShowVerticalLines(true);
		table.setShowHorizontalLines(true);
		table.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		table.setBackground(Color.white);

		// Style cho header
		JTableHeader header = table.getTableHeader();
		header.setFont(headerFont);
		header.setBackground(headerBgColor);
		header.setForeground(Color.BLACK);
		header.setBorder(BorderFactory.createLineBorder(new Color(200, 215, 225))); // Border cho header
		header.setPreferredSize(new Dimension(header.getPreferredSize().width, 45)); // Tăng chiều cao header

		// Căn giữa cho header
		((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

		// Style cho cells
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (!isSelected) {
					c.setBackground(row % 2 == 0 ? Color.WHITE : alternateRowColor);
				}
				return c;
			}
		};
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);

		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (!isSelected) {
					c.setBackground(row % 2 == 0 ? Color.WHITE : alternateRowColor);
				}
				return c;
			}
		};
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

		// Áp dụng renderer cho từng cột
		// Mã SP - căn giữa
		table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

		// Tên SP - căn trái (mặc định)
		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (!isSelected) {
					c.setBackground(row % 2 == 0 ? Color.WHITE : alternateRowColor);
				}
				setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0)); // Padding trái cho text
				return c;
			}
		};
		leftRenderer.setHorizontalAlignment(JLabel.LEFT);
		table.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);

		// Đơn giá - căn phải
		table.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);

		// Số lượng - căn giữa
		table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

		// Thành tiền - căn phải
		table.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);

		// Set column widths
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(80); // Mã SP
		columnModel.getColumn(1).setPreferredWidth(200); // Tên SP
		columnModel.getColumn(2).setPreferredWidth(100); // Đơn giá
		columnModel.getColumn(3).setPreferredWidth(80); // Số lượng
		columnModel.getColumn(4).setPreferredWidth(120); // Thành tiền

		// Disable column reordering
		table.getTableHeader().setReorderingAllowed(false);

		// Add row margin
		table.setIntercellSpacing(new Dimension(10, 10));
	}

	private JButton createStyledButton(String text, Color bg, Color fg, boolean isAccent) {
		JButton button = new JButton(text);
		button.setFont(BODY_FONT);
		button.setForeground(fg);
		button.setBackground(bg);
		button.setPreferredSize(new Dimension(100, 36));
		button.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(6, isAccent ? bg : BORDER),
				BorderFactory.createEmptyBorder(5, 15, 5, 15)));
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

	// Method để xử lý tìm kiếm sản phẩm
	private void searchProducts(String keyword) {
		keyword = keyword.toLowerCase().trim();
		for (int i = 0; i < productTabs.getTabCount(); i++) {
			JScrollPane scrollPane = (JScrollPane) productTabs.getComponentAt(i);
			JPanel productGrid = (JPanel) scrollPane.getViewport().getView();

			for (Component c : productGrid.getComponents()) {
				JPanel card = (JPanel) c;
				JPanel infoPanel = (JPanel) card.getComponent(1);
				JLabel nameLabel = (JLabel) infoPanel.getComponent(0);
				String productName = nameLabel.getText().toLowerCase();

				card.setVisible(productName.contains(keyword));
			}
			productGrid.revalidate();
			productGrid.repaint();
		}
	}

	// Method để tạo hóa đơn chi tiết
	private void showDetailedInvoice() {
		JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chi tiết hóa đơn", true);
		dialog.setLayout(new BorderLayout(10, 10));
		dialog.setSize(400, 600);
		dialog.setLocationRelativeTo(null);

		// Panel chứa nội dung hóa đơn
		JPanel invoicePanel = new JPanel();
		invoicePanel.setLayout(new BoxLayout(invoicePanel, BoxLayout.Y_AXIS));
		invoicePanel.setBackground(PRIMARY);
		invoicePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// Thông tin cửa hàng
		JLabel lblStoreName = new JLabel("TV SHOP");
		lblStoreName.setFont(TITLE_FONT);
		lblStoreName.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel lblAddress = new JLabel("Địa chỉ: 97 Man Thiện, Tp.Thủ Đức");
		lblAddress.setFont(BODY_FONT);
		lblAddress.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel lblPhone = new JLabel("SĐT: 0123456789");
		lblPhone.setFont(BODY_FONT);
		lblPhone.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Thông tin hóa đơn
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String invoiceNo = String.format("HD%06d", (int) (Math.random() * 1000000));

		JLabel lblInvoiceTitle = new JLabel("HÓA ĐƠN BÁN HÀNG", SwingConstants.CENTER);
		lblInvoiceTitle.setFont(HEADER_FONT);
		lblInvoiceTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel lblDate = new JLabel("Ngày: " + sdf.format(new Date()));
		lblDate.setFont(BODY_FONT);
		lblDate.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel lblInvoiceNo = new JLabel("Số HĐ: " + invoiceNo);
		lblInvoiceNo.setFont(BODY_FONT);
		lblInvoiceNo.setAlignmentX(Component.LEFT_ALIGNMENT);

		// Chi tiết sản phẩm
		JPanel productsPanel = new JPanel();
		productsPanel.setLayout(new BoxLayout(productsPanel, BoxLayout.Y_AXIS));
		productsPanel.setBackground(PRIMARY);
		productsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

		// Header cho bảng sản phẩm
		JPanel headerPanel = new JPanel(new GridLayout(1, 4));
		headerPanel.setBackground(SECONDARY);
		headerPanel.add(new JLabel("Sản phẩm"));
		headerPanel.add(new JLabel("SL", SwingConstants.CENTER));
		headerPanel.add(new JLabel("Đơn giá", SwingConstants.RIGHT));
		headerPanel.add(new JLabel("Thành tiền", SwingConstants.RIGHT));

		productsPanel.add(headerPanel);
		productsPanel.add(Box.createVerticalStrut(10));

		// Thêm từng sản phẩm
		for (int i = 0; i < cartModel.getRowCount(); i++) {
			JPanel productRow = new JPanel(new GridLayout(1, 4));
			productRow.setBackground(PRIMARY);

			productRow.add(new JLabel(cartModel.getValueAt(i, 1).toString()));
			productRow.add(new JLabel(cartModel.getValueAt(i, 3).toString(), SwingConstants.CENTER));
			productRow.add(new JLabel(currencyFormat.format(cartModel.getValueAt(i, 2)), SwingConstants.RIGHT));
			productRow.add(new JLabel(currencyFormat.format(cartModel.getValueAt(i, 4)), SwingConstants.RIGHT));

			productsPanel.add(productRow);
			productsPanel.add(Box.createVerticalStrut(5));
		}

		// Tổng tiền
		JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		totalPanel.setBackground(PRIMARY);
		totalPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER));

		JLabel lblTotalText = new JLabel("Tổng tiền:");
		lblTotalText.setFont(HEADER_FONT);
		JLabel lblTotalAmount = new JLabel(lblTotal.getText());
		lblTotalAmount.setFont(HEADER_FONT);
		lblTotalAmount.setForeground(ACCENT);

		totalPanel.add(lblTotalText);
		totalPanel.add(Box.createHorizontalStrut(10));
		totalPanel.add(lblTotalAmount);

		// Lời cảm ơn
		JLabel lblThankYou = new JLabel("Cảm ơn quý khách!", SwingConstants.CENTER);
		lblThankYou.setFont(BODY_FONT);
		lblThankYou.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Thêm tất cả vào panel chính
		invoicePanel.add(lblStoreName);
		invoicePanel.add(Box.createVerticalStrut(5));
		invoicePanel.add(lblAddress);
		invoicePanel.add(lblPhone);
		invoicePanel.add(Box.createVerticalStrut(20));
		invoicePanel.add(lblInvoiceTitle);
		invoicePanel.add(Box.createVerticalStrut(20));
		invoicePanel.add(lblDate);
		invoicePanel.add(lblInvoiceNo);
		invoicePanel.add(Box.createVerticalStrut(20));
		invoicePanel.add(productsPanel);
		invoicePanel.add(Box.createVerticalStrut(10));
		invoicePanel.add(totalPanel);
		invoicePanel.add(Box.createVerticalStrut(20));
		invoicePanel.add(lblThankYou);

		// Scroll pane cho invoice panel
		JScrollPane scrollPane = new JScrollPane(invoicePanel);
		scrollPane.setBorder(null);

		// Button panel
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.setBackground(PRIMARY);

		JButton btnPrint = createStyledButton("In hóa đơn", ACCENT, Color.WHITE, true);
		JButton btnClose = createStyledButton("Đóng", Color.WHITE, TEXT, false);

		btnPrint.addActionListener(e -> printInvoice());
		btnClose.addActionListener(e -> dialog.dispose());

		buttonPanel.add(btnPrint);
		buttonPanel.add(btnClose);

		dialog.add(scrollPane, BorderLayout.CENTER);
		dialog.add(buttonPanel, BorderLayout.SOUTH);
		dialog.setVisible(true);
	}

	private void printInvoice() {
		// TODO: Implement print functionality
		JOptionPane.showMessageDialog(this, "Chức năng in hóa đơn sẽ được cập nhật sau!", "Thông báo",
				JOptionPane.INFORMATION_MESSAGE);
	}

	// Method để thêm các nút điều chỉnh số lượng trong giỏ hàng
	private void addQuantityButtons() {
		// Add quantity adjustment column
		TableColumn quantityColumn = cartTable.getColumnModel().getColumn(3);
		quantityColumn.setCellRenderer(new QuantityRenderer());
		quantityColumn.setCellEditor(new QuantityEditor());
	}

	// Custom renderer cho cột số lượng
	class QuantityRenderer extends JPanel implements TableCellRenderer {
		private JLabel label;
		private JButton btnMinus;
		private JButton btnPlus;

		public QuantityRenderer() {
			setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
			setBackground(PRIMARY);

			btnMinus = new JButton("-");
			label = new JLabel("0");
			btnPlus = new JButton("+");

			styleQuantityButton(btnMinus);
			styleQuantityButton(btnPlus);

			add(btnMinus);
			add(label);
			add(btnPlus);
		}

		private void styleQuantityButton(JButton btn) {
			btn.setFont(BODY_FONT);
			btn.setPreferredSize(new Dimension(24, 24));
			btn.setBackground(SECONDARY);
			btn.setBorder(new RoundedBorder(4, BORDER));
			btn.setFocusPainted(false);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			label.setText(value.toString());
			return this;
		}
	}

	// Custom editor cho cột số lượng
	class QuantityEditor extends AbstractCellEditor implements TableCellEditor {
		private JPanel panel;
		private JLabel label;
		private JButton btnMinus;
		private JButton btnPlus;
		private int quantity;

		public QuantityEditor() {
			panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
			panel.setBackground(PRIMARY);

			btnMinus = new JButton("-");
			label = new JLabel();
			btnPlus = new JButton("+");

			styleQuantityButton(btnMinus);
			styleQuantityButton(btnPlus);

			btnMinus.addActionListener(e -> updateQuantity(-1));
			btnPlus.addActionListener(e -> updateQuantity(1));

			panel.add(btnMinus);
			panel.add(label);
			panel.add(btnPlus);
		}

		private void styleQuantityButton(JButton btn) {
			btn.setFont(BODY_FONT);
			btn.setPreferredSize(new Dimension(24, 24));
			btn.setBackground(SECONDARY);
			btn.setBorder(new RoundedBorder(4, BORDER));
			btn.setFocusPainted(false);
		}

		private void updateQuantity(int delta) {
			int newQuantity = quantity + delta;
			if (newQuantity > 0) {
				quantity = newQuantity;
				label.setText(String.valueOf(quantity));

				// Update total price
				int row = cartTable.getSelectedRow();
				double price = (double) cartModel.getValueAt(row, 2);
				cartModel.setValueAt(price * quantity, row, 4);
				updateTotal();
			}
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			quantity = Integer.parseInt(value.toString());
			label.setText(value.toString());
			return panel;
		}

		@Override
		public Object getCellEditorValue() {
			return quantity;
		}
	}
}
