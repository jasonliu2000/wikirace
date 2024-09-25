package com.jasonliu.app.wikirace.wiki;
import java.util.logging.Logger;

import com.jasonliu.app.wikirace.Constants;

import java.util.LinkedList;

public class WikiNode {
  String name;

  private static final Logger logger = Logger.getLogger(Constants.LOGGER);

  // TODO: look into whether ArrayList might be better
  LinkedList<String> pathToNode = new LinkedList<String>();

  WikiNode(String link) {
    name = link;
    pathToNode.add(name);
  }

  WikiNode(String link, LinkedList<String> pathUpToNode) {
    name = link;
    pathToNode.addAll(pathUpToNode);
    pathToNode.add(name);
  }

  void addNeighborsToBacklog() {
    LinkedList<String> links = WikiPage.getLinks(name);

    int backlogChange = 0;
    for (String l : links) {
      if (!WikiGraph.addedBefore(l)) {
        WikiNode neighborNode = new WikiNode(l, pathToNode);
        WikiGraph.addNode(neighborNode);
        backlogChange++;
      }
    }

    logger.info("    Added " + String.valueOf(backlogChange) + " links to backlog from wiki page " + name);
  }
}
