package com.jasonliu.app.wikirace.wiki;
import java.util.logging.Logger;

import com.jasonliu.app.wikirace.Constants;

public class Search implements Runnable {
  private static final Logger logger = Logger.getLogger(Constants.LOGGER);
  WikiNode wikiNode;
  String target;
  
  Search(WikiNode wikiNode, String target) {
    logger.info(String.format("Search obj initiated for: %s", wikiNode.name));
    this.wikiNode = wikiNode;
    this.target = target;
  }

  public void run() {
    try {
      
      logger.info(String.format("Thread %s is running for wiki article %s", Thread.currentThread().getId(), wikiNode.name));
      Thread.sleep(500);
      logger.info(String.format("ADDING article %s to visited", wikiNode.name));
      if (wikiNode.name == "Romania") {
        WikiRace.addNode(new WikiNode("Australia"));
        logger.info("aus added");
      }

      if (wikiNode.name.equals(target)) {
        logger.info("found target");
        WikiRace.targetFound();
      }

      WikiRace.addNodeToVisited(wikiNode);
    } catch (InterruptedException e) {
        logger.info(String.format("EXCEPTION: %s", wikiNode.name));
    }
    // WikiPage.getLinks(...)
  }
}
