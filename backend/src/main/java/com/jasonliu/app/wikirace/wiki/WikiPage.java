package com.jasonliu.app.wikirace.wiki;

import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Logger;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.jsoup.HttpStatusException;

import java.io.IOException;

import com.jasonliu.app.wikirace.Constants;

public class WikiPage {
	private static final Logger logger = Logger.getLogger(Constants.LOGGER);

	static String wikiBaseUrl = "https://en.wikipedia.org";
	static String wikiSubPath = "/wiki/";
	static String aTagsRegex = "^/wiki/(?!(?:.*:)).*";

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
		int attempts = 0, maxRetries = 3, retryDelay = 0;
		boolean exit = false;
		Connection.Response response;
		
		while (attempts < maxRetries) {
			try {
				Thread.sleep(retryDelay);
				response = Jsoup.connect(url).ignoreHttpErrors(true).execute();

				if (response.statusCode() == 429) {
					logger.severe(String.format("Received 429 error from server after attempting to get links from article %s (attempt %s)", page, Integer.toString(attempts)+1));
					retryDelay = Integer.parseInt(response.headers().get("retry-after")) * 1000;
					attempts++;

				} else if (response.statusCode() != 200) {
					throw new HttpStatusException(response.statusMessage(), response.statusCode(), url);

				} else {
					Element body = response.parse()
												.body()
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

					if (attempts > 0) {
						logger.info(String.format("Got links from wiki article %s after previous 429 errors", page));
					}

					break;
				}
			} catch(Exception e) {
				logger.severe(String.format("error here: %s", e.getMessage()));
				break;
			}
		} 

		return links;
	}
}