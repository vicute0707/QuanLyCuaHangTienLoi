package dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

public class DangXuatDialog extends JDialog{
	private static final Font TEXT_FONT = new Font("Segoe UI", Font.PLAIN, 14);
	private static final Font HEADER_FONT = new Font("Segoe UI Semibold", Font.PLAIN, 18);
	private static final Color PRIMARY = new Color(255, 255, 255); // White background
	private static final Color SECONDARY = new Color(243, 244, 246); // Light gray
	private static final Color TEXT_PRIMARY = new Color(17, 24, 39); // Dark text
	private static final Color ACCENT = new Color(37, 99, 235); // Blue
	private static final Color ACCENT_LIGHT = new Color(59, 130, 246); // Light blue

	
	private void showLogoutConfirmation() {
		JDialog dialog = new JDialog(this, "Xác nhận đăng xuất", true);
		dialog.setSize(400, 200);
		dialog.setLocationRelativeTo(this);
		dialog.setLayout(new BorderLayout());

		// Message panel
		JPanel messagePanel = new JPanel(new BorderLayout(20, 20));
		messagePanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
		messagePanel.setBackground(PRIMARY);

		JLabel iconLabel = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				// Warning icon
				g2d.setColor(ACCENT);
				g2d.fillOval(0, 0, 40, 40);
				g2d.setColor(Color.WHITE);
				g2d.setFont(new Font("Segoe UI", Font.BOLD, 25));
				g2d.drawString("!", 17, 28);
			}

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(40, 40);
			}
		};

		JLabel messageLabel = new JLabel("Bạn có chắc chắn muốn đăng xuất?");
		messageLabel.setFont(HEADER_FONT);
		messageLabel.setForeground(TEXT_PRIMARY);

		messagePanel.add(iconLabel, BorderLayout.WEST);
		messagePanel.add(messageLabel, BorderLayout.CENTER);

		// Buttons panel
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		buttonsPanel.setBackground(PRIMARY);
		buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

		JButton cancelButton = createStyledButton("Hủy", false);
		JButton confirmButton = createStyledButton("Đăng xuất", true);
		confirmButton.setForeground(Color.black);;
		cancelButton.addActionListener(e -> dialog.dispose());
		confirmButton.addActionListener(e -> {
			dialog.dispose();
			dispose();
			// new LoginForm().setVisible(true);
		});

		buttonsPanel.add(cancelButton);
		buttonsPanel.add(confirmButton);

		dialog.add(messagePanel, BorderLayout.CENTER);
		dialog.add(buttonsPanel, BorderLayout.SOUTH);
		dialog.setVisible(true);
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

}
