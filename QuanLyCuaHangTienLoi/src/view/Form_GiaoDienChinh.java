package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import dialog.DangXuatDialog;
import style.RoundedBorder;

public class Form_GiaoDienChinh extends JFrame {
	// Modern color scheme
	private static final Color PRIMARY = new Color(255, 255, 255); // White background
	private static final Color SECONDARY = new Color(243, 244, 246); // Light gray
	private static final Color ACCENT = new Color(37, 99, 235); // Blue
	private static final Color ACCENT_LIGHT = new Color(59, 130, 246); // Light blue
	private static final Color TEXT_PRIMARY = new Color(17, 24, 39); // Dark text
	private static final Color TEXT_SECONDARY = new Color(107, 114, 128); // Gray text

	// Section colors with transparency
	private static final Color DASHBOARD = new Color(99, 102, 241, 180); // Indigo
	private static final Color SALES = new Color(16, 185, 129, 180); // Emerald
	private static final Color PRODUCTS = new Color(239, 68, 68, 180); // Red
	private static final Color CUSTOMERS = new Color(245, 158, 11, 180); // Amber
	private static final Color STATS = new Color(139, 92, 246, 180); // Purple

	// Modern fonts
	private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
	private static final Font HEADER_FONT = new Font("Segoe UI Semibold", Font.PLAIN, 18);
	private static final Font MENU_FONT = new Font("Segoe UI", Font.PLAIN, 20);
	private static final Font TEXT_FONT = new Font("Segoe UI", Font.PLAIN, 14);

	private JPanel mainPanel;
	private CardLayout cardLayout;
	private JPanel contentPanel;
	private JPanel menuPanel;
	private JLabel selectedMenu;
	private int menuItemHeight = 56;
    private BufferedImage logoImage;


	public Form_GiaoDienChinh() {
		setTitle("Quản Lý Cửa Hàng");
		setSize(1600, 900);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mainPanel = new JPanel(new BorderLayout(0, 0));
		mainPanel.setBackground(PRIMARY);
		setContentPane(mainPanel);
		 try {
	            // Thay đổi đường dẫn tới file ảnh logo của bạn
	            logoImage = ImageIO.read(getClass().getResource("/icons/logoHSK.png"));
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		initComponents();
		setVisible(true);
	}

	private void initComponents() {
		createMenuPanel();
		createContentPanel();

		mainPanel.add(menuPanel, BorderLayout.WEST);
		mainPanel.add(contentPanel, BorderLayout.CENTER);
	}

	private void createMenuPanel() {
		menuPanel = new JPanel();
	    menuPanel.setPreferredSize(new Dimension(300, 0)); // Tăng từ 280 lên 300
	    menuPanel.setBackground(PRIMARY);
	    menuPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, SECONDARY));
	    menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

	    createLogoPanel();
	    createProfilePanel();
	    createMenuItems();
	    createBottomPanel();
	}

	private void createLogoPanel() {
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setBackground(PRIMARY);
        logoPanel.setPreferredSize(new Dimension(280, 80));
        logoPanel.setMaximumSize(new Dimension(280, 80));

        JPanel logoContent = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                // Vẽ hình tròn với gradient
                GradientPaint gradient = new GradientPaint(
                    20, 15, ACCENT,
                    60, 55, ACCENT_LIGHT
                );
                g2d.setPaint(gradient);
                g2d.fillOval(20, 15, 45, 45);

                // Vẽ ảnh logo nếu đã load được
                if (logoImage != null) {
                    // Tạo clip hình tròn
                    Shape clip = new java.awt.geom.Ellipse2D.Double(20, 15, 45, 45);
                    g2d.setClip(clip);
                    
                    // Scale và vẽ ảnh
                    g2d.drawImage(logoImage, 20, 15, 45, 45, null);
                    
                    // Reset clip
                    g2d.setClip(null);
                } else {
                    // Vẽ chữ TV nếu không có ảnh
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Segoe UI", Font.BOLD, 22));
                    FontMetrics fm = g2d.getFontMetrics();
                    String text = "TV";
                    int textX = 20 + (45 - fm.stringWidth(text)) / 2;
                    int textY = 15 + ((45 - fm.getHeight()) / 2) + fm.getAscent();
                    g2d.drawString(text, textX, textY);
                }

                // Vẽ viền cho hình tròn
                g2d.setColor(ACCENT_LIGHT);
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawOval(20, 15, 45, 45);

                // Vẽ tên shop
                g2d.setFont(TITLE_FONT);
                // Shadow
                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.drawString("TV Shop", 75, 46);
                // Text chính
                g2d.setColor(TEXT_PRIMARY);
                g2d.drawString("TV Shop", 74, 45);
            }
        };
        logoContent.setOpaque(false);

        logoPanel.add(logoContent, BorderLayout.CENTER);
        menuPanel.add(logoPanel);
    }



	private void createProfilePanel() {
		JPanel profilePanel = new JPanel();
		profilePanel.setBackground(PRIMARY);
		profilePanel.setMaximumSize(new Dimension(280, 100));
		profilePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		profilePanel.setLayout(new BorderLayout());

		// Avatar panel with modern design
		JPanel avatarPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				// Avatar circle with gradient
				GradientPaint gradient = new GradientPaint(0, 0, ACCENT, 50, 50, ACCENT_LIGHT);
				g2d.setPaint(gradient);
				g2d.fillOval(0, 0, 50, 50);

				// User icon
				g2d.setColor(Color.WHITE);
				g2d.setFont(new Font("Segoe UI", Font.BOLD, 20));
				FontMetrics fm = g2d.getFontMetrics();
				String initial = "A";
				int x = (50 - fm.stringWidth(initial)) / 2;
				int y = ((50 - fm.getHeight()) / 2) + fm.getAscent();
				g2d.drawString(initial, x, y);
			}

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(50, 50);
			}
		};
		avatarPanel.setOpaque(false);

		// User info panel
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.setOpaque(false);

		JLabel nameLabel = new JLabel("Admin User");
		nameLabel.setFont(HEADER_FONT);
		nameLabel.setForeground(TEXT_PRIMARY);

		JLabel roleLabel = new JLabel("Administrator");
		roleLabel.setFont(TEXT_FONT);
		roleLabel.setForeground(TEXT_SECONDARY);

		infoPanel.add(nameLabel);
		infoPanel.add(Box.createVerticalStrut(5));
		infoPanel.add(roleLabel);

		// Add components
		profilePanel.add(avatarPanel, BorderLayout.WEST);
		profilePanel.add(Box.createHorizontalStrut(15));
		profilePanel.add(infoPanel, BorderLayout.CENTER);

		menuPanel.add(profilePanel);
		menuPanel.add(createSeparator());
	}

	private JSeparator createSeparator() {
		JSeparator separator = new JSeparator();
		separator.setForeground(SECONDARY);
		separator.setMaximumSize(new Dimension(240, 1));
		return separator;
	}

	private void createMenuItems() {
		String[][] menuItems = {
		        { "Dashboard", "DASHBOARD", "dashboard" },
		        { "Bán Hàng", "SALES", "shopping-cart" },
		        { "Đơn Hàng", "ORDERS", "file-text" },  // Menu item mới
		        { "Sản Phẩm", "PRODUCTS", "box" },
		        // Menu item mới
		       
		        { "Nhân Viên", "EMPLOYEES", "user-plus" },  // Menu item mới
		        { "Thống Kê", "STATS", "chart-bar" },
//		        { "Cài Đặt", "SETTINGS", "settings" }
		    };

		    menuPanel.add(Box.createVerticalStrut(20));
		    for (String[] item : menuItems) {
		        JPanel menuItem = createMenuItem(item[0], item[1], item[2]);
		        menuPanel.add(menuItem);
		        menuPanel.add(Box.createVerticalStrut(25)); // Giảm khoảng cách giữa các menu item từ 30 xuống 25
		    }
		    menuPanel.add(Box.createVerticalStrut(20));
	}

	private JPanel createMenuItem(String text, String cardName, String iconName) {
		JPanel itemPanel = new JPanel(new BorderLayout());
		itemPanel.setMaximumSize(new Dimension(280, menuItemHeight));
		itemPanel.setBackground(PRIMARY);

		// Left accent bar
		JPanel indicator = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (selectedMenu != null && selectedMenu.getText().equals(text)) {
					Graphics2D g2d = (Graphics2D) g;
					g2d.setColor(ACCENT);
					g2d.fillRect(0, 0, 4, getHeight());
				}
			}
		};
		indicator.setPreferredSize(new Dimension(4, 0));
		indicator.setOpaque(false);

		// Content panel with icon and text
		JPanel content = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
		content.setOpaque(false);

		// Icon with modern design
		JLabel iconLabel = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				Color iconColor = selectedMenu != null && selectedMenu.getText().equals(text) ? ACCENT : TEXT_SECONDARY;

				// Draw icon based on type
				g2d.setColor(iconColor);
				switch (iconName) {
				case "dashboard":
					drawDashboardIcon(g2d);
					break;
				case "shopping-cart":
					drawCartIcon(g2d);
					break;
				case "box":
					drawBoxIcon(g2d);
					break;
				case "users":
					drawUsersIcon(g2d);
					break;
				case "chart-bar":
					drawChartIcon(g2d);
					break;
				case "settings":
					drawSettingsIcon(g2d);
					break;
				case "file-text": // Icon cho Đơn Hàng
				    g2d.drawRect(2, 2, 16, 16);
				    g2d.drawLine(6, 6, 14, 6);
				    g2d.drawLine(6, 10, 14, 10);
				    g2d.drawLine(6, 14, 11, 14);
				    break;
				
				case "user-plus": // Icon cho Nhân Viên
				    g2d.fillOval(2, 2, 8, 8);
				    g2d.drawArc(0, 12, 12, 8, 0, 180);
				    g2d.drawLine(14, 6, 18, 6);
				    g2d.drawLine(16, 4, 16, 8);
				    break;
				}
			}

			private void drawDashboardIcon(Graphics2D g2d) {
				g2d.drawRect(2, 2, 7, 7);
				g2d.drawRect(11, 2, 7, 7);
				g2d.drawRect(2, 11, 7, 7);
				g2d.drawRect(11, 11, 7, 7);
			}

			private void drawCartIcon(Graphics2D g2d) {
				g2d.drawPolyline(new int[] { 2, 5, 18, 15, 5 }, new int[] { 2, 2, 2, 12, 12 }, 5);
				g2d.fillOval(6, 14, 4, 4);
				g2d.fillOval(14, 14, 4, 4);
			}

			private void drawBoxIcon(Graphics2D g2d) {
				g2d.drawRect(2, 6, 16, 12);
				g2d.drawLine(2, 10, 18, 10);
			}

			private void drawUsersIcon(Graphics2D g2d) {
				g2d.fillOval(7, 2, 6, 6);
				g2d.drawArc(4, 10, 12, 8, 0, 180);
			}

			private void drawChartIcon(Graphics2D g2d) {
				g2d.drawLine(2, 18, 2, 2);
				g2d.drawLine(2, 18, 18, 18);
				g2d.fillRect(5, 8, 3, 8);
				g2d.fillRect(10, 4, 3, 12);
				g2d.fillRect(15, 12, 3, 4);
			}

			private void drawSettingsIcon(Graphics2D g2d) {
				g2d.fillOval(7, 7, 6, 6);
				for (int i = 0; i < 8; i++) {
					double angle = i * Math.PI / 4;
					int x1 = 10 + (int) (7 * Math.cos(angle));
					int y1 = 10 + (int) (7 * Math.sin(angle));
					int x2 = 10 + (int) (9 * Math.cos(angle));
					int y2 = 10 + (int) (9 * Math.sin(angle));
					g2d.drawLine(x1, y1, x2, y2);
				}
			}

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(20, 20);
			}
		};

		// Menu text
		JLabel menuText = new JLabel(text);
		menuText.setFont(MENU_FONT);
		menuText.setForeground(TEXT_SECONDARY);

		content.add(iconLabel);
		content.add(menuText);

		itemPanel.add(indicator, BorderLayout.WEST);
		itemPanel.add(content, BorderLayout.CENTER);

		// Hover and click effects
		itemPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (selectedMenu != menuText) {
					animateBackgroundChange(itemPanel, PRIMARY, SECONDARY);
					menuText.setForeground(TEXT_PRIMARY);
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (selectedMenu != menuText) {
					animateBackgroundChange(itemPanel, SECONDARY, PRIMARY);
					menuText.setForeground(TEXT_SECONDARY);
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (selectedMenu != null) {
					JPanel oldPanel = (JPanel) selectedMenu.getParent().getParent();
					animateBackgroundChange(oldPanel, SECONDARY, PRIMARY);
					selectedMenu.setForeground(TEXT_SECONDARY);
				}

				selectedMenu = menuText;
				animateBackgroundChange(itemPanel, PRIMARY, SECONDARY);
				menuText.setForeground(ACCENT);
				cardLayout.show(contentPanel, cardName);
			}
		});

		return itemPanel;
	}

	private void createBottomPanel() {
		menuPanel.add(Box.createVerticalGlue());
		menuPanel.add(createSeparator());
		menuPanel.add(Box.createVerticalStrut(20));

		// Bottom menu container
		JPanel bottomMenu = new JPanel();
		bottomMenu.setLayout(new BoxLayout(bottomMenu, BoxLayout.Y_AXIS));
		bottomMenu.setBackground(PRIMARY);
		bottomMenu.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

		// Profile button
		JPanel profileBtn = createBottomMenuItem("Thông tin cá nhân", "profile");
		bottomMenu.add(profileBtn);
		bottomMenu.add(Box.createVerticalStrut(10));

		// Logout button
		JPanel logoutBtn = createBottomMenuItem("Đăng xuất", "logout");
		logoutBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				DangXuatDialog dangxuatDialog = new DangXuatDialog();
			}
		});
		bottomMenu.add(logoutBtn);

		menuPanel.add(bottomMenu);
	}

	private JPanel createBottomMenuItem(String text, String iconType) {
		JPanel itemPanel = new JPanel(new BorderLayout());
		itemPanel.setMaximumSize(new Dimension(280, 45));
		itemPanel.setBackground(PRIMARY);

		JPanel content = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
		content.setOpaque(false);

		// Icon
		JLabel iconLabel = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				g2d.setColor(TEXT_SECONDARY);
				if (iconType.equals("profile")) {
					// Profile icon
					g2d.drawOval(2, 2, 16, 16);
					g2d.drawArc(6, 7, 8, 8, 0, 180);
					g2d.fillOval(8, 5, 4, 4);
				} else if (iconType.equals("logout")) {
					// Logout icon
					g2d.drawLine(8, 10, 18, 10);
					g2d.drawLine(14, 6, 18, 10);
					g2d.drawLine(14, 14, 18, 10);
					g2d.drawArc(2, 2, 8, 16, -60, 120);
				}
			}

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(20, 20);
			}
		};

		JLabel label = new JLabel(text);
		label.setFont(MENU_FONT);
		label.setForeground(TEXT_SECONDARY);

		content.add(iconLabel);
		content.add(label);
		itemPanel.add(content, BorderLayout.CENTER);

		// Hover effect
		itemPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				animateBackgroundChange(itemPanel, PRIMARY, SECONDARY);
				label.setForeground(TEXT_PRIMARY);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				animateBackgroundChange(itemPanel, SECONDARY, PRIMARY);
				label.setForeground(TEXT_SECONDARY);
			}
		});

		return itemPanel;
	}

	

	private JButton createStyledButton(String text, boolean isPrimary) {
		JButton button = new JButton(text);
		button.setFont(TEXT_FONT);
		button.setForeground(isPrimary ? Color.WHITE : TEXT_PRIMARY);
		button.setBackground(isPrimary ? ACCENT : Color.WHITE);
		button.setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(isPrimary ? ACCENT : SECONDARY),
						BorderFactory.createEmptyBorder(8, 20, 8, 20)));
		button.setFocusPainted(false);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));

		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				button.setBackground(isPrimary ? ACCENT_LIGHT : SECONDARY);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button.setBackground(isPrimary ? ACCENT : Color.WHITE);
			}
		});

		return button;
	}

	private void createContentPanel() {
		cardLayout = new CardLayout();
		contentPanel = new JPanel(cardLayout);
		contentPanel.setBackground(SECONDARY);

		// Add content panels
		Form_TrangChu form_TrangChu = new Form_TrangChu();
	    form_TrangChu.setPreferredSize(new Dimension(contentPanel.getWidth(), contentPanel.getHeight()));
	    Form_BanHang form_BanHang = new Form_BanHang();
	    Form_DonHang form_donhang = new Form_DonHang();
	    Form_NhanVien form_nhanvien = new Form_NhanVien();

		contentPanel.add(form_TrangChu, "DASHBOARD");
	    contentPanel.add(form_BanHang, "SALES");
	    contentPanel.add(form_donhang, "ORDERS");      // Panel mới
		Form_SanPham form_SanPham = new Form_SanPham();
	    contentPanel.add(form_SanPham, "PRODUCTS");
//	    contentPanel.add(createInventoryPanel(), "INVENTORY"); // Panel mới
//	    contentPanel.add(createCustomersPanel(), "CUSTOMERS");
	    contentPanel.add(form_nhanvien, "EMPLOYEES"); // Panel mới
	    Form_ThongKe form_thongke = new Form_ThongKe();
	    contentPanel.add(form_thongke, "STATS");


		// Show default panel
		cardLayout.show(contentPanel, "DASHBOARD");

	}

	// Content Panels
	


	

	

	private void animateBackgroundChange(JComponent component, Color from, Color to) {
		Timer timer = new Timer(16, null);
		float[] fromHSB = Color.RGBtoHSB(from.getRed(), from.getGreen(), from.getBlue(), null);
		float[] toHSB = Color.RGBtoHSB(to.getRed(), to.getGreen(), to.getBlue(), null);
		float[] current = fromHSB.clone();

		timer.addActionListener(e -> {
			boolean finished = true;
			for (int i = 0; i < 3; i++) {
				if (Math.abs(current[i] - toHSB[i]) > 0.01f) {
					current[i] += (toHSB[i] - current[i]) * 0.2f;
					finished = false;
				}
			}

			component.setBackground(Color.getHSBColor(current[0], current[1], current[2]));

			if (finished) {
				timer.stop();
				component.setBackground(to);
			}
			component.repaint();
		});

		timer.start();
	}

	




	

	// Main method
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

			// Set modern UI defaults
			UIManager.put("Panel.background", PRIMARY);
			UIManager.put("Button.font", TEXT_FONT);
			UIManager.put("Label.font", TEXT_FONT);
			UIManager.put("TextField.font", TEXT_FONT);
			UIManager.put("ComboBox.font", TEXT_FONT);
			UIManager.put("Table.font", TEXT_FONT);
			UIManager.put("TableHeader.font", HEADER_FONT);

			// Button styling
			UIManager.put("Button.background", ACCENT);
			UIManager.put("Button.foreground", Color.WHITE);
			UIManager.put("Button.select", ACCENT_LIGHT);
			UIManager.put("Button.focus", new Color(0, 0, 0, 0));
			UIManager.put("Button.border", BorderFactory.createEmptyBorder(10, 20, 10, 20));

			// Text field styling
			UIManager.put("TextField.background", Color.WHITE);
			UIManager.put("TextField.border", BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(SECONDARY), BorderFactory.createEmptyBorder(5, 10, 5, 10)));

			// ComboBox styling
			UIManager.put("ComboBox.background", Color.WHITE);
			UIManager.put("ComboBox.selectionBackground", ACCENT);
			UIManager.put("ComboBox.selectionForeground", Color.WHITE);

			// ScrollBar styling
			UIManager.put("ScrollBar.thumb", TEXT_SECONDARY);
			UIManager.put("ScrollBar.track", SECONDARY);
			UIManager.put("ScrollBar.width", 8);

		} catch (Exception e) {
			e.printStackTrace();
		}

		SwingUtilities.invokeLater(() -> {
			Form_GiaoDienChinh app = new Form_GiaoDienChinh();
			app.setVisible(true);
		});
	}
}
