package com.jasonliu.app.wikirace.controller;

public record Status(long id, String status, String timeDurationMilliseconds, String[] pathToTarget) {}
