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

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!Card.class.isAssignableFrom(obj.getClass())) {
      return false;
    }
    final Card other = (Card) obj;
    if (!this.title.equals(other.title)) {
      return false;
    }
    if (!this.code.equals(other.code)) {
      return false;
    }
    if (!this.link.equals(other.link)) {
      return false;
    }
    if (!this.description.equals(other.description)) {
      return false;
    }
    return true;
  }
}
