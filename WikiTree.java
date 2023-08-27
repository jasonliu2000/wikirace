import java.util.LinkedList;

public class WikiTree {
  String currentLink;
  String targetLink;
  int level = 0;

  LinkedList<String> backlog = new LinkedList<String>();

  public WikiTree(String start, String finish) {
    // TODO: assert that we have a start and finish
    backlog.add(start);
    // currentLink = start;
    targetLink = finish;
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
        return currentLink + " and " + targetLink + " are " + String.valueOf(level) + " links away";
      }

      WikiPage currentPage = new WikiPage(currentLink);

      // Add all links in current page to backlog - to be checked
      backlog.addAll(currentPage.getLinks());
      System.out.println("    Added " + String.valueOf(currentPage.getLinks().size()) + " links to backlog from wiki page " + currentLink);
    }

    level += 1;

    return Search();
  }
}
