package com.jasonliu.app.wikirace.dto;

public record StatusData(
  String startTime,
  String endTime,
  long elapsedTimeMilliseconds,
  String[] pathToTarget
) {}
