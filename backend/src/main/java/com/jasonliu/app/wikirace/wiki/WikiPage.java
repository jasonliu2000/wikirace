package com.jasonliu.app.wikirace.wiki;

import java.util.LinkedList;
import java.util.logging.Logger;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.io.IOException;

import com.jasonliu.app.wikirace.Constants;

public class WikiPage {
	private static final Logger logger = Logger.getLogger(Constants.LOGGER);

	static String wikiBaseUrl = "https://en.wikipedia.org";
	static String wikiSubPath = "/wiki/";
	static String aTagsRegex = "^/wiki/(?!(?:File:|Special:|Template:|Template_talk:|Wikipedia:|Help:)).*";

	private static String getUrl(String identifier) {
		return wikiBaseUrl + wikiSubPath + identifier;
	}

	public static void exists(String page) throws IOException {
		String url = getUrl(page);
		Connection connection = Jsoup.connect(url);
		connection.method(Connection.Method.GET);
		connection.execute();
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

			Elements notes = body.select("div[role=note]");
			for (Element note : notes) {
				note.remove();
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
			logger.severe(e.getMessage());
		}

		return links;
	}
}