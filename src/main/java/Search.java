import java.util.logging.Logger;

public class Search extends Thread {
  private static final Logger logger = Logger.getLogger(Constants.LOGGER);
  String start;
  String destination;
  
  Search(String start, String destination) {
    this.start = start;
    this.destination = destination;
  }

  public void run() {
    WikiGraph graph = new WikiGraph(start, destination);
    logger.info(graph.search());
  }
}
