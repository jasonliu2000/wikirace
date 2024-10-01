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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.jasonliu.app.wikirace.Constants.WikiraceStatus;
import com.jasonliu.app.wikirace.wiki.WikiRace;

@RestController
public class WikiRaceController {
	private static final Logger logger = Logger.getLogger("WikiRaceGlobalLogger");
	private final AtomicLong counter = new AtomicLong();
	private static WikiRace wikirace;

	@GetMapping("/ping")
	public Ping ping() {
		return new Ping(counter.incrementAndGet(), "pong");
	}

	@GetMapping("/wikirace/status/{counter}")
	public Status getWikiraceStatus(@PathVariable int counter) {
		WikiraceStatus testStatus = WikiraceStatus.FAILED;
		switch (testStatus) {
			case IN_PROGRESS:
				return new Status(counter, 
													"Wikirace is in progress.", 
													"",
													new String[]{});
			case COMPLETED:
				return new Status(counter,
													"Wikirace has completed.",
													"16000ms",
													new String[]{"Wikiracing", "United_States", "Virginia"});
			default:
				throw new WikiraceException();
		}
	}

	@PostMapping("/wikirace")
	public ResponseEntity<String> startWikirace(@RequestParam String start, @RequestParam String destination) {
		try {
			wikirace = WikiRace.initiate(start, destination);
			wikirace.start();

			URI location = new URI("/wikirace"); // this can throw an exception
			HttpHeaders responseHeaders = new HttpHeaders();
   		responseHeaders.setLocation(location);
			return new ResponseEntity<String>("Request accepted. Starting the wikirace now.", responseHeaders, HttpStatus.ACCEPTED);
			
		} catch (Exception e) {
			e.printStackTrace();
			// switch case for possible exceptions
			throw new ArticleNotFoundException();
		}
	}

	@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Wikipedia article not found.") // TODO: refactor to get reason to show up in http response
	public class ArticleNotFoundException extends RuntimeException {}

	public class WikiraceException extends RuntimeException {}

}