import java.util.HashSet;
import java.util.LinkedList;

public class WikiGraph {
  private static HashSet<String> history = new HashSet<String>();
  private static LinkedList<WikiNode> queue = new LinkedList<WikiNode>();
  
  long startTime;
  String startingPage;
  String destinationPage;
  WikiNode currentNode;

  int level = 0;

  public WikiGraph(String start, String finish) {
    
    startingPage = start;
    destinationPage = finish;

    WikiNode startNode = new WikiNode(startingPage);
    addNode(startNode);
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

  static void addNode(WikiNode node) {
    queue.add(node);
    history.add(node.name);
  }

  static WikiNode popNode() {
    return queue.pop();
  }

  public String search() {
    startTime = System.currentTimeMillis();
    System.out.println("!!!!! LEVEL = " + String.valueOf(level) + " !!!!!");

    int currentBacklogSize = queueSize();
    System.out.println("Current backlog size: " + currentBacklogSize);
    System.out.println("Backlog items: " + printQueue());

    for (int i = 0; i < currentBacklogSize; i++) {
      currentNode = popNode();
      System.out.println(" -- Current wiki page: " + currentNode.name);

      if (currentNode.name.equals(destinationPage)) {
        return successMessage(currentNode.pathToNode);
      }

      currentNode.addNeighborsToBacklog();
    }

    level += 1;
    return search();
  }

  String successMessage(LinkedList<String> visitedNodes) {
    String time = String.format("Time taken: %s ms", System.currentTimeMillis() - startTime);

    String linkString = (level == 1) ? "link" : "links";
    String message = String.format("%s and %s are %s %s away", startingPage, destinationPage, String.valueOf(level), linkString);

    String path = "Path taken to reach destination wiki page: ";
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
