public class WikiTree {
  String startNode;
  String targetNode;
  int level = 0;

  public WikiTree(String start, String finish) {
    startNode = start;
    targetNode = finish;
  }

  public String Search() {
    System.out.println("Going through " + startNode + "'s links:");
    level += 1;

    WikiPage startPage = new WikiPage(startNode);
    for (String link : startPage.getLinks()) {
      if (link.equals(targetNode)) {
        return startNode + " and " + targetNode + " are " + String.valueOf(level) + " links away";
      }
    }

    // System.out.println("Printing out " + targetNode + "'s links:");
    // WikiPage targetPage = new WikiPage(targetNode);
    // for (String link : targetPage.GetLinks()) {
    //   System.out.println(link);
    // }

    return "";
  }
}
