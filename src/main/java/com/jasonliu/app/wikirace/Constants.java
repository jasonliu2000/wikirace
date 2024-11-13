package com.jasonliu.app.wikirace;

public final class Constants {
  private Constants() {}

  public static final String LOGGER = "WikiRaceGlobalLogger";
  public static final String LOG_FILENAME = "wikirace.log";

  public static final String EXPECT_TWO_ARGUMENTS = "Invalid number of arguments. Expected 2 (First is the starting page and second is the target page)";
  public static final String REQUIRE_UNIQUE_ARGUMENTS = "Please provide a target Wikipedia article different from the starting one.";
  public static final String REQUIRE_STARTING_ARTICLE = "Please provide a starting Wikipedia article.";
  public static final String REQUIRE_TARGET_ARTICLE = "Please provide a target Wikipedia article.";
  public static final String REQUEST_ACCEPTED_MSG = "Request accepted. Starting the wikirace now.";

  public static final String NOT_FOUND_MSG = "Wikirace with this job id was not found.";
  public static final String NOT_STARTED_MSG = "Wikirace has not started yet.";
  public static final String IN_PROGRESS_MSG = "Wikirace is in progress.";
  public static final String COMPLETED_MSG = "Wikirace has completed.";
  public static final String FAILED_MSG = "Wikirace failed. Please try again.";

  public static enum WikiraceStatus {
    NOT_STARTED, IN_PROGRESS, COMPLETED, FAILED
  }
}
