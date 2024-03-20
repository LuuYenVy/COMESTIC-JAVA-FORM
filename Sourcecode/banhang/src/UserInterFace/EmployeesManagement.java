
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
import com.toedter.calendar.JDateChooser;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
public class EmployeesManagement extends javax.swing.JFrame {

    private Connection conn= new ConnectDB().createConn();;  
    private PreparedStatement pst = null;  
    private ResultSet rs = null;
    
    private boolean Add=false,Change=false,Addacc=false;
    private String sql = "SELECT * \n" +
                        "FROM employees\n" +
                        "LEFT JOIN positionn ON employees.positionid = positionn.id\n" +
                        "LEFT JOIN accounts ON employees.EmployeeCode = accounts.Employeeid;";
    
    private Detail detail;
    
    public EmployeesManagement(Detail d) {
        initComponents();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        detail=new Detail(d);
        Load(sql);
        Disabled();
        LoadPosition();
        lblStatus.setForeground(Color.red);
    }

    
    private void Enabled(){
        cbxPosition.setEnabled(true);
        txbCode.setEnabled(true);
        txbName.setEnabled(true);
        txbBirthday.setEnabled(true);
        cbxSex.setEnabled(true);
        txbPhone.setEnabled(true);
        txbLevel.setEnabled(true);
        txbAddress.setEnabled(true);
        txbEmail.setEnabled(true);
        
        lblStatus.setText("Status!");
    }
    
    private void Disabled(){
        cbxPosition.setEnabled(false);
        txbCode.setEnabled(false);
        txbName.setEnabled(false);
        txbBirthday.setEnabled(false);
        cbxSex.setEnabled(false);
        txbPhone.setEnabled(false);
        txbLevel.setEnabled(false);
        txbAddress.setEnabled(false);
        txbEmail.setEnabled(false);
        txtusername.setEnabled(false);
        txtpassword.setEnabled(false);
        
    }
    
    private void Refresh(){
        Add=false;
        Change=false;
        txbCode.setText("");
        txbName.setText("");
        ((JTextField)txbBirthday.getDateEditor().getUiComponent()).setText("");
        txbPhone.setText("");
        txbLevel.setText("");
        txbAddress.setText("");
        txbEmail.setText("");
        btnAdd.setEnabled(true);
        btnChange.setEnabled(false);
        btnDelete.setEnabled(false);
        btnSave.setEnabled(false);
        LoadPosition();
        LoadGT();
        lblStatus.setText("Status");
    }
    
    private void LoadGT(){
        cbxSex.removeAllItems();
        cbxSex.addItem("Male");
        cbxSex.addItem("Female");
        cbxSex.addItem("BueDue");
    }
    
    private void Load(String sql){
        try{
            String [] arr={"ID","Position","Name","Username","Salary Grade","DOB","Gender","Address","Phone","Email","Salary","Status","Password"};
            DefaultTableModel modle=new DefaultTableModel(arr,0);
            pst=conn.prepareStatement(sql);
            rs=pst.executeQuery();
            while(rs.next()){
                Vector vector = new Vector();
                vector.add(rs.getInt("EmployeeCode"));
                vector.add(rs.getString("Position") != null ? rs.getString("Position").trim() : "");
                vector.add(rs.getString("FullName") != null ? rs.getString("FullName").trim() : "");
                vector.add(rs.getString("UserName") != null ? rs.getString("UserName").trim() : "");
                vector.add(rs.getInt("Level"));
                vector.add(rs.getDate("YearofBirth") != null ? new SimpleDateFormat("dd/MM/yyyy").format(rs.getDate("YearofBirth")) : "");
                vector.add(rs.getString("Sex") != null ? rs.getString("Sex").trim() : "");
                vector.add(rs.getString("Address") != null ? rs.getString("Address").trim() : "");
                vector.add(rs.getString("Phone") != null ? rs.getString("Phone").trim() : "");
                vector.add(rs.getString("Email") != null ? rs.getString("Email").trim() : "");
                vector.add(rs.getString("Payroll") != null ? rs.getString("Payroll").trim() : "");
                vector.add(rs.getString("status") != null ? rs.getString("status").trim() : "");
                vector.add(rs.getString("PassWord") != null ? rs.getString("PassWord").trim() : "");
                modle.addRow(vector);
            }
            
            tableEmployees.setModel(modle);
             
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    private void LoadPosition(){
        cbxPosition.removeAllItems();
        String sqlcbxPosition = "SELECT * FROM Positionn";
        try {
            pst=conn.prepareStatement(sqlcbxPosition);
            rs=pst.executeQuery();
            while(rs.next()){
                this.cbxPosition.addItem(rs.getString("Position").trim());
            }
        }  
        catch (Exception e) {  
            e.printStackTrace();  
        }
    }
    
    private boolean checkNull(){
        boolean kq=true;
        if(String.valueOf(this.txbCode.getText()).length()==0){
            lblStatus.setText("You haven't entered Employee ID!");
            return false;
        }
        if(String.valueOf(this.txbName.getText()).length()==0){
            lblStatus.setText("You haven't entered Employee Name!");
            return false;
        }
        if(String.valueOf(this.txbLevel.getText()).length()==0){
            lblStatus.setText("You haven't entered Salary Grade!");
            return false;
        }
        if(String.valueOf(((JTextField)this.txbBirthday.getDateEditor().getUiComponent()).getText()).length()==0){
            lblStatus.setText("You haven't entered DOB!");
            return false;
            }
        if(String.valueOf(this.txbAddress.getText()).length()==0){
            lblStatus.setText("You haven't entered Address!");
            return false;
        }
        if(String.valueOf(this.txbPhone.getText()).length()==0){
            lblStatus.setText("You haven't entered Phone Number!");
            return false;
        }
        if(String.valueOf(this.txbEmail.getText()).length()==0){
            lblStatus.setText("You haven't entered Email!");
            return false;
        }
        if(String.valueOf(this.txbPayroll.getText()).length()==0){
            lblStatus.setText("You haven't entered level salary!");
            return false;
        }
        return kq;
    }
    
    private void addEmployees(){
        if(checkNull()){
           String sqlquery = "SELECT * FROM positionn WHERE position = ?";
            int id = 0;
            try {
                PreparedStatement statement = conn.prepareStatement(sqlquery);
                statement.setString(1, String.valueOf(this.cbxPosition.getSelectedItem()));
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    id = resultSet.getInt("id");
                }
            } catch(Exception ex){
                ex.printStackTrace();
            }
            String sqlInsert="INSERT INTO employees (Positionid,fullname,level,YearofBirth,Sex,Phone,Address,Email,Payroll) VALUES(?,?,?,?,?,?,?,?,?)";
            try{
                pst=conn.prepareStatement(sqlInsert);
                pst.setInt(1, id);
                pst.setString(2, txbName.getText());
                pst.setInt(3, Integer.parseInt(txbLevel.getText()));
                pst.setDate(4,new java.sql.Date(this.txbBirthday.getDate().getTime()));
                pst.setString(5, (String)cbxSex.getSelectedItem());
                pst.setString(6, this.txbPhone.getText());
                pst.setString(7, txbAddress.getText());
                pst.setString(8, txbEmail.getText());
                pst.setString(9, txbPayroll.getText() +" "+"VND");
                pst.executeUpdate();
                Refresh();
                lblStatus.setText("Add successfully!");
                Disabled();
                
                Load(sql);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
    private void addAccount(){


            String sqlInsert="INSERT INTO Accounts (username,FullName,password,employeeid) VALUES(?,?,?,?)";
            try{
                pst=conn.prepareStatement(sqlInsert);
                pst.setString(1, txtusername.getText());
                pst.setString(2, txbName.getText());
                pst.setString(3, txtpassword.getText());
                pst.setString(4, txbCode.getText());
                pst.executeUpdate();
                Refresh();
                lblStatus.setText("Add account successfully!");
                Disabled();
                
                Load(sql);
            }
            catch(Exception ex){
                lblStatus.setText("Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        
    }
    private void updateAccount(){


            String sqlInsert="Update Accounts set password=? where username=?";
            try{
                pst=conn.prepareStatement(sqlInsert);
                pst.setString(1, txtpassword.getText());
                pst.setString(2, txtusername.getText());
                pst.executeUpdate();
            }
            catch(Exception ex){
                lblStatus.setText("Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        
    }
    private void changeEmployees(){
        int Click=tableEmployees.getSelectedRow();
        TableModel model=tableEmployees.getModel();
        
        if(checkNull()){
            String sqlquery = "SELECT * FROM positionn WHERE position = ?";
            int id = 0;
            try {
                PreparedStatement statement = conn.prepareStatement(sqlquery);
                String selectedPosition = (String) this.cbxPosition.getSelectedItem();
                statement.setString(1, selectedPosition);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    id = resultSet.getInt("id");
                    System.out.println("vị trí:"+id);
                }
            } catch(Exception ex){
                ex.printStackTrace();
            }
            String sqlChange="UPDATE Employees SET Positionid=?,FullName=?,Level=?,YearofBirth=?,Sex=?,Address=?,Phone=?,Email=?,Payroll=? WHERE EmployeeCode='"+model.getValueAt(Click,0).toString().trim()+"'";
            try{
                pst=conn.prepareStatement(sqlChange);
                pst.setInt(1,id);
                pst.setString(2, txbName.getText());
                pst.setInt(3, Integer.parseInt(txbLevel.getText()));

                //pst.setDate(4, ((JTextField) txbBirthday.getDateEditor().getUiComponent()).getText());
                String dateString = ((JTextField) txbBirthday.getDateEditor().getUiComponent()).getText();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // hoặc một định dạng khác phù hợp với chuỗi của bạn
                LocalDate parsedDate = LocalDate.parse(dateString, formatter);

                java.sql.Date sqlDate = java.sql.Date.valueOf(parsedDate);
                pst.setDate(4, sqlDate);
                pst.setString(5, (String)cbxSex.getSelectedItem());
                pst.setString(6, this.txbAddress.getText());
                pst.setString(7, txbPhone.getText());
                pst.setString(8, txbEmail.getText());
                pst.setString(9, txbPayroll.getText() +" "+"VND");
                pst.executeUpdate();
                lblStatus.setText("Changes Successfully");
                Disabled();
                Refresh();
                Load(sql);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
    private boolean CheckEmail(){
        boolean kq=true;
        String email = txbEmail.getText();
        boolean containsAtSymbol = email.contains("@"); 

        if (containsAtSymbol) {
        
            kq=true;
        } else {
         
           kq=false;
           lblStatus.setText("Email must contain '@' character");
        }
        return kq;
    }
    private boolean CheckAccount(){
        String user = txtusername.getText();
        String password=txtpassword.getText();
        

        if (user.isEmpty() || password.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
    private boolean Check(){
        boolean kq=true;
        String sqlCheck="SELECT * FROM Employees";
        try{
            pst=conn.prepareStatement(sqlCheck);
            rs=pst.executeQuery();
            while(rs.next()){
                if(this.txbCode.getText().equals(rs.getString("EmployeeCode").toString().trim())){
                    return false;
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
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
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tableEmployees = new javax.swing.JTable();
        btnHomeBack = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txbCode = new javax.swing.JTextField();
        cbxPosition = new javax.swing.JComboBox<>();
        txbName = new javax.swing.JTextField();
        cbxSex = new javax.swing.JComboBox<>();
        txbPhone = new javax.swing.JTextField();
        txbAddress = new javax.swing.JTextField();
        txbEmail = new javax.swing.JTextField();
        txbPayroll = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txbLevel = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txbBirthday = new com.toedter.calendar.JDateChooser();
        jPanel2 = new javax.swing.JPanel();
        btnRefresh = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnChange = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        btnaddacount = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        txbEmployees = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtusername = new javax.swing.JTextField();
        txtpassword = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        tableEmployees.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tableEmployees.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tableEmployees.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableEmployeesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableEmployees);

        btnHomeBack.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnHomeBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/logout_1.png"))); // NOI18N
        btnHomeBack.setText("BACK");
        btnHomeBack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHomeBackMouseClicked(evt);
            }
        });

        lblStatus.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStatus.setText("Status");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel1.setText("Position");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel2.setText("Employee ID");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel3.setText("Name");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel4.setText("DOB");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel5.setText("Gender");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel6.setText("Address");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel7.setText("Phone");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel8.setText("Email:");

        txbCode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        cbxPosition.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbxPosition.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cbxPositionPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        txbName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        cbxSex.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txbPhone.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txbPhone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txbPhoneKeyReleased(evt);
            }
        });

        txbAddress.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txbEmail.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txbEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txbEmailActionPerformed(evt);
            }
        });

        txbPayroll.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txbPayroll.setEnabled(false);
        txbPayroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txbPayrollActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel10.setText("Salary");

        txbLevel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txbLevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txbLevelActionPerformed(evt);
            }
        });
        txbLevel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txbLevelKeyReleased(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel11.setText("Salary Grade");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbxPosition, 0, 140, Short.MAX_VALUE)
                    .addComponent(txbCode))
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txbName, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                    .addComponent(txbBirthday, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbxSex, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txbAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txbPhone)
                    .addComponent(txbEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txbPayroll, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txbLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(132, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(cbxPosition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txbCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(cbxSex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel6)
                                .addComponent(txbAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3)
                                .addComponent(txbName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel4)
                                .addComponent(txbBirthday, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel7)
                                .addComponent(txbPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txbLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel11))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txbPayroll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(txbEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/icons8_replay_30px.png"))); // NOI18N
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/plus.png"))); // NOI18N
        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnChange.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/icons8_support_30px.png"))); // NOI18N
        btnChange.setText("Edit");
        btnChange.setEnabled(false);
        btnChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeActionPerformed(evt);
            }
        });

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Delete.png"))); // NOI18N
        btnDelete.setText("Inactive");
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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 71, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 71, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 71, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 71, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        btnaddacount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/icons8_support_30px.png"))); // NOI18N
        btnaddacount.setText("add account");
        btnaddacount.setEnabled(false);
        btnaddacount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnaddacountMouseClicked(evt);
            }
        });
        btnaddacount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaddacountActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addComponent(btnChange, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(btnaddacount)))
                .addGap(85, 85, 85)
                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(91, 91, 91)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnRefresh)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnAdd)
                        .addComponent(btnChange)
                        .addComponent(btnaddacount)
                        .addComponent(btnDelete)
                        .addComponent(btnSave)))
                .addContainerGap(56, Short.MAX_VALUE))
        );

        jLabel12.setFont(new java.awt.Font("Times New Roman", 0, 28)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("EMPLOYEE MANAGEMENT");

        txbEmployees.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txbEmployeesActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Find.png"))); // NOI18N
        jButton2.setText("Search");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel9.setText("Username");

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        jLabel13.setText("Password");

        txtusername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtusernameActionPerformed(evt);
            }
        });

        txtpassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtpasswordActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnHomeBack)
                                .addGap(45, 45, 45)
                                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 1256, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtusername, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtpassword, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(73, 73, 73)
                        .addComponent(txbEmployees, javax.swing.GroupLayout.PREFERRED_SIZE, 489, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1218, Short.MAX_VALUE)
                    .addComponent(lblStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(138, 138, 138))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnHomeBack)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtusername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtpassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(txbEmployees, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(185, 185, 185))
        );

        setBounds(0, 0, 1322, 580);
    }// </editor-fold>//GEN-END:initComponents

    private void btnHomeBackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeBackMouseClicked
        Home home = new Home(detail);
        this.setVisible(false);
        home.setVisible(true);
    }//GEN-LAST:event_btnHomeBackMouseClicked

    private void tableEmployeesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableEmployeesMouseClicked
        cbxSex.removeAllItems();
        cbxPosition.removeAllItems();
        
        int Click=tableEmployees.getSelectedRow();
        TableModel model=tableEmployees.getModel();

        cbxPosition.addItem(model.getValueAt(Click,1).toString());
        txbCode.setText(model.getValueAt(Click,0).toString());
        txbName.setText(model.getValueAt(Click,2).toString());
        txbLevel.setText(model.getValueAt(Click,4).toString());
        ((JTextField)txbBirthday.getDateEditor().getUiComponent()).setText(model.getValueAt(Click,5).toString());
        cbxSex.addItem(model.getValueAt(Click,6).toString());
        txbAddress.setText(model.getValueAt(Click,7).toString());
        txbPhone.setText(model.getValueAt(Click,8).toString());
        txbEmail.setText(model.getValueAt(Click,9).toString());
        String []s=model.getValueAt(Click,10).toString().split("\\s");
        txbPayroll.setText(s[0]);
        txtusername.setText(model.getValueAt(Click,3).toString());
        txtpassword.setText(model.getValueAt(Click,12).toString());
        btnDelete.setEnabled(true);
        btnChange.setEnabled(true);
        String Status = model.getValueAt(Click,11).toString();
        if (Status.equals("inactive")) {
        btnDelete.setEnabled(true);
        btnDelete.setText("Active");       
        }
        else{
            btnDelete.setText("Inactive");
        }
        String username = model.getValueAt(Click, 3).toString();
        if (username.equals("")) {
            btnaddacount.setEnabled(true);
        } else {
             btnaddacount.setEnabled(false);
        }

    }//GEN-LAST:event_tableEmployeesMouseClicked

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        String sql2 = "SELECT MAX(employeecode) FROM employees";
        int id=0;
        try{
           PreparedStatement stat=conn.prepareStatement(sql2);
           ResultSet rs=stat.executeQuery();       

           if (rs.next()) {
            id = rs.getInt(1);
            } 
           
        }
        catch(Exception e){
             e.printStackTrace();
        }
        id=id+1;
        String IDString = String.valueOf(id); 
        Refresh();
        txbCode.setText(IDString);
        Add=true;
        btnAdd.setEnabled(false);
        btnSave.setEnabled(true);
        LoadGT();
        Enabled();
        LoadPosition();
        txbCode.setEnabled(false);
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeActionPerformed
        Add=false;
        Change=true;
        
        btnAdd.setEnabled(false);
        btnChange.setEnabled(false);
        btnDelete.setEnabled(false);
        btnSave.setEnabled(true);
        LoadGT();
        Enabled();
        LoadPosition();
        txbCode.setEnabled(false);
        txtusername.setEnabled(false);
        txtpassword.setEnabled(true);
    }//GEN-LAST:event_btnChangeActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
         String str=btnDelete.getText();
         
          int Click;
         if(str.equals("Inactive"))
            Click = JOptionPane.showConfirmDialog(null, "Are you sure you want to Inactivate this employee?","Notification",2);
         else
             Click = JOptionPane.showConfirmDialog(null, "Are you sure you want to activate this employee","Notification",2);
        if(Click ==JOptionPane.YES_OPTION){
            
            if(str.equals("Inactive")){
                String sqlDelete="Update Employees set status ='inactive' WHERE EmployeeCode=? ";//
            try{
                pst=conn.prepareStatement(sqlDelete);
                pst.setString(1, String.valueOf(this.txbCode.getText()));
                pst.executeUpdate();
                
                Disabled();
                Refresh();
                lblStatus.setText("activate Successfully!");
                Load(sql);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            
            }
            else if(str.equals("Active"))
            {
                String sqlDelete="Update Employees set status ='active' WHERE EmployeeCode=? ";//
            try{
                pst=conn.prepareStatement(sqlDelete);
                pst.setString(1, String.valueOf(this.txbCode.getText()));
                pst.executeUpdate();
                
                Disabled();
                Refresh();
                lblStatus.setText("Inactivate Successfully");
                Load(sql);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            }
            
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if(Add==true){
            if(Check()&&checkNull()&&CheckEmail()){
                if(CheckEmail()){
                    addEmployees();
                }
                
            }
            
        }else if(Change==true&&checkNull()){
            if(CheckEmail()){
                updateAccount();
                 changeEmployees();
                   
                }
            else
                lblStatus.setText("Email format invalid! ");
        }
        else if(Addacc==true){
            if(CheckAccount()){
                 addAccount();
                   
                }
            else
                lblStatus.setText("Username and password format invalid! ");
        }
        
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        LoadGT();
        Refresh();
        Disabled();
        LoadPosition();
        Load(sql);
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void txbLevelKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txbLevelKeyReleased
        String sql = "SELECT * FROM Positionn where Position=?";
        txbLevel.setText(cutChar(txbLevel.getText()));
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        try {
            pst=conn.prepareStatement(sql);
            pst.setString(1, this.cbxPosition.getSelectedItem().toString());
            rs=pst.executeQuery();
            while(rs.next()){
                if(txbLevel.getText().equals("")){
                    txbPayroll.setText("0");
                }
                else{   
                    if(txbLevel.getText().equals("0")){
                        int Level=Integer.parseInt(txbLevel.getText());
                        
                        String[]s=rs.getString("Payroll").trim().split("\\s");
                        
                        txbPayroll.setText(formatter.format((int)(convertedToNumbers(s[0])/2)));
                    }
                    else{
                        int Level=Integer.parseInt(txbLevel.getText().toString());
                        String[]s=rs.getString("Payroll").trim().split("\\s");
                        
                        txbPayroll.setText(formatter.format(Level*convertedToNumbers(s[0])));
                    }
                }   
            }
        }  
        catch (Exception e) {  
            e.printStackTrace();  
        }
    }//GEN-LAST:event_txbLevelKeyReleased

    private void cbxPositionPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cbxPositionPopupMenuWillBecomeInvisible
        txbLevel.setText("");
        txbPayroll.setText("");
    }//GEN-LAST:event_cbxPositionPopupMenuWillBecomeInvisible

    private void txbPhoneKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txbPhoneKeyReleased
        txbPhone.setText(cutChar(txbPhone.getText()));
    }//GEN-LAST:event_txbPhoneKeyReleased

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

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String sql = "select DISTINCT * from employees e, positionn p, accounts a where e.positionid=p.id and a.employeeid=e.EmployeeCode "
                +"and (p.Position like N'%"+this.txbEmployees.getText()
                +"%' or e.EmployeeCode like N'%"+this.txbEmployees.getText()
                +"%' or e.FullName like N'%"+this.txbEmployees.getText()
                +"%' or e.YearofBirth like N'%"+this.txbEmployees.getText()
                +"%' or e.Sex like N'%"+this.txbEmployees.getText()
                +"%' or e.Address like N'%"+this.txbEmployees.getText()
                +"%' or e.Phone like N'%"+this.txbEmployees.getText()
                +"%' or e.Email like N'%"+this.txbEmployees.getText()
                +"%' or Level like N'%"+this.txbEmployees.getText()
                +"%' or a.username like N'%"+this.txbEmployees.getText()
                +"%' or a.password like N'%"+this.txbEmployees.getText()
                +"%' or p.Payroll like N'%"+this.txbEmployees.getText()+"%');";
        Load(sql);
        txbEmployees.setText("");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txbPayrollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txbPayrollActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txbPayrollActionPerformed

    private void txbLevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txbLevelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txbLevelActionPerformed

    private void txbEmployeesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txbEmployeesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txbEmployeesActionPerformed

    private void btnaddacountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaddacountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnaddacountActionPerformed

    private void btnaddacountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnaddacountMouseClicked
        txtusername.setEnabled(true);
        txtpassword.setEnabled(true);
        Addacc=true;
        btnSave.setEnabled(true);
    }//GEN-LAST:event_btnaddacountMouseClicked

    private void txtusernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtusernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtusernameActionPerformed

    private void txtpasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtpasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtpasswordActionPerformed

    private void txbEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txbEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txbEmailActionPerformed

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EmployeesManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EmployeesManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EmployeesManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EmployeesManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Detail detail=new Detail();
                new EmployeesManagement(detail).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnChange;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnHomeBack;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnaddacount;
    private javax.swing.JComboBox<String> cbxPosition;
    private javax.swing.JComboBox<String> cbxSex;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTable tableEmployees;
    private javax.swing.JTextField txbAddress;
    private com.toedter.calendar.JDateChooser txbBirthday;
    private javax.swing.JTextField txbCode;
    private javax.swing.JTextField txbEmail;
    private javax.swing.JTextField txbEmployees;
    private javax.swing.JTextField txbLevel;
    private javax.swing.JTextField txbName;
    private javax.swing.JTextField txbPayroll;
    private javax.swing.JTextField txbPhone;
    private javax.swing.JTextField txtpassword;
    private javax.swing.JTextField txtusername;
    // End of variables declaration//GEN-END:variables
}
