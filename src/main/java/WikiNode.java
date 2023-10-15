import java.util.LinkedList;

public class WikiNode {
  String name;
  LinkedList<WikiNode> neighbors = new LinkedList<WikiNode>();

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

  void addNeighbors() {
    LinkedList<String> links = WikiPage.getLinks(name);
    links.forEach(l -> {
      WikiNode neighborNode = new WikiNode(l, pathToNode);
      this.neighbors.add(neighborNode);
    });
  }
}
