package com.jasonliu.app.wikirace.dto;

public record StatusData(
  String start,
  String target,
  String startTime,
  String endTime,
  long elapsedTimeMilliseconds,
  long timeToCompletionMilliseconds,
  String[] pathToTarget
) {}
