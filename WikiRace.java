import java.util.Scanner;

public class WikiRace {

  public static void main(String[] args) {
    if (args.length != 2) {
      throw new IllegalArgumentException("Invalid number of arguments. Expected 2 (First is starting page and second is the desired page)");
    }

    String start = args[0];
    String finish = args[1];

    System.out.println("We want to go from wiki page " + start + " to wiki page " + finish);

    WikiTree tree = new WikiTree(start, finish);
    System.out.println(tree.search());
  }
}