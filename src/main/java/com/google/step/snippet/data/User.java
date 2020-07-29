package com.google.step.snippet.data;

public final class User {
  private final String id;
  private final String email;
  private final String website;
  private final String language;

  public User(String id, String email, String website, String language) {
    this.id = id;
    this.email = email;
    this.website = website;
    this.language = language;
  }

  public String getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public String getWebsite() {
    return website;
  }

  public String getLanguage() {
    return language;
  }
}
