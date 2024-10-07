package com.jasonliu.app.wikirace.wiki;

import com.jasonliu.app.wikirace.Constants;

import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.FileHandler;

import java.util.concurrent.*;

import java.util.HashSet;
import java.util.LinkedList;

public class WikiRace extends Thread {
	private static WikiRace wikirace;

	private static final Logger logger = Logger.getLogger(Constants.LOGGER);
	private static FileHandler fileHandler;

	private long startTime;
	private static String startingPage;
  private static String targetPage;

	public static BlockingQueue<WikiNode> queue = new LinkedBlockingQueue<WikiNode>();
	// private static LinkedList<WikiNode> queue = new LinkedList<WikiNode>();
  private static HashSet<String> hints = new HashSet<String>();
	private static HashSet<String> history = new HashSet<String>();

	private static Boolean targetFound;
	private static ExecutorService executor;
	
  private WikiRace(String start, String target) {
		try {
			FileHandler fileHandler = new FileHandler("wikirace.log");
			fileHandler.setFormatter(new SimpleFormatter());
			logger.addHandler(fileHandler);
		} catch (Exception e) {
			logger.warning("Failed to setup log file");
		}

    if (!WikiPage.exists(start)) {
			logger.severe(Constants.REQUIRE_VALID_START);
      throw new IllegalArgumentException(Constants.REQUIRE_VALID_START);
    }

		if (start == target) {
			// TODO: look into returning API response early?
		}

    if (!WikiPage.exists(target)) {
			logger.severe(Constants.REQUIRE_VALID_TARGET);
      throw new IllegalArgumentException(Constants.REQUIRE_VALID_TARGET);
    }

		startingPage = start;
		targetPage = target;
		targetFound = false;
  }

	public static WikiRace initiate(String start, String target) {
		wikirace = new WikiRace(start, target);
		return wikirace;
	}

	public void run() {
		startTime = System.currentTimeMillis();
		logger.info(String.format("We want to go from wiki page %s to wiki page %s", startingPage, targetPage));

		WikiNode startNode = new WikiNode(startingPage);
		addNode(startNode);
		addNode(new WikiNode("United_States"));
		addNode(new WikiNode("United_Kingdom"));
		addNode(new WikiNode("Greece"));
		addNode(new WikiNode("Argentina"));
		addNode(new WikiNode("France"));
		addNode(new WikiNode("Mexico"));
		addNode(new WikiNode("Peru"));
		addNode(new WikiNode("Denmark"));
		addNode(new WikiNode("Sweden"));
		addNode(new WikiNode("Turkey"));
		addNode(new WikiNode("Syria"));
		addNode(new WikiNode("Albania"));
		addNode(new WikiNode("Hungary"));
		addNode(new WikiNode("Romania"));

		logger.info(String.format("Initial queue size: %s", String.valueOf(queueSize())));
		executeWikirace();

		if (fileHandler != null) {
			fileHandler.close();
		}

		String time = String.format("Time taken: %s ms", System.currentTimeMillis() - startTime);
		logger.info(time);
	}

	// to delete
	// public static BlockingQueue<WikiNode> queueVisited = new LinkedBlockingQueue<WikiNode>();
	private static LinkedList<WikiNode> queueVisited = new LinkedList<WikiNode>();
	static synchronized void addNodeToVisited(WikiNode node) {
      queueVisited.add(node);
			logger.info(String.format("ADDED article %s to visited", node.name));
  }
	//

	private void executeWikirace() {
		executor = Executors.newSingleThreadExecutor(); //newFixedThreadPool(10); // Runtime.getRuntime().availableProcessors()
		
		while (true) {
			logger.info("looping...");
			logger.info(String.format("thread status: %s", Thread.currentThread().getState()));
			WikiNode node;
			try {
				node = queue.take();
				// if (node.name == "poison") {
				// 	logger.info("breaking out of while loop");
				// 	break;
				// }
				executor.execute(new Search(node));
			} catch (InterruptedException | RejectedExecutionException e) {
				if (e.getClass().getName() == RejectedExecutionException.class.getName()) {
					break;
				} else {
					logger.severe(e.getMessage());
				}
			}
			
			logger.info("looped");
		}
	}

	static synchronized int queueSize() {
    return queue.size();
  }

	static synchronized void addNode(WikiNode node) {
		try {
      queue.put(node);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
    // history.add(node.name);
  }

  // static synchronized WikiNode popNode() {
	// 	logger.info(String.format("pre-pop q: %s", queue));
  //   return queue.take();
  // }

	public static synchronized void targetFound() {
		targetFound = true;
		// addNode(new WikiNode("poison")); 
		
		// Thread.currentThread().interrupt();
		// logger.info(String.format("thread interrupted!! status: %s", Thread.currentThread().isInterrupted()));

		logger.info("THREAD INTERRUPTED");
		logger.info(String.valueOf(targetFound));
		logger.info(String.format("post while loop q size: %s", String.valueOf(queueSize())));

		executor.shutdown();
		try {
			executor.awaitTermination(0, TimeUnit.MILLISECONDS); // pauses current thread from doing anything (ex. printing any log statements, whatever) until all current executor tasks finish (max 3 second wait)
		} catch (InterruptedException e) {
			logger.severe(e.getMessage());
		}
		
		logger.info("Done going thru queue");
		logger.info(String.valueOf(queueVisited.size()));
		logger.info(String.valueOf(queueSize()));
	}
}