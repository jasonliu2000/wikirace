package rest;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/ping")
	public Ping ping() {
		return new Ping(counter.incrementAndGet(), "pong");
	}

	@PostMapping("/wikirace")
	public String startWikirace(@RequestParam String start, @RequestParam String destination) {
		return "start: " + start + "\n" + "destination: " + destination;
	}
}