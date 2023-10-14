import java.util.List;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

public class WikiPage {
	String wikiBaseUrl = "https://en.wikipedia.org";
	String wikiSubPath = "/wiki/";
	String title;

	private LinkedList<String> links = new LinkedList<String>();

	public WikiPage(String endPath) {
		title = endPath;
		parsePage();		
	}

	public LinkedList<String> getLinks() {
		return links;
	}

	private void parsePage() {
		String url = wikiBaseUrl + wikiSubPath + title;
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

					urls.forEach(u -> {
						u = u.substring(wikiSubPath.length());

						// Only add to links if it's not in backlog yet
						if (!BacklogHistory.contains(u)) {
							links.addLast(u);
							BacklogHistory.add(u);
						}
					});
				}

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}