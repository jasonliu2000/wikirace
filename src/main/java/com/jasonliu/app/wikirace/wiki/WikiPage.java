package com.jasonliu.app.wikirace.wiki;

import java.util.LinkedList;
import java.util.logging.Logger;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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

	public static void exists(String page) throws ResponseStatusException {
		String url = getUrl(page);
		Connection connection = Jsoup.connect(url);
		connection.method(Connection.Method.GET);

		try {
			connection.execute();
		} catch (HttpStatusException e) {
			logger.severe(e.getMessage());

			int statusCode = e.getStatusCode();
			if (statusCode == 404) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The provided Wikipedia article '%s' does not exist. Please try again.", page));
			} else if (statusCode >= 500) {
				throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "The upstream Wikipedia server failed. Please try again.");
			} else {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The server failed to verify that the Wikipedia article exist. Please file a ticket for a fix.");
			}
		} catch (IOException e) {
			logger.severe(e.getMessage());
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Network error. Please check your internet connection and try again.");
	  } catch (Exception e) {
			logger.severe(e.toString());
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error. Please try again later and file a ticket for a fix if the issue persists.");
		}
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