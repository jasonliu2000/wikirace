package com.jasonliu.app.wikirace;

public final class Constants {
  private Constants() {}

  public static final String LOGGER = "WikiRaceGlobalLogger";
  public static final String EXPECT_TWO_ARGUMENTS = "Invalid number of arguments. Expected 2 (First is the starting page and second is the target page)";
  public static final String REQUIRE_VALID_START = "Please enter a valid starting wiki page.";
  public static final String REQUIRE_VALID_TARGET = "Please enter a valid target wiki page.";

  public static enum WikiraceStatus {
    IN_PROGRESS, COMPLETED, FAILED
  }
}
