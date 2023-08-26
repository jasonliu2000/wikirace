import java.util.Scanner;

public class WikiRace {

  public static void main(String[] args) {
    String input = "Wikiracing United_States";
    Scanner sc = new Scanner(input);
    // Scanner sc = new Scanner(System.in);

    String start = sc.next();
    String finish = sc.next();
    sc.close();

    WikiTree tree = new WikiTree(start, finish);
    System.out.println(tree.Search());
  }
}