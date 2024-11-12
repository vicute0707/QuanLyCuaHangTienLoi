// view/Form_Kho.java
package view;

import dao.KhoDAO;
import entity.Kho;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.List;

public class Form_Kho extends JPanel {
    private JTextField txtMaKho, txtTenKho;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh;
    private JTable tableKho;
    private KhoDAO khoDAO;
    private DefaultTableModel tableModel;

    public Form_Kho() {
        this.khoDAO = new KhoDAO();
        initComponents();
        loadKhoData();
    }
    

    

	private void initComponents() {
        setLayout(new BorderLayout());

        JPanel panelInput = new JPanel(new GridLayout(2, 2));
        JLabel label = new JLabel("Mã Kho:");
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panelInput.add(label);
        txtMaKho = new JTextField();
        txtMaKho.setEnabled(false); 
        panelInput.add(txtMaKho);
        
                JPanel panelButtons = new JPanel(new FlowLayout());
                panelInput.add(panelButtons);
                btnAdd = new JButton("Thêm");
                btnUpdate = new JButton("Sửa");
                btnDelete = new JButton("Xóa");
                btnRefresh = new JButton("Làm mới");
                
                        panelButtons.add(btnAdd);
                        panelButtons.add(btnUpdate);
                        panelButtons.add(btnDelete);
                        panelButtons.add(btnRefresh);
                        
                                btnAdd.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        addKho();
                                    }
                                });
                                
                                        btnUpdate.addActionListener(new ActionListener() {
                                            public void actionPerformed(ActionEvent e) {
                                                updateKho();
                                            }
                                        });
                                        
                                                btnDelete.addActionListener(new ActionListener() {
                                                    public void actionPerformed(ActionEvent e) {
                                                        deleteKho();
                                                    }
                                                });
                                                
                                                        btnRefresh.addActionListener(new ActionListener() {
                                                            public void actionPerformed(ActionEvent e) {
                                                                loadKhoData();
                                                                clearInputFields();
                                                            }
                                                        });

        JLabel label_1 = new JLabel("Tên Kho:");
        label_1.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panelInput.add(label_1);
        txtTenKho = new JTextField();
        panelInput.add(txtTenKho);

        tableModel = new DefaultTableModel(new Object[][]{}, new String[]{"Mã Kho", "Tên Kho"});
        tableKho = new JTable(tableModel);
        tableKho.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tableKho);

        add(panelInput, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.SOUTH);

        tableKho.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tableKho.getSelectedRow();
                if (selectedRow != -1) {
                    txtMaKho.setText(tableKho.getValueAt(selectedRow, 0).toString());
                    txtTenKho.setText(tableKho.getValueAt(selectedRow, 1).toString());
                }
            }
        });
    }

    private void loadKhoData() {
        try {
            List<Kho> khoList = khoDAO.getAllKho();
            tableModel.setRowCount(0); 
            for (Kho kho : khoList) {
                tableModel.addRow(new Object[]{kho.getMaKho(), kho.getTenKho()});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu kho!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void addKho() {
        try {
            String tenKho = txtTenKho.getText();
            if (tenKho.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên kho không được để trống!");
                return;
            }
            Kho kho = new Kho();
            kho.setTenKho(tenKho);
            khoDAO.addKho(kho);
            loadKhoData();
            clearInputFields();
            JOptionPane.showMessageDialog(this, "Thêm kho thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm kho!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void updateKho() {
        try {
            int maKho = Integer.parseInt(txtMaKho.getText());
            String tenKho = txtTenKho.getText();
            if (tenKho.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên kho không được để trống!");
                return;
            }
            Kho kho = new Kho(maKho, tenKho);
            khoDAO.updateKho(kho);
            loadKhoData();
            clearInputFields();
            JOptionPane.showMessageDialog(this, "Cập nhật kho thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật kho!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void deleteKho() {
        try {
            int maKho = Integer.parseInt(txtMaKho.getText());
            khoDAO.deleteKho(maKho);
            loadKhoData();
            clearInputFields();
            JOptionPane.showMessageDialog(this, "Xóa kho thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi xóa kho!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void clearInputFields() {
        txtMaKho.setText("");
        txtTenKho.setText("");
    }
}
