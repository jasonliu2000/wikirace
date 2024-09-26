package com.jasonliu.app.wikirace.wiki;
import java.util.logging.Logger;

import com.jasonliu.app.wikirace.Constants;

public class Search extends Thread {
  private static final Logger logger = Logger.getLogger(Constants.LOGGER);
  String start;
  String destination;
  
  Search(String start, String destination) {
    this.start = start;
    this.destination = destination;
  }

  public void run() {
    logger.info(String.format("Thread %s is running", Thread.currentThread().getId()));
    WikiGraph graph = new WikiGraph(start, destination);
  }
}
