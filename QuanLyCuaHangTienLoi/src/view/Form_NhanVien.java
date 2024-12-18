package view;

import java.awt.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.PatternSyntaxException;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import com.toedter.calendar.JDateChooser;

import dao.NhanVienDAO;
import dialog.ChiTietHoaDonDialog;
import entity.NhanVien;
import style.RoundedBorder;

public class Form_NhanVien extends JPanel {
	// Constants
	private static final Color PRIMARY = new Color(255, 255, 255);
	private static final Color SECONDARY = new Color(243, 244, 246);
	private static final Color ACCENT = new Color(37, 99, 235);
	private static final Color ACCENT_HOVER = new Color(29, 78, 216);
	private static final Color BORDER = new Color(229, 231, 235);
	private static final Color TEXT = new Color(17, 24, 39);
	private static final Color SUCCESS = new Color(22, 163, 74);
	private static final Color ERROR = new Color(220, 38, 38);

	// Font
	private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
	private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
	private static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);
	private NhanVienDAO nvDAO;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	// Components
	private JTable tblNhanVien;
	private DefaultTableModel tableModel;
	private JTextField txtSearch;
	private JButton btnAdd, btnEdit, btnDelete, btnRefresh;
	private JTextField txtMaNV, txtHoTen, txtSoDT, txtDiaChi;
	private JComboBox<String> cboChucVu;
	private JDateChooser dateNgaySinh;
	private JComboBox<String> cboGioiTinh;
	private JPanel formPanel;
	private boolean isEditing = false;

	public Form_NhanVien() {
		setLayout(new BorderLayout(20, 20));
		setBackground(new Color(249, 250, 251));
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	    nvDAO = new NhanVienDAO();

		initComponents();
	    loadData(); // Thay thế loadSampleData()
	}
	private void loadData() {
	    tableModel.setRowCount(0);
	    ArrayList<NhanVien> list = nvDAO.getList();
	    
	    for(NhanVien nv : list) {
	        Object[] row = {
	            nv.getMaNV(),
	            nv.getHoTen(),
	            nv.getGioiTinh(),
	            sdf.format(nv.getNgaySinh()),
	            nv.getSdt(),
	            nv.getDiaChi(),
	            nv.getChucVu()
	        };
	        tableModel.addRow(row);
	    }
	}

	private void initComponents() {
		// Title Panel
		JPanel titlePanel = new JPanel(new BorderLayout());
		titlePanel.setBackground(PRIMARY);
		titlePanel.setBorder(
				new CompoundBorder(new RoundedBorder(8, BORDER), BorderFactory.createEmptyBorder(15, 15, 15, 15)));

		JLabel lblTitle = new JLabel("Quản lý nhân viên");
		lblTitle.setFont(TITLE_FONT);
		titlePanel.add(lblTitle, BorderLayout.WEST);

		// Search field
		txtSearch = new JTextField(20);
		styleTextField(txtSearch, "Tìm kiếm nhân viên...");
		txtSearch.setPreferredSize(new Dimension(250, 40));
		txtSearch.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				searchNhanVien();
			}

			public void insertUpdate(DocumentEvent e) {
				searchNhanVien();
			}

			public void removeUpdate(DocumentEvent e) {
				searchNhanVien();
			}
		});

		// Button panel
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		buttonPanel.setBackground(PRIMARY);

		btnAdd = createStyledButton("Thêm", ACCENT, Color.blue, true);
		btnEdit = createStyledButton("Sửa", ACCENT, Color.darkGray, true);
		btnDelete = createStyledButton("Xóa", ERROR, Color.red, true);
		btnRefresh = createStyledButton("Làm mới", Color.WHITE, TEXT, false);

		btnAdd.addActionListener(e -> showAddForm());
		btnEdit.addActionListener(e -> showEditForm());
		btnDelete.addActionListener(e -> deleteNhanVien());
		btnRefresh.addActionListener(e -> refreshData());

		buttonPanel.add(txtSearch);
		buttonPanel.add(btnAdd);
		buttonPanel.add(btnEdit);
		buttonPanel.add(btnDelete);
		buttonPanel.add(btnRefresh);

		titlePanel.add(buttonPanel, BorderLayout.EAST);

		// Main content panel
		JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
		contentPanel.setBackground(getBackground());

		// Table
		createTable();
		JScrollPane scrollPane = new JScrollPane(tblNhanVien);
		scrollPane.setBorder(new RoundedBorder(8, BORDER));

		// Form panel (initially hidden)
		formPanel = createFormPanel();
		formPanel.setVisible(false);

		contentPanel.add(scrollPane, BorderLayout.CENTER);
		contentPanel.add(formPanel, BorderLayout.EAST);

		// Add components to main panel
		add(titlePanel, BorderLayout.NORTH);
		add(contentPanel, BorderLayout.CENTER);
	}

	private void createTable() {
		// Create table model with columns
		String[] columns = { "Mã NV", "Họ tên", "Giới tính", "Ngày sinh", "Số điện thoại", "Địa chỉ", "Chức vụ" };

		tableModel = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tblNhanVien = new JTable(tableModel);
		styleTable(tblNhanVien);

		// Add selection listener
		tblNhanVien.getSelectionModel().addListSelectionListener(e -> {
			boolean validSelection = tblNhanVien.getSelectedRow() != -1;
			btnEdit.setEnabled(validSelection);
			btnDelete.setEnabled(validSelection);
		});
	}

	private JPanel createFormPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 20));
		panel.setBackground(PRIMARY);
		panel.setBorder(
				new CompoundBorder(new RoundedBorder(8, BORDER), BorderFactory.createEmptyBorder(20, 20, 20, 20)));
		panel.setPreferredSize(new Dimension(400, 0));

		// Form title
		JLabel lblFormTitle = new JLabel("Thêm nhân viên mới");
		lblFormTitle.setFont(HEADER_FONT);

		// Fields panel
		JPanel fieldsPanel = new JPanel();
		fieldsPanel.setLayout(new GridBagLayout());
		fieldsPanel.setBackground(PRIMARY);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 0, 15, 0);

		// Mã nhân viên
		addFormField(fieldsPanel, "Mã nhân viên:", txtMaNV = createTextField(), gbc);

		// Họ tên
		addFormField(fieldsPanel, "Họ tên:", txtHoTen = createTextField(), gbc);

		// Giới tính
		String[] genders = { "Nam", "Nữ" };
		cboGioiTinh = new JComboBox<>(genders);
		styleComboBox(cboGioiTinh);
		addFormField(fieldsPanel, "Giới tính:", cboGioiTinh, gbc);

		// Ngày sinh
		dateNgaySinh = new JDateChooser();
		dateNgaySinh.setPreferredSize(new Dimension(200, 35));
		dateNgaySinh.setDateFormatString("dd/MM/yyyy");
		addFormField(fieldsPanel, "Ngày sinh:", dateNgaySinh, gbc);

		// Số điện thoại
		addFormField(fieldsPanel, "Số điện thoại:", txtSoDT = createTextField(), gbc);

		// Địa chỉ
		addFormField(fieldsPanel, "Địa chỉ:", txtDiaChi = createTextField(), gbc);

		// Chức vụ
		String[] roles = { "Nhân viên bán hàng", "Quản lý", "Thu ngân" };
		cboChucVu = new JComboBox<>(roles);
		styleComboBox(cboChucVu);
		addFormField(fieldsPanel, "Chức vụ:", cboChucVu, gbc);

		// Buttons panel
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		buttonsPanel.setBackground(PRIMARY);

		JButton btnSave = createStyledButton("Lưu", SUCCESS, Color.WHITE, true);
		JButton btnCancel = createStyledButton("Hủy", Color.WHITE, TEXT, false);

		btnSave.addActionListener(e -> saveNhanVien());
		btnCancel.addActionListener(e -> hideForm());

		buttonsPanel.add(btnSave);
		buttonsPanel.add(btnCancel);

		// Add all to form panel
		panel.add(lblFormTitle, BorderLayout.NORTH);
		panel.add(fieldsPanel, BorderLayout.CENTER);
		panel.add(buttonsPanel, BorderLayout.SOUTH);

		return panel;
	}

	private JTextField createTextField() {
		JTextField textField = new JTextField();
		textField.setPreferredSize(new Dimension(200, 35));
		styleTextField(textField, "");
		return textField;
	}

	private void addFormField(JPanel panel, String label, Component field, GridBagConstraints gbc) {
		JLabel lbl = new JLabel(label);
		lbl.setFont(BODY_FONT);
		gbc.insets = new Insets(0, 0, 5, 0);
		panel.add(lbl, gbc);
		gbc.insets = new Insets(0, 0, 15, 0);
		panel.add(field, gbc);
	}

	private void styleTextField(JTextField textField, String placeholder) {
		textField.setFont(BODY_FONT);
		textField.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(6, BORDER),
				BorderFactory.createEmptyBorder(5, 15, 5, 15)));

		if (!placeholder.isEmpty()) {
			textField.setForeground(Color.GRAY);
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
						textField.setForeground(Color.GRAY);
						textField.setText(placeholder);
					}
				}
			});
		}
	}

	private void styleComboBox(JComboBox<String> comboBox) {
		comboBox.setFont(BODY_FONT);
		comboBox.setPreferredSize(new Dimension(200, 35));
		comboBox.setBackground(PRIMARY);
		comboBox.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(6, BORDER),
				BorderFactory.createEmptyBorder(5, 15, 5, 15)));

		// Custom UI for dropdown
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
	}

	private JButton createStyledButton(String text, Color bg, Color fg, boolean isAccent) {
		JButton button = new JButton(text);
		button.setFont(BODY_FONT);
		button.setForeground(fg);
		button.setBackground(bg);
		button.setPreferredSize(new Dimension(100, 40));
		button.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(6, isAccent ? bg : BORDER),
				BorderFactory.createEmptyBorder(5, 15, 5, 15)));
		button.setFocusPainted(false);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));

		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				button.setBackground(isAccent ? bg.darker() : SECONDARY);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button.setBackground(bg);
			}
		});

		return button;
	}

	private void styleTable(JTable table) {
		// Font settings
		Font headerFont = new Font("Segoe UI", Font.BOLD, 14);
		Font cellFont = new Font("Segoe UI", Font.PLAIN, 14);

		// Colors
		Color headerBgColor = new Color(240, 247, 255);
		Color headerFgColor = new Color(30, 58, 138);
		Color rowEvenColor = Color.WHITE;
		Color rowOddColor = new Color(248, 250, 252);
		Color selectedBgColor = new Color(219, 234, 254);
		Color selectedFgColor = new Color(30, 58, 138);

		table.setFont(cellFont);
		table.setRowHeight(45);
		table.setShowGrid(true);
		table.setGridColor(BORDER);
		table.setBackground(rowEvenColor);
		table.setSelectionBackground(selectedBgColor);
		table.setSelectionForeground(selectedFgColor);

		// Header style
		JTableHeader header = table.getTableHeader();
		header.setFont(headerFont);
		header.setBackground(headerBgColor);
		header.setForeground(headerFgColor);
		header.setBorder(BorderFactory.createLineBorder(BORDER));
		header.setPreferredSize(new Dimension(header.getWidth(), 50));

		// Prevent column reordering and resizing
		header.setReorderingAllowed(false);
		header.setResizingAllowed(false);

		// Center align for header
		((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

		// Custom cell renderers
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

				if (!isSelected) {
					c.setBackground(row % 2 == 0 ? rowEvenColor : rowOddColor);
					c.setForeground(TEXT);
				}
				setHorizontalAlignment(JLabel.CENTER);
				setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
				return c;
			}
		};

		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

				if (!isSelected) {
					c.setBackground(row % 2 == 0 ? rowEvenColor : rowOddColor);
					c.setForeground(TEXT);
				}
				setHorizontalAlignment(JLabel.LEFT);
				setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 5));
				return c;
			}
		};

		// Apply renderers to columns
		table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Mã NV
		table.getColumnModel().getColumn(1).setCellRenderer(leftRenderer); // Họ tên
		table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // Giới tính
		table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Ngày sinh
		table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // SĐT
		table.getColumnModel().getColumn(5).setCellRenderer(leftRenderer); // Địa chỉ
		table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer); // Chức vụ

		// Set column widths
		int[] columnWidths = { 100, 200, 100, 120, 120, 250, 150 };
		for (int i = 0; i < columnWidths.length; i++) {
			table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
			table.getColumnModel().getColumn(i).setMinWidth(columnWidths[i]);
		}
	}




	private void showAddForm() {
		isEditing = false;
		((JLabel) formPanel.getComponent(0)).setText("Thêm nhân viên mới");
		clearForm();
		generateNewId();
		txtMaNV.setEditable(false);
		formPanel.setVisible(true);
	}

	private void showEditForm() {
		if (tblNhanVien.getSelectedRow() == -1) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa!", "Thông báo",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		isEditing = true;
		((JLabel) formPanel.getComponent(0)).setText("Sửa thông tin nhân viên");
		loadDataToForm();
		txtMaNV.setEditable(false);
		formPanel.setVisible(true);
	}

	private void generateNewId() {
		 txtMaNV.setText(nvDAO.generateNewId());
	}

	private void loadDataToForm() {
		int row = tblNhanVien.getSelectedRow();
		txtMaNV.setText(tableModel.getValueAt(row, 0).toString());
		txtHoTen.setText(tableModel.getValueAt(row, 1).toString());
		cboGioiTinh.setSelectedItem(tableModel.getValueAt(row, 2).toString());

		try {
			Date date = new SimpleDateFormat("dd/MM/yyyy").parse(tableModel.getValueAt(row, 3).toString());
			dateNgaySinh.setDate(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		txtSoDT.setText(tableModel.getValueAt(row, 4).toString());
		txtDiaChi.setText(tableModel.getValueAt(row, 5).toString());
		cboChucVu.setSelectedItem(tableModel.getValueAt(row, 6).toString());
	}

	private void clearForm() {
		txtMaNV.setText("");
		txtHoTen.setText("");
		cboGioiTinh.setSelectedIndex(0);
		dateNgaySinh.setDate(null);
		txtSoDT.setText("");
		txtDiaChi.setText("");
		cboChucVu.setSelectedIndex(0);
	}

	private void hideForm() {
		formPanel.setVisible(false);
		clearForm();
	}

	private void saveNhanVien() {
	    if (!validateForm()) return;

	    try {
	        NhanVien nv = new NhanVien();
	        nv.setMaNV(txtMaNV.getText().trim());
	        nv.setHoTen(txtHoTen.getText().trim());
	        nv.setGioiTinh(cboGioiTinh.getSelectedItem().toString());
	        nv.setNgaySinh(dateNgaySinh.getDate());
	        nv.setSdt(txtSoDT.getText().trim());
	        nv.setDiaChi(txtDiaChi.getText().trim());
	        nv.setChucVu(cboChucVu.getSelectedItem().toString());

	        boolean result;
	        if (isEditing) {
	            result = nvDAO.update(nv);
	            if(result) {
	                JOptionPane.showMessageDialog(this, 
	                    "Cập nhật thông tin nhân viên thành công!", 
	                    "Thông báo",
	                    JOptionPane.INFORMATION_MESSAGE);
	            }
	        } else {
	            result = nvDAO.add(nv);
	            if(result) {
	                JOptionPane.showMessageDialog(this, 
	                    "Thêm nhân viên mới thành công!", 
	                    "Thông báo",
	                    JOptionPane.INFORMATION_MESSAGE);
	            }
	        }

	        if(result) {
	            loadData();
	            hideForm();
	        } else {
	            showError("Xảy ra lỗi khi lưu thông tin!");
	        }
	        
	    } catch(Exception e) {
	        showError("Xảy ra lỗi: " + e.getMessage());
	    }
	}

	private boolean validateForm() {
		// Validate họ tên
		if (txtHoTen.getText().trim().isEmpty()) {
			showError("Vui lòng nhập họ tên!");
			return false;
		}

		// Validate ngày sinh
		if (dateNgaySinh.getDate() == null) {
			showError("Vui lòng chọn ngày sinh!");
			return false;
		}

		// Validate số điện thoại
		String phone = txtSoDT.getText().trim();
		if (phone.isEmpty()) {
			showError("Vui lòng nhập số điện thoại!");
			return false;
		}
		if (!phone.matches("\\d{10}")) {
			showError("Số điện thoại không hợp lệ!");
			return false;
		}

		// Validate địa chỉ
		if (txtDiaChi.getText().trim().isEmpty()) {
			showError("Vui lòng nhập địa chỉ!");
			return false;
		}

		return true;
	}

	private void showError(String message) {
		JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
	}

	private void deleteNhanVien() {
	    int row = tblNhanVien.getSelectedRow();
	    if (row == -1) {
	        JOptionPane.showMessageDialog(this,
	            "Vui lòng chọn nhân viên cần xóa!",
	            "Thông báo",
	            JOptionPane.WARNING_MESSAGE);
	        return;
	    }

	    String maNV = tableModel.getValueAt(row, 0).toString();
	    int confirm = JOptionPane.showConfirmDialog(this,
	        "Bạn có chắc muốn xóa nhân viên này?",
	        "Xác nhận xóa",
	        JOptionPane.YES_NO_OPTION);

	    if (confirm == JOptionPane.YES_OPTION) {
	        if(nvDAO.delete(maNV)) {
	            tableModel.removeRow(row);
	            JOptionPane.showMessageDialog(this,
	                "Xóa nhân viên thành công!",
	                "Thông báo",
	                JOptionPane.INFORMATION_MESSAGE);
	        } else {
	            showError("Xảy ra lỗi khi xóa nhân viên!");
	        }
	    }
	}


	private void refreshData() {
		hideForm();
	}

	private void searchNhanVien() {
	    String keyword = txtSearch.getText().toLowerCase().trim();
	    if(keyword.length() == 0) {
	        loadData();
	        return;
	    }
	    
	    tableModel.setRowCount(0);
	    ArrayList<NhanVien> list = nvDAO.search(keyword);
	    
	    for(NhanVien nv : list) {
	        Object[] row = {
	            nv.getMaNV(),
	            nv.getHoTen(),
	            nv.getGioiTinh(),
	            sdf.format(nv.getNgaySinh()),
	            nv.getSdt(),
	            nv.getDiaChi(),
	            nv.getChucVu()
	        };
	        tableModel.addRow(row);
	    }
	}
}
