import java.util.LinkedList;

public class WikiGraph {
  long startTime;
  String startingPage;
  String destinationPage;
  WikiNode currentNode;

  int level = 0;

  public WikiGraph(String start, String finish) {
    startTime = System.currentTimeMillis();
    startingPage = start;
    destinationPage = finish;

    WikiNode startNode = new WikiNode(startingPage);
    Backlog.add(startNode);
  }

  public String search() {
    System.out.println("!!!!! LEVEL = " + String.valueOf(level) + " !!!!!");

    int currentBacklogSize = Backlog.size();
    System.out.println("Current backlog size: " + currentBacklogSize);
    System.out.println("Backlog items: " + Backlog.printBacklog());

    for (int i = 0; i < currentBacklogSize; i++) {
      currentNode = Backlog.pop();
      System.out.println(" -- Current wiki page: " + currentNode.name);

      if (currentNode.name.equals(destinationPage)) {
        return successMessage(currentNode.pathToNode);
      }

      currentNode.addNeighborsToBacklog();
    }

    level += 1;
    return search();
  }

  private String successMessage(LinkedList<String> visitedNodes) {
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
