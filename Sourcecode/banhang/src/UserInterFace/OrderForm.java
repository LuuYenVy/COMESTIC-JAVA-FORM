
package UserInterFace;

import java.awt.Color;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.ListSelectionModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class OrderForm extends javax.swing.JFrame {

    private static Connection conn= new ConnectDB().createConn();;//,connData=null,connProduct;  
    private static PreparedStatement pst = null;  
    private static ResultSet rs = null;
    
    private boolean Add=false,Change=false;
    private String sql = "SELECT * \n" +
"FROM Invoices o\n" +
"LEFT JOIN Customers c ON o.customerID = c.ID; ";
    
    private Detail detail;
    
    public OrderForm(Detail d) {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
  
        detail=new Detail(d);
        Load(sql);
        Disabled();
        lblStatus.setForeground(Color.red);
    }
    
   
    
    private void Enabled(){
        txbID.setEnabled(true);
        cbxClassify.setEnabled(true);
        txbName.setEnabled(true);
        txbAddress.setEnabled(true);
       //txbPhone.setEnabled(true);
        cbxClassify.setEnabled(true);
        //txbAmount.setEnabled(true);
        //txbDate.setEnabled(true);
        cbxPaymentMethods.setEnabled(true);
        //btnProduct.setEnabled(true);
        txttotal.setEnabled(true);
        txbAddress.setEnabled(true);
        txbAddress.setEnabled(true);
        lblStatus.setText("Status!");
    }
    
    private void Disabled(){
        txbID.setEnabled(false);
        cbxClassify.setEnabled(false);
        txbName.setEnabled(false);
        txbAddress.setEnabled(false);
        //txbPhone.setEnabled(false);
        cbxProduct.setEnabled(false);
        //btnProduct.setEnabled(false);
        txbAmount.setEnabled(false);
        txbPrice.setEnabled(false);
        //txbWarrantyPeriod.setEnabled(false);
        txbIntoMoney.setEnabled(false);
        //txbDate.setEnabled(false);
        txttotal.setEnabled(false);
        cbxPaymentMethods.setEnabled(true);
        txbAddress.setEnabled(false);
        txbAmount.setEnabled(false);
 
    }
    private void updateproducts(int id,int quantityChange){
        String sql2 = "update products set sold=sold+? where id = ?;";

                try {
                    pst = conn.prepareStatement(sql2);
                    
                        pst.setInt(1, quantityChange);
                        pst.setInt(2, id);
                        pst.executeUpdate();
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                String sql3 = "update products set quantityremaining=quantityremaining - ? where id = ?;";

                try {
                    pst = conn.prepareStatement(sql3);
                   
                        pst.setInt(1, quantityChange);
                        pst.setInt(2, id);
                        pst.executeUpdate();
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
    }
   
    private void displayOrderDetails(int orderId) {
    //private void displayOrderDetails(int orderId) Xóa dữ liệu hiện có trong bảng tableDetail (nếu có)

    DefaultTableModel model2 = (DefaultTableModel) tbdetail.getModel();
    model2.setRowCount(0);
    //String sql1="select * from  DetailOrders where OrderID = ?;";
    String sql1 = "SELECT do.invoicedetailid, p.name, do.Quantity, p.Price, do.Subtotal " +
              "FROM InvoiceDetails do " +
              "INNER JOIN Products p ON do.ProductID = p.ID " +
              "WHERE do.InvoiceID = ?;";
    
    tbdetail.removeAll();
    String [] arr={"Detail Invoice ID","Product Name","Quantity","Price Per Unit","Total Price"};
    model2 = new DefaultTableModel(arr,0);
        try{
            
            pst=conn.prepareStatement(sql1);
            pst.setInt(1,orderId);
            rs=pst.executeQuery();
            while(rs.next()){
                Vector vector=new Vector();
                vector.add(rs.getInt("InvoiceDetailID"));
                vector.add(rs.getString("name").trim());
                vector.add(rs.getInt("Quantity"));
                vector.add(rs.getString("Price").trim());
                 vector.add(rs.getInt("Subtotal"));
                model2.addRow(vector);
            }
            tbdetail.setModel(model2);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    
    // Cập nhật lại bảng tableDetail để hiển thị dữ liệu mới
        tbdetail.setModel(model2);
}
    public void Load(String sql){
        tableOrder.removeAll();
        try{
            String [] arr={"Detail ID","Customer Name","Purchase Date","Total Amount ","Address","Payment Methos","Status"};
        DefaultTableModel modle=new DefaultTableModel(arr,0);
        pst=conn.prepareStatement(sql);
        rs=pst.executeQuery();
        while(rs.next()){
        Vector vector=new Vector();
        vector.add(rs.getInt("id"));

        // Xử lý giá trị NULL cho tên khách hàng
        String name = rs.getString("Name");
        vector.add(name != null ? name.trim() : null);

        // Xử lý giá trị NULL cho ngày mua hàng
        Date invoiceDate = rs.getDate("invoiceDate");
        vector.add(invoiceDate != null ? new SimpleDateFormat("dd/MM/yyyy").format(invoiceDate) : null);

        vector.add(rs.getInt("TotalAmount"));

        // Xử lý giá trị NULL cho địa chỉ giao hàng
        String address = rs.getString("ShippingAddress");
        vector.add(address != null ? address.trim() : null);

        // Xử lý giá trị NULL cho phương thức thanh toán
        String payment = rs.getString("Payment");
        vector.add(payment != null ? payment.trim() : null);
        // Xử lý giá trị NULL cho trạng thái
        String status = rs.getString("status");
        vector.add(status != null ? status.trim() : null);

        modle.addRow(vector);

            }
            tableOrder.setModel(modle);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    private void LoadClassify(){
        String sql = "SELECT * FROM Category";
        try {
        
            pst=conn.prepareStatement(sql);
            rs=pst.executeQuery();
            while(rs.next()){
                this.cbxClassify.addItem(rs.getString("CategoryName").trim());
            }
        }  
        catch (Exception e) {  
            e.printStackTrace();  
        }
    }
    
    private void LoadPaymentMethods(){
        cbxPaymentMethods.removeAllItems();
        cbxPaymentMethods.addItem("Bank");
        cbxPaymentMethods.addItem("Cash");
    }
    
    private void Refresh(){
        Add=false;
        Change=false;
        txbName.setText("");
        txbAddress.setText("");
        //txbPhone.setText("");
        txbID.setText("");
        txbAmount.setText("");
        txbPrice.setText("");
       // txbWarrantyPeriod.setText("");
        txbIntoMoney.setText("");
        //((JTextField)txbDate.getDateEditor().getUiComponent()).setText("");
        cbxClassify.removeAllItems();
        cbxProduct.removeAllItems();
        cbxPaymentMethods.removeAllItems();
        txbID.setEnabled(true);
        //btnAdd.setEnabled(true);
        btnDelete.setEnabled(false);
        btnSave.setEnabled(false);
        Disabled();
    }
    
    
    private void changeOrder(){
        int Click=tableOrder.getSelectedRow();
        TableModel model=tableOrder.getModel();
        int idorder=Integer.parseInt(txbID.getText());
        ListSelectionModel selectionModel = tbdetail.getSelectionModel();
                if (selectionModel.isSelectionEmpty()) {
        if(checkNull()){
            String sqlChange="UPDATE Invoices SET  ShippingAddress=? WHERE ID=?";
            try{
    
                pst=conn.prepareStatement(sqlChange);
                
                pst.setString(1, this.txbAddress.getText());
                
                pst.setString(2, this.txbID.getText());
                pst.executeUpdate();
                lblStatus.setText("Changes Successfully!");
                Disabled();
                Refresh();
                Load(sql);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
                }
                else {
                    if(checkNull()){
            String sqlChange="UPDATE Invoicedetails SET  Quantity=?, Subtotal=? WHERE invoiceDetailID=?";
            try{
                   int selectedRow = tbdetail.getSelectedRow();
                pst=conn.prepareStatement(sqlChange);
                int id=(int) tbdetail.getValueAt(selectedRow, 0);
               String amountText = this.txbAmount.getText();
                int amount = Integer.parseInt(amountText);
                pst.setInt(1, amount);
                String text = txbPrice.getText(); // Lấy chuỗi từ JTextField                
                text = text.replaceAll("[^0-9]", ""); // Loại bỏ các ký tự không phải số
                int price = Integer.parseInt(text); // Chuyển đổi chuỗi thành số nguyên

                int total=Integer.parseInt(txbAmount.getText())*price;
                pst.setInt(2, total);
                pst.setInt(3, id);
                pst.executeUpdate();
                int quantityChange= Integer.parseInt(txbAmount.getText())-amount;
                 updateproducts(id,quantityChange);
                lblStatus.setText("Change Successfully!");
                Disabled();
                Refresh();
                Load(sql);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
           
                }}
    }
    
    public boolean Check(){
        boolean kq=true;
        String sqlCheck="SELECT * FROM Invoices";
        try{

            pst=conn.prepareStatement(sqlCheck);
            rs=pst.executeQuery();
            while(rs.next()){
                if(this.txbID.getText().equals(rs.getString("ID").toString().trim())){
                    return false;                                           
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return kq;
    }
    
    public boolean checkNull(){
        boolean kq=true;
        ListSelectionModel selectionModel = tbdetail.getSelectionModel();
                if (selectionModel.isSelectionEmpty()) {
        
        if(String.valueOf(this.txbName.getText()).length()==0){
            lblStatus.setText("You haven't entered Customer Name!");
            return false;
        }
        if(String.valueOf(this.txbAddress.getText()).length()==0){
            lblStatus.setText("You haven't entered Address!");
            return false;
        }
                }
                else{

        if(String.valueOf(this.cbxClassify.getSelectedItem()).length()==0){
            lblStatus.setText("You haven't entered Category!");
            return false;
        }
        if(String.valueOf(this.cbxProduct.getSelectedItem()).length()==0){
            lblStatus.setText("You haven't entered Product Name!");
            return false;
        }
        if(String.valueOf(this.txbAmount.getText()).length()==0){
            lblStatus.setText("You haven't entered Quantity!");
            return false;
        }
            }
        return kq;
    }
    
    private double convertedToNumbers(String s){
        String number="";
        String []array=s.replace(","," ").split("\\s");
        for(String i:array){
            number=number.concat(i);
        }
        return Double.parseDouble(number);
    }
    
    private String cutChar(String arry){
        return arry.replaceAll("\\D+","");
    }
    
    private void loadData(){
        cbxClassify.removeAllItems();
        String sql = "SELECT * FROM Products where Name=?";
        try {

            pst=conn.prepareStatement(sql);
            pst.setString(1, this.cbxProduct.getSelectedItem().toString());
            rs=pst.executeQuery();
            while(rs.next()){
                cbxClassify.addItem(rs.getString("Classify").trim());
            }
        }  
        catch (Exception e) {  
            e.printStackTrace();  
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableOrder = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbdetail = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnBackHome = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        txbID = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txbName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txbAddress = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cbxProduct = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txbPrice = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txbIntoMoney = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        cbxPaymentMethods = new javax.swing.JComboBox<>();
        cbxClassify = new javax.swing.JComboBox<>();
        txbAmount = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txttotal = new javax.swing.JTextField();
        txbOrders = new javax.swing.JTextField();
        btnOrders = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btnRefresh = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        tableOrder.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        tableOrder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tableOrder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableOrderMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tableOrder);

        tbdetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbdetail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbdetailMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbdetail);

        jLabel1.setText("Invoice List");

        jLabel8.setText("Invoice Detail");

        lblStatus.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStatus.setText("Status");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 583, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 596, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblStatus)
                .addGap(91, 91, 91))
        );

        jLabel2.setFont(new java.awt.Font("Verdana", 0, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("INVOICE MANAGEMENT");

        btnBackHome.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnBackHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/logout_1.png"))); // NOI18N
        btnBackHome.setText("BACK");
        btnBackHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBackHomeMouseClicked(evt);
            }
        });

        jLabel15.setText("Invoice ID");

        txbID.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txbID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txbIDActionPerformed(evt);
            }
        });

        jLabel3.setText("Customer Name");

        jLabel4.setText("Address");

        jLabel6.setText("Category");

        jLabel7.setText("Product Name");
        jLabel7.setToolTipText("");

        cbxProduct.setMaximumRowCount(50);
        cbxProduct.setEnabled(false);
        cbxProduct.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cbxProductPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        cbxProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxProductActionPerformed(evt);
            }
        });

        jLabel9.setText("Quantity");

        jLabel10.setText("Price");
        jLabel10.setToolTipText("");

        txbPrice.setEnabled(false);

        jLabel12.setText("Total Amount");

        txbIntoMoney.setEnabled(false);

        jLabel13.setText("Payment Method");

        cbxPaymentMethods.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxPaymentMethodsActionPerformed(evt);
            }
        });

        cbxClassify.setEnabled(false);
        cbxClassify.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cbxClassifyPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        cbxClassify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxClassifyActionPerformed(evt);
            }
        });

        txbAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txbAmountKeyReleased(evt);
            }
        });

        jLabel11.setText("Total Amount ");

        txbOrders.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txbOrdersActionPerformed(evt);
            }
        });

        btnOrders.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Find.png"))); // NOI18N
        btnOrders.setText("Search");
        btnOrders.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOrdersActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txbID, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addComponent(txbName)
                    .addComponent(txbAddress))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addComponent(txbOrders, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnOrders)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(cbxPaymentMethods, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(250, 250, 250)))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel10))
                                .addGap(12, 12, 12)))
                        .addGap(14, 14, 14))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txbIntoMoney, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(59, 59, 59)
                        .addComponent(jLabel6)
                        .addGap(22, 22, 22)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbxClassify, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txbPrice, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addComponent(txbAmount, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addComponent(txttotal))
                .addGap(20, 20, 20))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(txbID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12)
                            .addComponent(txbIntoMoney, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(txbName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel13)
                                    .addComponent(cbxPaymentMethods, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7)))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cbxClassify, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6)))
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txbAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel9)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbxProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel10)
                                .addComponent(txbPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4)
                                .addComponent(txbAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel11)
                                .addComponent(txttotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txbOrders, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnOrders))
                        .addGap(15, 15, 15))))
        );

        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/icons8_refresh_30px.png"))); // NOI18N
        btnRefresh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRefreshMouseClicked(evt);
            }
        });
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Delete.png"))); // NOI18N
        btnDelete.setText("Cancel");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Save.png"))); // NOI18N
        btnSave.setText("Save");
        btnSave.setEnabled(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 24, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 24, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 24, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 24, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(237, 237, 237)
                .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(92, 92, 92)
                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(88, 88, 88)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(239, 239, 239)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(208, 208, 208))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnRefresh))
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                                .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jLabel14.setText("INVOICE INFORMATION");

        jLabel16.setText("INVOICE DETAIL INFORMATION");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(jLabel14))
                                    .addComponent(btnBackHome, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(50, 50, 50)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 653, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel16)
                                        .addGap(350, 350, 350)))))
                        .addGap(0, 81, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1237, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnBackHome)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel14))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel16)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBackHomeMouseClicked
        if(this.detail.getUser().toString().toString().equals("Admin")){
            Home home=new Home(detail);
            this.setVisible(false);
            home.setVisible(true);
        }
        else{
            HomeUser home=new HomeUser(detail);
            this.setVisible(false);
            home.setVisible(true);
        }
    }//GEN-LAST:event_btnBackHomeMouseClicked

    private void cbxClassifyPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cbxClassifyPopupMenuWillBecomeInvisible
        cbxProduct.removeAllItems();
        String sql = "SELECT * FROM Products where Classify=?";
        try {
      
            pst=conn.prepareStatement(sql);
            pst.setString(1, this.cbxClassify.getSelectedItem().toString());
            rs=pst.executeQuery();
            while(rs.next()){
                this.cbxProduct.addItem(rs.getString("Name").trim());
            }
        }  
        catch (Exception e) {  
            e.printStackTrace();  
        }
        if(cbxProduct.getItemCount()==0){
            cbxProduct.setEnabled(false);
            txbAmount.setEnabled(false);
            txbAmount.setText("");
            txbPrice.setText("");
           // txbWarrantyPeriod.setText("");
            txbIntoMoney.setText("");
        }
        else cbxProduct.setEnabled(true);
    }//GEN-LAST:event_cbxClassifyPopupMenuWillBecomeInvisible

    private void cbxProductPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cbxProductPopupMenuWillBecomeInvisible
        String sql = "SELECT * FROM Products where Name=?";
        try {
       
            pst=conn.prepareStatement(sql);
            pst.setString(1, this.cbxProduct.getSelectedItem().toString());
            rs=pst.executeQuery();
            while(rs.next()){
                txbAmount.setEnabled(true);
                txbPrice.setText(rs.getString("Price").trim());
                //txbWarrantyPeriod.setText(String.valueOf(rs.getInt("WarrantyPeriod"))+" "+rs.getString("SingleTime").trim());
            }
        }  
        catch (Exception e) {  
            e.printStackTrace();  
        }
    }//GEN-LAST:event_cbxProductPopupMenuWillBecomeInvisible

    private void txbAmountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txbAmountKeyReleased
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        txbAmount.setText(cutChar(txbAmount.getText()));
        if(txbAmount.getText().equals("")){
            String []s=txbPrice.getText().split("\\s");
            txbIntoMoney.setText("0"+" "+s[1]);
        }
        else{
            int soluong=Integer.parseInt(txbAmount.getText());
            
            String []s=txbPrice.getText().split("\\s");
            
            txbIntoMoney.setText(formatter.format(convertedToNumbers(s[0])*soluong)+" "+s[1]);
        }
    }//GEN-LAST:event_txbAmountKeyReleased
    private void displayinformation(int orderID){
        int selectedRow = tableOrder.getSelectedRow();
    if (selectedRow != -1) {
        DefaultTableModel model = (DefaultTableModel) tableOrder.getModel();
        String id = model.getValueAt(selectedRow, 0).toString();
        String name = model.getValueAt(selectedRow, 1).toString();
        String address = model.getValueAt(selectedRow, 4).toString();
        String intoMoney = model.getValueAt(selectedRow, 3).toString();
        String paymentMethod = model.getValueAt(selectedRow, 5).toString();

        txbID.setText(id);
        txbName.setText(name);
        txbAddress.setText(address);
        txbIntoMoney.setText(intoMoney);
        cbxPaymentMethods.setSelectedItem(paymentMethod);
    } else {
        // Không có dòng nào được chọn
        txbID.setText("");
        txbName.setText("");
        txbAddress.setText("");
        txbIntoMoney.setText("");
        cbxPaymentMethods.setSelectedIndex(0);
    }
    }
    private void tableOrderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableOrderMouseClicked
         // Lấy hàng được chọn trong bảng tableOrder
    int selectedRow = tableOrder.getSelectedRow();
    
    // Lấy giá trị trong cột orderId của hàng được chọn
    int orderId = (int) tableOrder.getValueAt(selectedRow, 0);
    // Gọi phương thức để hiển thị thông tin chi tiết sản phẩm cho hóa đơn có orderId tương ứng
    displayinformation(orderId);
    displayOrderDetails(orderId);       
        btnDelete.setEnabled(true);
        String status=(String)tableOrder.getValueAt(selectedRow, 6);
        if (status.equals("inactive")) {

            btnDelete.setEnabled(false);
                }
    }//GEN-LAST:event_tableOrderMouseClicked

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        int Click = JOptionPane.showConfirmDialog(null,"Are you sure you want to Cancel Order?","Notification",2);
        //System.out.println("id: "+this.txbID.getText()+"IDrr");
        if(Click ==JOptionPane.YES_OPTION){
            
            ListSelectionModel selectionModel = tbdetail.getSelectionModel();
                if (selectionModel.isSelectionEmpty()) {
            try{
                String sqlDelete1="Update Invoices set status='inactive' WHERE ID=?";
                pst=conn.prepareStatement(sqlDelete1);
                 int selectedRow = tableOrder.getSelectedRow();
                // Lấy giá trị trong cột orderId của hàng được chọn
                int orderId = (int) tableOrder.getValueAt(selectedRow, 0);
                pst.setInt(1,orderId);
                int rowsAffected = pst.executeUpdate();

                if (rowsAffected > 0) {
                   
                    lblStatus.setText("Cancel Successfully!");
                    Disabled();
                    Refresh();
                    Load(sql);
                } else {
                    lblStatus.setText("Cannot update status after 3 days from OrderDate");
                }

                
            }
            catch(Exception ex){
                // This block will catch the MySQL trigger error.
                lblStatus.setText("Error: " + ex.getMessage());
            }}
               
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if(Add==true){
            if(Check()){
  
            }
            else lblStatus.setText("Không thể thêm đơn đặt hàng vì mã đơn đặt hàng bạn nhập đã tồn tại");
        }else if(Change==true){
                changeOrder();
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnRefreshMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRefreshMouseClicked
        Refresh();
    }//GEN-LAST:event_btnRefreshMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        int lick=JOptionPane.showConfirmDialog(null,"Are you sure you want to exit?","Notification",2);
        if(lick==JOptionPane.OK_OPTION){
            System.exit(0);
        }
        else{
            if(lick==JOptionPane.CANCEL_OPTION){    
                this.setVisible(true);
            }
        }
    }//GEN-LAST:event_formWindowClosing

    private void cbxProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxProductActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxProductActionPerformed

    private void txbIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txbIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txbIDActionPerformed
private void displaydetailinformation(int detailorderID){
        int selectedRow = tbdetail.getSelectedRow();
    if (selectedRow != -1) {
        DefaultTableModel model = (DefaultTableModel) tbdetail.getModel();      
        String name = model.getValueAt(selectedRow, 1).toString();
        String amount = model.getValueAt(selectedRow, 2).toString();
        String Unitprice = model.getValueAt(selectedRow, 3).toString();
        String total = model.getValueAt(selectedRow, 4).toString();
        txbAmount.setText(amount);
        txbPrice.setText(Unitprice);
        txttotal.setText(total);
        cbxProduct.setSelectedItem(name);
        cbxProduct.addItem(name);
      String sqlquery="select * from category f, products p, invoiceDetails d where f.id=p.categoryid and p.id=d.productid and d.invoicedetailid=?";
      try{
          pst=conn.prepareStatement(sqlquery);
            pst.setString(1, model.getValueAt(selectedRow, 0).toString());
            rs=pst.executeQuery();
            while(rs.next()){
  
                String classify= rs.getString("categoryname").trim();
                cbxClassify.setSelectedItem(classify);
                cbxClassify.addItem(classify);
        
            }
      } catch(Exception ex){
                ex.printStackTrace();
            }
    } else {
        // Không có dòng nào được chọn
 
        txbAmount.setText("");
       txbPrice.setText("");
        txttotal.setText("");

    }
    }
    private void tbdetailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbdetailMouseClicked
 
       int selectedRow = tbdetail.getSelectedRow();
    
    // Lấy giá trị trong cột orderId của hàng được chọn
         int detailorderId = (int) tbdetail.getValueAt(selectedRow, 0);

       displaydetailinformation(detailorderId);
        cbxClassify.setEnabled(true);
         txbAmount.setEnabled(false);
        //btnProduct.setEnabled(true);
        txbPrice.setEnabled(false);
        txttotal.setEnabled(false);
        cbxClassify.setEnabled(true);
        LoadClassify();

        btnDelete.setEnabled(true);// TODO add your handling code here:
    }//GEN-LAST:event_tbdetailMouseClicked

    private void cbxClassifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxClassifyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxClassifyActionPerformed

    private void txbOrdersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txbOrdersActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txbOrdersActionPerformed

    private void btnOrdersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOrdersActionPerformed
        //String sql3 = "SELECT * FROM Invoices o LEFT JOIN Customers c ON o.customerID = c.ID and o.ID LIKE N'%" + this.txbOrders.getText() + "%' OR c.Name LIKE N'%" + this.txbOrders.getText()+"%' OR o.ShippingAddress LIKE N'%" + this.txbOrders.getText()  + "%' OR o.TotalAmount LIKE N'%" + this.txbOrders.getText() + "%' OR o.invoiceDate LIKE N'%" + this.txbOrders.getText() + "%' OR o.Payment LIKE N'%" + this.txbOrders.getText() + "%' OR o.status LIKE N'%" + this.txbOrders.getText() + "%'";
        String sql3 = "SELECT * FROM Invoices o " +
              "LEFT JOIN Customers c ON o.customerID = c.ID " +
              "WHERE c.Name LIKE N'%" + this.txbOrders.getText() + "%' " +
              "OR o.ShippingAddress LIKE N'%" + this.txbOrders.getText() + "%' " +
              "OR o.TotalAmount LIKE N'%" + this.txbOrders.getText() + "%' " + // Giả sử TotalAmount có thể được so sánh dựa trên số
              "OR o.invoiceDate LIKE N'%" + this.txbOrders.getText() + "%' " +
              "OR o.Payment LIKE N'%" + this.txbOrders.getText() + "%' " +
              "OR o.status LIKE N'%" + this.txbOrders.getText() + "%'";

        Load(sql3);
        txbOrders.setText("");
        // TODO add your handling code here:
    }//GEN-LAST:event_btnOrdersActionPerformed

    private void cbxPaymentMethodsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxPaymentMethodsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxPaymentMethodsActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRefreshActionPerformed

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(OrderForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OrderForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OrderForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OrderForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Detail detail=new Detail();
                new OrderForm(detail).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBackHome;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnOrders;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cbxClassify;
    private javax.swing.JComboBox<String> cbxPaymentMethods;
    private javax.swing.JComboBox<String> cbxProduct;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTable tableOrder;
    private javax.swing.JTable tbdetail;
    private javax.swing.JTextField txbAddress;
    private javax.swing.JTextField txbAmount;
    private javax.swing.JTextField txbID;
    private javax.swing.JTextField txbIntoMoney;
    private javax.swing.JTextField txbName;
    private javax.swing.JTextField txbOrders;
    private javax.swing.JTextField txbPrice;
    private javax.swing.JTextField txttotal;
    // End of variables declaration//GEN-END:variables
}
