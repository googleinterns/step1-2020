package com.google.step.snippet.data;

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
