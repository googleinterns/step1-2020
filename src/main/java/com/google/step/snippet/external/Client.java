package com.google.step.snippet.external;

import com.google.step.snippet.data.Card;

public abstract class Client {

  public static final String URL = "url";
  public static final String FEEDBACK = "feedback";
  public static final String UP = "upvote";
  public static final String DOWN = "downvote";

  public abstract Card search(String url, String query);

  public abstract String getCseId();

  protected String getVotes(String url) {
    return "0";
  }
}
