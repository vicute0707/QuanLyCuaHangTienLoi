package style;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class ModernScrollBarUI extends BasicScrollBarUI {
	private static final Color BACKGROUND = new Color(249, 250, 251);
	private static final Color ACCENT = new Color(37, 99, 235);
	private static final Color ACCENT_HOVER = new Color(29, 78, 216);

	@Override
	protected void configureScrollBarColors() {
		this.thumbColor = new Color(ACCENT.getRed(), ACCENT.getGreen(), ACCENT.getBlue(), 100);
		this.trackColor = BACKGROUND;
	}

	@Override
	protected JButton createDecreaseButton(int orientation) {
		return createZeroButton();
	}

	@Override
	protected JButton createIncreaseButton(int orientation) {
		return createZeroButton();
	}

	private JButton createZeroButton() {
		JButton button = new JButton();
		button.setPreferredSize(new Dimension(0, 0));
		button.setMinimumSize(new Dimension(0, 0));
		button.setMaximumSize(new Dimension(0, 0));
		return button;
	}

	@Override
	protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
		if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
			return;
		}

		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setPaint(thumbColor);
		g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2, thumbBounds.width - 4, thumbBounds.height - 4, 8, 8);

		g2.dispose();
	}
}
