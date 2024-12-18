package com.jasonliu.app.wikirace.wiki;

import com.jasonliu.app.wikirace.Constants;
import com.jasonliu.app.wikirace.Constants.WikiraceStatus;

import java.util.logging.Logger;
import java.util.LinkedList;

public class Search implements Runnable {
  private static final Logger logger = Logger.getLogger(Constants.LOGGER);
  WikiRace wikiRace;
  WikiNode wikiNode;
  String target;
  
  Search(WikiRace wikiRace, WikiNode wikiNode, String target) {
    this.wikiRace = wikiRace;
    this.wikiNode = wikiNode;
    this.target = target;
  }

  public void run() {
    if (!(wikiRace.isTargetFound())) {
      logger.info(String.format("Thread %s is running for wiki article %s (via path: %s)", Thread.currentThread().getId(), wikiNode.name, wikiNode.pathToNode.toString()));

      LinkedList<String> linksInArticle = WikiPage.getLinks(wikiNode.name);
      for (String link : linksInArticle) {
        WikiNode childNode = new WikiNode(link, wikiNode.pathToNode);
        if (link.equals(target)) {
          String[] pathToTarget = childNode.pathToNode.toArray(new String[childNode.pathToNode.size()]);
          wikiRace.targetFound(pathToTarget);
          break;
        } else {
          wikiRace.addNodeToQueue(childNode);
        }
      }
    }
  }
}
