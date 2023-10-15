public class WikiRace {

  public static void main(String[] args) {
    if (args.length != 2) {
      throw new IllegalArgumentException("Invalid number of arguments. Expected 2 (First is starting page and second is the desired page)");
    }

    String start = args[0];
    String destination = args[1];

    System.out.println("We want to go from wiki page " + start + " to wiki page " + destination);

    WikiGraph graph = new WikiGraph(start, destination);
    System.out.println(graph.search());
  }
}