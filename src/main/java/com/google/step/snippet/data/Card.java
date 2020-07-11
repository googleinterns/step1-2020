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
}
