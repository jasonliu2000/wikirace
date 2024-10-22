package com.jasonliu.app.wikirace.controller;
import java.net.URI;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.jasonliu.app.wikirace.Constants;
import com.jasonliu.app.wikirace.Constants.WikiraceStatus;
import com.jasonliu.app.wikirace.wiki.WikiPage;
import com.jasonliu.app.wikirace.wiki.WikiRace;

@RestController
public class WikiRaceController {
	private static final Logger logger = Logger.getLogger("WikiRaceGlobalLogger");
	private final AtomicLong pongCounter = new AtomicLong();
	private final AtomicLong wikiraceJobId = new AtomicLong();
	private static WikiRace wikirace;

	@GetMapping("/ping")
	public Ping ping() {
		return new Ping(pongCounter.incrementAndGet(), "pong");
	}

	@GetMapping("/wikirace/status")
	public Status getWikiraceStatus() {
		// consider if we want to set the status back to NOT_STARTED after a completed wikirace
		switch (WikiRace.getStatus()) {
			case NOT_STARTED:
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wikirace has not started yet.");
			case FAILED:
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Wikirace failed. Please try again.");
			case IN_PROGRESS:
				return new Status("Wikirace is in progress.", 
													"",
													new String[]{});
			case COMPLETED:
				String duration = WikiRace.getTimeDuration();
				String[] path = WikiRace.getPathToTarget();
				return new Status("Wikirace has completed.",
													duration,
													path);
			default:
				throw new WikiraceException();
		}
	}

	@PostMapping("/wikirace")
	public ResponseEntity<String> startWikirace(@RequestParam String start, @RequestParam String target) {
		if (start.isEmpty() || !WikiPage.exists(start)) {
			logger.severe(Constants.REQUIRE_VALID_START);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The provided starting Wikipedia article does not exist. Please try again.");
    }

		if (start.equals(target)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please provide a target Wikipedia article different from the starting one,");
		}

		if (target.isEmpty() || !WikiPage.exists(target)) {
			logger.severe(Constants.REQUIRE_VALID_TARGET);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The provided target Wikipedia article does not exist. Please try again.");
    }

		try {
			wikirace = WikiRace.initiate(start, target);
			wikirace.start();

			URI location = new URI(String.format("/wikirace/%s", wikiraceJobId.incrementAndGet())); // this can throw an exception
			HttpHeaders responseHeaders = new HttpHeaders();
   		responseHeaders.setLocation(location);
			return new ResponseEntity<String>("Request accepted. Starting the wikirace now.", responseHeaders, HttpStatus.ACCEPTED);
			
		} catch (Exception e) {
			logger.severe(e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to start wikirace. Please try again.");
		}
	}

	@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Wikipedia article not found.") // TODO: refactor to get reason to show up in http response
	public class ArticleNotFoundException extends RuntimeException {}

	public class WikiraceException extends RuntimeException {}
}