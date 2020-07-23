package com.google.step.snippet.data;

public final class Card {
  private final String title;
  private final String code;
  private final String link;
  private final String description;

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
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!Card.class.isAssignableFrom(obj.getClass())) {
      return false;
    }

    final Card other = (Card) obj;
    return this.title.equals(other.title)
        && ((this.code == null && other.code == null) || this.code.equals(other.code))
        && this.link.equals(other.link)
        && this.description.equals(other.description);
  }
}
