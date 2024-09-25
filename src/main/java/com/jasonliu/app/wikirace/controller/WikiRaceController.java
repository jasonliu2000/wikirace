package com.jasonliu.app.wikirace.controller;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.jasonliu.app.wikirace.wiki.WikiRace;

@RestController
public class WikiRaceController {
	// private static final Logger logger = Logger.getLogger("WikiRaceGlobalLogger");
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/ping")
	public Ping ping() {
		return new Ping(counter.incrementAndGet(), "pong");
	}

	@PostMapping("/wikirace")
	public String startWikirace(@RequestParam String start, @RequestParam String destination) {
		try {
			// logger.info("it's starting...");
			new WikiRace(start, destination);
			// logger.info("it's started...");
			return "success";
			
		} catch (Exception e) {
			throw new ArticleNotFoundException();
		}
	}

	@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Wikipedia article not found") // TODO: refactor to get reason to show up in http response
	public class ArticleNotFoundException extends RuntimeException {}

}