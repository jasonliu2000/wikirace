package com.jasonliu.app.wikirace.model;

import com.jasonliu.app.wikirace.Constants;
import com.jasonliu.app.wikirace.Constants.WikiraceStatus;
import com.jasonliu.app.wikirace.wiki.WikiRace;
import com.jasonliu.app.wikirace.dto.StatusData;
import com.jasonliu.app.wikirace.dto.Status;

import java.util.concurrent.atomic.AtomicLong;
import java.time.Instant;
import java.time.Duration;
import java.util.HashMap;

public class WikiRaceModel {
  private static final AtomicLong wikiRaceJobId = new AtomicLong();
  private static HashMap<Long, WikiRaceModel> wikiRaces = new HashMap<Long, WikiRaceModel>();

  private WikiRace wikiRace;

  private final long id;
  public final String start;
  public final String target;

  private WikiraceStatus status;
  private Instant startTime;
  private Instant endTime;
  private long timeToCompleteMillis;
  private String[] pathToTarget;

  public WikiRaceModel(String start, String target) {
    id = wikiRaceJobId.incrementAndGet();
    wikiRaces.put(id, this);

    this.start = start;
    this.target = target;
    status = WikiraceStatus.NOT_STARTED;
  }

  public synchronized static HashMap<Long, WikiRaceModel> getWikiRaces() {
    return wikiRaces;
  }

  public synchronized static boolean doesWikiRaceWithIdExist(long id) {
    return wikiRaces.containsKey(id);
  }

  public synchronized static void purgeWikiRace(long id) {
    wikiRaces.remove(id);
  }

  public long getId() {
    return id;
  }

  // consider what to do with this moving forward
  public synchronized WikiraceStatus getStatus() {
    return status;
  }

  public synchronized Status getAPIStatus() {
    String message = "";
    String endTimeString = "";
    Duration elapsedTime = Duration.between(startTime, Instant.now());
    long timeToCompletion = 0;

    switch (status) {
      case IN_PROGRESS:
        message = Constants.IN_PROGRESS_MSG;
        break;
      case COMPLETED:
        message = Constants.COMPLETED_MSG;
        timeToCompletion = Duration.between(startTime, endTime).toMillis();
        endTimeString = endTime.toString();
        break;
    }

    StatusData data = new StatusData(
      start,
      target,
      startTime.toString(),
      endTimeString,
      elapsedTime.toMillis(),
      timeToCompletion,
      pathToTarget
    );

    return new Status(
      id,
      status,
      message,
      data,
      Instant.now().toString()
    );
  }

  public synchronized void setProgressStart() {
    if (startTime == null) {
      startTime = Instant.now();
    }

    status = WikiraceStatus.IN_PROGRESS;
  }

  public synchronized void setFailure() {
    if (endTime == null) {
      endTime = Instant.now();
    }

    status = WikiraceStatus.FAILED;
  }

  public synchronized void setCompleted(String[] pathToTarget) {
    if (endTime == null) {
      endTime = Instant.now();
    }

    status = WikiraceStatus.COMPLETED;
    this.pathToTarget = pathToTarget;
  }
  
}
