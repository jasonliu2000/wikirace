package com.jasonliu.app.wikirace.wiki;
import java.util.logging.Logger;

import com.jasonliu.app.wikirace.Constants;

import java.util.HashSet;
import java.util.LinkedList;

public class WikiGraph {
  private static final Logger logger = Logger.getLogger(Constants.LOGGER);
  private static HashSet<String> history = new HashSet<String>();
  private static LinkedList<WikiNode> queue = new LinkedList<WikiNode>();
  private static HashSet<String> hints = new HashSet<String>();
  
  long startTime;
  String startingPage;
  String targetPage;
  WikiNode currentNode;

  int level = 0;

  public WikiGraph(String start, String finish) {
    startingPage = start;
    targetPage = finish;
    WikiNode startNode = new WikiNode(startingPage);
    addNodeToQueue(startNode);
    // search();
  }

  static boolean addedBefore(String link) {
    return history.contains(link);
  }

  static int queueSize() {
    return queue.size();
  }

  static String printQueue() {
    LinkedList<String> names = new LinkedList<String>();
    for (WikiNode n : queue) {
      names.add(n.name);
    }

    return names.toString();
  }

  static void addNodeToQueue(WikiNode node) {
    if (hints.contains(node.name)) {
      queue.addFirst(node);
    } else {
      queue.add(node);
    }
    history.add(node.name);
  }

  static WikiNode popNode() {
    return queue.pop();
  }

  // public String search() {
  //   if (level == 1) {
  //     LinkedList<String> targetLinks = WikiPage.getLinks(targetPage);
  //     for (String link : targetLinks) {
  //       hints.add(link);
  //     }
  //   }

  //   startTime = System.currentTimeMillis();
  //   logger.info("!!!!! LEVEL = " + String.valueOf(level) + " !!!!!");

  //   int currentQueueSize = queueSize();
  //   logger.info("Current queue size: " + currentQueueSize);
  //   logger.info("Items in queue: " + printQueue());

  //   for (int i = 0; i < currentQueueSize; i++) {
  //     currentNode = popNode();
  //     if (currentNode.name.equals(targetPage)) {
  //       logger.info(successMessage(currentNode.pathToNode));
  //       return successMessage(currentNode.pathToNode);
  //     }

  //     currentNode.addNeighborsToBacklog();
  //   }

  //   level += 1;
  //   return search();
  // }

  String successMessage(LinkedList<String> visitedNodes) {
    String time = String.format("Time taken: %s ms", System.currentTimeMillis() - startTime);

    String linkString = (level == 1) ? "link" : "links";
    String message = String.format("%s and %s are %s %s away", startingPage, targetPage, String.valueOf(level), linkString);

    String path = "Path taken to reach target wiki page: ";
    String[] pagesVisited = new String[visitedNodes.size()];
    for (int i = 0; i < pagesVisited.length; i++) {
      if (i < pagesVisited.length - 1) {
        path += visitedNodes.pop() + " -> ";
      } else {
        path += visitedNodes.pop();
      }
    }

    return String.format("%s\n%s\n%s", time, message, path);
  }
}
