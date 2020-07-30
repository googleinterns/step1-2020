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
  private static final String TOTAL = "totalvotes";

  public abstract Card search(String url, String query);

  public abstract String getCseId();

  protected long getVotes(String url) {
    Query.FilterPredicate filter = new Query.FilterPredicate(URL, FilterOperator.EQUAL, url);
    Query query = new Query(FEEDBACK).setFilter(filter);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Entity feedback = datastore.prepare(query).asSingleEntity();
    if (feedback != null) {
      return (long) feedback.getProperty(TOTAL);
    } else {
      return 0;
    }
  }
}
