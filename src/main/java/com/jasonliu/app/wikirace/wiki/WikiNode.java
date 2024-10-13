package com.jasonliu.app.wikirace.wiki;

import java.util.LinkedList;

public class WikiNode {
  String name;
  LinkedList<WikiNode> childNodes = new LinkedList<WikiNode>();

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

  // May not be used, but keep for now
  public LinkedList<WikiNode> getChildNodes() {
    if (childNodes.size() > 0) {
      return childNodes;
    }

    LinkedList<String> links = WikiPage.getLinks(name);
    for (String link : links) {
      childNodes.add(new WikiNode(link, pathToNode));
    }

    return childNodes;
  }
}
