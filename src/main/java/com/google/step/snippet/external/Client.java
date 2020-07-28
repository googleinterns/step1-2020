package com.google.step.snippet.external;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.step.snippet.data.Card;
import java.util.ArrayList;

public abstract class Client {

  public static final String URL = "url";
  public static final String FEEDBACK = "feedback";
  public static final String UP = "upvote";
  public static final String DOWN = "downvote";
  private static final String UPVOTERS = "upvoters";
  private static final String DOWNVOTERS = "downvoters";

  public abstract Card search(String url, String query);

  public abstract String getCseId();

  protected long getVotes(String url) {
    Query.FilterPredicate filter = new Query.FilterPredicate(URL, FilterOperator.EQUAL, url);
    Query query = new Query(FEEDBACK).setFilter(filter);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Entity feedback = datastore.prepare(query).asSingleEntity();
    if (feedback != null) {
      return (long) feedback.getProperty(UP) - (long) feedback.getProperty(DOWN);
    } else {
      return 0;
    }
  }

  protected String getUpvote(String url) {
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      return "black";
    }
    Query.FilterPredicate filter = new Query.FilterPredicate(URL, FilterOperator.EQUAL, url);
    Query query = new Query(FEEDBACK).setFilter(filter);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Entity feedback = datastore.prepare(query).asSingleEntity();
    if (feedback == null) {
      return "black";
    }
    ArrayList<String> upvoters = (ArrayList<String>) feedback.getProperty(UPVOTERS);
    if (upvoters != null && upvoters.contains(userService.getCurrentUser().getUserId())) {
      return "green";
    }
    return "black";
  }

  protected String getDownvote(String url) {
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      return "black";
    }
    Query.FilterPredicate filter = new Query.FilterPredicate(URL, FilterOperator.EQUAL, url);
    Query query = new Query(FEEDBACK).setFilter(filter);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Entity feedback = datastore.prepare(query).asSingleEntity();
    if (feedback == null) {
      return "black";
    }
    ArrayList<String> downvotes = (ArrayList<String>) feedback.getProperty(DOWNVOTERS);
    if (downvotes != null && downvotes.contains(userService.getCurrentUser().getUserId())) {
      return "red";
    }
    return "black";
  }
}
