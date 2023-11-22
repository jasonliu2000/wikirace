public class WikiRace {

  public static void main(String[] args) {
    if (args.length != 2) {
      throw new IllegalArgumentException(Constants.EXPECT_TWO_ARGUMENTS);
    }

    if (!WikiPage.exists(args[0])) {
      throw new IllegalArgumentException(Constants.REQUIRE_VALID_START);
    }

    if (!WikiPage.exists(args[1])) {
      throw new IllegalArgumentException(Constants.REQUIRE_VALID_DESTINATION);
    }

    String start = args[0];
    String destination = args[1];

    System.out.println("We want to go from wiki page " + start + " to wiki page " + destination);

    WikiGraph graph = new WikiGraph(start, destination);
    System.out.println(graph.search());
  }
}