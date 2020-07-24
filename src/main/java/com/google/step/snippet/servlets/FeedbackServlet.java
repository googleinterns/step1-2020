package com.google.step.snippet.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
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
  private static final String UPVOTERS = "upvoters";
  private static final String DOWNVOTERS = "downvoters";
  private static final String URL = "url";
  private static final String FEEDBACK = "feedback";

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      response.setContentType("application/json");
      response.getWriter().println(new JSONObject());
      return;
    }

    Entity feedbackEntity = null;
    String url = request.getParameter(URL);

    if (url == null) {
      return;
    }

    /* Query for card feedback by card URL */
    Query.FilterPredicate filter = new Query.FilterPredicate(URL, FilterOperator.EQUAL, url);
    Query query = new Query(FEEDBACK).setFilter(filter);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    feedbackEntity = datastore.prepare(query).asSingleEntity();

    if (feedbackEntity == null) {
      feedbackEntity = new Entity(FEEDBACK);
      feedbackEntity.setProperty(URL, url);
      feedbackEntity.setProperty(UP, (long) 0);
      feedbackEntity.setProperty(DOWN, (long) 0);
      feedbackEntity.setProperty(TOTAL, (long) 0);
      feedbackEntity.setProperty(UPVOTERS, new ArrayList<String>());
      feedbackEntity.setProperty(DOWNVOTERS, new ArrayList<String>());
    }

    long upVotes = (long) feedbackEntity.getProperty(UP);
    long downVotes = (long) feedbackEntity.getProperty(DOWN);
    long totalVotes = (long) feedbackEntity.getProperty(TOTAL);

    ArrayList<String> upvoters;
    if (feedbackEntity.getProperty(UPVOTERS) == null) {
      upvoters = new ArrayList<String>();
    } else {
      upvoters = (ArrayList<String>) feedbackEntity.getProperty(UPVOTERS);
    }
    ArrayList<String> downvoters;
    if (feedbackEntity.getProperty(DOWNVOTERS) == null) {
      downvoters = new ArrayList<String>();
    } else {
      downvoters = (ArrayList<String>) feedbackEntity.getProperty(DOWNVOTERS);
    }

    String USER_ID = userService.getCurrentUser().getUserId();
    JSONObject json = new JSONObject();

    if (request.getParameter(UP) != null) {
      if (!upvoters.contains(USER_ID)) {
        upvoters.add(USER_ID);
        feedbackEntity.setProperty(UPVOTERS, upvoters);
        if (!downvoters.contains(USER_ID)) {
          totalVotes += 1;
        } else {
          totalVotes += 2;
          downvoters.remove(USER_ID);
          feedbackEntity.setProperty(DOWN, downVotes - 1);
          feedbackEntity.setProperty(DOWNVOTERS, downvoters);
        }
        feedbackEntity.setProperty(UP, upVotes + 1);
        json.put("toggleUpvote", "true");
      } else {
        totalVotes -= 1;
        upvoters.remove(USER_ID);
        feedbackEntity.setProperty(UPVOTERS, upvoters);
        feedbackEntity.setProperty(UP, upVotes - 1);
        json.put("toggleUpvote", "false");
      }
      feedbackEntity.setProperty(TOTAL, totalVotes);
      json.put("toggleDownvote", "false");
    } else if (request.getParameter(DOWN) != null) {
      if (!downvoters.contains(USER_ID)) {
        downvoters.add(USER_ID);
        feedbackEntity.setProperty(DOWNVOTERS, downvoters);
        if (!upvoters.contains(USER_ID)) {
          totalVotes -= 1;
        } else {
          totalVotes -= 2;
          upvoters.remove(USER_ID);
          feedbackEntity.setProperty(UP, upVotes - 1);
          feedbackEntity.setProperty(UPVOTERS, upvoters);
        }
        feedbackEntity.setProperty(DOWN, downVotes + 1);
        json.put("toggleDownvote", "true");
      } else {
        totalVotes += 1;
        downvoters.remove(USER_ID);
        feedbackEntity.setProperty(DOWNVOTERS, downvoters);
        feedbackEntity.setProperty(DOWN, downVotes - 1);
        json.put("toggleDownvote", "false");
      }
      feedbackEntity.setProperty(TOTAL, totalVotes);
      json.put("toggleUpvote", "false");
    }
    datastore.put(feedbackEntity);
    json.put("totalVotes", Long.toString(totalVotes));
    response.setContentType("application/json");
    response.getWriter().println(json);
  }
}
