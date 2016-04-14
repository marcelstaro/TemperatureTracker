package helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import beans.Temperature;

import java.sql.PreparedStatement;

import servlet.UserService;
import test.InsertTemperatures;

public class SqlHelper {
	
	private Connection con = null;
    private Statement st = null;
    private ResultSet rs = null;
    
    private final String url = "jdbc:mysql://us-cdbr-iron-east-03.cleardb.net/heroku_b5d7097a0e320e6";//"jdbc:mysql://localhost:3306/temperatures";
    private final String dbuser = "bc988fe6542bb9";//marcelstaro
    private final String dbpassword = "4e9d6dec";//pass
    
	
	public Connection getConnection() throws SQLException { 
        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
    	
        return DriverManager.getConnection(url, dbuser, dbpassword);
	}
		
	public String getPasswordFromUser(String username) {

    	String password = null;

        try {
            con = getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT PASSWORD FROM USER WHERE USERNAME='"+username+"';");

            if (rs.next()) {
                password = rs.getString(1);
                System.out.println("Password retrieved: " + password);
            } 

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(InsertTemperatures.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(InsertTemperatures.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            } 
        }
        
		return password;
	}
	
	public ArrayList<Temperature> getTemperaturesFromUser(String username) {
		rs = null;
		
		ArrayList<Temperature> temperatures = new ArrayList<Temperature>();
		
		try {
            con = getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM TEMPERATURE WHERE USERNAME='"+username+ "';");

            
            while ( rs.next() ) {
				int id = rs.getInt("id");
				String date = rs.getString("DATE");
				String tempF=rs.getString("TEMPERATURE_FARENHEIT");
				
				temperatures.add(new Temperature(id, username, date, tempF));
            }
            
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(InsertTemperatures.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(InsertTemperatures.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            } 
        }	
		
		return temperatures;
	}

	public boolean insertTemperature(String date, String temperature, String username) {		
		boolean result = false;
		
		try {
            con = getConnection();
            con.setAutoCommit(false);
            PreparedStatement stmt = con.prepareStatement("INSERT INTO TEMPERATURE(DATE, TEMPERATURE_FARENHEIT, USERNAME) " +
							"VALUES(?,?,?)");
            			
			stmt.setString(1, date);
			stmt.setString(2, temperature);
			stmt.setString(3, username);
			stmt.executeUpdate();		
			con.commit();
			
			result = true;

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(InsertTemperatures.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(InsertTemperatures.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            } 
        }
		
		
		return result;
	}
	
	public static void main (String... args) {
		SqlHelper sql = new SqlHelper();
		
		System.out.println("result from query: "+sql.getPasswordFromUser("testUser"));
		
		System.out.println("test1");
		System.out.println(sql.insertTemperature("04/13/2016","56","marcel"));

		System.out.println("test2");
	}
}
