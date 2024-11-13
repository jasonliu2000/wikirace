package com.jasonliu.app.wikirace.wiki;

import com.jasonliu.app.wikirace.Constants;
import com.jasonliu.app.wikirace.Constants.WikiraceStatus;

import java.util.logging.Logger;
import java.util.concurrent.*;
import java.util.HashSet;

public class WikiRace extends Thread {

	private static final Logger logger = Logger.getLogger(Constants.LOGGER);
	private WikiraceStatus status;

	private long startTime;
	private long timeToCompleteMillis;
	private boolean targetFound;

	private final String start;
  private final String target;
	private String[] pathToTarget;

	private BlockingQueue<WikiNode> queue;
	private HashSet<String> queueHistory;
	private final ExecutorService executor = Executors.newFixedThreadPool(10);
	
  public WikiRace(String start, String target) {
		this.start = start;
		this.target = target;

		timeToCompleteMillis = 0;
		status = WikiraceStatus.NOT_STARTED;

		pathToTarget = new String[]{};
		queueHistory = new HashSet<String>();
		queue = new LinkedBlockingQueue<WikiNode>();

		WikiNode startNode = new WikiNode(this.start);
		addNodeToQueue(startNode);
  }

	public void run() {
		status = WikiraceStatus.IN_PROGRESS;
		startTime = System.currentTimeMillis();
		
		while (true) {
			WikiNode node;
			try {
				node = queue.take();
				executor.execute(new Search(this, node, target));
			} catch (InterruptedException e) {
				status = WikiraceStatus.FAILED;
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
			status = WikiraceStatus.COMPLETED;
			
			timeToCompleteMillis = System.currentTimeMillis() - startTime;
			logger.info(String.format("Time taken: %s ms", timeToCompleteMillis));

			pathToTarget = pathTaken;
			logger.info(String.format("Path to target: %s", String.join(" -> ", pathToTarget)));

			targetFound = true;
			executor.shutdown();
		}
	}

	public WikiraceStatus getStatus() {
		return status;
	}

	public long getTimeDuration() {
		return timeToCompleteMillis;
	}

	public String[] getPathToTarget() {
		return pathToTarget;
	}
}