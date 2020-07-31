package com.google.step.snippet.external;

import com.google.step.snippet.data.Card;

public abstract class Client {

  public abstract Card search(String url, String query);

  public abstract String getCseId();

  protected long getVotes(String url) {
    // TODO: Add vote retrieval from datastore
    return 0;
  }
}
