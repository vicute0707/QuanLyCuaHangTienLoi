package dialog;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import dao.DanhMucDAO;
import dao.SanPhamDAO;
import entity.SanPham;
import style.RoundedBorder;
import view.Form_SanPham;

public class ThemSanPhamDialog extends JDialog {
    // Colors
    private static final Color PRIMARY = new Color(255, 255, 255);
    private static final Color SECONDARY = new Color(243, 244, 246);
    private static final Color ACCENT = new Color(37, 99, 235);
    private static final Color ACCENT_HOVER = new Color(29, 78, 216);
    private static final Color TEXT = new Color(17, 24, 39);
    private static final Color TEXT_SECONDARY = new Color(107, 114, 128);
    private static final Color BORDER = new Color(229, 231, 235);
    
    // Fonts
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    
    // Component sizes
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
    private SanPhamDAO sanPhamDAO;
    private String selectedImagePath;
    private DecimalFormat currencyFormat;
    DanhMucDAO dmDao;
    private Form_SanPham parentForm;
    

    public ThemSanPhamDialog(Form_SanPham parentForm) {
        super(SwingUtilities.getWindowAncestor(parentForm));
    	 dmDao = new DanhMucDAO();
         sanPhamDAO = new SanPhamDAO();
         if(parentForm == null) {
        	 throw new IllegalArgumentException("Form parrent null ");
         }
         this.parentForm = parentForm;
         // Initialize formatter
         currencyFormat = (DecimalFormat) NumberFormat.getInstance(new Locale("vi", "VN"));
         currencyFormat.applyPattern("###,###");
         
         setTitle("Thêm Sản Phẩm Mới");
         setSize(600, 650);
         setLocationRelativeTo(null);
         setModalityType(ModalityType.APPLICATION_MODAL);
         setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
         
         initComponents();
         loadInitialData();
         setupValidation();
         
       
    }
    private void loadInitialData() {
        // Tự động generate mã SP mới
        String newMaSP = sanPhamDAO.generateNewId();
        txtMaSP.setText(newMaSP);
        txtMaSP.setEditable(false);
        
        // Load danh mục từ database
        loadCategories();
    }
    private void loadCategories() {
        cmbDanhMuc.removeAllItems();
        ArrayList<String> categories = dmDao.getAllDanhMuc();
        cmbDanhMuc.addItem("Chọn danh mục");
        for(String category : categories) {
            cmbDanhMuc.addItem(category);
        }
    }
    private void setupValidation() {
        // Validate số lượng chỉ nhập số
        txtSoLuong.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
            }
        });

        // Validate giá bán chỉ nhập số
        txtGiaBan.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
            }
        });
        
        // Format giá bán khi focus lost
        txtGiaBan.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (!txtGiaBan.getText().equals("Nhập giá bán")) {
                    try {
                        double amount = Double.parseDouble(txtGiaBan.getText().replaceAll("[,.]", ""));
                        txtGiaBan.setText(currencyFormat.format(amount));
                    } catch (NumberFormatException ex) {
                        txtGiaBan.setText("");
                    }
                }
            }
            
            @Override
            public void focusGained(FocusEvent e) {
                if (!txtGiaBan.getText().equals("Nhập giá bán")) {
                    txtGiaBan.setText(txtGiaBan.getText().replaceAll("[,.]", ""));
                }
            }
        });
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

    private JPanel createFormSection(String title) {
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

        // Mã sản phẩm
        addFormField(panel, gbc, 0, "Mã sản phẩm:", 
            txtMaSP = createStyledTextField("Nhập mã sản phẩm"));

        // Tên sản phẩm
        addFormField(panel, gbc, 1, "Tên sản phẩm:", 
            txtTenSP = createStyledTextField("Nhập tên sản phẩm"));

        // Danh mục
        String[] categories = {"Chọn danh mục", "Nước giải khát", "Thực phẩm", "Bánh kẹo", "Đồ dùng"};
        cmbDanhMuc = new JComboBox<>(categories);
        styleComboBox(cmbDanhMuc);
        addFormField(panel, gbc, 2, "Danh mục:", cmbDanhMuc);

        // Đơn vị tính
        addFormField(panel, gbc, 3, "Đơn vị tính:", 
            txtDonViTinh = createStyledTextField("Nhập đơn vị tính"));

        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
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

    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, 
                            String labelText, JComponent field) {
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

    private void selectImage() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Image files", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            selectedImagePath = file.getAbsolutePath();
            lblImagePath.setText(file.getName());
            
            // Hiển thị preview
            ImageIcon imageIcon = new ImageIcon(selectedImagePath);
            Image image = imageIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            pnlImagePreview.getGraphics().drawImage(image, 0, 0, null);
        }
    }
    private boolean validateInputs() {
        if (txtTenSP.getText().isEmpty() || txtTenSP.getText().equals("Nhập tên sản phẩm")) {
            showError("Vui lòng nhập tên sản phẩm!");
            return false;
        }
        
        if (cmbDanhMuc.getSelectedIndex() == 0) {
            showError("Vui lòng chọn danh mục!");
            return false;
        }
        
        if (txtDonViTinh.getText().isEmpty() || txtDonViTinh.getText().equals("Nhập đơn vị tính")) {
            showError("Vui lòng nhập đơn vị tính!");
            return false;
        }
        
        if (txtGiaBan.getText().isEmpty() || txtGiaBan.getText().equals("Nhập giá bán")) {
            showError("Vui lòng nhập giá bán!");
            return false;
        }
        
        if (txtSoLuong.getText().isEmpty() || txtSoLuong.getText().equals("Nhập số lượng")) {
            showError("Vui lòng nhập số lượng!");
            return false;
        }
        
        return true;
    }
    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
            message,
            "Lỗi",
            JOptionPane.ERROR_MESSAGE);
    }

    private void saveProduct() {  
        if (!validateInputs()) {  
            return;  
        }  

        try {  
            SanPham sp = new SanPham();  
            sp.setMaSP(txtMaSP.getText());  
            sp.setTenSP(txtTenSP.getText());  
            sp.setMaDM(dmDao.getMaDanhMuc(cmbDanhMuc.getSelectedItem().toString()));
            sp.setTenDM(cmbDanhMuc.getSelectedItem().toString());
            sp.setDonVi(txtDonViTinh.getText());  

            String giaStr = txtGiaBan.getText().replaceAll("[,đ\\s.]", "");  
            sp.setGiaBan(Double.parseDouble(giaStr));  

            String soLuongStr = txtSoLuong.getText().replaceAll("[,\\s.]", "");  
            sp.setSoLuong(Integer.parseInt(soLuongStr));  

            if (selectedImagePath != null && !selectedImagePath.isEmpty()) {  
                sp.setHinhAnh(selectedImagePath);  
            }  

            // Xác định trạng thái  
            if (sp.getSoLuong() == 0) {  
                sp.setTrangThai("Hết hàng");  
            } else if (sp.getSoLuong() <= 10) {  
                sp.setTrangThai("Sắp hết");  
            } else {  
                sp.setTrangThai("Còn hàng");  
            }  

            System.out.println("Saving product: " + sp.getMaSP());  

            // Kiểm tra xem sanPhamDAO có được khởi tạo không  
            if (sanPhamDAO == null) {  
                showError("Lỗi: SanPhamDAO chưa được khởi tạo.");  
                return;  
            }  

            if (sanPhamDAO.add(sp)) {  
                JOptionPane.showMessageDialog(this,  
                        "Thêm sản phẩm thành công!",  
                        "Thông báo",  
                        JOptionPane.INFORMATION_MESSAGE);  
                System.out.println("Product saved successfully");  

                // Cập nhật bảng trong form cha  
                if (parentForm != null) {  
                    parentForm.refreshTable();  
                } else {  
                    System.err.println("Warning: parentForm is null!");  
                }  
                dispose();  
            } else {  
                showError("Không thể thêm sản phẩm!");  
            }  

        } catch (NumberFormatException e) {  
            showError("Giá bán hoặc số lượng không hợp lệ!");  
            e.printStackTrace();  
        } catch (Exception e) {  
            showError("Đã xảy ra lỗi: " + e.getMessage());  
            e.printStackTrace();  
        }  
    }  

 
}