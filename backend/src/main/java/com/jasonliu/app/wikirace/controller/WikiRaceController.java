package com.jasonliu.app.wikirace.controller;

import java.net.URI;
import java.io.IOException;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.FileHandler;
import java.util.Map;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.apache.catalina.connector.Response;
import org.jsoup.HttpStatusException;

import com.jasonliu.app.wikirace.Constants;
import com.jasonliu.app.wikirace.wiki.WikiPage;
import com.jasonliu.app.wikirace.wiki.WikiRace;
import com.jasonliu.app.wikirace.model.WikiRaceModel;
import com.jasonliu.app.wikirace.dto.WikiRaceAttempt;
import com.jasonliu.app.wikirace.dto.Ping;
import com.jasonliu.app.wikirace.dto.Status;

@RestController
@RequestMapping("/api")
public class WikiRaceController {
	private static final Logger logger = Logger.getLogger("WikiRaceGlobalLogger");
	private final AtomicLong pongCounter = new AtomicLong();

	WikiRaceController() {
		try {
			FileHandler fileHandler = new FileHandler(Constants.LOG_FILENAME);
			fileHandler.setFormatter(new SimpleFormatter());
			logger.addHandler(fileHandler);
		} catch (Exception e) {
			logger.warning("Failed to setup log file");
		}
	}

	@GetMapping("/ping")
	public Ping ping() {
		logger.info(String.format("Ping endpoint called %s times", pongCounter.incrementAndGet()));
		return new Ping(pongCounter.get(), "pong");
	}

	@GetMapping("/wikiraces")
	public ResponseEntity<ArrayList<Status>> listStatuses() {

		ArrayList<Status> statuses = new ArrayList<Status>();

		for (Map.Entry<Long, WikiRaceModel> entry : WikiRaceModel.getWikiRaces().entrySet()) {
			WikiRaceModel wikiRace = entry.getValue();
			statuses.add(wikiRace.getAPIStatus());
		}

		return ResponseEntity.ok(statuses);
	}

	@GetMapping("/wikiraces/{wikiraceId}")
	public Status getStatus(@PathVariable long wikiraceId) {

		if (!WikiRaceModel.doesWikiRaceWithIdExist(wikiraceId)) {
			logger.severe(String.format("Wikirace with id %s not found.", wikiraceId));
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, Constants.NOT_FOUND_MSG);
		}

		logger.fine(String.format("Getting status of wikirace with id: %s", wikiraceId));
		WikiRaceModel wikiRace = WikiRaceModel.getWikiRaces().get(wikiraceId);
		
		switch (wikiRace.getStatus()) {
			case NOT_STARTED:
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constants.NOT_STARTED_MSG);
			case IN_PROGRESS:
				return wikiRace.getAPIStatus();
			case COMPLETED:
				return wikiRace.getAPIStatus();
			default:
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, Constants.FAILED_MSG);
		}
	}

	@PostMapping("/wikirace")
	public ResponseEntity<String> startWikiRace(@RequestBody WikiRaceAttempt attempt) {
		String start = attempt.getStart();
		String target = attempt.getTarget();

		logger.info(String.format("Wikirace attempted with '%s' as the starting article and '%s' as the target article", start, target));
		validateWikiArticles(start, target);

		WikiRaceModel model = new WikiRaceModel(start, target);
		try {
			WikiRace wikiRace = new WikiRace(model);
			wikiRace.start();
			
			URI location = new URI(String.format("/wikiraces/%s", model.getId()));

			HttpHeaders responseHeaders = new HttpHeaders();
   		responseHeaders.setLocation(location);
			return new ResponseEntity<String>("Request accepted. Starting the wikirace now.", responseHeaders, HttpStatus.ACCEPTED);
			
		} catch (Exception e) {
			logger.severe(e.getMessage());

			if (model != null) {
				WikiRaceModel.purgeWikiRace(model.getId());
			}
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to start wikirace. Please try again.");
		}
	}

	private static void throwExceptionIfArticleDoesNotExist(String article) throws ResponseStatusException {

		try {
			WikiPage.exists(article);
		} catch (HttpStatusException e) {
				logger.severe(e.getMessage());
	
				int statusCode = e.getStatusCode();
				if (statusCode == 404) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The provided Wikipedia article '%s' does not exist. Please try again.", article));
				} else if (statusCode >= 500 || statusCode == 429) {
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

	private static void validateWikiArticles(String start, String target) throws ResponseStatusException {

		if (start.isEmpty()) {
			logger.severe("A starting Wikipedia article was not provided in the query params");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constants.REQUIRE_STARTING_ARTICLE);
		}
		if (target.isEmpty()) {
			logger.severe("A target Wikipedia article was not provided in the query params");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constants.REQUIRE_TARGET_ARTICLE);
		}

		throwExceptionIfArticleDoesNotExist(start);

		if (target.equals(start)) {
			logger.severe("Start and target articles were the same");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Constants.REQUIRE_UNIQUE_ARGUMENTS);
		}

		throwExceptionIfArticleDoesNotExist(target);

		logger.info("Start and target wiki articles have been validated");
	}

}