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
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.step.snippet.data.User;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that renders user setting page */
@WebServlet("/user")
public class UserSettingServlet extends HttpServlet {
  private static final String WEBSITE_PARAMETER = "website";
  private static final String LANGUAGE_PARAMETER = "language";
  private static final String ID_PARAMETER = "id";
  private static final String EMAIL_PARAMETER = "email";
  private static final String USER_CLASS_PARAMETER = "UserInfo";
  private static final String USER_PARAMETER = "user";
  private static final Set<String> VALID_WEBSITES =
      Collections.unmodifiableSet(
          new HashSet<>(Arrays.asList("GeeksForGeeks", "StackOverflow", "W3Schools")));

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    UserService userService = UserServiceFactory.getUserService();
    // In case unauthenticated user access setting page through URL.
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/");
      return;
    }
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Entity userEntity = getUserEntity(datastore, userService);
    User user =
        new User(
            (String) userEntity.getProperty(ID_PARAMETER),
            (String) userEntity.getProperty(EMAIL_PARAMETER),
            (String) userEntity.getProperty(WEBSITE_PARAMETER),
            (String) userEntity.getProperty(LANGUAGE_PARAMETER));
    request.setAttribute(USER_PARAMETER, user);
    request.getRequestDispatcher("WEB-INF/templates/user_dashboard.jsp").forward(request, response);
    referer = request.getHeader("referer");
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/home");
      return;
    }

    String website = request.getParameter(WEBSITE_PARAMETER);
    String language = request.getParameter(LANGUAGE_PARAMETER);
    String id = userService.getCurrentUser().getUserId();

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Entity entity = new Entity(USER_PARAMETER, id);
    entity.setProperty(ID_PARAMETER, id);
    entity.setProperty(WEBSITE_PARAMETER, website);
    entity.setProperty(LANGUAGE_PARAMETER, language);
    datastore.put(entity);
    response.sendRedirect(referer);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/");
      return;
    }
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    String website = request.getParameter(WEBSITE_PARAMETER);
    String language = request.getParameter(LANGUAGE_PARAMETER);
    Entity userEntity = getUserEntity(datastore, userService);
    if (VALID_WEBSITES.contains(website)) {
      userEntity.setProperty(WEBSITE_PARAMETER, website);
    }
    userEntity.setProperty(LANGUAGE_PARAMETER, language);
    datastore.put(userEntity);
    response.sendRedirect("/user");
  }

  /**
   * @param datastore the datastore service containing existing user information
   * @param userService the current user information
   * @return a new user entity if the user doesn't exist, the user information if it exists
   */
  private Entity getUserEntity(DatastoreService datastore, UserService userService) {
    String id = userService.getCurrentUser().getUserId();
    Query.FilterPredicate filterUser =
        new Query.FilterPredicate(ID_PARAMETER, FilterOperator.EQUAL, id);
    Query queryUser = new Query(USER_CLASS_PARAMETER).setFilter(filterUser);
    Entity userEntity = datastore.prepare(queryUser).asSingleEntity();
    if (userEntity == null) {
      userEntity = new Entity(USER_CLASS_PARAMETER);
      userEntity.setProperty(ID_PARAMETER, id);
      userEntity.setProperty(EMAIL_PARAMETER, userService.getCurrentUser().getEmail());
    }
    return userEntity;
  }
}
