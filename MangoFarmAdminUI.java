import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Vector;

public class MangoFarmAdminUI extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);

    // ตารางสินค้า (ตัวอย่าง)
    private DefaultTableModel productModel;
    private JTable productTable;

    // ช่องกรอกสินค้า
    private JTextField tfProdCode, tfProdName, tfPrice, tfStock, tfCategory;

    // ตารางคำสั่งซื้อ (ตัวอย่าง)
    private DefaultTableModel orderModel;
    private JTable orderTable;

    public MangoFarmAdminUI() {
        super("สวนมะม่วง - ระบบจัดการ (Admin)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 700));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        installNimbusLAF();

        // Header
        add(buildHeader(), BorderLayout.NORTH);

        // Left Menu
        add(buildLeftMenu(), BorderLayout.WEST);

        // Content (Cards)
        buildAllPages();
        add(contentPanel, BorderLayout.CENTER);
    }

    private void installNimbusLAF() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
        }
    }

    private JComponent buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(new EmptyBorder(12, 16, 12, 16));
        JLabel title = new JLabel("สวนมะม่วง – แดชบอร์ดผู้ดูแลระบบ");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        JLabel sub = new JLabel("จัดการผู้ใช้ • สินค้า • คำสั่งซื้อ • ขนส่ง • สิทธิ์ • รายงาน • ตั้งค่า");
        sub.setForeground(new Color(90, 90, 90));
        JPanel box = new JPanel(new GridLayout(2, 1));
        box.setOpaque(false);
        box.add(title);
        box.add(sub);
        header.add(box, BorderLayout.WEST);

        JButton btnLogout = new JButton("ออกจากระบบ");
        header.add(btnLogout, BorderLayout.EAST);
        return header;
    }

    private JComponent buildLeftMenu() {
        JPanel left = new JPanel();
        left.setLayout(new GridLayout(0, 1, 8, 8));
        left.setBorder(new EmptyBorder(16, 12, 16, 12));
        left.setPreferredSize(new Dimension(260, getHeight()));
        left.setBackground(new Color(248, 248, 248));

        left.add(menuButton("เมนูผู้ใช้งาน", "PAGE_HOME"));
        left.add(menuButton("การจัดการผู้ใช้", "PAGE_USERS"));
        left.add(menuButton("การจัดการสินค้า", "PAGE_PRODUCTS"));
        left.add(menuButton("การจัดการคำสั่งซื้อ", "PAGE_ORDERS"));
        left.add(menuButton("การจัดการสถานะและการขนส่ง", "PAGE_SHIPPING"));
        left.add(menuButton("ระบบสิทธิ์และบทบาท", "PAGE_ROLES"));
        left.add(menuButton("รายงานและสถิติ", "PAGE_REPORTS"));
        left.add(menuButton("การตั้งค่าระบบ", "PAGE_SETTINGS"));

        return new JScrollPane(left);
    }

    private JButton menuButton(String text, String cardKey) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(Color.WHITE);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(10, 12, 10, 12)));
        btn.addActionListener((ActionEvent e) -> cardLayout.show(contentPanel, cardKey));
        return btn;
    }

    private void buildAllPages() {
        contentPanel.add(buildHomePage(), "PAGE_HOME");
        contentPanel.add(buildUsersPage(), "PAGE_USERS");
        contentPanel.add(buildProductsPage(), "PAGE_PRODUCTS");
        contentPanel.add(buildOrdersPage(), "PAGE_ORDERS");
        contentPanel.add(buildShippingPage(), "PAGE_SHIPPING");
        contentPanel.add(buildRolesPage(), "PAGE_ROLES");
        contentPanel.add(buildReportsPage(), "PAGE_REPORTS");
        contentPanel.add(buildSettingsPage(), "PAGE_SETTINGS");
    }

    private JComponent buildHomePage() {
        JPanel p = pageWrapper("เมนูผู้ใช้งาน");
        p.add(infoCard("ภาพรวมระบบ",
                "จำนวนผู้ใช้: 128\nสินค้าทั้งหมด: 42 รายการ\nคำสั่งซื้อที่กำลังดำเนินการ: 7\nสถานะขนส่งที่รอดำเนินการ: 3"));
        p.add(infoCard("ทางลัด",
                "- เพิ่มสินค้าใหม่\n- สร้างคำสั่งซื้อ\n- ดูรายงานวันนี้\n- ตั้งค่าการชำระเงิน"));
        return new JScrollPane(p);
    }

    private JComponent buildUsersPage() {
        JPanel p = pageWrapper("การจัดการผู้ใช้");
        p.add(infoCard("คำอธิบาย",
                "เพิ่ม/ลบ/แก้ไขผู้ใช้งาน กำหนดสิทธิ์การเข้าถึงระบบ และรีเซ็ตรหัสผ่าน"));
        p.add(placeholderTable("ตารางผู้ใช้ (ตัวอย่าง)", new String[] {
                "รหัสผู้ใช้", "ชื่อ", "อีเมล", "บทบาท", "สถานะ"
        }, new Object[][] {
                { "U001", "สมชาย", "somchai@example.com", "Admin", "เปิดใช้งาน" },
                { "U002", "ปวีณา", "paweena@example.com", "Staff", "เปิดใช้งาน" },
                { "U003", "ชญาน์", "chayan@example.com", "Viewer", "ปิดใช้งาน" }
        }));
        return new JScrollPane(p);
    }

    private JComponent buildProductsPage() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel h = new JLabel("การจัดการสินค้า");
        h.setFont(h.getFont().deriveFont(Font.BOLD, 18f));
        root.add(h, BorderLayout.NORTH);

        // ตารางสินค้า
        productModel = new DefaultTableModel(new String[] {
                "รหัสสินค้า", "ชื่อสินค้า", "หมวดหมู่", "ราคา/กก.", "สต็อก(กก.)"
        }, 0);
        productTable = new JTable(productModel);
        productTable.setFillsViewportHeight(true);
        JScrollPane tableScroll = new JScrollPane(productTable);
        root.add(tableScroll, BorderLayout.CENTER);

        // ใส่ข้อมูลตัวอย่าง
        addSampleProducts();

        // ฟอร์ม CRUD
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setBorder(new EmptyBorder(12, 0, 0, 0));

        tfProdCode = new JTextField();
        tfProdName = new JTextField();
        tfCategory = new JTextField("มะม่วง");
        tfPrice = new JTextField();
        tfStock = new JTextField();

        form.add(new JLabel("รหัสสินค้า:"));
        form.add(tfProdCode);
        form.add(new JLabel("ชื่อสินค้า:"));
        form.add(tfProdName);
        form.add(new JLabel("หมวดหมู่:"));
        form.add(tfCategory);
        form.add(new JLabel("ราคา/กก.:"));
        form.add(tfPrice);
        form.add(new JLabel("สต็อก(กก.):"));
        form.add(tfStock);

        JPanel crud = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("เพิ่ม");
        JButton btnUpdate = new JButton("แก้ไข");
        JButton btnDelete = new JButton("ลบ");
        JButton btnClear = new JButton("ล้างฟอร์ม");
        crud.add(btnClear);
        crud.add(btnAdd);
        crud.add(btnUpdate);
        crud.add(btnDelete);

        JPanel south = new JPanel(new BorderLayout());
        south.add(form, BorderLayout.CENTER);
        south.add(crud, BorderLayout.SOUTH);
        root.add(south, BorderLayout.SOUTH);

        // Event handlers
        btnAdd.addActionListener(e -> addProduct());
        btnUpdate.addActionListener(e -> updateProduct());
        btnDelete.addActionListener(e -> deleteProduct());
        btnClear.addActionListener(e -> clearProductForm());

        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting())
                fillFormFromSelectedRow();
        });

        return root;
    }

    private void addSampleProducts() {
        productModel.addRow(new Object[] { "MN-001", "มะม่วงน้ำดอกไม้ (คัดพิเศษ)", "มะม่วง", "120", "350" });
        productModel.addRow(new Object[] { "MN-002", "มะม่วงเขียวเสวย", "มะม่วง", "95", "220" });
        productModel.addRow(new Object[] { "MN-003", "มะม่วงฟ้าลั่น", "มะม่วง", "80", "180" });
    }

    private void addProduct() {
        if (tfProdCode.getText().isBlank() || tfProdName.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "กรอกรหัสและชื่อสินค้าให้ครบ", "แจ้งเตือน",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        productModel.addRow(new Object[] {
                tfProdCode.getText().trim(),
                tfProdName.getText().trim(),
                tfCategory.getText().trim(),
                tfPrice.getText().trim(),
                tfStock.getText().trim()
        });
        clearProductForm();
    }

    private void updateProduct() {
        int row = productTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "กรุณาเลือกแถวที่ต้องการแก้ไข", "แจ้งเตือน",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        productModel.setValueAt(tfProdCode.getText().trim(), row, 0);
        productModel.setValueAt(tfProdName.getText().trim(), row, 1);
        productModel.setValueAt(tfCategory.getText().trim(), row, 2);
        productModel.setValueAt(tfPrice.getText().trim(), row, 3);
        productModel.setValueAt(tfStock.getText().trim(), row, 4);
    }

    private void deleteProduct() {
        int row = productTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "กรุณาเลือกแถวที่ต้องการลบ", "แจ้งเตือน", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int ok = JOptionPane.showConfirmDialog(this, "ยืนยันการลบรายการนี้?", "ยืนยัน", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION)
            productModel.removeRow(row);
    }

    private void clearProductForm() {
        tfProdCode.setText("");
        tfProdName.setText("");
        tfCategory.setText("มะม่วง");
        tfPrice.setText("");
        tfStock.setText("");
        productTable.clearSelection();
    }

    private void fillFormFromSelectedRow() {
        int row = productTable.getSelectedRow();
        if (row < 0)
            return;
        tfProdCode.setText(String.valueOf(productModel.getValueAt(row, 0)));
        tfProdName.setText(String.valueOf(productModel.getValueAt(row, 1)));
        tfCategory.setText(String.valueOf(productModel.getValueAt(row, 2)));
        tfPrice.setText(String.valueOf(productModel.getValueAt(row, 3)));
        tfStock.setText(String.valueOf(productModel.getValueAt(row, 4)));
    }

    private JComponent buildOrdersPage() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel h = new JLabel("การจัดการคำสั่งซื้อ");
        h.setFont(h.getFont().deriveFont(Font.BOLD, 18f));
        root.add(h, BorderLayout.NORTH);

        orderModel = new DefaultTableModel(new String[] {
                "เลขที่คำสั่งซื้อ", "ลูกค้า", "รายการสินค้า", "ยอดรวม (บาท)", "สถานะ"
        }, 0);

        orderTable = new JTable(orderModel);
        orderTable.setFillsViewportHeight(true);
        root.add(new JScrollPane(orderTable), BorderLayout.CENTER);

        // ตัวอย่างข้อมูล
        orderModel.addRow(new Object[] { "ORD-2025-0001", "บ.อิ่มอร่อย จำกัด", "มะม่วงน้ำดอกไม้ 50กก.", "6,000",
                "กำลังแพ็คของ" });
        orderModel.addRow(new Object[] { "ORD-2025-0002", "คุณธนกฤต", "มะม่วงเขียวเสวย 20กก.", "1,900", "รอชำระเงิน" });

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnNew = new JButton("สร้างคำสั่งซื้อ");
        JButton btnMarkPacked = new JButton("ทำเครื่องหมาย: แพ็คแล้ว");
        JButton btnMarkShipped = new JButton("ทำเครื่องหมาย: ส่งออกแล้ว");
        actions.add(btnNew);
        actions.add(btnMarkPacked);
        actions.add(btnMarkShipped);
        root.add(actions, BorderLayout.SOUTH);

        btnNew.addActionListener(e -> JOptionPane.showMessageDialog(this, "ฟอร์มสร้างคำสั่งซื้อ (ตัวอย่าง)"));
        btnMarkPacked.addActionListener(e -> setOrderStatus("แพ็คแล้ว"));
        btnMarkShipped.addActionListener(e -> setOrderStatus("ส่งออกแล้ว"));

        return root;
    }

    private void setOrderStatus(String status) {
        int row = orderTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "เลือกคำสั่งซื้อก่อน", "แจ้งเตือน", JOptionPane.WARNING_MESSAGE);
            return;
        }
        orderModel.setValueAt(status, row, 4);
    }

    private JComponent buildShippingPage() {
        JPanel p = pageWrapper("การจัดการสถานะและการขนส่ง");
        p.add(infoCard("เวิร์กโฟลว์ขนส่ง (ตัวอย่าง)",
                "สร้างใบแพ็ค • พิมพ์ป้ายพัสดุ • นัดรับพัสดุ • อัปเดตเลขพัสดุ • ติดตามสถานะ"));
        p.add(infoCard("ผู้ให้บริการขนส่ง",
                "ไปรษณีย์ไทย • Kerry • J&T • Flash\n\nรองรับการส่งแบบแช่เย็นสำหรับผลไม้สุก"));
        return new JScrollPane(p);
    }

    private JComponent buildRolesPage() {
        JPanel p = pageWrapper("ระบบสิทธิ์และบทบาท");
        p.add(placeholderTable("รายการบทบาท (ตัวอย่าง)", new String[] {
                "ชื่อบทบาท", "คำอธิบาย", "สิทธิ์หลัก"
        }, new Object[][] {
                { "Admin", "จัดการทุกส่วนของระบบ", "ผู้ใช้, สินค้า, คำสั่งซื้อ, รายงาน, ตั้งค่า" },
                { "Staff", "จัดการสินค้าและคำสั่งซื้อ", "สินค้า, คำสั่งซื้อ" },
                { "Viewer", "ดูข้อมูลอย่างเดียว", "อ่านอย่างเดียว" }
        }));
        return new JScrollPane(p);
    }

    private JComponent buildReportsPage() {
        JPanel p = pageWrapper("รายงานและสถิติ");
        p.add(infoCard("ตัวอย่างรายงาน",
                "- ยอดขายรายวัน/สัปดาห์/เดือน\n- สินค้าขายดี (Top Sellers)\n- สต็อกต่ำกว่ากำหนด (Low Stock)\n- ประสิทธิภาพขนส่ง/วันจัดส่ง"));
        p.add(infoCard("ทิปส์",
                "สามารถต่อยอดด้วยกราฟ/ชาร์ต (เช่น JFreeChart) หรือส่งออก Excel/CSV"));
        return new JScrollPane(p);
    }

    private JComponent buildSettingsPage() {
        JPanel p = pageWrapper("การตั้งค่าระบบ");
        p.add(placeholderForm(
                new String[] { "ชื่อร้าน/สวน", "อีเมลติดต่อ", "เบอร์ติดต่อ", "ที่อยู่จัดส่งเริ่มต้น",
                        "เงื่อนไขการสั่งซื้อ" },
                new String[] { "สวนมะม่วงภูผา", "contact@mango.example.com", "08x-xxx-xxxx", "อ.กันทรวิชัย จ.มหาสารคาม",
                        "ขั้นต่ำ 10 กก." }));
        return new JScrollPane(p);
    }

    // ---------- UI helper blocks ----------
    private JPanel pageWrapper(String title) {
        JPanel wrap = new JPanel();
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setBorder(new EmptyBorder(16, 16, 16, 16));
        JLabel h = new JLabel(title);
        h.setFont(h.getFont().deriveFont(Font.BOLD, 18f));
        h.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrap.add(h);
        wrap.add(Box.createVerticalStrut(12));
        return wrap;
    }

    private JComponent infoCard(String title, String body) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 225, 225)),
                new EmptyBorder(14, 16, 14, 16)));
        JLabel h = new JLabel(title);
        h.setFont(h.getFont().deriveFont(Font.BOLD, 15f));
        JTextArea ta = new JTextArea(body);
        ta.setEditable(false);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setOpaque(false);
        card.add(h, BorderLayout.NORTH);
        card.add(ta, BorderLayout.CENTER);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        return card;
    }

    private JComponent placeholderTable(String title, String[] columns, Object[][] data) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 225, 225)),
                new EmptyBorder(14, 16, 14, 16)));
        JLabel h = new JLabel(title);
        h.setFont(h.getFont().deriveFont(Font.BOLD, 15f));
        JTable table = new JTable(new DefaultTableModel(data, columns));
        table.setFillsViewportHeight(true);
        card.add(h, BorderLayout.NORTH);
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));
        return card;
    }

    private JComponent placeholderForm(String[] labels, String[] defaults) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 225, 225)),
                new EmptyBorder(14, 16, 14, 16)));
        JLabel h = new JLabel("ตั้งค่าทั่วไป");
        h.setFont(h.getFont().deriveFont(Font.BOLD, 15f));
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        for (int i = 0; i < labels.length; i++) {
            form.add(new JLabel(labels[i] + ":"));
            form.add(new JTextField(defaults[i]));
        }
        JButton save = new JButton("บันทึกการตั้งค่า");
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(save);
        card.add(h, BorderLayout.NORTH);
        card.add(form, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        return card;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MangoFarmAdminUI().setVisible(true));
    }
}
