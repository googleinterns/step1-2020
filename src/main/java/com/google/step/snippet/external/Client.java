package com.google.step.snippet.external;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.step.snippet.data.Card;

public abstract class Client {

  public static final String URL = "url";
  public static final String FEEDBACK = "feedback";
  public static final String UP = "upvote";
  public static final String DOWN = "downvote";

  public abstract Card search(String url, String query);

  public abstract String getCseId();

  protected long getVotes(String url) {
    System.out.println(url);
    Query.FilterPredicate filter = new Query.FilterPredicate(URL, FilterOperator.EQUAL, url);
    System.out.println("gets here 1");
    Query query = new Query(FEEDBACK).setFilter(filter);
    System.out.println("gets here 2");
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    System.out.println("gets here 3");
    Entity feedback = datastore.prepare(query).asSingleEntity();
    if (feedback != null) {
      System.out.println("gets here 4");
      return (long) feedback.getProperty(UP) - (long) feedback.getProperty(DOWN);
    } else {
      return 0;
    }
  }
}
