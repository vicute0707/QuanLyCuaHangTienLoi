package dialog;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import style.RoundedBorder;

public class ChiTietHoaDonDialog extends JDialog {
    // Constants
    private static final Color PRIMARY = new Color(255, 255, 255);
    private static final Color SECONDARY = new Color(243, 244, 246);
    private static final Color ACCENT = new Color(37, 99, 235);
    private static final Color BORDER = new Color(229, 231, 235);
    private static final Color TEXT = new Color(17, 24, 39);
    
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    
    // Components
    private DecimalFormat currencyFormat;
    private DefaultTableModel tableModel;
    private JTable tblChiTiet;
    private JLabel lblTotalAmount;
    
    // Data from Form_BanHang
    private String staffName;
    private String customerName;
    private String customerPhone;
    private String customerAddress;
    private double totalAmount;
    private DefaultTableModel cartModel;

    public ChiTietHoaDonDialog(Frame parent, String staffName, String customerName, 
            String customerPhone, String customerAddress, 
            DefaultTableModel cartModel, double totalAmount) {
        super(parent, "Chi tiết hóa đơn", true);
        this.staffName = staffName;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerAddress = customerAddress;
        this.cartModel = cartModel;
        this.totalAmount = totalAmount;
        this.currencyFormat = new DecimalFormat("#,### đ");
        
        initComponents();
        loadData();
    }

    private void initComponents() {
        setSize(800, 700);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        getContentPane().setBackground(PRIMARY);
        
        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(PRIMARY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Top Section - Header Info
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Center Section - Products Table
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);
        
        // Bottom Section - Totals and Buttons
        mainPanel.add(createBottomPanel(), BorderLayout.SOUTH);
        
        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(PRIMARY);
        
        // Store Info
        JPanel storePanel = new JPanel();
        storePanel.setLayout(new BoxLayout(storePanel, BoxLayout.Y_AXIS));
        storePanel.setBackground(PRIMARY);
        
        JLabel lblStoreName = new JLabel("TV SHOP");
        lblStoreName.setFont(TITLE_FONT);
        lblStoreName.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblStoreAddress = new JLabel("97 Man Thiện, Tp.Thủ Đức");
        lblStoreAddress.setFont(BODY_FONT);
        lblStoreAddress.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblStorePhone = new JLabel("0123 456 789");
        lblStorePhone.setFont(BODY_FONT);
        lblStorePhone.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        storePanel.add(lblStoreName);
        storePanel.add(Box.createVerticalStrut(5));
        storePanel.add(lblStoreAddress);
        storePanel.add(Box.createVerticalStrut(3));
        storePanel.add(lblStorePhone);
        
        // Invoice Info
        JPanel invoicePanel = new JPanel(new BorderLayout(20, 10));
        invoicePanel.setBackground(PRIMARY);
        invoicePanel.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(8, BORDER),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Left side of invoice info
        JPanel leftInfo = new JPanel(new GridLayout(4, 1, 0, 10));
        leftInfo.setBackground(PRIMARY);
        
        // Generate invoice number and date
        String invoiceNo = String.format("HD%06d", (int)(Math.random() * 1000000));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String currentDate = sdf.format(new Date());
        
        addInfoRow(leftInfo, "Số hóa đơn:", invoiceNo);
        addInfoRow(leftInfo, "Ngày:", currentDate);
        addInfoRow(leftInfo, "Nhân viên:", staffName);
        
        // Right side of invoice info
        JPanel rightInfo = new JPanel(new GridLayout(4, 1, 0, 10));
        rightInfo.setBackground(PRIMARY);
        
        addInfoRow(rightInfo, "Khách hàng:", customerName);
        addInfoRow(rightInfo, "Số điện thoại:", customerPhone);
        if (customerAddress != null && !customerAddress.isEmpty()) {
            addInfoRow(rightInfo, "Địa chỉ:", customerAddress);
        }
        
        invoicePanel.add(leftInfo, BorderLayout.WEST);
        invoicePanel.add(rightInfo, BorderLayout.EAST);
        
        // Add all to header panel
        panel.add(storePanel, BorderLayout.NORTH);
        panel.add(invoicePanel, BorderLayout.CENTER);
        
        return panel;
    }

    private void addInfoRow(JPanel panel, String label, String value) {
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        rowPanel.setBackground(PRIMARY);
        
        JLabel lblTitle = new JLabel(label);
        lblTitle.setFont(BODY_FONT);
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(HEADER_FONT);
        
        rowPanel.add(lblTitle);
        rowPanel.add(lblValue);
        panel.add(rowPanel);
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Table Model
        String[] columns = {"STT", "Sản phẩm", "Đơn giá", "Số lượng", "Thành tiền"};
        tableModel = new DefaultTableModel(columns, 0);
        
        tblChiTiet = new JTable(tableModel);
        styleTable(tblChiTiet);
        
        JScrollPane scrollPane = new JScrollPane(tblChiTiet);
        scrollPane.setBorder(new RoundedBorder(8, BORDER));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(PRIMARY);
        
        // Summary Panel
        JPanel summaryPanel = new JPanel(new GridBagLayout());
        summaryPanel.setBackground(PRIMARY);
        summaryPanel.setBorder(new RoundedBorder(8, BORDER));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(15, 15, 15, 10);
        
        JLabel lblTotal = new JLabel("Tổng tiền thanh toán:");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        summaryPanel.add(lblTotal, gbc);
        
        gbc.gridx = 1;
        gbc.insets = new Insets(15, 10, 15, 15);
        
        lblTotalAmount = new JLabel(currencyFormat.format(totalAmount));
        lblTotalAmount.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTotalAmount.setForeground(ACCENT);
        summaryPanel.add(lblTotalAmount, gbc);
        
        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setBackground(PRIMARY);
        
        JButton btnPrint = createStyledButton("In hóa đơn", ACCENT, Color.BLUE, true);
        JButton btnClose = createStyledButton("Đóng", Color.WHITE, TEXT, false);
        
        btnPrint.setPreferredSize(new Dimension(150, 40));
        btnClose.setPreferredSize(new Dimension(100, 40));
        
        btnPrint.addActionListener(e -> printInvoice());
        btnClose.addActionListener(e -> dispose());
        
        buttonsPanel.add(btnPrint);
        buttonsPanel.add(btnClose);
        
        panel.add(summaryPanel, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private JButton createStyledButton(String text, Color bg, Color fg, boolean isAccent) {
        JButton button = new JButton(text);
        button.setFont(HEADER_FONT);
        button.setForeground(fg);
        button.setBackground(bg);
        button.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(6, isAccent ? bg : BORDER),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
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
        Color headerBgColor = new Color(220, 241, 255);
        Color selectedBgColor = new Color(240, 248, 255);
        Color alternateRowColor = new Color(250, 252, 255);
        
        table.setFont(cellFont);
        table.setRowHeight(35);
        table.setShowGrid(true);
        table.setGridColor(BORDER);
        table.setSelectionBackground(selectedBgColor);
        table.setSelectionForeground(TEXT);
        
        // Style header
        JTableHeader header = table.getTableHeader();
        header.setFont(headerFont);
        header.setBackground(headerBgColor);
        header.setForeground(TEXT);
        header.setBorder(BorderFactory.createLineBorder(BORDER));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        
        // Cell renderers
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : alternateRowColor);
                }
                return c;
            }
        };
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : alternateRowColor);
                }
                return c;
            }
        };
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
        // Apply renderers
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // STT
        table.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);  // Đơn giá
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Số lượng
        table.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);  // Thành tiền
        
        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(50);  // STT
        table.getColumnModel().getColumn(1).setPreferredWidth(250); // Tên SP
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // Đơn giá
        table.getColumnModel().getColumn(3).setPreferredWidth(80);  // Số lượng
        table.getColumnModel().getColumn(4).setPreferredWidth(120); // Thành tiền
    }

    private void loadData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Load data from cart model
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            tableModel.addRow(new Object[]{
                i + 1, // STT
                cartModel.getValueAt(i, 1), // Tên SP
                cartModel.getValueAt(i, 2), // Đơn giá
                cartModel.getValueAt(i, 3), // Số lượng
                cartModel.getValueAt(i, 4)  // Thành tiền
            });
        }
    }

    private void printInvoice() {
        // TODO: Implement printing functionality
        JOptionPane.showMessageDialog(this,
            "Chức năng in hóa đơn sẽ được cập nhật sau!",
            "Thông báo",
            JOptionPane.INFORMATION_MESSAGE);
    }
}
