package com.jasonliu.app.wikirace.wiki;

import com.jasonliu.app.wikirace.Constants;
import com.jasonliu.app.wikirace.Constants.WikiraceStatus;

import java.util.logging.Logger;
import java.util.concurrent.*;
import java.util.HashSet;

public class WikiRace extends Thread {
	private static WikiRace wikirace;

	private static final Logger logger = Logger.getLogger(Constants.LOGGER);

	private static WikiraceStatus status = WikiraceStatus.NOT_STARTED;
	private static long startTime;
	private static String time;
	private static String startingPage;
  private static String targetPage;
	private static String[] pathToTarget;

	private static BlockingQueue<WikiNode> queue;
	private static HashSet<String> queueHistory;

	private static ExecutorService executor;
	
  private WikiRace(String start, String target) {
		time = "";
		pathToTarget = new String[]{};
		status = WikiraceStatus.IN_PROGRESS;

		queueHistory = new HashSet<String>();
		queue = new LinkedBlockingQueue<WikiNode>();

		startingPage = start;
		targetPage = target;
  }

	public static WikiRace initiate(String start, String target) {
		wikirace = new WikiRace(start, target);
		return wikirace;
	}

	public void run() {
		startTime = System.currentTimeMillis();
		WikiNode startNode = new WikiNode(startingPage);
		addNodeToQueue(startNode);
		executeWikirace();
	}

	private void executeWikirace() {
		executor = Executors.newFixedThreadPool(10);
		
		while (true) {
			WikiNode node;
			try {
				node = queue.take();
				executor.execute(new Search(node, targetPage));
			} catch (InterruptedException e) {
				status = WikiraceStatus.FAILED;
				logger.severe(e.getMessage());
			} catch (RejectedExecutionException e) {
				break;
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

	public static synchronized void targetFound(String[] pathTaken) {
		time = calculateTimeDuration();
		logger.info(String.format("Time taken: %s ms", time));

		status = WikiraceStatus.COMPLETED;
		pathToTarget = pathTaken;
		executor.shutdown();
	}

	public static WikiraceStatus getStatus() {
		return status;
	}

	public static String getTimeDuration() {
		return time;
	}

	private static String calculateTimeDuration() {
		return String.valueOf(System.currentTimeMillis() - startTime);
	}

	public static String[] getPathToTarget() {
		return pathToTarget;
	}
}