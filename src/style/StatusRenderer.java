package style;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class StatusRenderer extends JLabel implements TableCellRenderer{
	private static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);
	private static final Color SUCCESS = new Color(16, 185, 129);
	private static final Color WARNING = new Color(245, 158, 11);
	public StatusRenderer() {
		setOpaque(true);
		setHorizontalAlignment(JLabel.CENTER);
		setFont(BODY_FONT);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		setText(value.toString());

		// Style based on status
		if (value.toString().equals("Còn hàng")) {
			setForeground(SUCCESS);
			setBackground(new Color(SUCCESS.getRed(), SUCCESS.getGreen(), SUCCESS.getBlue(), 20));
		} else {
			setForeground(WARNING);
			setBackground(new Color(WARNING.getRed(), WARNING.getGreen(), WARNING.getBlue(), 20));
		}

		if (isSelected) {
			setBackground(table.getSelectionBackground());
		}

		setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

		return this;
	}
	

}
