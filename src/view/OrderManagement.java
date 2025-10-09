package view;

import dao.DBConnection;
import dao.OrderDAO;
import model.Order;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.OrderDetail;
import model.ProductStatistic;
import java.sql.*;
import java.util.*;


/**
 *
 * @author ADMIN
 */
public class OrderManagement extends javax.swing.JFrame {
    private OrderDAO orderDAO;

    /**
     * Creates new form OrderManagement
     */
    public OrderManagement() {
        initComponents();
        setLocationRelativeTo(null); // Căn giữa khi khởi động
        orderDAO = new OrderDAO();
        loadOrders();
        loadProductsToComboBox();
        setupStatisticComboBox();

        // 🔹 Gọi sự kiện khi chọn sản phẩm để tự động thống kê
        cbProducts.addActionListener(e -> loadStatistics());

        // 🔹 Không cho chỉnh sửa thủ công kết quả thống kê
        txtQuantity.setEditable(false);
        txtTotalRevenue.setEditable(false);
    }
    
    // 1. Nạp dữ liệu Order vào bảng orderTable
    private void loadOrders() {
        List<Order> list = OrderDAO.getAllOrders();
        DefaultTableModel model = (DefaultTableModel) tblOrders.getModel();
        model.setRowCount(0);
        for (Order o : list) {
            model.addRow(new Object[]{
                o.getId(),//mã
                o.getOrderDate(),//ngày đặt
                o.getOrderDelivery(),//ngày giao
                o.getCreatedBy(),//người đặt
                o.getAddress(),//địa chỉ
                o.getNote(),//ghi chú
                o.getTotalAmount()//tổng tiền
            });
        }
        // clear detail table
        ((DefaultTableModel) tblOrderDetail.getModel()).setRowCount(0);
    }
    
    // Ví dụ trong JFrame hiển thị chi tiết đơn hàng
    private void loadOrderDetails(String orderId) {
        // Lấy model của bảng
        DefaultTableModel model = (DefaultTableModel) tblOrderDetail.getModel();
        model.setRowCount(0); // xóa các hàng cũ

        OrderDAO orderDAO = new OrderDAO();
        List<OrderDetail> details = orderDAO.getOrderDetails(orderId);

        for (OrderDetail od : details) {
            model.addRow(new Object[]{
                od.getOrderDetailId(),  // Mã chi tiết đơn hàng
                od.getOrderId(),        //mã đơn hàng
                od.getProductId(),      // Mã sản phẩm
                od.getPrice(),          // Đơn giá
                od.getQuantity(),       // Số lượng
                od.getAmount()          //  Thành tiền (nếu có)
            });
        }

        // Gán model lại cho JTable
        tblOrderDetail.setModel(model);
    }
    
    private void loadProductsToComboBox() {
        cbProducts.removeAllItems();
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT name FROM product")) {
            while (rs.next()) {
                cbProducts.addItem(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    private void setupStatisticComboBox() {
    // Thêm các lựa chọn thống kê
        cbxStatisticType.removeAllItems();
        cbxStatisticType.addItem("Ngày");
        cbxStatisticType.addItem("Tuần");
        cbxStatisticType.addItem("Tháng");
        cbxStatisticType.addItem("Năm");

        // Lắng nghe sự kiện khi thay đổi lựa chọn
        cbxStatisticType.addActionListener(e -> loadStatistics());

        // Mặc định chọn "Ngày"
        cbxStatisticType.setSelectedIndex(0);
    }
    
    private void loadStatistics() {
        try {
            String type = cbxStatisticType.getSelectedItem().toString();
            String queryType = "";

            switch (type) {
                case "Ngày":
                    queryType = "DAY";
                    break;
                case "Tuần":
                    queryType = "WEEK";
                    break;
                case "Tháng":
                    queryType = "MONTH";
                    break;
                case "Năm":
                    queryType = "YEAR";
                    break;
            }

            // Thống kê theo sản phẩm
            String selectedProduct = (String) cbProducts.getSelectedItem();
            if (selectedProduct != null && !selectedProduct.isEmpty()) {
                ProductStatistic ps = orderDAO.getProductStatistic(queryType, selectedProduct);
                txtQuantity.setText("Số lượng bán: " + ps.getTotalQuantity());
                txtTotalRevenue.setText("Doanh thu sản phẩm: " + ps.getTotalRevenue() + " VND");
            } else {
                txtQuantity.setText("");
                txtTotalRevenue.setText("");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi thống kê: " + e.getMessage());
        }
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblOrders = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblOrderDetail = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        cbxStatisticType = new javax.swing.JComboBox<>();
        txtTotalRevenue = new javax.swing.JTextField();
        btnComeBack = new javax.swing.JButton();
        cbProducts = new javax.swing.JComboBox<>();
        txtQuantity = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Quản lý đơn hàng");

        tblOrders.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã đơn hàng", "Ngày đặt hàng", "Ngày giao hàng", "Người đặt", "Địa chỉ", "Ghi chú", "Tổng số tiền"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblOrders.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblOrdersMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblOrders);

        tblOrderDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã chi tiết", "Mã đơn hàng", "Mã sản phẩm", "Giá", "Số lượng", "Thành tiền"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblOrderDetail);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Thống kê:");

        cbxStatisticType.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cbxStatisticType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Thời gian" }));

        txtTotalRevenue.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        btnComeBack.setText("Quay lại");
        btnComeBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnComeBackActionPerformed(evt);
            }
        });

        cbProducts.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cbProducts.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tên sản phẩm" }));

        txtQuantity.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btnComeBack)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(477, 477, 477)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1071, Short.MAX_VALUE)
                                .addComponent(jScrollPane2))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(cbxStatisticType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(41, 41, 41)
                                .addComponent(cbProducts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(45, 45, 45)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtTotalRevenue, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
                                    .addComponent(txtQuantity))))))
                .addContainerGap(60, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btnComeBack)
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(26, 26, 26)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbxStatisticType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbProducts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(txtTotalRevenue, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblOrdersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblOrdersMouseClicked
        int row = tblOrders.getSelectedRow();
        if (row != -1) {
            String orderId = tblOrders.getValueAt(row, 0).toString();
            loadOrderDetails(orderId);
        }
    }//GEN-LAST:event_tblOrdersMouseClicked

    private void btnComeBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnComeBackActionPerformed
        this.dispose();
        AdminForm admin = new AdminForm();
        admin.setVisible(true);
        // TODO add your handling code here:
    }//GEN-LAST:event_btnComeBackActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(OrderManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OrderManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OrderManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OrderManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new OrderManagement().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnComeBack;
    private javax.swing.JComboBox<String> cbProducts;
    private javax.swing.JComboBox<String> cbxStatisticType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblOrderDetail;
    private javax.swing.JTable tblOrders;
    private javax.swing.JTextField txtQuantity;
    private javax.swing.JTextField txtTotalRevenue;
    // End of variables declaration//GEN-END:variables
}
