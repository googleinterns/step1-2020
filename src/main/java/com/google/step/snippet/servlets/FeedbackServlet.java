package com.google.step.snippet.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/vote")
public class FeedbackServlet extends HttpServlet {

  private static final String UP = "upvote";
  private static final String DOWN = "downvote";
  private static final String URL = "url";
  private static final String FEEDBACK = "feedback";

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Entity feedbackEntity = null;
    String url = request.getParameter(URL);

    if (url == null) {
      response.sendRedirect(request.getHeader("Referer"));
    }

    /* Query for card feedback by card URL */
    Query.FilterPredicate filter = new Query.FilterPredicate(URL, FilterOperator.EQUAL, url);
    Query query = new Query(FEEDBACK).setFilter(filter);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    feedbackEntity = datastore.prepare(query).asSingleEntity();
    long totalVotes = 0;

    if (feedbackEntity == null) {
      feedbackEntity = new Entity(FEEDBACK);
      feedbackEntity.setProperty(URL, url);
      /* Initialize vote count */
      if (request.getParameter(UP) != null) {
        totalVotes = 1;
        feedbackEntity.setProperty(UP, (long) 1);
        feedbackEntity.setProperty(DOWN, (long) 0);
      } else if (request.getParameter(DOWN) != null) {
        totalVotes = -1;
        feedbackEntity.setProperty(DOWN, (long) 1);
        feedbackEntity.setProperty(UP, (long) 0);
      }
    } else {
      /* Update with upvote count */
      if (request.getParameter(UP) != null) {
        totalVotes =
            (long) feedbackEntity.getProperty(UP) - (long) feedbackEntity.getProperty(DOWN) + 1;
        feedbackEntity.setProperty(UP, (long) feedbackEntity.getProperty(UP) + 1);
      }
      /* Update with downvote count */
      else if (request.getParameter(DOWN) != null) {
        totalVotes =
            (long) feedbackEntity.getProperty(UP) - (long) feedbackEntity.getProperty(DOWN) - 1;
        feedbackEntity.setProperty(DOWN, (long) feedbackEntity.getProperty(DOWN) + 1);
      }
    }
    /* Convert vote count to response string */
    String totalStr;
    if (totalVotes < 0) {
      totalStr = "-" + Long.toString(Math.abs(totalVotes));
    } else {
      totalStr = Long.toString(totalVotes);
    }
    datastore.put(feedbackEntity);
    response.setContentType("text/plain");
    response.getWriter().println(totalStr);
  }
}
