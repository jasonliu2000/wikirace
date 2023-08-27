import java.util.LinkedHashSet;

/*
 * This is a singleton class containing a history of links that have been added to our backlog
 * This is used to check whether a link has already been added to our backlog, or if we have already visited the link before
 */
class BacklogHistory {
  private static LinkedHashSet<String> history = new LinkedHashSet<String>();

  private BacklogHistory() {}

  static boolean contains(String link) {
    return history.contains(link);
  }

  static void add(String link) {
    history.add(link);
  }
}
