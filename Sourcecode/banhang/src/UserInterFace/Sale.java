
package UserInterFace;

import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;


import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.awt.Desktop;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

class Sale extends javax.swing.JFrame implements Runnable {
    
    private Connection conn= new ConnectDB().createConn();;  
    private PreparedStatement pst = null;  
    private ResultSet rs = null;
    private boolean Add=false,Change=false, Pay=false;
    private String sql = "Select * from Invoices";
    
    private Thread thread;
    private Detail detail;
    public String MaHD;
    public int CustomerID;
    public Sale(Detail d) {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        detail=new Detail(d);
        lblStatus.setForeground(Color.red);
        setData();
        
     
        Pays();
        Start();
        //Load(sql);
        checkBill();
    }

    
    private void setData(){
        Disabled();
        lblName.setText(detail.getName());
        lblDate.setText(String.valueOf(new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())));
    }
    
    private void Pays(){
        lbltotalMoney.setText("0 VND");
         javax.swing.JTable tableBill = this.tableBill; // Thay yourJTableObject bằng tên đúng của bảng "tablebill"
         int rowCount = tableBill.getRowCount();
         int totalmoney=0;
                for (int i = 0; i < rowCount; i++) {
                    String cleanValue =(String) tableBill.getValueAt(i, 4);
                    cleanValue = cleanValue.replaceAll("[^\\d]", "");
                    int number = Integer.parseInt(cleanValue);
                    totalmoney=totalmoney+number;
                }
         // Tạo một đối tượng NumberFormat
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setGroupingUsed(true); // Bật sử dụng phân cách hàng nghìn
        
        // Biến đổi số thành chuỗi với định dạng phân cách hàng nghìn và đơn vị tiền tệ
        String formattedMoney = numberFormat.format(totalmoney) + " VND";
        //System.out.println(formattedMoney+" vnd");
        lbltotalMoney.setText(formattedMoney);
    }
    
    private void Start(){
        if(thread==null){
            thread= new Thread(this);
            thread.start();
        }
    }
    
    private void Update(){
        lblTime.setText(String.valueOf(new SimpleDateFormat("HH:mm:ss").format(new java.util.Date())));
    }
    
    private void Enabled(){
        cbxClassify.setEnabled(true);
    }
    
    private void Disabled(){
        cbxClassify.setEnabled(false);
        txbAmount.setEnabled(false);
        cbxProduct.setEnabled(false);
    }
    
    private void Refresh(){
        Add=false;
        Change=false;
        Pay=false;
        txbCode.setText("");
        txbPrice.setText("");
        txbAmount.setText("");
        txbIntoMoney.setText("");
        txbMoney.setText("");
        lblSurplus.setText("0 VND");
        cbxProduct.removeAllItems();
        cbxClassify.removeAllItems();
        btnChange.setEnabled(false);
        btnDelete.setEnabled(false);
        btnSave.setEnabled(false);
        btnPrint.setEnabled(false);
        Disabled();
    }
    
    private void checkBill(){
        if(tableBill.getRowCount()==0){
            lbltotalMoney.setText("0 VND");
            txbMoney.setText("");
            lblSurplus.setText("0 VND");
            btnPay.setEnabled(false);
            txbMoney.setEnabled(false);
        }
        else {
            btnPay.setEnabled(true);
            txbMoney.setEnabled(true);
        }
    }
    
    private boolean Check() {
        boolean kq=true;
        DefaultTableModel model = (DefaultTableModel) tableBill.getModel();
        String id=txbCode.getText();
        int rowCount = model.getRowCount();
                for (int row = 0; row < rowCount; row++) {
                    // Lấy giá trị của từng ô trong dòng
                     Object value1 = model.getValueAt(row, 0); // Cột 1           
                    String stringValue = value1.toString();

                    if (id.equals(stringValue)) {
                        return false;
                    }

                 
                }
        return kq;
    }

    private boolean checkNull(){
        boolean kq=true;
        if(String.valueOf(this.txbCode.getText()).length()==0){
            lblStatus.setText("You need to choose product!");
            return false;
        }
        else if(String.valueOf(this.txbAmount.getText()).length()==0){
                lblStatus.setText("You need to choose quantity!");
                return false;
        }
        return kq;
    }
    
    private void Sucessful(){
        btnSave.setEnabled(false);
        btnAdd.setEnabled(true);
        btnNew.setEnabled(true);
        btnChange.setEnabled(false);
        btnDelete.setEnabled(false);
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
    
    private void changeProduct() {
     
        // Lấy chỉ số dòng đang chọn
                int selectedRow = tableBill.getSelectedRow();
                if (selectedRow == -1) {
                    // Không có dòng nào được chọn
                    return;
                }

                // Truy cập dữ liệu mới từ JTextFields
                String id = txbCode.getText();
                String name = (String) cbxProduct.getSelectedItem();
                String sl = txbAmount.getText();
                String money =txbIntoMoney.getText();

                // Cập nhật dữ liệu trong JTable với dữ liệu mới
                DefaultTableModel model = (DefaultTableModel) tableBill.getModel();
                model.setValueAt(id, selectedRow, 0); // Cột 1
                model.setValueAt(name, selectedRow, 1); // Cột 2
                model.setValueAt(sl, selectedRow, 2);
                model.setValueAt(money, selectedRow, 3);
                
    }
    
//    private void Load(String sql){
//        tableBill.removeAll();
//        try{
//            String [] arr={"Mã Sản Phẩm","Tên Sản Phẩm","Số Lượng","Tiền"};
//            DefaultTableModel modle=new DefaultTableModel(arr,0);
//            pst=conn.prepareStatement(sql);
//            rs=pst.executeQuery();
//            while(rs.next()){
//                Vector vector=new Vector();
//                vector.add(rs.getInt("Code"));
//                vector.add(rs.getString("Product").trim());
//                vector.add(rs.getInt("Amount"));
//                vector.add(rs.getString("IntoMoney").trim());
//                modle.addRow(vector);
//            }
//            tableBill.setModel(modle);
//        }
//        catch(Exception ex){
//            ex.printStackTrace();
//        }
//    }
    
//   
    
    private void checkProducts(){
        String sqlCheck="SELECT QuantityRemaining FROM Products WHERE ID='"+txbCode.getText()+"'";
        try{
            pst=conn.prepareCall(sqlCheck);
            rs=pst.executeQuery();
            while(rs.next()){
                if(rs.getInt("QuantityRemaining")==0){
                    lblStatus.setText("not enough stock!!");
                    btnSave.setEnabled(false);
                    txbAmount.setEnabled(false);
                }
                else{
                    lblStatus.setText("Product have  "+rs.getInt("QuantityRemaining")+" unit in stock!!");
                    btnSave.setEnabled(true);
                    txbAmount.setEnabled(true);
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
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
    
    private void loadPriceandClassify(String s){
        String sql = "SELECT p.price, c.categoryname AS price, c.categoryname\n" +
                    "FROM Products p\n" +
                    "JOIN Category c ON p.categoryid= c.id\n" +
                    "WHERE p.ID =?";
        try {
            pst=conn.prepareStatement(sql);
            pst.setString(1,String.valueOf(s ));
            rs=pst.executeQuery();
            while(rs.next()){
                cbxClassify.addItem(rs.getString("Categoryname").trim());
                txbPrice.setText(rs.getString("Price").trim());
            }
        }  
        catch (Exception e) {  
            e.printStackTrace();  
        }
    }

    public int LayMaHDInt(){

        int a=0;
        Connection con=new ConnectDB().createConn();
        String strQ="SELECT MAX(ID) FROM Invoices";
        try{
           PreparedStatement stat=con.prepareStatement(strQ);
           ResultSet rs=stat.executeQuery();       

           if (rs.next()) {
            a = rs.getInt(1);
            } 
          
            con.close();
        }
        catch(Exception e){
             e.printStackTrace();
             System.out.println("error invoice");
        }
        return a+1;
    }
    private Customers searchCustomer() {
    String phoneNumber = txtPhone.getText();
    Customers cus= new Customers();
    String sqlcheckcustomer = "select * from CUSTOMERS where Phone = ?;";
                try {
                    pst = conn.prepareStatement(sqlcheckcustomer);
                    pst.setString(1, txtPhone.getText());
                    ResultSet rs = pst.executeQuery();

                    if (rs.next()) {
                        cus.ID = rs.getInt("ID"); 
                        cus.Name = rs.getString("Name");
                        cus.Address = rs.getString("Address");
                        cus.Phone = rs.getString("Phone");
                        System.out.println("Khách hàng tồn tại!"+ CustomerID);
                    } 

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
    return cus;
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tableBill = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cbxClassify = new javax.swing.JComboBox<>();
        cbxProduct = new javax.swing.JComboBox<>();
        txbIntoMoney = new javax.swing.JTextField();
        txbAmount = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txbCode = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txbPrice = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnChange = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        btnPrint = new javax.swing.JButton();
        btnPay = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        lblName = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        lblTime = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        txtaddress = new javax.swing.JTextField();
        txtTenKH = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lbltotalMoney = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txbMoney = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        lblSurplus = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        btnBackHome = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();
        cbbthanhtoan = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtID = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        tableBill.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        tableBill.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product ID", "Product Name", "Quantity", "Price", "Total amount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tableBill.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableBillMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableBill);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel1.setText("Category");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel2.setText("Product Name");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel3.setText("Quantity");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel4.setText("Total Amount");

        cbxClassify.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cbxClassifyPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

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

        txbIntoMoney.setEnabled(false);

        txbAmount.setEnabled(false);
        txbAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txbAmountActionPerformed(evt);
            }
        });
        txbAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txbAmountKeyReleased(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel5.setText("Product ID");

        txbCode.setEnabled(false);

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel7.setText("Price");

        txbPrice.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txbCode, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txbAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cbxClassify, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbxProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txbPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txbIntoMoney, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(295, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbxProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7))
                    .addComponent(txbPrice, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2)
                        .addComponent(cbxClassify, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txbAmount)
                        .addComponent(jLabel3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 11, Short.MAX_VALUE))
                    .addComponent(txbIntoMoney, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txbCode)
                        .addComponent(jLabel5)))
                .addContainerGap())
        );

        btnAdd.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/plus.png"))); // NOI18N
        btnAdd.setText("Add");
        btnAdd.setEnabled(false);
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnDelete.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Delete.png"))); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnSave.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Save.png"))); // NOI18N
        btnSave.setText("Save");
        btnSave.setEnabled(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnChange.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnChange.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/icons8_settings_30px.png"))); // NOI18N
        btnChange.setText("Edit");
        btnChange.setEnabled(false);
        btnChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeActionPerformed(evt);
            }
        });

        btnNew.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/New.png"))); // NOI18N
        btnNew.setText("New Invoice");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        btnPrint.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Print Sale.png"))); // NOI18N
        btnPrint.setText("Print Invoice");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });

        btnPay.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnPay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Pay.png"))); // NOI18N
        btnPay.setText("Payment");
        btnPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayActionPerformed(evt);
            }
        });

        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/icons8_refresh_30px.png"))); // NOI18N
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblName.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblName.setText("Name");
        lblName.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                lblNameAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        lblDate.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblDate.setText("Date");

        lblTime.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblTime.setText("Time");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setText("Time");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel12.setText("Date");

        jLabel18.setText("Staff");

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel13.setText("Name");

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel14.setText("Address");

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel17.setText("Phone");

        txtPhone.setBorder(null);
        txtPhone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPhoneActionPerformed(evt);
            }
        });
        txtPhone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPhoneKeyPressed(evt);
            }
        });

        txtaddress.setBorder(null);
        txtaddress.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtaddress.setEnabled(false);
        txtaddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtaddressActionPerformed(evt);
            }
        });

        txtTenKH.setBorder(null);
        txtTenKH.setEnabled(false);

        jLabel19.setText("Customer");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel12)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDate, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTime, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel18)
                    .addComponent(lblName, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel17)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtaddress, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                            .addComponent(txtTenKH)
                            .addComponent(txtPhone))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblName)
                            .addComponent(jLabel13))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblDate)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel14))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblTime)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel17)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(txtaddress, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(txtTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnChange, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPay, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(112, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btnChange, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPay, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        jLabel8.setText("Total Amount");

        lbltotalMoney.setText("0 VND");

        jLabel9.setText("Customer Receipt");

        txbMoney.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txbMoney.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txbMoneyKeyReleased(evt);
            }
        });

        jLabel10.setText("Customer Surplus");

        lblSurplus.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblSurplus.setText("0 VND");

        jLabel11.setFont(new java.awt.Font("Verdana", 0, 36)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("SALE");

        btnBackHome.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnBackHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/logout_1.png"))); // NOI18N
        btnBackHome.setText("BACK");
        btnBackHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBackHomeMouseClicked(evt);
            }
        });

        lblStatus.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStatus.setText("Status");

        cbbthanhtoan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cash", "Bank" }));
        cbbthanhtoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbthanhtoanActionPerformed(evt);
            }
        });

        jLabel15.setText("Payment Method");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel16.setText("Invoice ID");

        txtID.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(265, 265, 265))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lbltotalMoney, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txbMoney, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(jLabel10)
                        .addGap(33, 33, 33)
                        .addComponent(lblSurplus, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(81, 81, 81)
                        .addComponent(cbbthanhtoan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnBackHome, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 637, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1289, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 1205, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(377, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBackHome, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(11, 11, 11)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbltotalMoney, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txbMoney, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel9)
                    .addComponent(lblSurplus)
                    .addComponent(jLabel15)
                    .addComponent(cbbthanhtoan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblStatus)
                .addGap(64, 64, 64))
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

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        Refresh();
        Add=true;
        btnAdd.setEnabled(false);
        btnSave.setEnabled(true);
        Enabled();
        LoadClassify();

    }//GEN-LAST:event_btnAddActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        int Click = JOptionPane.showConfirmDialog(null, "Do you want to create a new Invoices?", "Notification",2);
        if(Click ==JOptionPane.YES_OPTION){
                DefaultTableModel model = (DefaultTableModel) tableBill.getModel();
                model.setRowCount(0); // Xóa tất cả các dòng trong mô hình dữ liệu
        
                this.lblStatus.setText("New invoice created!");
                int ID = LayMaHDInt();
                String IDString = String.valueOf(ID);     
                txtID.setText(IDString);
                //Load(sql);
                checkBill();
                Refresh();
                btnAdd.setEnabled(true);
            
        }
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if(checkNull()==true){
        if (Integer.parseInt(txbAmount.getText()) == 0) {
            lblStatus.setText("Quantity must not 0");
        }
        else{
            
            if(Add==true){
            if(Check()){

                String id = txbCode.getText();
                String name = (String) cbxProduct.getSelectedItem();
                String sl = txbAmount.getText();
                String money =txbPrice.getText();
                String totalmoney =txbIntoMoney.getText();
                DefaultTableModel model = (DefaultTableModel) tableBill.getModel();
                model.addRow(new Object[]{id,name,sl,money,totalmoney });
                // Cập nhật JTable
                model.fireTableDataChanged();
                lblStatus.setText("Add Successfully!");
            }
            else lblStatus.setText("Product already exist");
        }else if(Change==true){
            changeProduct();
        }
        checkBill();
        Pays();
        btnAdd.setEnabled(true);
            
             
        }
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void cbxClassifyPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cbxClassifyPopupMenuWillBecomeInvisible
        cbxProduct.removeAllItems();
        String sql = "SELECT * FROM Products p, category c where p.categoryid =c.ID and c.categoryname = ?";
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
            txbCode.setText("");
            txbPrice.setText("");
            txbAmount.setText("");
            txbIntoMoney.setText("");
        }
        else cbxProduct.setEnabled(true);
    }//GEN-LAST:event_cbxClassifyPopupMenuWillBecomeInvisible

    private void txbAmountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txbAmountKeyReleased
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        
        txbAmount.setText(cutChar(txbAmount.getText()));
        
        if(txbAmount.getText().equals("")){
            String []s=txbPrice.getText().split("\\s");
            txbIntoMoney.setText("0"+" "+s[1]);
        }
        else{
            String sqlCheck="SELECT QuantityRemaining FROM Products WHERE ID='"+txbCode.getText()+"'";
            try{
            pst=conn.prepareStatement(sqlCheck);
            rs=pst.executeQuery();
            
                while(rs.next()){
                    if((rs.getInt("QuantityRemaining")-Integer.parseInt(txbAmount.getText()))<0){
                        String []s=txbPrice.getText().split("\\s");
                        txbIntoMoney.setText("0"+" "+s[1]);
                        
                        lblStatus.setText("The quaity is not enough!!");
                        btnSave.setEnabled(false);
                    }
                    else{
                        int soluong=Integer.parseInt(txbAmount.getText().toString());
                        String []s=txbPrice.getText().split("\\s");
                        txbIntoMoney.setText(formatter.format(convertedToNumbers(s[0])*soluong)+" "+s[1]);
                        
                        lblStatus.setText("stock enough!!");
                        btnSave.setEnabled(true);
                    }
                }
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_txbAmountKeyReleased

    private void cbxProductPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cbxProductPopupMenuWillBecomeInvisible
        String sql = "SELECT * FROM Products where Name=?";
        try {
            pst=conn.prepareStatement(sql);
            pst.setString(1, this.cbxProduct.getSelectedItem().toString());
            rs=pst.executeQuery();
            while(rs.next()){
                txbCode.setText(rs.getString("ID").trim());
                txbPrice.setText(rs.getString("Price").trim());
                txbAmount.setEnabled(true);
            }
        }  
        catch (Exception e) {  
            e.printStackTrace();  
        }
        checkProducts();
    }//GEN-LAST:event_cbxProductPopupMenuWillBecomeInvisible

    private void btnChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeActionPerformed
        Add=false;
        Change=true;
        btnAdd.setEnabled(false);
        btnChange.setEnabled(false);
        btnDelete.setEnabled(false);
        btnSave.setEnabled(true);
        txbAmount.setEnabled(true);
        txtTenKH.setEnabled(true);
        txtaddress.setEnabled(true);
    }//GEN-LAST:event_btnChangeActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        int Click = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete product?", "Notification",2);
        if(Click ==JOptionPane.YES_OPTION){
            int selectedRow = tableBill.getSelectedRow();
            if (selectedRow != -1) {
                DefaultTableModel model = (DefaultTableModel) tableBill.getModel();
                model.removeRow(selectedRow); // Xóa dòng đang chọn khỏi mô hình dữ liệu
            }

                this.lblStatus.setText("Delete Successfully!");
                Refresh();
                //Load(sql);
                Sucessful();
                checkBill();
                Pays();
           
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        Refresh();
    }//GEN-LAST:event_btnRefreshActionPerformed
     int orderID;
    private void btnPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayActionPerformed

        if(Pay==true){
                String sqlcheckcustomer = "select * from CUSTOMERS where Phone = ?;";
                String input = txtTenKH.getText().trim()+txtaddress.getText().trim()+txtPhone.getText().trim();
                if (input.isEmpty()) {
                    System.out.println("Khách vãng lai");
                    CustomerID=4;
                } else {
                     
                
                try {
                    pst = conn.prepareStatement(sqlcheckcustomer);
                    pst.setString(1, txtPhone.getText());
                    ResultSet rs = pst.executeQuery();

                    if (rs.next()) {
                        CustomerID = rs.getInt("ID"); // Lấy giá trị của trường ID
                        System.out.println("Tìm thấy khách hàng!"+ CustomerID);
                    } else {
                        String sqlinsertCustomer = "Insert into CUSTOMERS (NAME,PHONE,ADDRESS) values(?,?,?);";

                        try {
                            pst = conn.prepareStatement(sqlinsertCustomer,Statement.RETURN_GENERATED_KEYS);
                            pst.setString(1, txtTenKH.getText());
                            pst.setString(2, txtPhone.getText());
                            pst.setString(3, txtaddress.getText());
                            pst.executeUpdate();
                            // Lấy giá trị OrderID được tạo tự động
                            ResultSet generatedKeys = pst.getGeneratedKeys();

                            if (generatedKeys.next()) {
                                CustomerID = generatedKeys.getInt(1);
                                System.out.println("CustomerID: " + CustomerID);
                            } else {
                                System.out.println("Error ID Customer.");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        System.out.println("Đã thêm khách hàng mới!");
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                }
            // insert in to Orders
            String sqlPay="INSERT INTO Invoices (customerID,TotalAmount,ShippingAddress, payment,employeeid) VALUES(?,?,?,?,?)";
            try{
                pst = conn.prepareStatement(sqlPay, Statement.RETURN_GENERATED_KEYS);
               
               pst.setInt(1, CustomerID);
                String totalMoney = lbltotalMoney.getText();
                totalMoney = totalMoney.replace(",", "").replace(" VND", "");
                double amount = Double.parseDouble(totalMoney);
                pst.setDouble(2, amount);
                pst.setString(3, txtaddress.getText());
                String selectedText = (String) cbbthanhtoan.getSelectedItem();
                pst.setString(4, selectedText);
                pst.setInt(5, detail.getID());
                pst.executeUpdate();
                // Lấy giá trị OrderID được tạo tự động
                ResultSet generatedKeys = pst.getGeneratedKeys();
               
                if (generatedKeys.next()) {
                    orderID = generatedKeys.getInt(1);
                    System.out.println("Invoices: " + orderID);
                } else {
                    System.out.println("Error ID Invoices.");
                }
                javax.swing.JTable tableBill = this.tableBill; 
                int rowCount = tableBill.getRowCount();
                
                // Chèn dữ liệu vào bảng "DetailOrder"
                String insertQuery = "INSERT INTO invoiceDetails (InvoiceID, ProductID, Quantity, UnitPrice,Subtotal) VALUES (?, ?, ?,?,?);";
                try{
                pst=conn.prepareStatement(insertQuery);
                for (int i = 0; i < rowCount; i++) {
                    int orderIDs = orderID;
                    int productID = Integer.parseInt(tableBill.getValueAt(i, 0).toString()); 
                    int quantity = Integer.parseInt(tableBill.getValueAt(i, 2).toString()); 
                    String unitPriceStr = (String) tableBill.getValueAt(i, 3);
                    unitPriceStr = unitPriceStr.replace(",", "").replace(" VND", "");
                    int UnitPrice = Integer.parseInt(unitPriceStr);
                    //String PriceStr = (String) tableBill.getValueAt(i, 4);
                    //unitPriceStr = PriceStr.replace(",", "").replace(" VND", "");
                    int Subtotal = quantity*UnitPrice;
                    System.out.println("total: "+Subtotal);
                    pst.setInt(1, orderIDs);
                    pst.setInt(2, productID);
                    pst.setInt(3, quantity);
                    pst.setInt(4, UnitPrice);
                    pst.setInt(5, Subtotal);
                    pst.executeUpdate();
                }
                }
                catch(Exception ex){
                ex.printStackTrace();
                }
                // update sold products
                String sql2 = "update products set sold=sold+? where id = ?;";

                try {
                    pst = conn.prepareStatement(sql2);
                    for (int i = 0; i < rowCount; i++) {
                       
                        int ID = Integer.parseInt(tableBill.getValueAt(i, 0).toString());

                        int Sold = Integer.parseInt(tableBill.getValueAt(i, 2).toString());
                         System.out.println(Sold+" "+ID);
                        pst.setInt(1, Sold);
                        pst.setInt(2, ID);
                        pst.executeUpdate();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                String sql3 = "update products set quantityremaining=quantityremaining - ? where id = ?;";

                try {
                    pst = conn.prepareStatement(sql3);
                    for (int i = 0; i < rowCount; i++) {
                       
                        int ID = Integer.parseInt(tableBill.getValueAt(i, 0).toString());

                        int Sold = Integer.parseInt(tableBill.getValueAt(i, 2).toString());
                         System.out.println(Sold+" "+ID);
                        pst.setInt(1, Sold);
                        pst.setInt(2, ID);
                        pst.executeUpdate();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                
                lblStatus.setText("Pay Successfully!");
                Disabled();
                Sucessful();
               // consistency();
                
                btnPrint.setEnabled(true);
                btnAdd.setEnabled(false);
                btnPay.setEnabled(false);
                txbMoney.setEnabled(false);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
             
        }
        else if(Pay==false){
            JOptionPane.showMessageDialog(null, "you need type the money !");
        }
    }//GEN-LAST:event_btnPayActionPerformed

    private void txbMoneyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txbMoneyKeyReleased
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        if(txbMoney.getText().equals("")){
            String []s=lbltotalMoney.getText().split("\\s");
            lblSurplus.setText("0"+" "+s[1]);
        }
        else{
            txbMoney.setText(formatter.format(convertedToNumbers(txbMoney.getText())));
            
            String s1=txbMoney.getText();
            String[] s2=lbltotalMoney.getText().split("\\s");
            
            if((convertedToNumbers(s1)-convertedToNumbers(s2[0]))>=0){
                lblSurplus.setText(formatter.format((convertedToNumbers(s1)-convertedToNumbers(s2[0])))+" "+s2[1]);
                lblStatus.setText("Enough Money!");
                Pay=true;
            }
            else {
                
                lblSurplus.setText(formatter.format((convertedToNumbers(s1)-convertedToNumbers(s2[0])))+" "+s2[1]);
                lblStatus.setText("Not Enough Money!");
                Pay=false;
            }
        }
    }//GEN-LAST:event_txbMoneyKeyReleased
    DecimalFormat formatter = new DecimalFormat("###,###,###.##");
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed

      // TODO add your handling code here:
        
            Document document = new Document(PageSize.A4);
            String filename= txtID.getText();;
            try{
                PdfWriter writer= PdfWriter.getInstance(document,new FileOutputStream("src/reports/HD"+filename+".pdf"));
                document.open();
                document.addAuthor(lblName.getText());
                document.addCreationDate();
                document.addCreator("LQHD");
                document.addTitle("Bill Invoice");
                document.addSubject("Bill Invoice");
                File filefontTieuDe= new File("src/fonts/vuArialBold.ttf");
                BaseFont bfTieuDe= BaseFont.createFont(filefontTieuDe.getAbsolutePath(),BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
                Font fontTieuDe1=new Font(bfTieuDe,16);
                fontTieuDe1.setColor(BaseColor.BLUE);
                Font fontTieuDe2=new Font(bfTieuDe,13);
                fontTieuDe2.setColor(BaseColor.BLUE);
                Font fontTieuDe3=new Font(bfTieuDe,13);
                Font fontTieuDe4=new Font(bfTieuDe,12);
                File filefontNoiDung=new File("src/fonts/vuArial.ttf");
                BaseFont bfNoiDung=
                BaseFont.createFont(filefontNoiDung.getAbsolutePath(),
                BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
                Font fontNoiDung1=new Font(bfNoiDung,13);
                Font fontNoiDung2=new Font(bfNoiDung,12);
                Font fontNoiDung3=new Font(bfNoiDung,13);
              
         
      
                
                Paragraph prgTenPK= new Paragraph("UIT COSMETIC STORE",fontTieuDe2);
                prgTenPK.setIndentationLeft(10);
                document.add(prgTenPK);
                Paragraph prgDiaChiPK = new Paragraph ("Hàn Thuyên, phường Linh Trung, Quận Thủ Đức , tp. Hồ Chí Minh",fontNoiDung2);
                prgDiaChiPK.setIndentationLeft(10);
                document.add(prgDiaChiPK);
                Paragraph prgSoDTPK = new Paragraph ("Phone Number: 090 000 000",fontNoiDung2);
                prgSoDTPK.setIndentationLeft(10);
                Paragraph prgd = new Paragraph ("--------------------------------------------------------------------------------------------------------------------------------",fontNoiDung2);
                prgd.setIndentationLeft(10);
                document.add(prgd);
                //chentieude
                Paragraph prgTieuDe= new Paragraph("Bill Invoice", fontTieuDe1);
                prgTieuDe.setAlignment(Element.ALIGN_CENTER);
                prgTieuDe.setSpacingBefore(10);
                prgTieuDe.setSpacingAfter(10);
                document.add(prgTieuDe);
                 PdfPTable tableTTNV=new PdfPTable(4);
                tableTTNV.setWidthPercentage(90);
                tableTTNV.setSpacingBefore(10);
                tableTTNV.setSpacingAfter(10);
                Paragraph prgd1 = new Paragraph ("--------------------------------------------------------------------------------------------------------------------------------",fontNoiDung2);
                prgd1.setIndentationLeft(10);
                document.add(prgd1);
                float[] tableTTNV_columnWidths={210,500,310,300};
                tableTTNV.setWidths(tableTTNV_columnWidths);
                 try{
                   Connection con=new ConnectDB().createConn();
                    String sql2="select * from invoices order by invoicedate desc limit 1";
             
                    PreparedStatement pres2=con.prepareStatement(sql2);
      
                    ResultSet rs2=pres2.executeQuery();
                    
               
                    while(rs2.next()){
                        PdfPCell cellDATE=new PdfPCell(new Paragraph("Date: ",fontTieuDe4));
                        
                        cellDATE.setBorder(0);
                        cellDATE.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cellDATE.setVerticalAlignment(Element.ALIGN_LEFT);
                        tableTTNV.addCell(cellDATE);
      
                        PdfPCell celldate= new PdfPCell(new
                        Paragraph(rs2.getString("invoicedate"),fontNoiDung3));
                         celldate.setBorder(0);
                        celldate.setHorizontalAlignment(Element.ALIGN_LEFT);
                        celldate.setVerticalAlignment(Element.ALIGN_LEFT);
                        tableTTNV.addCell(celldate);
                 
                        PdfPCell cellNV=new PdfPCell(new Paragraph("Employee name: ",fontTieuDe4));
                          cellNV.setBorder(0);
                        cellNV.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cellNV.setVerticalAlignment(Element.ALIGN_LEFT);
                        tableTTNV.addCell(cellNV);
                        PdfPCell cellTenNV= new PdfPCell(new
                        Paragraph(lblName.getText(),fontNoiDung3));
                        cellTenNV.setBorder(0);
                         cellTenNV.setPaddingLeft(10);
                        cellTenNV.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cellTenNV.setVerticalAlignment(Element.ALIGN_LEFT);
                        tableTTNV.addCell(cellTenNV);
                        
                       }document.add(tableTTNV);}
                      catch(SQLException e){
                    System.out.println(e);
                    System.out.println("Lỗi");
                                    }
                   Paragraph prgd2 = new Paragraph ("--------------------------------------------------------------------------------------------------------------------------------",fontNoiDung2);
                prgd2.setIndentationLeft(10);
                document.add(prgd2);     
                ////
                Paragraph prgSP= new Paragraph ("Detail: ",fontTieuDe3);
                prgSP.setSpacingBefore(10);
                prgSP.setSpacingAfter(10);
                document.add(prgSP);
                PdfPTable tableDV= new PdfPTable(4);
                tableDV.setWidthPercentage(80);
                tableDV.setSpacingBefore(10);
                tableDV.setSpacingAfter(10);
                float[] tableDV_columnWidths={200,350,140,200};
                tableDV.setWidths(tableDV_columnWidths);
                
                 PdfPCell cellTDMaSP=new PdfPCell(new Paragraph("Product ID",fontTieuDe4));
                cellTDMaSP.setBorderColor(BaseColor.BLACK);
                cellTDMaSP.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellTDMaSP.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tableDV.addCell(cellTDMaSP);
                 
                 PdfPCell cellTDTenSP=new PdfPCell(new Paragraph("Product Name",fontTieuDe4));
                cellTDTenSP.setBorderColor(BaseColor.BLACK);
                cellTDTenSP.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellTDTenSP.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tableDV.addCell(cellTDTenSP);
 
               

                PdfPCell cellTDSL=new PdfPCell(new Paragraph("Quatity",fontTieuDe4));
                cellTDSL.setBorderColor(BaseColor.BLACK);
                cellTDSL.setHorizontalAlignment(Element.ALIGN_CENTER);
               cellTDSL.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tableDV.addCell(cellTDSL);
                

                
                PdfPCell cellTDThanhTien=new PdfPCell(new Paragraph("total money",fontTieuDe4));
                cellTDThanhTien.setBorderColor(BaseColor.BLACK);
                cellTDThanhTien.setHorizontalAlignment(Element.ALIGN_CENTER);
               cellTDThanhTien.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tableDV.addCell(cellTDThanhTien); 
                int tong=0;
                //chèn thông tin dv từ csdl vào bảng
               try{
                   Connection con=new ConnectDB().createConn();
                   String strSQL="  select * from Invoices o ,InvoiceDetails d,Products p where o.ID=d.invoiceID and d.productid=p.id and o.ID= "+ txtID.getText();
                  
                  
                    PreparedStatement pres= con.prepareStatement(strSQL);
                    ResultSet rs=pres.executeQuery();
                    
                    while(rs.next()){
                       
      
                        PdfPCell cellMADV= new PdfPCell(new
                        Paragraph(rs.getString("id"),fontNoiDung3));
                        cellMADV.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cellMADV.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        tableDV.addCell(cellMADV);

                        PdfPCell cellTenDV= new PdfPCell(new
                        Paragraph(rs.getString("name"),fontNoiDung3));
                         cellTenDV.setPaddingLeft(10);
                        cellTenDV.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                        cellTenDV.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        tableDV.addCell(cellTenDV);
                        
                        
                        
           
                       PdfPCell cellSL= new PdfPCell(new
                        Paragraph((rs.getString("Quantity")),fontNoiDung3));
                        cellSL.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cellSL.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        tableDV.addCell(cellSL);
                                              
                       PdfPCell cellDonGia= new PdfPCell(new
                        Paragraph((rs.getString("subtotal")),fontNoiDung3));
                        cellDonGia.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cellDonGia.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        tableDV.addCell(cellDonGia);
//                        String subtotal = rs.getString("subtotal");
//                        //subtotal = subtotal.replace(",", "").replace(" VND", "");
//                        int subtotalValue = Integer.parseInt(subtotal);
                        String subtotal = rs.getString("subtotal");
                        double subtotalValue = Double.parseDouble(subtotal);
                        int subtotalIntValue = (int) subtotalValue;

                        tong=tong+subtotalIntValue;
                        
                        
                    }
                   
                   
                }
                catch(SQLException e){
                    System.out.println(e);
                    System.out.println("error invoice");
                                    }
                document.add(tableDV);
                 Paragraph prgd3 = new Paragraph ("--------------------------------------------------------------------------------------------------------------------------------",fontNoiDung2);
                prgd3.setIndentationLeft(10);
                document.add(prgd3); 
                PdfPTable tableTTBS=new PdfPTable(2);
                tableTTBS.setWidthPercentage(90);
                tableTTBS.setSpacingBefore(10);
                tableTTBS.setSpacingAfter(10);
        
                float[] tableTTBS_columnWidths={300,200};
                tableTTBS.setWidths(tableTTBS_columnWidths);
                
                
                PdfPCell cellTongCong=new PdfPCell(new Paragraph("Total money:",fontNoiDung3));
                cellTongCong.setBorder(0);
     
                cellTongCong.setHorizontalAlignment(Element.ALIGN_LEFT);
                cellTongCong.setVerticalAlignment(Element.ALIGN_TOP);
                tableTTBS.addCell(cellTongCong);

                
 
                String tongValue = String.valueOf(tong);
                PdfPCell cellTongTien = new PdfPCell(new Paragraph(tongValue, fontTieuDe4));
               
                cellTongTien.setBorder(0);
       
                cellTongTien.setHorizontalAlignment(Element.ALIGN_LEFT);
                cellTongTien.setVerticalAlignment(Element.ALIGN_TOP);
                tableTTBS.addCell(cellTongTien);
                    
                  document.add(tableTTBS);
                Paragraph prgd4 = new Paragraph ("--------------------------------------------------------------------------------------------------------------------------------",fontNoiDung2);
                prgd4.setIndentationLeft(10);
                document.add(prgd4);
                PdfPTable tablethank=new PdfPTable(1);
                tableTTBS.setWidthPercentage(90);
                tableTTBS.setSpacingBefore(10);
                tableTTBS.setSpacingAfter(10);
        
                float[] tablethank_columnWidths={500};
                tableTTBS.setWidths(tableTTBS_columnWidths);
                PdfPCell cellthank=new PdfPCell(new Paragraph("Have a nice day \n\n\n\n\n ",fontTieuDe4));
             
                cellthank.setBorder(0);
                cellthank.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellthank.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tablethank.addCell(cellthank);
                // muc 4.4       
                document.add(tablethank);
                document.close();
                writer.close();
            }
            catch(Exception e)
            {
                 e.printStackTrace();
            }
            try{
                File file =new File ("report/"+filename+".pdf");
                if(!Desktop.isDesktopSupported()){
                    System.out.println("not supported");
                    return;
                }
                 Desktop desktop= Desktop.getDesktop();
                 if(file.exists())
                     desktop.open(file);
                }
            catch(Exception e){
                 e.printStackTrace();
                }

              
      
    }//GEN-LAST:event_btnPrintActionPerformed

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

    private void lblNameAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblNameAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_lblNameAncestorAdded

    private void txbAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txbAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txbAmountActionPerformed

    private void cbbthanhtoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbthanhtoanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbbthanhtoanActionPerformed

    private void txtaddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtaddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtaddressActionPerformed

    private void tableBillMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableBillMouseClicked
        cbxClassify.removeAllItems();
        cbxProduct.removeAllItems();

        int Click=tableBill.getSelectedRow();
        TableModel model=tableBill.getModel();

        txbCode.setText(model.getValueAt(Click,0).toString());
        cbxProduct.addItem(model.getValueAt(Click,1).toString());
        txbAmount.setText(model.getValueAt(Click,2).toString());
        txbIntoMoney.setText(model.getValueAt(Click,3).toString());

        loadPriceandClassify(model.getValueAt(Click,0).toString());

        btnChange.setEnabled(true);
        btnDelete.setEnabled(true);
    }//GEN-LAST:event_tableBillMouseClicked

    private void txtPhoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPhoneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPhoneActionPerformed

    private void txtPhoneKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhoneKeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
        // Xử lý khi người dùng nhấn Enter ở đây
        // Ví dụ: Gọi hàm xử lý tìm kiếm khách hàng
        Customers cus= searchCustomer();
        txtTenKH.setText(cus.Name);
        txtaddress.setText(cus.Address);
        txtPhone.setText(cus.Phone);
        if (txtTenKH.getText() == null || txtTenKH.getText().trim().isEmpty()) {
            txtTenKH.setEnabled(true);
            txtaddress.setEnabled(true);
    System.out.println("Customer is null or empty!");
        } 
        else{
        txtaddress.setEnabled(false);
        txtTenKH.setEnabled(false);
        }

        }
    }//GEN-LAST:event_txtPhoneKeyPressed


    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Sale.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Sale.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Sale.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Sale.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Detail detail=new Detail();
                new Sale(detail).setVisible(true);
            }
        });
    }
     
            
                                               

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnBackHome;
    private javax.swing.JButton btnChange;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnPay;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cbbthanhtoan;
    private javax.swing.JComboBox<String> cbxClassify;
    private javax.swing.JComboBox<String> cbxProduct;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblSurplus;
    private javax.swing.JLabel lblTime;
    private javax.swing.JLabel lbltotalMoney;
    private javax.swing.JTable tableBill;
    private javax.swing.JTextField txbAmount;
    private javax.swing.JTextField txbCode;
    private javax.swing.JTextField txbIntoMoney;
    private javax.swing.JTextField txbMoney;
    private javax.swing.JTextField txbPrice;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtTenKH;
    private javax.swing.JTextField txtaddress;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run() {
        while(true){
        Update();  
            try{
                Thread.sleep(1);  
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
