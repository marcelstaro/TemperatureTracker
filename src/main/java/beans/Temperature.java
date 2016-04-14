package beans;

public class Temperature {
	private int id;
	private String username;
	private String date;
	private String temperature;
	
	public Temperature(int id, String username, String date, String temperature) {
		this.id = id;
		this.username = username;
		this.date = date;
		this.temperature = temperature;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getTemperature() {
		return temperature;
	}
	
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	
	public int getId() {
		return id;
	}
		
	public void setId(int id) {
		this.id = id;
	}
}
