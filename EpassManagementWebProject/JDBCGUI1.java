import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;

public class JDBCGUI1 extends Frame implements ActionListener {
	TextArea ta;
	String data="";
	static String databaseURL;
	static Connection conn;
	static Statement stmt;
	static ResultSet rs;
	Label lblname,lblcourse,lbldate,lblemail;
	TextField txtname,txtcourse, txtdate,txtemail;
	Button btninsert;
	static String insertcmd;
	static String selectcmd;
	public JDBCGUI1()throws Exception{
		
		ta=new TextArea();
		setLayout(null);
		ta.setBounds(60,50,800,200);
		add(ta);
		connect();
		selectStudent();
		ta.setText(data);
		lblname=new Label("Student Name:\t");
		lblcourse=new Label("Course Name:\t");
		lbldate=new Label("DOB:\t");
		lblemail=new Label("Email:\t");

		txtname=new TextField(20);
		txtcourse=new TextField(20);
		txtdate=new TextField(20);
		txtemail=new TextField(20);
	
		lblname.setBounds(80,300,150, 20);
		lblcourse.setBounds(80,330,150, 20);
		lbldate.setBounds(80,360,150, 20);
		lblemail.setBounds(80,390,150, 20);

		txtname.setBounds(250,300,150, 20);
		txtcourse.setBounds(250,330,150, 20);
		txtdate.setBounds(250,360,150, 20);
		txtemail.setBounds(250,390,150, 20);

		btninsert=new Button("Insert");
		btninsert.setBounds(80, 450, 150,20);

		btninsert.addActionListener(this);
		

		add(lblname);add(lblcourse);add(lbldate);add(lblemail);
		add(txtname);add(txtcourse);add(txtdate);add(txtemail);add(btninsert);

		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				System.out.println("Pressed window closing...");
				System.exit(0);
			}
		});
	}
	public void paint(Graphics g){
		try{
			selectStudent();
		}catch(Exception e){}
		ta.setText(data);
	}
	public void actionPerformed(ActionEvent ae){
		if (ae.getSource()==btninsert){
			try{
			btninsert.setBackground(Color.blue);
			insertcmd="insert into student values('"+txtname.getText()+"','"+txtcourse.getText()+"','"+txtdate.getText()+"','"+txtemail.getText()+"')";
			System.out.println("Insert command:"+insertcmd);
			stmt=conn.createStatement();
			stmt.executeUpdate(insertcmd);
			System.out.println("Student record inserted!");
			}catch(Exception e){}
			repaint();
			}
			
	}
	void connect()throws Exception{
		Class.forName("com.mysql.cj.jdbc.Driver");  
		conn=DriverManager.getConnection(  
		"jdbc:mysql://localhost/college","root","root");
		System.out.println("Connection established successfully!");
	}
	void selectStudent()throws Exception{
		data="";
		selectcmd="select * from student";
		stmt=conn.createStatement();
		rs=stmt.executeQuery(selectcmd);
		while(rs.next())
			data+=rs.getString(1)+"\t"+rs.getString(2)+"\t"+rs.getString(3)+"\t"+rs.getString(4)+"\n";
	}
	public static void main(String args[])throws Exception{
		JDBCGUI1 appwin=new JDBCGUI1();
		appwin.setSize(950,600);
		appwin.setTitle("JDBC Student");
		appwin.setVisible(true);
		appwin.setBackground(Color.pink);
	}
}


