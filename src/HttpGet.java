import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.awt.image.BufferedImage;

import javax.net.ssl.HttpsURLConnection;
import javax.imageio.ImageIO;

/**
 * @author isaac
 * 
 */
public class HttpGet {

	/**
	 * @param args
	 */
	private final String USER_AGENT = "Robotest";
	private static java.awt.image.BufferedImage img;

	// HTTP GET request
	BufferedImage getImage() throws Exception {

		String url = "http://10.25.37.21/snapshot.cgi";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");
		String userpass = "admin" + ":" + "admin";
        String basicAuth = "Basic " + new String(Base64Coder.encode(userpass.getBytes()));
        con.setRequestProperty ("Authorization", basicAuth);
        //con.setRequestProperty ("Token", Token);
		
		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		// BufferedReader in = new BufferedReader(new InputStreamReader(
		// con.getInputStream()));
		img = ImageIO.read(con.getInputStream());
		// print result
		return img;

	}
}
