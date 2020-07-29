package com.google.step.snippet.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

@WebServlet("/vote")
public class FeedbackServlet extends HttpServlet {

  private static final String UP = "upvote";
  private static final String DOWN = "downvote";
  private static final String TOTAL = "totalvotes";
  private static final String UPVOTED = "upvoted";
  private static final String DOWNVOTED = "downvoted";
  private static final String URL = "url";
  private static final String FEEDBACK = "feedback";
  private static final String USER = "user";
  private static final String UID = "uid";
  private static final String EMAIL = "email";
  private static final String TOG_UP = "toggleUpvote";
  private static final String TOG_DOWN = "toggleDownvote";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    JSONObject json = new JSONObject();
    json.put(TOG_UP, "disabled");
    json.put(TOG_DOWN, "disabled");

    UserService userService = UserServiceFactory.getUserService();
    if (userService.isUserLoggedIn()) {
      String uid = userService.getCurrentUser().getUserId();
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      Query.FilterPredicate filterUser = new Query.FilterPredicate(UID, FilterOperator.EQUAL, uid);
      Query queryUser = new Query(USER).setFilter(filterUser);
      Entity userEntity = datastore.prepare(queryUser).asSingleEntity();
      String url = request.getParameter(URL);

      // If a user has voted previously, retrieve their past voting status
      if (userEntity != null && url != null) {
        ArrayList<String> userUpCards = (ArrayList<String>) userEntity.getProperty(UPVOTED);
        ArrayList<String> userDownCards = (ArrayList<String>) userEntity.getProperty(DOWNVOTED);
        if (userUpCards != null && userUpCards.contains(url)) {
          json.put(TOG_UP, "active");
        } else if (userDownCards != null && userDownCards.contains(url)) {
          json.put(TOG_DOWN, "active");
        }
      }
    }
    response.setContentType("application/json");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    // Invalid url in POST request
    String url = request.getParameter(URL);
    if (url == null) {
      return;
    }

    // Query for feedback card entity
    Query.FilterPredicate filter = new Query.FilterPredicate(URL, FilterOperator.EQUAL, url);
    Query query = new Query(FEEDBACK).setFilter(filter);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Entity feedbackEntity = datastore.prepare(query).asSingleEntity();

    // Initialize feedback entity if none exists in datastore
    if (feedbackEntity == null) {
      feedbackEntity = new Entity(FEEDBACK);
      feedbackEntity.setProperty(URL, url);
      feedbackEntity.setProperty(TOTAL, (long) 0);
    }

    // Return unchanged total voate count
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      JSONObject json = new JSONObject();
      response.setContentType("application/json");
      response.getWriter().println(json);
      return;
    }

    // Query for user info by user id
    String uid = userService.getCurrentUser().getUserId();
    Query.FilterPredicate filterUser = new Query.FilterPredicate(UID, FilterOperator.EQUAL, uid);
    Query queryUser = new Query(USER).setFilter(filterUser);
    Entity userEntity = datastore.prepare(queryUser).asSingleEntity();

    // If the user does not exist, greate a new user entity
    if (userEntity == null) {
      userEntity = new Entity(USER);
      userEntity.setProperty(UID, uid);
      userEntity.setProperty(EMAIL, userService.getCurrentUser().getEmail());
    }

    // Get list of user's upvoted and downvoted cards
    ArrayList<String> upCards;
    if (userEntity.getProperty(UPVOTED) == null) {
      upCards = new ArrayList<String>();
    } else {
      upCards = (ArrayList<String>) userEntity.getProperty(UPVOTED);
    }
    ArrayList<String> downCards;
    if (userEntity.getProperty(DOWNVOTED) == null) {
      downCards = new ArrayList<String>();
    } else {
      downCards = (ArrayList<String>) userEntity.getProperty(DOWNVOTED);
    }

    // Add and remove card in user's list of upvoted and downvoted cards accordingly
    if (request.getParameter(UP) != null) {
      if (!upCards.contains(url)) {
        upCards.add(url);
        if (downCards.contains(url)) {
          downCards.remove(url);
        }
      } else {
        upCards.remove(url);
      }
    } else if (request.getParameter(DOWN) != null) {
      if (!downCards.contains(url)) {
        downCards.add(url);
        if (upCards.contains(url)) {
          upCards.remove(url);
        }
      } else {
        downCards.remove(url);
      }
    }
    userEntity.setProperty(UPVOTED, upCards);
    userEntity.setProperty(DOWNVOTED, downCards);
    datastore.put(userEntity);

    // Get the toggle status of upvote and downvote buttons
    String upVoteStatus = "disabled";
    String downVoteStatus = "disabled";
    if (userEntity.getProperty(UPVOTED) != null
        && ((ArrayList<String>) userEntity.getProperty(UPVOTED)).contains(url)) {
      upVoteStatus = "active";
    }
    if (userEntity.getProperty(DOWNVOTED) != null
        && ((ArrayList<String>) userEntity.getProperty(DOWNVOTED)).contains(url)) {
      downVoteStatus = "active";
    }
    JSONObject json = new JSONObject();
    json.put(TOG_UP, upVoteStatus);
    json.put(TOG_DOWN, downVoteStatus);

    // Calculate the total votes per card
    long totalUpvotes = 0;
    long totalDownvotes = 0;
    Query allUsers = new Query(USER);
    PreparedQuery results = datastore.prepare(allUsers);
    for (Entity entity : results.asIterable()) {
      ArrayList<String> userUpCards = (ArrayList<String>) entity.getProperty(UPVOTED);
      ArrayList<String> userDownCards = (ArrayList<String>) entity.getProperty(DOWNVOTED);
      if (userUpCards != null && userUpCards.contains(url)) {
        totalUpvotes++;
      }
      if (userDownCards != null && userDownCards.contains(url)) {
        totalDownvotes++;
      }
    }
    json.put(TOTAL, Long.toString(totalUpvotes - totalDownvotes));
    feedbackEntity.setProperty(TOTAL, Long.toString(totalUpvotes - totalDownvotes));
    datastore.put(feedbackEntity);

    response.setContentType("application/json");
    response.getWriter().println(json);
  }
}
