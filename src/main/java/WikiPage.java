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

	public static boolean exists(String page) {
		String url = getUrl(page);
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

	public static LinkedList<String> getLinks(String page) {
		String url = getUrl(page);
		LinkedList<String> links = new LinkedList<String>();

		try {
			Document doc = Jsoup.connect(url).get();
			Element body = doc.body();

			Elements paragraphs = body.select("p");
			for (Element p : paragraphs) {
				Elements hyperlinks = p.select("a");

				List<String> urls = hyperlinks.eachAttr("href");
				urls.removeIf(u -> !u.startsWith(wikiSubPath));
				urls.removeIf(u -> u.endsWith("Citation_needed"));
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