package com.jasonliu.app.wikirace;

public final class Constants {
  private Constants() {}

  public static final String LOGGER = "WikiRaceGlobalLogger";
  public static final String LOG_FILENAME = "wikirace.log";

  public static final String EXPECT_TWO_ARGUMENTS = "Invalid number of arguments. Expected 2 (First is the starting page and second is the target page)";
  public static final String REQUIRE_STARTING_ARTICLE = "Please provide a starting Wikipedia article.";
  public static final String REQUIRE_TARGET_ARTICLE = "Please provide a target Wikipedia article.";

  public static enum WikiraceStatus {
    NOT_STARTED, IN_PROGRESS, COMPLETED, FAILED
  }
}
