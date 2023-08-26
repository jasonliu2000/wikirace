import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;

public class WikiPage {

	public static void main (String[] args) {
		try {
				Document doc = Jsoup.connect("https://en.wikipedia.org/wiki/Wikiracing").get();
				String body = doc.body().text();
				System.out.println(body);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}