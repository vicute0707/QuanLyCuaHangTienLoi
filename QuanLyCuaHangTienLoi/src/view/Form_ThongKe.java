package view;

import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.*;
import org.jfree.data.general.*;
import com.toedter.calendar.JDateChooser;
import com.itextpdf.text.*;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

public class Form_ThongKe extends JPanel {

	// region Constants
	private static final class Colors {
		static final Color PRIMARY = new Color(255, 255, 255);
		static final Color SECONDARY = new Color(243, 244, 246);
		static final Color ACCENT = new Color(37, 99, 235);
		static final Color BORDER = new Color(229, 231, 235);
		static final Color TEXT = new Color(17, 24, 39);
		static final Color SUCCESS = new Color(22, 163, 74);
		static final Color ERROR = new Color(220, 38, 38);
		static final Color HEADER_BG = new Color(240, 247, 255);
		static final Color HEADER_FG = new Color(30, 58, 138);
		static final Color ROW_EVEN = Color.WHITE;
		static final Color ROW_ODD = new Color(248, 250, 252);
		static final Color SELECTED_BG = new Color(219, 234, 254);
		static final Color SELECTED_FG = new Color(30, 58, 138);

		static final Color[] CHART_COLORS = { new Color(37, 99, 235), new Color(59, 130, 246), new Color(96, 165, 250),
				new Color(147, 197, 253), new Color(191, 219, 254) };
	}

	private static final class Fonts {
		static final Font TITLE = new Font("Segoe UI", Font.BOLD, 24);
		static final Font HEADER = new Font("Segoe UI", Font.BOLD, 16);
		static final Font BODY = new Font("Segoe UI", Font.PLAIN, 14);
		static final Font VALUE = new Font("Segoe UI", Font.BOLD, 24);
		static final Font TABLE_HEADER = new Font("Segoe UI", Font.BOLD, 14);
		static final Font TABLE_CELL = new Font("Segoe UI", Font.PLAIN, 14);
	}

	private static final class Dimensions {
		static final Dimension DATE_CHOOSER = new Dimension(150, 35);
		static final Dimension BUTTON = new Dimension(130, 35);
		static final Dimension CHART = new Dimension(800, 400);
		static final int HEADER_HEIGHT = 45;
		static final int ROW_HEIGHT = 40;
		static final int BORDER_RADIUS = 8;
		static final int BUTTON_RADIUS = 6;
	}
	// endregion

	// region Fields
	private final JTabbedPane tabbedPane;
	private final JDateChooser dateFrom;
	private final JDateChooser dateTo;
	private final DecimalFormat currencyFormat;
	private final SimpleDateFormat dateFormat;

	private JTable tblProducts;

	private DefaultTableModel productModel;

	private JFreeChart revenueChart;
	private JFreeChart categoryChart;
	private JFreeChart orderChart;
	private ChartPanel revenueChartPanel;
	private ChartPanel categoryChartPanel;
	private ChartPanel orderChartPanel;
	// endregion

	// region Constructor
	public Form_ThongKe() {
		// Initialize components
		tabbedPane = new JTabbedPane();
		dateFrom = new JDateChooser();
		dateTo = new JDateChooser();
		currencyFormat = new DecimalFormat("#,### đ");
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		setupUI();
		initializeData();
	}
	// endregion

	// region UI Setup
	private void setupUI() {
		setLayout(new BorderLayout(20, 20));
		setBackground(new Color(249, 250, 251));
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		add(createHeaderPanel(), BorderLayout.NORTH);
		add(createMainContent(), BorderLayout.CENTER);
	}

	private JPanel createHeaderPanel() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(Colors.PRIMARY);
		header.setBorder(createRoundedBorder(Dimensions.BORDER_RADIUS));

		JLabel title = new JLabel("Thống kê & Báo cáo");
		title.setFont(Fonts.TITLE);
		header.add(title, BorderLayout.WEST);
		header.add(createDateRangePanel(), BorderLayout.EAST);

		return header;
	}

	private JPanel createDateRangePanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		panel.setBackground(Colors.PRIMARY);

		// From date
		panel.add(new JLabel("Từ:"));
		setupDateChooser(dateFrom);
		panel.add(dateFrom);

		// To date
		panel.add(new JLabel("Đến:"));
		setupDateChooser(dateTo);
		panel.add(dateTo);

		// Refresh button
		JButton btnRefresh = createButton("Cập nhật", Colors.ACCENT, Color.blue, true);
		btnRefresh.addActionListener(e -> refreshData());
		panel.add(btnRefresh);

		return panel;
	}

	private JComponent createMainContent() {
		tabbedPane.setFont(Fonts.HEADER);
		tabbedPane.setBackground(Colors.PRIMARY);

		tabbedPane.addTab("Doanh thu", createRevenuePanel());
		tabbedPane.addTab("Sản phẩm", createProductPanel());
		tabbedPane.addTab("Đơn hàng", createOrderPanel());

		return tabbedPane;
	}
	// endregion

	// region Panel Creation
	private JPanel createRevenuePanel() {
		JPanel panel = new JPanel(new BorderLayout(0, 20));
		panel.setBackground(Colors.PRIMARY);
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// Summary cards
		panel.add(createRevenueSummaryCards(), BorderLayout.NORTH);

		// Chart panel
		JPanel chartPanel = new JPanel(new BorderLayout());
		chartPanel.setBackground(Colors.PRIMARY);

		// Chart header
		JPanel chartHeader = new JPanel(new BorderLayout());
		chartHeader.setBackground(Colors.PRIMARY);

		JLabel chartTitle = new JLabel("Biểu đồ doanh thu");
		chartTitle.setFont(Fonts.HEADER);
		chartHeader.add(chartTitle, BorderLayout.WEST);

		JButton btnExport = createButton("Xuất báo cáo", Colors.SUCCESS, Color.blue, true);
		btnExport.addActionListener(e -> exportRevenueReport());
		chartHeader.add(btnExport, BorderLayout.EAST);

		chartPanel.add(chartHeader, BorderLayout.NORTH);

		// Revenue chart
		createRevenueChart();
		chartPanel.add(revenueChartPanel, BorderLayout.CENTER);

		panel.add(chartPanel, BorderLayout.CENTER);

		return panel;
	}

	private JPanel createProductPanel() {
		JPanel panel = new JPanel(new BorderLayout(0, 20));
		panel.setBackground(Colors.PRIMARY);
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// Grid layout for product stats
		JPanel statsGrid = new JPanel(new GridLayout(1, 2, 20, 0));
		statsGrid.setBackground(panel.getBackground());

		// Left side - Product table
		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.setBackground(Colors.PRIMARY);

		JPanel leftHeader = new JPanel(new BorderLayout());
		leftHeader.setBackground(Colors.PRIMARY);
		leftHeader.add(new JLabel("Top sản phẩm bán chạy"), BorderLayout.WEST);

		JButton btnExport = createButton("Xuất báo cáo", Colors.SUCCESS, Color.blue, true);
		btnExport.addActionListener(e -> exportProductReport());
		leftHeader.add(btnExport, BorderLayout.EAST);

		leftPanel.add(leftHeader, BorderLayout.NORTH);

		createProductTable();
		leftPanel.add(new JScrollPane(tblProducts), BorderLayout.CENTER);

		// Right side - Category chart
		JPanel rightPanel = new JPanel(new BorderLayout());
		rightPanel.setBackground(Colors.PRIMARY);

		JLabel rightTitle = new JLabel("Phân bố theo danh mục");
		rightTitle.setFont(Fonts.HEADER);
		rightPanel.add(rightTitle, BorderLayout.NORTH);

		createCategoryChart();
		rightPanel.add(categoryChartPanel, BorderLayout.CENTER);

		statsGrid.add(leftPanel);
		statsGrid.add(rightPanel);
		panel.add(statsGrid, BorderLayout.CENTER);

		return panel;
	}

	private JPanel createOrderPanel() {
		JPanel panel = new JPanel(new BorderLayout(0, 20));
		panel.setBackground(Colors.PRIMARY);
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// Summary cards
		panel.add(createOrderSummaryCards(), BorderLayout.NORTH);

		// Order chart panel
		JPanel chartPanel = new JPanel(new BorderLayout());
		chartPanel.setBackground(Colors.PRIMARY);

		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setBackground(Colors.PRIMARY);
		headerPanel.add(new JLabel("Thống kê đơn hàng"), BorderLayout.WEST);

		JButton btnExport = createButton("Xuất báo cáo", Colors.SUCCESS, Color.blue, true);
		btnExport.addActionListener(e -> exportOrderReport());
		headerPanel.add(btnExport, BorderLayout.EAST);

		chartPanel.add(headerPanel, BorderLayout.NORTH);

		createOrderChart();
		chartPanel.add(orderChartPanel, BorderLayout.CENTER);

		panel.add(chartPanel, BorderLayout.CENTER);

		return panel;
	}

	// region Chart Creation
	private void createRevenueChart() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		// Add sample data
		dataset.addValue(1500000, "Doanh thu", "T1");
		dataset.addValue(1800000, "Doanh thu", "T2");
		dataset.addValue(1600000, "Doanh thu", "T3");
		dataset.addValue(2100000, "Doanh thu", "T4");
		dataset.addValue(1900000, "Doanh thu", "T5");
		dataset.addValue(2300000, "Doanh thu", "T6");

		revenueChart = ChartFactory.createLineChart("Biểu đồ doanh thu", "Tháng", "Doanh thu (VNĐ)", dataset,
				PlotOrientation.VERTICAL, true, true, false);

		customizeChart(revenueChart);
		revenueChartPanel = new ChartPanel(revenueChart);
		revenueChartPanel.setPreferredSize(Dimensions.CHART);
	}

	private void createCategoryChart() {
		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue("Nước giải khát", 35);
		dataset.setValue("Bánh kẹo", 25);
		dataset.setValue("Thực phẩm", 20);
		dataset.setValue("Đồ dùng", 15);
		dataset.setValue("Khác", 5);

		categoryChart = ChartFactory.createPieChart("Phân bố danh mục", dataset, true, true, false);

		customizePieChart(categoryChart);
		categoryChartPanel = new ChartPanel(categoryChart);
		categoryChartPanel.setPreferredSize(new Dimension(400, 400));
	}

	private void createOrderChart() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		// Add sample data
		dataset.addValue(25, "Đơn hàng", "T1");
		dataset.addValue(30, "Đơn hàng", "T2");
		dataset.addValue(28, "Đơn hàng", "T3");
		dataset.addValue(35, "Đơn hàng", "T4");
		dataset.addValue(32, "Đơn hàng", "T5");
		dataset.addValue(40, "Đơn hàng", "T6");

		orderChart = ChartFactory.createLineChart("Biểu đồ đơn hàng theo tháng", "Tháng", "Số đơn hàng", dataset,
				PlotOrientation.VERTICAL, true, true, false);

		customizeChart(orderChart);
		orderChartPanel = new ChartPanel(orderChart);
		orderChartPanel.setPreferredSize(Dimensions.CHART);
	}

	private void customizeChart(JFreeChart chart) {
		chart.setBackgroundPaint(Colors.PRIMARY);
		CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Colors.PRIMARY);
		plot.setDomainGridlinePaint(new Color(220, 220, 220));
		plot.setRangeGridlinePaint(new Color(220, 220, 220));

		LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
		renderer.setSeriesPaint(0, Colors.ACCENT);
		renderer.setSeriesStroke(0, new BasicStroke(2.0f));
	}

	private void customizePieChart(JFreeChart chart) {
		chart.setBackgroundPaint(Colors.PRIMARY);
		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setBackgroundPaint(Colors.PRIMARY);

		// Set colors for pie sections
		for (int i = 0; i < Colors.CHART_COLORS.length; i++) {
			plot.setSectionPaint(i, Colors.CHART_COLORS[i]);
		}
	}
	// endregion

	// region Table Creation
	private void createProductTable() {
		productModel = new DefaultTableModel(new String[] { "Sản phẩm", "Số lượng", "Doanh thu", "Tỷ lệ" }, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tblProducts = new JTable(productModel);
		styleTable(tblProducts);

		// Add sample data
		Object[][] data = { { "Coca Cola", 150, "1,800,000 đ", "25%" }, { "Pepsi", 120, "1,440,000 đ", "20%" },
				{ "Fanta", 90, "1,080,000 đ", "15%" }, { "Snack Lay's", 85, "850,000 đ", "12%" },
				{ "Kẹo gấu", 75, "750,000 đ", "10%" } };

		for (Object[] row : data) {
			productModel.addRow(row);
		}
	}

	private void styleTable(JTable table) {
		// Table settings
		table.setFont(Fonts.TABLE_CELL);
		table.setRowHeight(Dimensions.ROW_HEIGHT);
		table.setBackground(Colors.ROW_EVEN);
		table.setSelectionBackground(Colors.SELECTED_BG);
		table.setSelectionForeground(Colors.SELECTED_FG);
		table.setShowGrid(true);
		table.setGridColor(Colors.BORDER);
		table.setBorder(BorderFactory.createLineBorder(Colors.BORDER));

		// Header settings
		JTableHeader header = table.getTableHeader();
		header.setFont(Fonts.TABLE_HEADER);
		header.setBackground(Colors.HEADER_BG);
		header.setForeground(Colors.HEADER_FG);
		header.setBorder(BorderFactory.createLineBorder(Colors.BORDER));
		header.setPreferredSize(new Dimension(header.getWidth(), Dimensions.HEADER_HEIGHT));
		header.setReorderingAllowed(false);

		// Center align header
		((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

		// Custom cell renderer
		table.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
	}

	private class CustomTableCellRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			if (!isSelected) {
				c.setBackground(row % 2 == 0 ? Colors.ROW_EVEN : Colors.ROW_ODD);
			}

			// Align numbers and currency to right
			if (value instanceof Number || (value instanceof String && value.toString().matches(".*\\d+.*đ"))) {
				setHorizontalAlignment(JLabel.RIGHT);
			} else {
				setHorizontalAlignment(JLabel.LEFT);
			}

			setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
			return c;
		}
	}
	// endregion

	// region Summary Cards
	private JPanel createRevenueSummaryCards() {
		JPanel panel = new JPanel(new GridLayout(1, 4, 20, 0));
		panel.setBackground(Colors.PRIMARY);

		addSummaryCard(panel, "Tổng doanh thu", "15,000,000 đ", "+12.5%", true);
		addSummaryCard(panel, "Tổng đơn hàng", "150", "+8.3%", true);
		addSummaryCard(panel, "Giá trị TB/đơn", "100,000 đ", "-2.1%", false);
		addSummaryCard(panel, "Lợi nhuận", "3,500,000 đ", "+15.2%", true);

		return panel;
	}

	private JPanel createOrderSummaryCards() {
		JPanel panel = new JPanel(new GridLayout(1, 4, 20, 0));
		panel.setBackground(Colors.PRIMARY);

		addSummaryCard(panel, "Tổng đơn hàng", "150", null, true);
		addSummaryCard(panel, "Đơn thành công", "142", "94.7%", true);
		addSummaryCard(panel, "Đơn hủy", "8", "5.3%", false);
		addSummaryCard(panel, "Đang xử lý", "5", null, true);

		return panel;
	}

	private void addSummaryCard(JPanel parent, String title, String value, String change, boolean isPositive) {
		JPanel card = new JPanel();
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setBackground(Colors.PRIMARY);
		card.setBorder(createRoundedBorder(Dimensions.BORDER_RADIUS));

		// Title
		JLabel lblTitle = new JLabel(title);
		lblTitle.setFont(Fonts.BODY);
		lblTitle.setForeground(Colors.TEXT);
		lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

		// Value
		JLabel lblValue = new JLabel(value);
		lblValue.setFont(Fonts.VALUE);
		lblValue.setForeground(Colors.ACCENT);
		lblValue.setAlignmentX(Component.LEFT_ALIGNMENT);

		card.add(lblTitle);
		card.add(Box.createVerticalStrut(10));
		card.add(lblValue);

		// Change indicator
		if (change != null) {
			JLabel lblChange = new JLabel(isPositive ? "+" + change : change);
			lblChange.setFont(Fonts.BODY);
			lblChange.setForeground(isPositive ? Colors.SUCCESS : Colors.ERROR);
			lblChange.setAlignmentX(Component.LEFT_ALIGNMENT);

			card.add(Box.createVerticalStrut(5));
			card.add(lblChange);
		}

		parent.add(card);
	}
	// endregion

	// region Export Functions
	private void exportRevenueReport() {
		try {
			File file = chooseExportFile("PDF Files", "pdf");
			if (file != null) {
				generateRevenuePDF(file);
				showMessage("Xuất báo cáo doanh thu thành công!", MessageType.SUCCESS);
			}
		} catch (Exception e) {
			showMessage("Lỗi khi xuất báo cáo: " + e.getMessage(), MessageType.ERROR);
		}
	}

	private void exportProductReport() {
		try {
			File file = chooseExportFile("Excel Files", "xlsx");
			if (file != null) {
				generateProductExcel(file);
				showMessage("Xuất báo cáo sản phẩm thành công!", MessageType.SUCCESS);
			}
		} catch (Exception e) {
			showMessage("Lỗi khi xuất báo cáo: " + e.getMessage(), MessageType.ERROR);
		}
	}

	private void exportOrderReport() {
		try {
			File file = chooseExportFile("PDF Files", "pdf");
			if (file != null) {
				generateOrderPDF(file);
				showMessage("Xuất báo cáo đơn hàng thành công!", MessageType.SUCCESS);
			}
		} catch (Exception e) {
			showMessage("Lỗi khi xuất báo cáo: " + e.getMessage(), MessageType.ERROR);
		}
	}

	private File chooseExportFile(String description, String extension) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Chọn nơi lưu báo cáo");
		fileChooser.setFileFilter(new FileNameExtensionFilter(description, extension));

		if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			if (!file.getName().toLowerCase().endsWith("." + extension)) {
				file = new File(file.getAbsolutePath() + "." + extension);
			}
			return file;
		}
		return null;
	}

	private void generateRevenuePDF(File file) throws IOException, DocumentException {
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();

		// Add title
		addPDFTitle(document, "BÁO CÁO DOANH THU");

		// Add date range
		addPDFDateRange(document);

		// Add summary
		document.add(new Paragraph("\nTổng quan:"));
		document.add(new Paragraph("Tổng doanh thu: 15,000,000 đ"));
		document.add(new Paragraph("Tổng đơn hàng: 150"));
		document.add(new Paragraph("Giá trị trung bình/đơn: 100,000 đ"));
		document.add(new Paragraph("Lợi nhuận: 3,500,000 đ"));

		// Add chart
		if (revenueChart != null) {
			addChartToPDF(document, revenueChart);
		}

		document.close();
	}

	private void generateOrderPDF(File file) throws IOException, DocumentException {
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();

		// Add title
		addPDFTitle(document, "BÁO CÁO ĐƠN HÀNG");

		// Add date range
		addPDFDateRange(document);

		// Add summary
		document.add(new Paragraph("\nTổng quan:"));
		document.add(new Paragraph("Tổng số đơn hàng: 150"));
		document.add(new Paragraph("Đơn thành công: 142 (94.7%)"));
		document.add(new Paragraph("Đơn hủy: 8 (5.3%)"));
		document.add(new Paragraph("Đơn đang xử lý: 5"));

		// Add chart
		if (orderChart != null) {
			addChartToPDF(document, orderChart);
		}

		document.close();
	}

	private void generateProductExcel(File file) throws IOException {
		Workbook workbook = new HSSFWorkbook();

		// Create sheet
		Sheet sheet = workbook.createSheet("Thống kê sản phẩm");

		// Create header row
		Row headerRow = sheet.createRow(0);
		String[] columns = { "Sản phẩm", "Số lượng", "Doanh thu", "Tỷ lệ" };

		// Create header cells
		for (int i = 0; i < columns.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(columns[i]);

			// Style header cells
			CellStyle headerStyle = workbook.createCellStyle();
			Font headerFont = (Font) workbook.createFont();
			((org.apache.poi.ss.usermodel.Font) headerFont).setBold(true);
			headerStyle.setFont((org.apache.poi.ss.usermodel.Font) headerFont);
			cell.setCellStyle(headerStyle);
		}

		// Add data rows
		for (int i = 0; i < productModel.getRowCount(); i++) {
			Row row = sheet.createRow(i + 1);
			for (int j = 0; j < productModel.getColumnCount(); j++) {
				Cell cell = row.createCell(j);
				cell.setCellValue(productModel.getValueAt(i, j).toString());
			}
		}

		// Autosize columns
		for (int i = 0; i < columns.length; i++) {
			sheet.autoSizeColumn(i);
		}

		// Write to file
		try (FileOutputStream fileOut = new FileOutputStream(file)) {
			workbook.write(fileOut);
		} finally {
			workbook.close();
		}
	}

	private void addPDFTitle(Document document, String title) throws DocumentException {
		Paragraph titlePara = new Paragraph(title);
		titlePara.setAlignment(Element.ALIGN_CENTER);
		titlePara.setSpacingAfter(20);
		document.add(titlePara);
	}

	private void addPDFDateRange(Document document) throws DocumentException {
		Paragraph dateRange = new Paragraph(String.format("Từ ngày: %s đến ngày: %s",
				dateFormat.format(dateFrom.getDate()), dateFormat.format(dateTo.getDate())));
		dateRange.setSpacingAfter(20);
		document.add(dateRange);
	}

	private void addChartToPDF(Document document, JFreeChart chart) throws DocumentException, IOException {
		BufferedImage chartImage = chart.createBufferedImage(600, 400);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(chartImage, "png", baos);
		Image image = Image.getInstance(baos.toByteArray());
		image.setAlignment(Element.ALIGN_CENTER);
		image.scaleToFit(500, 300);
		document.add(image);
	}
	// endregion

	// region Utility Methods
	private void setupDateChooser(JDateChooser chooser) {
		chooser.setPreferredSize(Dimensions.DATE_CHOOSER);
		chooser.setDateFormatString("dd/MM/yyyy");
		chooser.setFont(Fonts.BODY);
	}

	private JButton createButton(String text, Color bg, Color fg, boolean isAccent) {
		JButton button = new JButton(text);
		button.setFont(Fonts.BODY);
		button.setForeground(fg);
		button.setBackground(bg);
		button.setPreferredSize(Dimensions.BUTTON);
		button.setBorder(createRoundedButtonBorder(isAccent ? bg : Colors.BORDER));
		button.setFocusPainted(false);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));

		setupButtonHover(button, bg, isAccent);

		return button;
	}

	private void setupButtonHover(JButton button, Color defaultBg, boolean isAccent) {
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				button.setBackground(isAccent ? defaultBg.darker() : Colors.SECONDARY);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button.setBackground(defaultBg);
			}
		});
	}

	private Border createRoundedBorder(int radius) {
		return BorderFactory.createCompoundBorder(new RoundedBorder(radius, Colors.BORDER),
				BorderFactory.createEmptyBorder(15, 15, 15, 15));
	}

	private Border createRoundedButtonBorder(Color color) {
		return BorderFactory.createCompoundBorder(new RoundedBorder(Dimensions.BUTTON_RADIUS, color),
				BorderFactory.createEmptyBorder(5, 15, 5, 15));
	}

	private void initializeData() {
		// Set initial date range
		dateFrom.setDate(getFirstDayOfMonth());
		dateTo.setDate(new Date());

		// Load initial data
		loadData();
	}

	private Date getFirstDayOfMonth() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	private void loadData() {
		try {
			// TODO: Implement actual data loading from database
			updateTables();
			updateCharts();
		} catch (Exception e) {
			showMessage("Lỗi khi tải dữ liệu: " + e.getMessage(), MessageType.ERROR);
		}
	}

	private void updateTables() {
		// Update product table
		updateProductTable();

	}

	private void updateProductTable() {
		productModel.setRowCount(0);

		Object[][] data = { { "Coca Cola", 150, "1,800,000 đ", "25%" }, { "Pepsi", 120, "1,440,000 đ", "20%" },
				{ "Fanta", 90, "1,080,000 đ", "15%" }, { "Snack Lay's", 85, "850,000 đ", "12%" },
				{ "Kẹo gấu", 75, "750,000 đ", "10%" } };

		for (Object[] row : data) {
			productModel.addRow(row);
		}
	}

	private void updateCharts() {
		revenueChartPanel.repaint();
		categoryChartPanel.repaint();
		orderChartPanel.repaint();
	}

	private void refreshData() {
		try {
			validateDateRange();
			loadData();
			showMessage("Đã cập nhật dữ liệu thành công!", MessageType.SUCCESS);
		} catch (Exception e) {
			showMessage("Lỗi khi cập nhật dữ liệu: " + e.getMessage(), MessageType.ERROR);
		}
	}

	private void validateDateRange() throws IllegalArgumentException {
		if (dateFrom.getDate() == null || dateTo.getDate() == null) {
			throw new IllegalArgumentException("Vui lòng chọn khoảng thời gian!");
		}
		if (dateFrom.getDate().after(dateTo.getDate())) {
			throw new IllegalArgumentException("Ngày bắt đầu phải trước ngày kết thúc!");
		}
	}

	private enum MessageType {
		SUCCESS, ERROR
	}

	private void showMessage(String message, MessageType type) {
		String title = type == MessageType.SUCCESS ? "Thành công" : "Lỗi";
		int messageType = type == MessageType.SUCCESS ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;

		JOptionPane.showMessageDialog(this, message, title, messageType);
	}

	// Inner class for rounded borders
	private static class RoundedBorder extends AbstractBorder {
		private final int radius;
		private final Color color;

		RoundedBorder(int radius, Color color) {
			this.radius = radius;
			this.color = color;
		}

		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setColor(color);
			g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
			g2d.dispose();
		}

		@Override
		public Insets getBorderInsets(Component c) {
			return new Insets(radius / 2, radius / 2, radius / 2, radius / 2);
		}

		@Override
		public boolean isBorderOpaque() {
			return false;
		}
	}
}