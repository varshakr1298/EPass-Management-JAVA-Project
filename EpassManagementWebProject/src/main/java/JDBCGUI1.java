import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

public class JDBCGUI1 extends Frame implements ActionListener {
	TextArea ta;
	String data="";
	static String databaseURL;
	static Connection conn;
	static Statement stmt;
	static ResultSet rs;
	Label lblid,lblname,lblfromD,lbltoD,lblvehicleType,lblvehicleNumber,lblnum,lblstatus;
	TextField txtid,txtname,txtfromD,txttoD,txtvehicleType,txtvehicleNumber,txtnum,txtstatus;
	Button btnregister,btnviewApproved,btnviewRejected,btnupdate,btnshow;
	static String insertcmd;
	static String deletecmd;
	static String updatecmd;
	static String selectcmd;
	static String showcmd;
	static PreparedStatement pstatement;
	static String selectApprovedcmd;
	static String selectRejectedcmd;

	public JDBCGUI1()throws Exception{
		
		ta=new TextArea();
		setLayout(null);
		ta.setBounds(60,50,800,200);
		add(ta);
		connect();
		selectEPass();
		selectEPassApproved();
		selectEPassRejected();
		ta.setText(data);
		lblid=new Label("EPass ID:\t");
		lblname=new Label("Traveller Name:\t");
		lblfromD=new Label("From District:\t");
		lbltoD=new Label("To District:\t");
		lblvehicleType=new Label("Vehicle Type:\t");
		lblvehicleNumber=new Label("Vehicle Number:\t");
		lblnum=new Label("TAP:\t");
		lblstatus=new Label("Status:\t");

		txtid=new TextField(20);
		txtname=new TextField(20);
		txtfromD=new TextField(20);
		txttoD=new TextField(20);
		txtvehicleType=new TextField(20);
		txtvehicleNumber=new TextField(20);
		txtnum=new TextField(20);
		txtstatus=new TextField(20);
		
		btnregister=new Button("Register EPass!");
		btnshow=new Button("View All");
		btnviewApproved=new Button("View Approved");
		btnupdate=new Button("Update");
		btnviewRejected=new Button("View Rejected");
	
		lblid.setBounds(80,300,150, 20);
		lblname.setBounds(80,330,150, 20);
		lblfromD.setBounds(80,360,150, 20);
		lbltoD.setBounds(80,390,150, 20);
		lblvehicleType.setBounds(80,420,150, 20);
		lblvehicleNumber.setBounds(80,450,150, 20);
		lblnum.setBounds(80,480,150, 20);
		lblstatus.setBounds(80,510,150, 20);

		txtid.setBounds(250,300,150, 20);
		txtname.setBounds(250,330,150, 20);
		txtfromD.setBounds(250,360,150, 20);
		txttoD.setBounds(250,390,150, 20);
		txtvehicleType.setBounds(250,420,150, 20);
		txtvehicleNumber.setBounds(250,450,150, 20);
		txtnum.setBounds(250,480,150, 20);
		txtstatus.setBounds(250,510,150, 20);

		btnregister.setBounds(250, 560, 150,20);
		btnshow.setBounds(250, 590, 150,20);
		btnviewApproved.setBounds(250, 620, 150,20);
		btnupdate.setBounds(250, 650, 150,20);
		btnviewRejected.setBounds(250, 680, 150,20);

		btnregister.addActionListener(this);
		btnviewApproved.addActionListener(this);
		btnshow.addActionListener(this);
		btnupdate.addActionListener(this);
		btnviewRejected.addActionListener(this);
		

		add(lblid);add(lblname);add(lblfromD);add(lbltoD);add(lblvehicleType);add(lblvehicleNumber);add(lblnum);add(lblstatus);
		add(txtid);add(txtname);add(txtfromD);add(txttoD);add(txtvehicleType);add(txtvehicleNumber);add(txtnum);add(txtstatus);
		add(btnregister);add(btnshow);add(btnviewApproved);add(btnupdate);add(btnviewRejected);

		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				System.out.println("Pressed window closing...");
				System.exit(0);
			}
		});
	}
	public void paint(Graphics g){
		try{
            selectEPass();
		}catch(Exception e){}
		ta.setText(data);
	}
	void updateStatus() throws Exception{
		selectcmd="select * from epass";
		stmt=conn.createStatement();
		rs=stmt.executeQuery(selectcmd);
		String status="";
		int noOfAdditionalPassengers=0;
		String vehicleType="";
		while(rs.next()) {
			status=rs.getString(8);
			noOfAdditionalPassengers=rs.getInt(7);
			vehicleType=rs.getString(5);
			if(!status.equals("Approved")) {
				if(vehicleType.equals("Bike") && noOfAdditionalPassengers<=1) {
		    		status="Approved";
		    	}
				else if(vehicleType.equals("Car") && noOfAdditionalPassengers<=3) {
		    		status="Approved";
		    	}
		    	else if(vehicleType.equals("Van") && noOfAdditionalPassengers<=8) {
		    		status="Approved";
		    	}else {
		    		status="Rejected";
		    	}
	    	}
	    	updatecmd="update epass set status = ?  where num = ?  and vehicleType = ?";
			pstatement=conn.prepareStatement(updatecmd);
			pstatement.setString(1,status);
			pstatement.setInt(2,noOfAdditionalPassengers);
			pstatement.setString(3,vehicleType);
			pstatement.executeUpdate();
			//System.out.println(updatecmd+noOfAdditionalPassengers);
			System.out.print("EPass Record Updated!");
		}
	}
	public void actionPerformed(ActionEvent ae){
		if (ae.getSource()==btnregister){
			System.out.print("Register EPass Button Pressed!");
			URI uri = null;
			try {
				uri= new URI("http://localhost:8080/EpassManagementWebProject/RegisterEPassForm.html");
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (Desktop.isDesktopSupported()) {
				Desktop desktop = Desktop.getDesktop();
				try {
					desktop.browse(uri);
				} catch (Exception ex) {
				}
			} 
			repaint();
			}
		if(ae.getSource()==btnviewApproved) {
			System.out.println("View Approved Button Pressed!");
			try {
				System.out.println("Calling function!");
				selectEPassApproved();
			} catch (Exception e) {
			}
			System.out.println("Setting Data!");
			ta.setText(data);
		}
		if(ae.getSource()==btnviewRejected) {
			System.out.println("View Rejected Button Pressed!");
			try {
				System.out.println("Calling function!");
				selectEPassRejected();
			} catch (Exception e) {
			}
			System.out.println("Setting Data!");
			ta.setText(data);
		}
		if(ae.getSource()==btnshow) {
			System.out.print("View All Button Pressed!");
			try {
			showcmd="select * from epass where epassid="+Integer.parseInt(txtid.getText())+"";
			stmt=conn.createStatement();
			rs=stmt.executeQuery(showcmd);
			System.out.println(showcmd);
			rs.next();
			txtname.setText(rs.getString(2)+"");
			txtfromD.setText(rs.getString(3)+"");
			txttoD.setText(rs.getString(4)+"");
			txtvehicleType.setText(rs.getString(5)+"");
			txtvehicleNumber.setText(rs.getString(6)+"");
			txtnum.setText(rs.getInt(7)+"");
			txtstatus.setText(rs.getString(8)+"");
			}catch(Exception e) {	
			}
			repaint();
		}
		if(ae.getSource()==btnupdate) {
			try {
				System.out.print("Update Button Pressed!");
				updateStatus();
				
			}catch(Exception e) {}
				repaint();
			
		}
	}
	void connect()throws Exception{
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn=DriverManager.getConnection(  
			"jdbc:mysql://localhost/epassManagement","root","asdf1234");
		} catch (SQLException | ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Connection Established Successfully!");
	}
	void selectEPassApproved()throws Exception{
		data="";
		selectApprovedcmd="select * from epass where status=\"Approved\"";
		stmt=conn.createStatement();
		rs=stmt.executeQuery(selectApprovedcmd);
		System.out.println(selectApprovedcmd);
		while(rs.next()) {
			data+=rs.getInt(1)+"\t"+rs.getString(2)+"\t"+rs.getString(3)+"\t"+rs.getString(4)+"\t"+rs.getString(5)+"\t"+rs.getString(6)+"\t"+rs.getInt(7)+"\t"+rs.getString(8)+"\n";
		}
	}
	void selectEPassRejected()throws Exception{
		data="";
		selectRejectedcmd="select * from epass where status=\"Rejected\"";
		stmt=conn.createStatement();
		rs=stmt.executeQuery(selectRejectedcmd);
		System.out.println(selectRejectedcmd);
		while(rs.next()) {
			data+=rs.getInt(1)+"\t"+rs.getString(2)+"\t"+rs.getString(3)+"\t"+rs.getString(4)+"\t"+rs.getString(5)+"\t"+rs.getString(6)+"\t"+rs.getInt(7)+"\t"+rs.getString(8)+"\n";
		}
	}
	void selectEPass()throws Exception{
		data="";
		selectcmd="select * from epass";
		stmt=conn.createStatement();
		rs=stmt.executeQuery(selectcmd);
		while(rs.next()) {
			data+=rs.getInt(1)+"\t"+rs.getString(2)+"\t"+rs.getString(3)+"\t"+rs.getString(4)+"\t"+rs.getString(5)+"\t"+rs.getString(6)+"\t"+rs.getInt(7)+"\t"+rs.getString(8)+"\n";
		}
	}
	public static void main(String args[])throws Exception{
		JDBCGUI1 appwin=new JDBCGUI1();
		appwin.setSize(1000,1000);
		appwin.setTitle("EPass Management");
		appwin.setVisible(true);
		appwin.setBackground(Color.pink);
	}
}


