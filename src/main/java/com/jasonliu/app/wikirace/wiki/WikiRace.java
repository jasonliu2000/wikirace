package com.jasonliu.app.wikirace.wiki;

import com.jasonliu.app.wikirace.Constants;
import com.jasonliu.app.wikirace.Constants.WikiraceStatus;

import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.FileHandler;
import java.util.concurrent.*;
import java.util.HashSet;

public class WikiRace extends Thread {
	private static WikiRace wikirace;

	private static final Logger logger = Logger.getLogger(Constants.LOGGER);
	private static FileHandler fileHandler;

	private static WikiraceStatus status = WikiraceStatus.NOT_STARTED;
	private long startTime;
	private static String time;
	private static String startingPage;
  private static String targetPage;
	private static String[] pathToTarget;

	public static BlockingQueue<WikiNode> queue = new LinkedBlockingQueue<WikiNode>();
  private static HashSet<String> hints = new HashSet<String>();
	private static HashSet<String> queueHistory = new HashSet<String>();

	private static Boolean targetFound;
	private static ExecutorService executor;
	
  private WikiRace(String start, String target) {
		time = "";
		pathToTarget = new String[]{};
		status = WikiraceStatus.IN_PROGRESS;

		try {
			FileHandler fileHandler = new FileHandler(Constants.LOG_FILENAME);
			fileHandler.setFormatter(new SimpleFormatter());
			logger.addHandler(fileHandler);
		} catch (Exception e) {
			logger.warning("Failed to setup log file");
		}

    if (!WikiPage.exists(start)) {
			setStatusFailed();
			logger.severe(Constants.REQUIRE_VALID_START);
    }

		if (start == target) {
			status = WikiraceStatus.COMPLETED;
		}

    if (!WikiPage.exists(target)) {
			setStatusFailed();
			logger.severe(Constants.REQUIRE_VALID_TARGET);
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
		addNodeToQueue(startNode);
		executeWikirace();

		if (fileHandler != null) {
			fileHandler.close();
		}

		time = String.valueOf(System.currentTimeMillis() - startTime); // TODO: refactor into a setter method
		logger.info(String.format("Time taken: %s ms", time));
	}

	private void executeWikirace() {
		executor = Executors.newSingleThreadExecutor(); //newFixedThreadPool(10); // Runtime.getRuntime().availableProcessors()
		
		while (true) {
			WikiNode node;
			try {
				node = queue.take();
				executor.execute(new Search(node, targetPage));
			} catch (InterruptedException | RejectedExecutionException e) {
				if (e.getClass().getName() == RejectedExecutionException.class.getName()) {
					break;
				} else {
					setStatusFailed();
					logger.severe(e.getMessage());
				}
			}
		}
	}

	static synchronized int queueSize() {
    return queue.size();
  }

	static synchronized void addNodeToQueue(WikiNode node) {
		try {
			if (queueHistory.add(node.name)) {
				queue.put(node);
			}
		} catch (InterruptedException e) {
			logger.severe(e.getMessage());
			Thread.currentThread().interrupt();
		}
  }

	public static synchronized void targetFound() {
		targetFound = true;
		status = WikiraceStatus.COMPLETED;

		executor.shutdown();

		// addNodeToQueue(new WikiNode("poison")); 
		
		// Thread.currentThread().interrupt();
		// logger.info(String.format("thread interrupted!! status: %s", Thread.currentThread().isInterrupted()));

		try {
			executor.awaitTermination(0, TimeUnit.MILLISECONDS); // pauses current thread from doing anything (ex. printing any log statements, whatever) until all current executor tasks finish (max 3 second wait)
		} catch (InterruptedException e) {
			logger.severe(e.getMessage());
		}
	}

	public static WikiraceStatus getStatus() {
		return status;
	}

	private static void setStatusFailed() {
		status = WikiraceStatus.FAILED;
	}

	public static String getTimeDuration() {
		return time;
	}

	public static String[] getPathToTarget() {
		return pathToTarget;
	}
}