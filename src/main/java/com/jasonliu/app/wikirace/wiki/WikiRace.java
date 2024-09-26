package com.jasonliu.app.wikirace.wiki;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.jasonliu.app.wikirace.Constants;

import java.util.logging.FileHandler;

public class WikiRace {
	private static final Logger logger = Logger.getLogger(Constants.LOGGER);
	private static FileHandler fileHandler;
	
  public WikiRace(String start, String destination) {
		try {
			FileHandler fileHandler = new FileHandler("wikirace.log");
			fileHandler.setFormatter(new SimpleFormatter());
			logger.addHandler(fileHandler);
		} catch (Exception e) {
			logger.warning("Failed to setup log file");
		}

    if (!WikiPage.exists(start)) {
			logger.severe(Constants.REQUIRE_VALID_START);
      throw new IllegalArgumentException(Constants.REQUIRE_VALID_START);
    }

		if (start == destination) {
			// return early
		}

    if (!WikiPage.exists(destination)) {
			logger.severe(Constants.REQUIRE_VALID_DESTINATION);
      throw new IllegalArgumentException(Constants.REQUIRE_VALID_DESTINATION);
    }

		logger.info(String.format("We want to go from wiki page %s to wiki page %s", start, destination));
    Search thread = new Search(start, destination);
		thread.start();

		if (fileHandler != null) {
			fileHandler.close();
		}
  }
}