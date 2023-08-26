public class WikiTree {
  String startNode;
  String targetNode;

  public WikiTree(String start, String finish) {
    startNode = start;
    targetNode = finish;
  }

  public String Search() {
    return "We want to go from wiki page " + startNode + " to wiki page " + targetNode;
  }
}
