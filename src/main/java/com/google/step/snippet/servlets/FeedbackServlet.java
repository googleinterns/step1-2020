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
  private static final String NONE = "none";
  private static final String URL = "url";
  private static final String FEEDBACK = "feedback";
  private static final String USER = "UserInfo";
  private static final String ID = "id";
  private static final String EMAIL = "email";
  private static final String STATUS = "status";

  private Entity getUserEntity(DatastoreService datastore, UserService userService) {
    String id = userService.getCurrentUser().getUserId();
    Query.FilterPredicate filterUser = new Query.FilterPredicate(ID, FilterOperator.EQUAL, id);
    Query queryUser = new Query(USER).setFilter(filterUser);
    Entity userEntity = datastore.prepare(queryUser).asSingleEntity();
    if (userEntity == null) {
      userEntity = new Entity(USER);
      userEntity.setProperty(ID, id);
      userEntity.setProperty(EMAIL, userService.getCurrentUser().getEmail());
    }
    return userEntity;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    JSONObject json = new JSONObject();
    UserService userService = UserServiceFactory.getUserService();

    if (userService.isUserLoggedIn()) {
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      String url = request.getParameter(URL);
      Entity userEntity = getUserEntity(datastore, userService);

      // If a user has previously voted on this card, retrieve their past voting status
      if (url != null) {
        ArrayList<String> userUpCards = (ArrayList<String>) userEntity.getProperty(UPVOTED);
        ArrayList<String> userDownCards = (ArrayList<String>) userEntity.getProperty(DOWNVOTED);
        if (userUpCards != null && userUpCards.contains(url)) {
          json.put(STATUS, UPVOTED);
        } else if (userDownCards != null && userDownCards.contains(url)) {
          json.put(STATUS, UPVOTED);
        }
      }
    }
    response.setContentType("application/json");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    // Invalid POST request if URL is invalid or user is not logged
    String url = request.getParameter(URL);
    UserService userService = UserServiceFactory.getUserService();
    if (url == null || !userService.isUserLoggedIn()) {
      JSONObject json = new JSONObject();
      json.put(STATUS, NONE);
      response.setContentType("application/json");
      response.getWriter().println(json);
      return;
    }

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Entity userEntity = getUserEntity(datastore, userService);

    // Get lists of user's upvoted and downvoted cards
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

    // Add or remove card from user's list of upvoted and downvoted cards
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

    // Retrieve toggle status of upvote and downvote buttons for user
    String status = NONE;
    if (userEntity.getProperty(UPVOTED) != null
        && ((ArrayList<String>) userEntity.getProperty(UPVOTED)).contains(url)) {
      status = UPVOTED;
    }
    if (userEntity.getProperty(DOWNVOTED) != null
        && ((ArrayList<String>) userEntity.getProperty(DOWNVOTED)).contains(url)) {
      status = DOWNVOTED;
    }

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

    // Update card's feedback entity with new vote total
    Query.FilterPredicate filter = new Query.FilterPredicate(URL, FilterOperator.EQUAL, url);
    Query query = new Query(FEEDBACK).setFilter(filter);
    Entity feedbackEntity = datastore.prepare(query).asSingleEntity();
    if (feedbackEntity == null) {
      feedbackEntity = new Entity(FEEDBACK);
      feedbackEntity.setProperty(URL, url);
      feedbackEntity.setProperty(TOTAL, (long) 0);
    }
    feedbackEntity.setProperty(TOTAL, totalUpvotes - totalDownvotes);
    datastore.put(feedbackEntity);

    // Construct json response with toggle status and total vote count
    JSONObject json = new JSONObject();
    json.put(STATUS, status);
    json.put(TOTAL, totalUpvotes - totalDownvotes);
    response.setContentType("application/json");
    response.getWriter().println(json);
  }
}
