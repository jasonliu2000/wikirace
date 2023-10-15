import java.util.LinkedHashSet;
import java.util.LinkedList;

/*
 * This is a singleton class containing the backlog of nodes to visit and a
 * history of nodes (by name) that have been added to our backlog before
 */
class Backlog {
  private static LinkedHashSet<String> history = new LinkedHashSet<String>();
  private static LinkedList<WikiNode> backlog = new LinkedList<WikiNode>();

  private Backlog() {}

  static boolean addedBefore(String link) {
    return history.contains(link);
  }

  static int size() {
    return backlog.size();
  }

  static String printBacklog() {
    LinkedList<String> names = new LinkedList<String>();
    for (WikiNode n : backlog) {
      names.add(n.name);
    }

    return names.toString();
  }

  static void add(WikiNode node) {
    backlog.add(node);
    history.add(node.name);
  }

  static WikiNode pop() {
    return backlog.pop();
  }
}
