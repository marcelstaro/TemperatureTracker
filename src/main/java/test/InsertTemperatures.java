package test;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class InsertTemperatures {
	private final String USER_AGENT = "Mozilla/5.0";
	
	public void sendPost(String date, String temperature, String username, String password) throws Exception {

		URL url = new URL("http://localhost:8080/TemperatureTrackerLocal/NewRecord");
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("date", date);
        params.put("temp", temperature);
        params.put("username", username);
        params.put("password", password);

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

        for (int c; (c = in.read()) >= 0;)
            System.out.print((char)c);

	}
	
	public static void main(String[] args) throws Exception {
		new InsertTemperatures().sendPost("04/12/2016","56","marcel","mypass0123");
	}
}
