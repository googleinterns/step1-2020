package com.google.step.snippet.data;

public final class Card {
  private String title;
  private String code;
  private String link;
  private String description;

  public Card(String title, String code, String link, String description) {
    this.title = title;
    this.code = code;
    this.link = link;
    this.description = description;
  }

  public String getTitle() {
    return title;
  }

  public String getCode() {
    return code;
  }

  public String getLink() {
    return link;
  }

  public String getDescription() {
    return description;
  }
}
