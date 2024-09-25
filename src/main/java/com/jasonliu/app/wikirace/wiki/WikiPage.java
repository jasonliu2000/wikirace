package com.jasonliu.app.wikirace.wiki;
import java.util.LinkedList;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

public class WikiPage {
	static String wikiBaseUrl = "https://en.wikipedia.org";
	static String wikiSubPath = "/wiki/";
	static String aTagsRegex = "^/wiki/(?!(?:File:|Special:|Template:|Template_talk:|Wikipedia:|Help:)).*";

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
			Element body = doc.body()
												.select("#mw-content-text .mw-parser-output")
												.first();
												
			if (body.selectFirst(".infobox") != null) {
				body.selectFirst(".infobox").remove();
			}
			
			Element terminateTag = body.selectFirst("#See_also");
			if (terminateTag == null) {
				terminateTag = body.selectFirst("#Notes");
			}
			if (terminateTag == null) {
				terminateTag = body.selectFirst("#References");
			}
			if (terminateTag != null) {
				terminateTag.parent().nextElementSiblings().remove();
			}

			Elements aTags = body.getElementsByAttributeValueMatching("href", aTagsRegex);

			for (Element a : aTags) {
				String urlPath = a.attr("href");
				urlPath = urlPath.substring(wikiSubPath.length());
				links.add(urlPath);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		return links;
	}
}