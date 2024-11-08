package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dao.HienThiDAO;
import style.RoundedBorder;

public class Form_TrangChu extends JPanel {
	private static final Color SECONDARY = new Color(243, 244, 246); // Light gray
	private static final Color PRIMARY = new Color(255, 255, 255); // White background

	private static final Color ACCENT = new Color(37, 99, 235); // Blue
	private static final Color ACCENT_LIGHT = new Color(59, 130, 246); // Light blue
	private static final Color TEXT_PRIMARY = new Color(17, 24, 39); // Dark text
	private static final Color TEXT_SECONDARY = new Color(107, 114, 128); // Gray text

	// Section colors with transparency
	private static final Color DASHBOARD = new Color(99, 102, 241, 180); // Indigo
	private static final Color SALES = new Color(16, 185, 129, 180); // Emerald
	private static final Color PRODUCTS = new Color(239, 68, 68, 180); // Red
	private static final Color EMPLOYEE = new Color(245, 158, 11, 180); // Amber
	private static final Color STATS = new Color(139, 92, 246, 180); // Purple

	// Modern fonts
	private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
	private static final Font HEADER_FONT = new Font("Segoe UI Semibold", Font.PLAIN, 18);
	private static final Font MENU_FONT = new Font("Segoe UI", Font.PLAIN, 20);
	private static final Font TEXT_FONT = new Font("Segoe UI", Font.PLAIN, 14);
	private HienThiDAO hienthiDAO;

	public Form_TrangChu() {
		hienthiDAO = new HienThiDAO();
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		add(createDashboardPanel());
	}

	private JPanel createDashboardPanel() {
		JPanel panel = new JPanel(new BorderLayout(20, 20));
		panel.setBackground(SECONDARY);
		panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

		// Header
		JPanel headerPanel = createContentHeader("Dashboard", "Tổng quan hệ thống");
		panel.add(headerPanel, BorderLayout.NORTH);

		// Main content
		JPanel mainContent = new JPanel(new GridLayout(2, 2, 20, 20));
		mainContent.setOpaque(false);

		// Lấy dữ liệu từ DAO
		HienThiDAO hienThiDAO = new HienThiDAO();
		HashMap<String, String[]> thongKe = hienThiDAO.getThongKeTrangChu();

		// Hiển thị dữ liệu
		if (thongKe.containsKey("DoanhThu")) {
			String[] dtData = thongKe.get("DoanhThu");
			mainContent.add(createStatCard("Doanh thu", dtData[0], dtData[1], DASHBOARD));
		}

		if (thongKe.containsKey("DonHang")) {
			String[] dhData = thongKe.get("DonHang");
			mainContent.add(createStatCard("Đơn hàng", dhData[0], dhData[1], SALES));
		}

		if (thongKe.containsKey("NhanVien")) {
			String[] nvData = thongKe.get("NhanVien");
			mainContent.add(createStatCard("Nhân viên", nvData[0], nvData[1], EMPLOYEE));
		}

		if (thongKe.containsKey("SanPham")) {
			String[] spData = thongKe.get("SanPham");
			mainContent.add(createStatCard("Sản phẩm", spData[0], spData[1], PRODUCTS));
		}

		panel.add(mainContent, BorderLayout.CENTER);
		return panel;
	}

	

	private JPanel createContentHeader(String title, String subtitle) {
		JPanel header = new JPanel(new BorderLayout());
		header.setOpaque(false);
		header.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
		titlePanel.setOpaque(false);

		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(TITLE_FONT);
		titleLabel.setForeground(TEXT_PRIMARY);

		JLabel subtitleLabel = new JLabel(subtitle);
		subtitleLabel.setFont(TEXT_FONT);
		subtitleLabel.setForeground(TEXT_SECONDARY);

		titlePanel.add(titleLabel);
		titlePanel.add(Box.createVerticalStrut(5));
		titlePanel.add(subtitleLabel);

		header.add(titlePanel, BorderLayout.WEST);
		return header;
	}

	private JPanel createPlaceholderPanel(String title, String subtitle) {
		JPanel panel = new JPanel(new BorderLayout(20, 20));
		panel.setBackground(SECONDARY);
		panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

		// Header
		JPanel headerPanel = createContentHeader(title, subtitle);
		panel.add(headerPanel, BorderLayout.NORTH);

		// Content card
		JPanel card = new JPanel();
		card.setBackground(PRIMARY);
		card.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(10, SECONDARY),
				BorderFactory.createEmptyBorder(30, 30, 30, 30)));

		JLabel placeholderLabel = new JLabel("Đang phát triển...");
		placeholderLabel.setFont(HEADER_FONT);
		placeholderLabel.setForeground(TEXT_SECONDARY);
		card.add(placeholderLabel);

		panel.add(card, BorderLayout.CENTER);
		return panel;
	}

	private JPanel createStatCard(String title, String value, String change, Color color) {
		JPanel card = new JPanel(new BorderLayout(15, 15));
		card.setBackground(PRIMARY);
		RoundedBorder bd = new RoundedBorder(10, color);
		card.setBorder(BorderFactory.createCompoundBorder(bd, BorderFactory.createEmptyBorder(20, 20, 20, 20)));

		// Icon
		JLabel iconLabel = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				g2d.setColor(color);
				g2d.fillOval(0, 0, 40, 40);
			}

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(40, 40);
			}
		};

		// Content
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.setOpaque(false);

		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(TEXT_FONT);
		titleLabel.setForeground(TEXT_SECONDARY);

		JLabel valueLabel = new JLabel(value);
		valueLabel.setFont(TITLE_FONT);
		valueLabel.setForeground(TEXT_PRIMARY);

		JLabel changeLabel = new JLabel(change);
		changeLabel.setFont(TEXT_FONT);
		changeLabel.setForeground(change.startsWith("↑") ? SALES : PRODUCTS);

		content.add(titleLabel);
		content.add(Box.createVerticalStrut(10));
		content.add(valueLabel);
		content.add(Box.createVerticalStrut(5));
		content.add(changeLabel);

		card.add(iconLabel, BorderLayout.WEST);
		card.add(content, BorderLayout.CENTER);

		return card;
	}
}
