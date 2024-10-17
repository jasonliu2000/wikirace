package com.jasonliu.app.wikirace.wiki;

import com.jasonliu.app.wikirace.Constants;
import com.jasonliu.app.wikirace.Constants.WikiraceStatus;

import java.util.logging.Logger;
import java.util.LinkedList;

public class Search implements Runnable {
  private static final Logger logger = Logger.getLogger(Constants.LOGGER);
  WikiNode wikiNode;
  String target;
  
  Search(WikiNode wikiNode, String target) {
    this.wikiNode = wikiNode;
    this.target = target;
  }

  public void run() {
    if (!(WikiRace.getStatus() == WikiraceStatus.COMPLETED)) {
      logger.info(String.format("Thread %s is running for wiki article %s (via path: %s)", Thread.currentThread().getId(), wikiNode.name, wikiNode.pathToNode.toString()));

      LinkedList<String> linksInArticle = WikiPage.getLinks(wikiNode.name);
      for (String link : linksInArticle) {
        WikiNode childNode = new WikiNode(link, wikiNode.pathToNode);
        if (link.equals(target)) {
          logger.info("Wikipedia target article has been found");
          String[] pathToTarget = childNode.pathToNode.toArray(new String[childNode.pathToNode.size()]);
          WikiRace.targetFound(pathToTarget);
          break;
        } else {
          WikiRace.addNodeToQueue(childNode);
        }
      }
    }
  }
}
