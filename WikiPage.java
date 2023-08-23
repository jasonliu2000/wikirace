import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class WikiPage {

	public static void main (String[] args) {
		try {
			URL url = new URL("https://en.wikipedia.org/wiki/Wikiracing");

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			int responseCode = connection.getResponseCode();
			if (responseCode != HttpURLConnection.HTTP_OK) {
				System.out.println("HTTP request failed with response code: " + responseCode);

			} else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

				String line;
				StringBuilder content = new StringBuilder();

				while ((line = reader.readLine()) != null) {
					content.append(line);
				}

				reader.close();
				connection.disconnect();

				System.out.println(content.toString());
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}