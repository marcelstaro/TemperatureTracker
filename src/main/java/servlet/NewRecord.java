package servlet;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import helpers.SqlHelper;

/**
 * Servlet implementation class NewRecord
 */
@WebServlet("/NewRecord")
public class NewRecord extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NewRecord() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String str_date=request.getParameter("date");
		String temp=request.getParameter("temp");	

		String username=request.getParameter("username");
		String password=request.getParameter("password");	
		ServletOutputStream output=response.getOutputStream();

		Connection c = null;
		PreparedStatement stmt = null;

		if (UserService.authenticate(username, password) != UserService.AUTH_SUCCESS) {
			output.println("failed: User or password not valid.");
		} else if((str_date == null) || (temp == null)) {
				output.println("failed: date or temperature is null");
			} else if((str_date.trim().length() <= 0) && (temp.trim().length() <= 0)) {
				output.println("failed: date or temperature is empty");
			} else {			
					if (new SqlHelper().insertTemperature(str_date, temp, username)) {
						output.println("success");	
					}
			} 
		
		try { if (output != null) output.close();	} catch (Exception e) { e.printStackTrace(); }
	}

}
