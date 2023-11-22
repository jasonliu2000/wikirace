import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.FileHandler;

public class WikiRace {
	private static final Logger logger = Logger.getLogger(Constants.LOGGER);
	private static FileHandler fileHandler;
	
  public static void main(String[] args) {
		try {
			FileHandler fileHandler = new FileHandler("wikirace.log");
			fileHandler.setFormatter(new SimpleFormatter());
			logger.addHandler(fileHandler);
		} catch (Exception e) {
			logger.warning("Failed to setup log file");
		}

    if (args.length != 2) {
			logger.severe(Constants.EXPECT_TWO_ARGUMENTS);
      throw new IllegalArgumentException(Constants.EXPECT_TWO_ARGUMENTS);
    }

    if (!WikiPage.exists(args[0])) {
			logger.severe(Constants.REQUIRE_VALID_START);
      throw new IllegalArgumentException(Constants.REQUIRE_VALID_START);
    }

    if (!WikiPage.exists(args[1])) {
			logger.severe(Constants.REQUIRE_VALID_DESTINATION);
      throw new IllegalArgumentException(Constants.REQUIRE_VALID_DESTINATION);
    }

    String start = args[0];
    String destination = args[1];

		logger.info(String.format("We want to go from wiki page %s to wiki page %s", start, destination));
    WikiGraph graph = new WikiGraph(start, destination);
    logger.info(graph.search());

		if (fileHandler != null) {
			fileHandler.close();
		}
  }
}