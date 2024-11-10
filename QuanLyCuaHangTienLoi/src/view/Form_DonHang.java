package view;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import dialog.ChiTietHoaDonDialog;
import style.RoundedBorder;

public class Form_DonHang extends JPanel {
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
    private JTable tblDonHang;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JComboBox<String> cboFilter;
    private DecimalFormat currencyFormat;
    private SimpleDateFormat dateFormat;

    public Form_DonHang() {
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        currencyFormat = new DecimalFormat("#,### đ");
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        initComponents();
        loadSampleData();
    }

    private void initComponents() {
        // Title Panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(PRIMARY);
        titlePanel.setBorder(new CompoundBorder(
            new RoundedBorder(8, BORDER),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblTitle = new JLabel("Quản lý đơn hàng");
        lblTitle.setFont(TITLE_FONT);
        titlePanel.add(lblTitle, BorderLayout.WEST);

        // Actions Panel (Search & Filter)
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        actionsPanel.setBackground(PRIMARY);

        // Search field
        txtSearch = new JTextField(20);
        txtSearch.setPreferredSize(new Dimension(200, 35));
        styleTextField(txtSearch, "Tìm kiếm đơn hàng...");
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { searchOrders(); }
            public void insertUpdate(DocumentEvent e) { searchOrders(); }
            public void removeUpdate(DocumentEvent e) { searchOrders(); }
        });

        // Filter combo box
        String[] filterOptions = {"Tất cả", "Hôm nay", "Tuần này", "Tháng này"};
        cboFilter = new JComboBox<>(filterOptions);
        cboFilter.setPreferredSize(new Dimension(150, 35));
        styleComboBox(cboFilter);
        cboFilter.addActionListener(e -> filterOrders());

        actionsPanel.add(txtSearch);
        actionsPanel.add(cboFilter);

        titlePanel.add(actionsPanel, BorderLayout.EAST);

        // Table
        createTable();
        JScrollPane scrollPane = new JScrollPane(tblDonHang);
        scrollPane.setBorder(new RoundedBorder(8, BORDER));

        // Control Panel (below table)
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.setBackground(PRIMARY);
        controlPanel.setBorder(new CompoundBorder(
            new RoundedBorder(8, BORDER),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Stats Panel (left side of control panel)
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 0));
        statsPanel.setBackground(PRIMARY);

        addStatLabel(statsPanel, "Tổng đơn hàng:", "0");
        addStatLabel(statsPanel, "Doanh thu:", "0 đ");
        
        // Buttons Panel (right side of control panel)
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setBackground(PRIMARY);

        JButton btnRefresh = createStyledButton("Làm mới", Color.WHITE, TEXT, false);
        JButton btnExport = createStyledButton("Xuất Excel", ACCENT, Color.BLUE, true);
        
        btnRefresh.addActionListener(e -> refreshData());
        btnExport.addActionListener(e -> exportToExcel());

        buttonsPanel.add(btnRefresh);
        buttonsPanel.add(btnExport);

        controlPanel.add(statsPanel, BorderLayout.WEST);
        controlPanel.add(buttonsPanel, BorderLayout.EAST);

        // Add all components to main panel
        add(titlePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void createTable() {
        // Create table model with columns
        String[] columns = {
            "Mã ĐH", "Ngày tạo", "Khách hàng", "SĐT", "Nhân viên", 
            "Tổng tiền", "Trạng thái", "Thao tác"
        };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Only allow editing action column
            }
        };

        tblDonHang = new JTable(tableModel);
        styleTable(tblDonHang);

        // Add action buttons column
        TableColumn actionColumn = tblDonHang.getColumnModel().getColumn(7);
        actionColumn.setCellRenderer((TableCellRenderer) new ActionButtonRenderer());
        actionColumn.setCellEditor((TableCellEditor) new ActionButtonEditor());
    }

    private void styleTable(JTable table) {
        // Font settings
        Font headerFont = new Font("Segoe UI", Font.BOLD, 14);
        Font cellFont = new Font("Segoe UI", Font.PLAIN, 14);
        
        // Custom colors for table
        Color headerBgColor = new Color(240, 247, 255);  // Màu nền header - xanh nhạt
        Color headerFgColor = new Color(30, 58, 138);    // Màu chữ header - xanh đậm
        Color rowEvenColor = new Color(255, 255, 255);   // Màu nền dòng chẵn - trắng
        Color rowOddColor = new Color(248, 250, 252);    // Màu nền dòng lẻ - xám nhạt
        Color selectedBgColor = new Color(219, 234, 254); // Màu nền khi select - xanh nhạt
        Color selectedFgColor = new Color(30, 58, 138);   // Màu chữ khi select - xanh đậm
        Color borderColor = new Color(226, 232, 240);     // Màu đường kẻ bảng
        
        // Table settings
        table.setFont(cellFont);
        table.setRowHeight(45); // Tăng chiều cao hàng
        table.setBackground(rowEvenColor);
        table.setGridColor(borderColor);
        table.setSelectionBackground(selectedBgColor);
        table.setSelectionForeground(selectedFgColor);
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        table.setBorder(BorderFactory.createLineBorder(borderColor));
        
        // Intercell spacing
        table.setIntercellSpacing(new Dimension(10, 0));
        
        // Header style
        JTableHeader header = table.getTableHeader();
        header.setFont(headerFont);
        header.setBackground(headerBgColor);
        header.setForeground(headerFgColor);
        header.setBorder(BorderFactory.createLineBorder(borderColor));
        header.setPreferredSize(new Dimension(header.getWidth(), 50)); // Tăng chiều cao header
        
        // Make header not resizable and reorderable
        header.setResizingAllowed(false);
        header.setReorderingAllowed(false);
        
        // Center align for header
        ((DefaultTableCellRenderer)header.getDefaultRenderer())
            .setHorizontalAlignment(JLabel.CENTER);
        
        // Custom renderers
        // Center alignment renderer
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);
                
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? rowEvenColor : rowOddColor);
                    c.setForeground(TEXT);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
                return c;
            }
        };
        
        // Right alignment renderer
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.RIGHT);
                
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? rowEvenColor : rowOddColor);
                    c.setForeground(TEXT);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
                return c;
            }
        };
        
        // Left alignment renderer with padding
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.LEFT);
                
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? rowEvenColor : rowOddColor);
                    c.setForeground(TEXT);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 5));
                return c;
            }
        };
        
        // Apply renderers to columns
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Mã ĐH
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // Ngày tạo
        table.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);   // Khách hàng
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // SĐT
        table.getColumnModel().getColumn(4).setCellRenderer(leftRenderer);   // Nhân viên
        table.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);  // Tổng tiền
        
        // Set preferred widths
        int[] columnWidths = {100, 150, 200, 120, 150, 150, 120, 100};
        for (int i = 0; i < columnWidths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
            table.getColumnModel().getColumn(i).setMinWidth(columnWidths[i]);
        }
    }


    // Custom renderer for status column
    class StatusRenderer extends JLabel implements TableCellRenderer {

        private static final Color SUCCESS_BG = new Color(220, 252, 231);
        private static final Color SUCCESS_FG = new Color(22, 163, 74);
        private static final Color PROCESSING_BG = new Color(254, 249, 195);
        private static final Color PROCESSING_FG = new Color(202, 138, 4);
        private static final Color CANCELED_BG = new Color(254, 226, 226);
        private static final Color CANCELED_FG = new Color(220, 38, 38);
        
        public StatusRenderer() {
            setOpaque(true);
            setHorizontalAlignment(JLabel.CENTER);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            
            // Apply rounded border and padding
            setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(15, getForegroundForStatus(value.toString())),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
            ));
            
            if (!isSelected) {
                setBackground(getBackgroundForStatus(value.toString()));
                setForeground(getForegroundForStatus(value.toString()));
            } else {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            }
            
            return this;
        }
        
        private Color getBackgroundForStatus(String status) {
            switch (status) {
                case "Hoàn thành": return SUCCESS_BG;
                case "Đang xử lý": return PROCESSING_BG;
                case "Đã hủy": return CANCELED_BG;
                default: return Color.WHITE;
            }
        }
        
        private Color getForegroundForStatus(String status) {
            switch (status) {
                case "Hoàn thành": return SUCCESS_FG;
                case "Đang xử lý": return PROCESSING_FG;
                case "Đã hủy": return CANCELED_FG;
                default: return TEXT;
            }
        }
    }

    // Custom renderer for action buttons
    class ActionButtonRenderer extends JPanel implements TableCellRenderer {

        private JButton btnView;

        public ActionButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
            setOpaque(true);
            btnView = new JButton("Chi tiết");
            btnView.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btnView.setForeground(Color.BLUE);
            btnView.setBackground(ACCENT);
            btnView.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(15, ACCENT),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
            ));
            btnView.setFocusPainted(false);
            add(btnView);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(isSelected ? table.getSelectionBackground() : 
                row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
            return this;
        }
    }

    // Custom editor for action buttons
    class ActionButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private JButton btnView;
        private String orderId;

        public ActionButtonEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            btnView = createStyledButton("Chi tiết", ACCENT, Color.WHITE, true);
            btnView.setPreferredSize(new Dimension(80, 30));
            btnView.addActionListener(e -> {
                viewOrderDetails(orderId);
                fireEditingStopped();
            });
            panel.add(btnView);
        }
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            orderId = table.getValueAt(row, 0).toString();
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }

    private void addStatLabel(JPanel panel, String label, String value) {
        JLabel lblTitle = new JLabel(label);
        lblTitle.setFont(BODY_FONT);
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(HEADER_FONT);
        lblValue.setForeground(ACCENT);
        
        panel.add(lblTitle);
        panel.add(lblValue);
    }

    private void loadSampleData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Add sample data
        Object[][] sampleData = {
            {"HD001", new Date(), "Nguyễn Văn A", "0123456789", "NV001", 250000.0, "Hoàn thành", null},
            {"HD002", new Date(), "Trần Thị B", "0987654321", "NV002", 480000.0, "Đang xử lý", null},
            {"HD003", new Date(), "Phạm Văn C", "0369852147", "NV001", 180000.0, "Đã hủy", null}
        };
        
        for (Object[] row : sampleData) {
            row[1] = dateFormat.format(row[1]); // Format date
            row[5] = currencyFormat.format(row[5]); // Format currency
            tableModel.addRow(row);
        }
        
        updateStats();
    }

    private void searchOrders() {
        String keyword = txtSearch.getText().toLowerCase().trim();
        // TODO: Implement search functionality
    }

    private void filterOrders() {
        String filter = (String) cboFilter.getSelectedItem();
        // TODO: Implement filter functionality
    }

    private void refreshData() {
        loadSampleData();
    }

    private void exportToExcel() {
        // TODO: Implement export functionality
        JOptionPane.showMessageDialog(this,
            "Chức năng xuất Excel sẽ được cập nhật sau!",
            "Thông báo",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void viewOrderDetails(String orderId) {
        // TODO: Show order details dialog
        ChiTietHoaDonDialog dialog = new ChiTietHoaDonDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            "NV001", // Sample data - replace with actual data
            "Nguyễn Văn A",
            "0123456789",
            "97 Man Thiện",
            getOrderDetails(orderId),
            250000.0
        );
        dialog.setVisible(true);
    }
    private DefaultTableModel getOrderDetails(String orderId) {
        // Create a sample table model for order details
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Mã SP");
        model.addColumn("Tên SP");
        model.addColumn("Đơn giá");
        model.addColumn("Số lượng");
        model.addColumn("Thành tiền");
        
        // Add sample data based on order ID
        // In real application, this would fetch data from database
        if (orderId.equals("HD001")) {
            model.addRow(new Object[]{"SP001", "Coca Cola", 12000.0, 2, 24000.0});
            model.addRow(new Object[]{"SP002", "Pepsi", 12000.0, 3, 36000.0});
            model.addRow(new Object[]{"SP003", "Snack Lay's", 10000.0, 1, 10000.0});
        }
        
        return model;
    }

    private void updateStats() {
        // Update total orders count
        JLabel lblTotalOrders = (JLabel) ((JPanel) ((JPanel) getComponent(2))
            .getComponent(0)).getComponent(1);
        lblTotalOrders.setText(String.valueOf(tableModel.getRowCount()));
        
        // Calculate and update total revenue
        double totalRevenue = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String revenueStr = tableModel.getValueAt(i, 5).toString()
                .replace(" đ", "").replace(",", "");
            totalRevenue += Double.parseDouble(revenueStr);
        }
        
        JLabel lblTotalRevenue = (JLabel) ((JPanel) ((JPanel) getComponent(2))
            .getComponent(0)).getComponent(3);
        lblTotalRevenue.setText(currencyFormat.format(totalRevenue));
    }

    // Utility methods
    private void styleTextField(JTextField textField, String placeholder) {
        textField.setFont(BODY_FONT);
        textField.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(6, BORDER),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        
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

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(BODY_FONT);
        comboBox.setBackground(PRIMARY);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(6, BORDER),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        
        // Style the dropdown
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
        button.setPreferredSize(new Dimension(100, 35));
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

    // Additional features that could be implemented
    private void searchOrders(String keyword) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        tblDonHang.setRowSorter(sorter);
        
        if (keyword.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            try {
                RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter("(?i)" + keyword, 0, 2, 3, 4);
                sorter.setRowFilter(rf);
            } catch (PatternSyntaxException e) {
                return;
            }
        }
        
        updateStats();
    }

    private void filterOrders(String filterType) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        tblDonHang.setRowSorter(sorter);
        
        if (filterType.equals("Tất cả")) {
            sorter.setRowFilter(null);
        } else {
            Calendar cal = Calendar.getInstance();
            Date now = cal.getTime();
            
            try {
                RowFilter<DefaultTableModel, Object> rf = new RowFilter<DefaultTableModel, Object>() {
                    @Override
                    public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                        try {
                            Date orderDate = dateFormat.parse(entry.getValue(1).toString());
                            
                            switch (filterType) {
                                case "Hôm nay":
                                    return isSameDay(orderDate, now);
                                case "Tuần này":
                                    return isThisWeek(orderDate);
                                case "Tháng này":
                                    return isThisMonth(orderDate);
                                default:
                                    return true;
                            }
                        } catch (ParseException e) {
                            return true;
                        }
                    }
                };
                sorter.setRowFilter(rf);
            } catch (PatternSyntaxException e) {
                return;
            }
        }
        
        updateStats();
    }

    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    private boolean isThisWeek(Date date) {
        Calendar now = Calendar.getInstance();
        Calendar orderDate = Calendar.getInstance();
        orderDate.setTime(date);
        return now.get(Calendar.YEAR) == orderDate.get(Calendar.YEAR) &&
               now.get(Calendar.WEEK_OF_YEAR) == orderDate.get(Calendar.WEEK_OF_YEAR);
    }

    private boolean isThisMonth(Date date) {
        Calendar now = Calendar.getInstance();
        Calendar orderDate = Calendar.getInstance();
        orderDate.setTime(date);
        return now.get(Calendar.YEAR) == orderDate.get(Calendar.YEAR) &&
               now.get(Calendar.MONTH) == orderDate.get(Calendar.MONTH);
    }
}
