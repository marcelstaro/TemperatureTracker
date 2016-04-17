package servlet;


import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import beans.Temperature;
import helpers.SqlHelper;

/**
 * Servlet implementation class RaspberryPi
 */
@WebServlet("/Temperature.json")
public class RaspberryPi extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RaspberryPi() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 *//*
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ServletOutputStream output=response.getOutputStream();
		output.println("Since we implemented authentication, this server will no "
				+ "longer support GET methods, because passwords can be cached and bookmarked with GET"
				+ " (You can literally see the password in the request url string!!!)"
				+ ", which is why we use post instead.");
		output.close();
	}*/
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath()).append(" Marcel");
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.reset();
		response.resetBuffer();

		ServletOutputStream output=response.getOutputStream();
		String username=request.getParameter("username");
		String password=request.getParameter("password");	

		
		int auth_res = UserService.authenticate(username, password);
		
		if(auth_res == UserService.AUTH_SUCCESS) {

				response.setContentType("application/json");
				ArrayList<Temperature> temperatures = new SqlHelper().getTemperaturesFromUser(username);

				JSONArray array = new JSONArray();

				for (Temperature t : temperatures) {
					JSONObject obj = new JSONObject();				
					obj.put("id", t.getId());
					obj.put("date",t.getDate());
					obj.put("temp",t.getTemperature());
					array.add(obj);
				}

				JSONObject finalObj=new JSONObject();
				finalObj.put("temperature",array);
				String jsonText = JSONValue.toJSONString(finalObj);

				output.println(jsonText);
			} else {//if (auth_res == UserService.AUTH_INCORRECT_PWD || auth_res == UserService.AUTH_DOESNT_EXIST) {
				output.println("Failed: User or password not valid.");
			}
		
		try { if (output != null) output.close();	} catch (Exception e) { e.printStackTrace(); }

	}
}
