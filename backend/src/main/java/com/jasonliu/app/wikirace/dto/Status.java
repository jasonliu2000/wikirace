package com.jasonliu.app.wikirace.dto;

import com.jasonliu.app.wikirace.Constants.WikiraceStatus;

public record Status(
  long id, 
  WikiraceStatus status, 
  String message, 
  StatusData data,
  String timestamp
) {}
