import java.util.LinkedList;

public class WikiTree {
  long startTime;
  String startingPage;
  String targetPage;
  WikiNode currentNode;

  int level = 0;

  public WikiTree(String start, String finish) {
    startTime = System.currentTimeMillis();
    startingPage = start;
    targetPage = finish;

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

      if (currentNode.name.equals(targetPage)) {
        return successMessage(currentNode.pathToNode);
      }

      currentNode.addNeighbors();

      int backlogChange = 0;
      for (WikiNode n : currentNode.neighbors) {
        if (!Backlog.addedBefore(n.name)) {
          Backlog.add(n);
          backlogChange++;
        }
      }

      System.out.println("    Added " + String.valueOf(backlogChange) + " links to backlog from wiki page " + currentNode.name);
    }

    level += 1;
    return search();
  }

  private String successMessage(LinkedList<String> path) {
    String time = String.format("Time taken: %s ms", System.currentTimeMillis() - startTime);

    String linkString = (level == 1) ? "link" : "links";
    String message = String.format("%s and %s are %s %s away", startingPage, targetPage, String.valueOf(level), linkString);

    return String.format("%s\n%s\n%s", time, message, path);
  }
}
