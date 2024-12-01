package com.jasonliu.app.wikirace.wiki;

import com.jasonliu.app.wikirace.Constants;
import com.jasonliu.app.wikirace.Constants.WikiraceStatus;
import com.jasonliu.app.wikirace.model.WikiRaceModel;

import java.util.logging.Logger;
import java.util.concurrent.*;
import java.util.HashSet;
import java.time.Instant;

public class WikiRace extends Thread {

	private static final Logger logger = Logger.getLogger(Constants.LOGGER);

	private WikiraceStatus status;
	private boolean targetFound;
	private final WikiRaceModel progressTracker;
	
	private final String start;
  private final String target;

	private BlockingQueue<WikiNode> queue;
	private HashSet<String> queueHistory;
	private final ExecutorService executor = Executors.newFixedThreadPool(10);
	
  public WikiRace(WikiRaceModel model) {
		progressTracker = model;
		this.start = model.start;
		this.target = model.target;

		queueHistory = new HashSet<String>();
		queue = new LinkedBlockingQueue<WikiNode>();

		WikiNode startNode = new WikiNode(this.start);
		addNodeToQueue(startNode);
  }

	public void run() {
		progressTracker.setProgressStart();
		
		while (true) {
			WikiNode node;
			try {
				node = queue.take();
				executor.execute(new Search(this, node, target));
			} catch (InterruptedException e) {
				progressTracker.setFailure();
				logger.severe(e.getMessage());
			} catch (RejectedExecutionException e) {
				break;
			}
		}
	}

	synchronized int queueSize() {
    return queue.size();
  }

	synchronized void addNodeToQueue(WikiNode node) {
		try {
			if (queueHistory.add(node.name)) {
				queue.put(node);
			}
		} catch (InterruptedException e) {
			logger.severe(e.getMessage());
			Thread.currentThread().interrupt();
		}
  }

	public synchronized void targetFound(String[] pathTaken) {
		if (!targetFound) {
			logger.info("Wikipedia target article has been found");
			progressTracker.setCompleted(pathTaken);
			
			// progressTracker.setTimeDuration(System.currentTimeMillis() - startTimeMillis);
			// logger.info(String.format("Time taken: %s ms", System.currentTimeMillis() - startTimeMillis));

			logger.info(String.format("Path to target: %s", String.join(" -> ", pathTaken)));

			targetFound = true;
			executor.shutdown();
		}
	}

	public synchronized boolean isTargetFound() {
		return targetFound;
	}

}