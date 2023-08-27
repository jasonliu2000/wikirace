import java.util.LinkedList;

public class WikiTree {
  String startLink;
  String targetLink;
  String currentLink;
  long startTime;

  int level = 0;
  LinkedList<String> backlog = new LinkedList<String>();

  public WikiTree(String start, String finish) {
    // TODO: assert that we have a start and finish
    startLink = start;
    targetLink = finish;

    backlog.add(start);
    startTime = System.currentTimeMillis();
  }

  public String Search() {
    System.out.println("!!!!! LEVEL = " + String.valueOf(level) + " !!!!!");

    int currentBacklogSize = backlog.size();
    System.out.println("Current backlog size: " + currentBacklogSize);
    System.out.println("Backlog items: " + backlog.toString());

    for (int i = 0; i < currentBacklogSize; i++) {
      String currentLink = backlog.pop();
      System.out.println(" -- Current link: " + currentLink);

      if (currentLink.equals(targetLink)) {
        System.out.println(String.format("Time taken: %s ms", System.currentTimeMillis() - startTime));
        return SuccessMessage();
      }

      WikiPage currentPage = new WikiPage(currentLink);

      // Add all links in current page to backlog - to be checked
      backlog.addAll(currentPage.getLinks());
      System.out.println("    Added " + String.valueOf(currentPage.getLinks().size()) + " links to backlog from wiki page " + currentLink);
    }

    level += 1;

    return Search();
  }

  String SuccessMessage() {
    String linkString = (level == 1) ? "link" : "links";
    return String.format("%s and %s are %s %s away", startLink, targetLink, String.valueOf(level), linkString);
  }
}
