package com.jasonliu.app.wikirace.wiki;

import java.util.LinkedList;

public class WikiNode {
  String name;
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
}
