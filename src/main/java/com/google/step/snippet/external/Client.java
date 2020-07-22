package com.google.step.snippet.external;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.step.snippet.data.Card;

public interface Client {

  public static final String URL = "url";
  public static final String FEEDBACK = "feedback";
  public static final String UP = "upvote";
  public static final String DOWN = "downvote";

  Card search(String url);

  String getCseId();

  default Entity getFeedback(String url) {
    Query.FilterPredicate filter = new Query.FilterPredicate(URL, FilterOperator.EQUAL, url);
    Query query = new Query(FEEDBACK).setFilter(filter);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    return datastore.prepare(query).asSingleEntity();
  }

  default long getUpvote(Entity feedback) {
    if (feedback != null) {
      return (long) feedback.getProperty(UP);
    } else {
      return 0;
    }
  }

  default long getDownvote(Entity feedback) {
    if (feedback != null) {
      return (long) feedback.getProperty(DOWN);
    } else {
      return 0;
    }
  }
}
