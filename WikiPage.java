import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

public class WikiPage {
	String wikiBaseUrl = "https://en.wikipedia.org";
	String wikiSubPath = "/wiki/";

	private LinkedList<String> links = new LinkedList<String>();
	String title;

	public WikiPage(String endPath) {
		title = endPath;
		String url = wikiBaseUrl + wikiSubPath + endPath;

		try {
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

					for (String u : urls) {
						String linkName = u.substring(wikiSubPath.length());
						links.addLast(linkName);
					}
				}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public LinkedList<String> getLinks() {
		return links;
	}
}