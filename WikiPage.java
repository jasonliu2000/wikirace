import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

public class WikiPage {

	public static void main (String[] args) {
		try {
				Document doc = Jsoup.connect("https://en.wikipedia.org/wiki/Wikiracing").get();
				Element body = doc.body();

				Elements paragraphs = body.select("p");
				for (Element p : paragraphs) {
					Elements hyperlinks = p.select("a");

					List<String> urls = hyperlinks.eachAttr("href");
					urls.removeIf(u -> !u.startsWith("/wiki"));
					urls.removeIf(u -> u.endsWith("Citation_needed"));
					// TODO: consider removing subcategory part in urls
					// ex. "/wiki/White_paper#In_government" should be changed to "/wiki/White_paper"

					for (String u : urls) {
						System.out.println(u);
					}
				}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}