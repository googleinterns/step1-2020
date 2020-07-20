// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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

/** Servlet that renders a basic homepage. */
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
      System.out.println("Invalid Card");
      response.sendRedirect(request.getHeader("Referer"));
    }

    Query.FilterPredicate filter = new Query.FilterPredicate(URL, FilterOperator.EQUAL, url);
    Query query = new Query(FEEDBACK).setFilter(filter);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    feedbackEntity = datastore.prepare(query).asSingleEntity();
    long count = 0;

    if (feedbackEntity == null) {
      feedbackEntity = new Entity(FEEDBACK);
      feedbackEntity.setProperty(URL, url);

      if (request.getParameter(UP) != null) {
        count = 1;
        feedbackEntity.setProperty(UP, (long) 1);
        feedbackEntity.setProperty(DOWN, (long) 0);
      } else if (request.getParameter(DOWN) != null) {
        count = 1;
        feedbackEntity.setProperty(DOWN, (long) 1);
        feedbackEntity.setProperty(UP, (long) 0);
      }
    } else {
      if (request.getParameter(UP) != null) {
        count = (long) feedbackEntity.getProperty(UP) + 1;
        feedbackEntity.setProperty(UP, (long) feedbackEntity.getProperty(UP) + 1);
      } else if (request.getParameter(DOWN) != null) {
        count = (long) feedbackEntity.getProperty(DOWN) + 1;
        feedbackEntity.setProperty(DOWN, (long) feedbackEntity.getProperty(DOWN) + 1);
      }
    }
    datastore.put(feedbackEntity);
    response.setContentType("text/plain");
    response.getWriter().println(Long.toString(count));
  }
}
