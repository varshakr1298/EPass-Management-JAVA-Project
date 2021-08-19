

import java.awt.Color;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RegisterEPass
 */
class EPass {
	public static int EPassID=101;
	String travellerName;
	String fromDistrict;
	String toDistrict;
	String vehicleType;
	String vehicleNumber;
	int numberOfAdditionalPassengers;
	public String status;
	public EPass(int EPassID,String travellerName,String fromDistrict,String toDistrict,String vehicleType,String vehicleNumber,int numberOfAdditionalPassengers){
		this.travellerName=travellerName;
		this.fromDistrict=fromDistrict;
		this.toDistrict=toDistrict;
		this.vehicleType=vehicleType;
		this.vehicleNumber=vehicleNumber;
		this.numberOfAdditionalPassengers=numberOfAdditionalPassengers;
		this.status="Pending";
		this.EPassID=EPassID;
	}
	public String toString() {
		return "EPassID: "+EPassID+"\n Traveller Name: "+travellerName+"\n From District: "+fromDistrict+"\n To District: "+toDistrict+"\n Vehicle Type: "+vehicleType+
				"\n Vehicle Number: " + vehicleNumber+"\nNumber of Additional Passengers: "+numberOfAdditionalPassengers+"\nStatus: "+status;
	}
}
@WebServlet("/RegisterEPass")

public class RegisterEPass extends HttpServlet {
	static Connection conn;
	static String insertcmd;
	static PreparedStatement p;
	static Statement stmt;
	static ResultSet rs;
	private static final long serialVersionUID = 1L;
    public static void AddEPass(EPass e) {
    	try{
			insertcmd="insert into epass values(?,?,?,?,?,?,?,?)";
			System.out.println("Insert command:"+insertcmd);
			p=conn.prepareStatement(insertcmd);
			p.setInt(1,e.EPassID);
			p.setString(2,e.travellerName);
			p.setString(3,e.fromDistrict);
			p.setString(4,e.toDistrict);
			p.setString(5,e.vehicleType);
			p.setString(6,e.vehicleNumber);
			p.setInt(7,e.numberOfAdditionalPassengers);
			p.setString(8,e.status);
			int i=p.executeUpdate();  
			if(i>0)  
			System.out.print("EPass has been successfully registered!");  
			      
			          
			}catch (Exception e2) {System.out.println(e);}  		
    }
    public static void updateEPass(EPass e) {
    	if(e.vehicleType=="Bike" && e.numberOfAdditionalPassengers==1) {
    		e.status="Approved";
    	}else {
    		e.status="Rejected";
    	}
    	if(e.vehicleType=="Car" && e.numberOfAdditionalPassengers==3) {
    		e.status="Approved";
    	}else {
    		e.status="Rejected";
    	}
    	if(e.vehicleType=="Van" && e.numberOfAdditionalPassengers==8) {
    		e.status="Approved";
    	}else {
    		e.status="Rejected";
    	}

    }
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String travellerName=request.getParameter("tname");
		String fromDistrict=request.getParameter("fromD");
		String toDistrict=request.getParameter("toD");
		String vehicleType=request.getParameter("vehicleType");
		String vehicleNumber=request.getParameter("vehicleNumber");
		int numberAddP=Integer.parseInt(request.getParameter("num"));
		String query="select epassid from epass order by epassid DESC LIMIT 1";
		int epassid=0;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn=DriverManager.getConnection(  
			"jdbc:mysql://localhost/epassManagement","root","asdf1234");
		} catch (SQLException | ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Connection established successfully!");
		try {
			stmt=conn.createStatement();
			rs=stmt.executeQuery(query);
			rs.next();
			if(rs!=null)
				epassid=rs.getInt(1);
			else
				epassid=101;
			System.out.print(epassid);
		} catch (SQLException e2) {
		}
		
		EPass e=new EPass(++epassid,travellerName,fromDistrict,toDistrict,vehicleType,vehicleNumber,numberAddP);
		System.out.print(e);
		AddEPass(e);
		
		PrintWriter output = response.getWriter();
		output.print("Record Inserted Successfully!");
	}

}
