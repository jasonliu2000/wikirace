import java.util.List;
import java.util.LinkedList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

public class WikiPage {
	static String wikiBaseUrl = "https://en.wikipedia.org";
	static String wikiSubPath = "/wiki/";

	private static String getUrl(String identifier) {
		return wikiBaseUrl + wikiSubPath + identifier;
	}

	public static boolean exists(String article) {
		String url = getUrl(article);
		try {
			Connection connection = Jsoup.connect(url);
			connection.method(Connection.Method.GET);

			Connection.Response response = connection.execute();
			if (response.statusCode() == 200) {
				return true;
			}
		} catch (Exception toBeIgnored) {
			// Ignore 404s but have to handle other possible exceptions
		}

		return false;
	}

	public static LinkedList<String> getLinks(String article) {
		String url = getUrl(article);
		LinkedList<String> links = new LinkedList<String>();

		try {
			// Connection connection = Jsoup.connect(url);
			// Document doc = connection.get();
			Document doc = Jsoup.connect(url).get();
			Element body = doc.body();

			Elements paragraphs = body.select("p");
			for (Element p : paragraphs) {
				Elements hyperlinks = p.select("a");

				List<String> urls = hyperlinks.eachAttr("href");
				urls.removeIf(u -> !u.startsWith(wikiSubPath));
				urls.removeIf(u -> u.endsWith("Citation_needed"));
				// TODO: consider removing subcategory fragment in urls
				// ex. "/wiki/White_paper#In_government" should be changed to "/wiki/White_paper"

				urls.forEach(u -> {
					u = u.substring(wikiSubPath.length());
					links.add(u);
				});
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		return links;
	}
}