package com.google.step.YOUR_PROJECT_NAME_HERE.data;

public final class Card {
  private String title;
  private String code;
  private String link;
  private String description;

  public Card() {}

  public Card(String title, String code, String link, String description) {
    this.title = title;
    this.code = code;
    this.link = link;
    this.description = description;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getTitle() {
    return this.title;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getCode() {
    return this.code;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getLink() {
    return this.link;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDescription() {
    return this.description;
  }
}
