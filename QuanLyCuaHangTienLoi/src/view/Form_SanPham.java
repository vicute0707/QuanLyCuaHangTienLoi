package view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;

import dialog.SuaSanPhamDialog;
import dialog.ThemSanPhamDialog;
import style.ModernScrollBarUI;
import style.RoundedBorder;
import style.StatusRenderer;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class Form_SanPham extends JPanel {
	// Colors
	private static final Color PRIMARY = new Color(255, 255, 255);
	private static final Color SECONDARY = new Color(243, 244, 246);
	private static final Color ACCENT = new Color(37, 99, 235);
	private static final Color ACCENT_HOVER = new Color(29, 78, 216);
	private static final Color BACKGROUND = new Color(249, 250, 251);
	private static final Color BORDER = new Color(229, 231, 235);
	private static final Color TEXT = new Color(17, 24, 39);
	private static final Color TEXT_SECONDARY = new Color(107, 114, 128);
	private static final Color SUCCESS = new Color(16, 185, 129);
	private static final Color WARNING = new Color(245, 158, 11);

	// Fonts
	private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
	private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 16);
	private static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);

	// Component sizes
	private static final int BUTTON_HEIGHT = 36;
	private static final int TABLE_ROW_HEIGHT = 60;
	private static final int HEADER_HEIGHT = 45;

	// Components
	private JTable tblSanPham;
	private JTextField txtSearch;
	private JLabel lblTotalProducts;
	private JComboBox<String> cmbCategory;
	private DefaultTableModel modelSanPham;

	public Form_SanPham() {
		setLayout(new BorderLayout(0, 0));
		setBackground(BACKGROUND);
		setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

		// Thay đổi thứ tự khởi tạo
		createTable(); // Khởi tạo table trước
		initComponents();
		loadSampleData();
	}

	private void initComponents() {
		// Add main sections
		add(createHeaderPanel(), BorderLayout.NORTH);
		add(createMainContent(), BorderLayout.CENTER);
	}

	private JPanel createHeaderPanel() {
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
		headerPanel.setOpaque(false);

		// Title section
		JPanel titleSection = new JPanel(new BorderLayout(20, 0));
		titleSection.setOpaque(false);

		// Left side with title and total count
		JPanel titleLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
		titleLeft.setOpaque(false);

		JLabel lblTitle = new JLabel("Quản lý sản phẩm");
		lblTitle.setFont(TITLE_FONT);
		lblTitle.setForeground(TEXT);

		JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		totalPanel.setOpaque(false);
		totalPanel.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(8, BORDER),
				BorderFactory.createEmptyBorder(5, 15, 5, 15)));

		lblTotalProducts = new JLabel("0");
		lblTotalProducts.setFont(new Font("Segoe UI", Font.BOLD, 16));
		lblTotalProducts.setForeground(ACCENT);

		JLabel lblProductsText = new JLabel(" sản phẩm");
		lblProductsText.setFont(BODY_FONT);
		lblProductsText.setForeground(TEXT_SECONDARY);

		totalPanel.add(lblTotalProducts);
		totalPanel.add(lblProductsText);

		titleLeft.add(lblTitle);
		titleLeft.add(totalPanel);

		// Right side with buttons
		JPanel titleRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		titleRight.setOpaque(false);

		JButton btnDelete = createStyledButton("Xóa", new Color(239, 68, 68), Color.red, true);
		JButton btnEdit = createStyledButton("Sửa", new Color(245, 158, 11), Color.darkGray, true);
		JButton btnAdd = createStyledButton("Thêm sản phẩm", ACCENT, Color.BLUE, true);
		JButton btnExport = createStyledButton("Xuất Excel", Color.WHITE, TEXT, false);



		// Enable/disable buttons based on selection
		tblSanPham.getSelectionModel().addListSelectionListener(e -> {
			boolean hasSelection = tblSanPham.getSelectedRow() != -1;
			btnDelete.setEnabled(hasSelection);
			btnEdit.setEnabled(hasSelection);
		});

		// Add button actions
		
		btnAdd.addActionListener(e -> {
			ThemSanPhamDialog dialog = new ThemSanPhamDialog();
			dialog.setVisible(true);
			
		});
		btnEdit.addActionListener(e -> {
			int row = tblSanPham.getSelectedRow();
			if (row != -1) {
				setEnabled(true);
				SuaSanPhamDialog dialog = new SuaSanPhamDialog(tblSanPham, row);
		        dialog.setVisible(true);
				
			}
		});
		btnDelete.addActionListener(e -> {
			int row = tblSanPham.getSelectedRow();
			if (row != -1) {
				showDeleteConfirmDialog(row);
			}
		});
		btnExport.addActionListener(e -> exportToExcel());

		titleRight.add(btnExport);
		titleRight.add(btnDelete);
		titleRight.add(btnEdit);
		titleRight.add(btnAdd);

		titleSection.add(titleLeft, BorderLayout.WEST);
		titleSection.add(titleRight, BorderLayout.EAST);

		// Actions section (search and filter) giữ nguyên
		JPanel actionsSection = new JPanel(new BorderLayout(20, 0));
		actionsSection.setOpaque(false);
		actionsSection.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

		// Filter panel
		JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
		filterPanel.setOpaque(false);

		JLabel lblCategory = new JLabel("Danh mục:");
		lblCategory.setFont(BODY_FONT);
		lblCategory.setForeground(TEXT);

		String[] categories = { "Tất cả", "Nước giải khát", "Thực phẩm", "Bánh kẹo", "Đồ dùng" };
		cmbCategory = new JComboBox<>(categories);
		cmbCategory.setFont(BODY_FONT);
		cmbCategory.setPreferredSize(new Dimension(150, BUTTON_HEIGHT));
		styleComboBox(cmbCategory);

		filterPanel.add(lblCategory);
		filterPanel.add(cmbCategory);

		// Search panel
		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		searchPanel.setOpaque(false);

		txtSearch = new JTextField(25);
		txtSearch.setPreferredSize(new Dimension(250, BUTTON_HEIGHT));
		styleTextField(txtSearch, "Tìm kiếm sản phẩm...");

		JButton btnSearch = createStyledButton("Tìm kiếm", ACCENT, Color.BLUE, true);

		searchPanel.add(txtSearch);
		searchPanel.add(btnSearch);

		actionsSection.add(filterPanel, BorderLayout.WEST);
		actionsSection.add(searchPanel, BorderLayout.EAST);

		// Add all sections to header
		headerPanel.add(titleSection);
		headerPanel.add(actionsSection);

		return headerPanel;

	}

	private JPanel createMainContent() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setOpaque(false);

		// Create table
		createTable();

		// Custom scrollpane
		JScrollPane scrollPane = new JScrollPane(tblSanPham);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		styleScrollBar(scrollPane);

		// Table container with padding and border
		JPanel tableContainer = new JPanel(new BorderLayout());
		tableContainer.setBackground(PRIMARY);
		tableContainer.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(8, BORDER),
				BorderFactory.createEmptyBorder(1, 1, 1, 1)));
		tableContainer.add(scrollPane);

		mainPanel.add(tableContainer, BorderLayout.CENTER);

		return mainPanel;
	}

	private void createTable() {
		String[] columns = { "Mã SP", "Hình ảnh", "Tên sản phẩm", "Danh mục", "Giá bán", "Tồn kho", "Đơn vị",
				"Trạng thái" };

		modelSanPham = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

			@Override
			public Class<?> getColumnClass(int column) {
				if (column == 1)
					return ImageIcon.class;
				return Object.class;
			}
		};

		tblSanPham = new JTable(modelSanPham);

		// Style table
		tblSanPham.setFont(BODY_FONT);
		tblSanPham.setRowHeight(TABLE_ROW_HEIGHT);
		tblSanPham.setShowGrid(false);
		tblSanPham.setShowHorizontalLines(true);
		tblSanPham.setGridColor(BORDER);
		tblSanPham.setBackground(PRIMARY);
		tblSanPham.setSelectionBackground(new Color(ACCENT.getRed(), ACCENT.getGreen(), ACCENT.getBlue(), 30));
		tblSanPham.setSelectionForeground(TEXT);

		// Header style
		JTableHeader header = tblSanPham.getTableHeader();
		header.setFont(SUBTITLE_FONT);
		header.setBackground(PRIMARY);
		header.setForeground(TEXT);
		header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
		header.setPreferredSize(new Dimension(header.getWidth(), HEADER_HEIGHT));

		// Column sizes
		int[] columnWidths = { 80, 80, 250, 150, 120, 100, 100, 120 };
		for (int i = 0; i < columnWidths.length; i++) {
			tblSanPham.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
		}

		// Center align for specific columns
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);

		int[] centerColumns = { 0, 4, 5, 6, 7 };
		for (int i : centerColumns) {
			tblSanPham.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		StatusRenderer statusRenderer = new StatusRenderer();

		// Status column renderer
		tblSanPham.getColumnModel().getColumn(7).setCellRenderer(statusRenderer);
	}

	// Utility methods for styling components
	private JButton createStyledButton(String text, Color bg, Color fg, boolean isAccent) {
		JButton button = new JButton(text);
		button.setFont(BODY_FONT);
		button.setForeground(fg);
		button.setBackground(bg);
		button.setPreferredSize(new Dimension(button.getPreferredSize().width, BUTTON_HEIGHT));
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

	private void styleTextField(JTextField textField, String placeholder) {
		textField.setFont(BODY_FONT);
		textField.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(6, BORDER),
				BorderFactory.createEmptyBorder(5, 15, 5, 15)));

		// Placeholder text
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

	private void styleComboBox(JComboBox<String> comboBox) {
		comboBox.setBackground(PRIMARY);
		comboBox.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(6, BORDER),
				BorderFactory.createEmptyBorder(5, 15, 5, 15)));
	}

	private void styleScrollBar(JScrollPane scrollPane) {
		ModernScrollBarUI modernScroll = new ModernScrollBarUI();
		scrollPane.getVerticalScrollBar().setUI(modernScroll);
		scrollPane.getHorizontalScrollBar().setUI(modernScroll);
	}

	

	// Custom renderer for status column


	// Load sample data
	private void loadSampleData() {
		String[] statuses = { "Còn hàng", "Sắp hết", "Hết hàng" };
		String[] categories = { "Nước giải khát", "Thực phẩm", "Bánh kẹo", "Đồ dùng" };

		// Sample products
		Object[][] products = {
				{ "SP001", loadProductImage("/icons/logoHSK.png"), "Coca Cola", "Nước giải khát", "12,000đ", 150, "Chai",
						"Còn hàng", null },
				{ "SP002", loadProductImage("/icons/pepsi.png"), "Pepsi", "Nước giải khát", "12,000đ", 120, "Chai",
						"Còn hàng", null },
				{ "SP003", loadProductImage("/icons/myhao.png"), "Mì Hảo Hảo", "Thực phẩm", "4,000đ", 50, "Gói",
						"Sắp hết", null },
				{ "SP004", loadProductImage("/icons/kokomi.png"), "Mì Kokomi", "Thực phẩm", "4,500đ", 0, "Gói",
						"Hết hàng", null },
				{ "SP005", loadProductImage("/icons/oreo.png"), "Bánh Oreo", "Bánh kẹo", "15,000đ", 80, "Hộp",
						"Còn hàng", null }, };

		// Add to table
		for (Object[] product : products) {
			modelSanPham.addRow(product);
		}

		updateTotalCount();
	}

	private ImageIcon loadProductImage(String path) {
		try {
			// Load and scale image
			ImageIcon icon = new ImageIcon(getClass().getResource(path));
			Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
			return new ImageIcon(img);
		} catch (Exception e) {
			// Return default image if loading fails
			return new ImageIcon(new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB));
		}
	}

	private void updateTotalCount() {
		int total = modelSanPham.getRowCount();
		lblTotalProducts.setText(String.valueOf(total));
	}

	// Add product dialog
	

	// Edit product dialog - similar to add dialog but pre-filled
	

	// Delete confirmation dialog
	private void showDeleteConfirmDialog(int row) {
		String productName = (String) modelSanPham.getValueAt(row, 2);
		int option = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa sản phẩm \"" + productName + "\"?",
				"Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

		if (option == JOptionPane.YES_OPTION) {
			modelSanPham.removeRow(row);
			updateTotalCount();
		}
	}

	// Export to Excel
	private void exportToExcel() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Chọn vị trí lưu file");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			// Implement Excel export logic here
			JOptionPane.showMessageDialog(this, "Xuất Excel thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	// Add event listeners
	private void addEventListeners() {
		// Table mouse listener for action buttons
		tblSanPham.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int column = tblSanPham.columnAtPoint(e.getPoint());
				int row = tblSanPham.rowAtPoint(e.getPoint());

				if (column == 8) { // Action column
					Rectangle cellRect = tblSanPham.getCellRect(row, column, false);
					Point clickPoint = e.getPoint();
				}
			}
		});

		// Category filter listener
		cmbCategory.addActionListener(e -> {
			String selectedCategory = (String) cmbCategory.getSelectedItem();
			// Implement filtering logic
		});

		// Search functionality
		txtSearch.getDocument().addDocumentListener((DocumentListener) new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				search();
			}

			public void removeUpdate(DocumentEvent e) {
				search();
			}

			public void insertUpdate(DocumentEvent e) {
				search();
			}

			private void search() {
				// Implement search logic
			}
		});
	}
}