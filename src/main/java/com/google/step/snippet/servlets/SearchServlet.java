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

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.step.snippet.data.Card;
import com.google.step.snippet.external.Client;
import com.google.step.snippet.external.GeeksForGeeksClient;
import com.google.step.snippet.external.StackOverflowClient;
import com.google.step.snippet.external.W3SchoolsClient;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/** Servlet that handles searches. */
@WebServlet("/search")
public class SearchServlet extends HttpServlet {

  private static final String W3_CSE_ID = "INSERT_W3SCHOOLS_CSE_ID";
  private static final String STACK_CSE_ID = "INSERT_STACKOVERFLOW_CSE_ID";
  private static final String GEEKS_CSE_ID = "INSERT_GEEKSFORGEEKS_CSE_ID";
  private static final String API_KEY = "INSERT_API_KEY";
  private static final String CSE_ITEMS = "items";
  private static final String CSE_LINK = "link";
  private static final String AUTH_URL = "authUrl";
  private static final String AUTH_LABEL = "authLabel";
  private static final String CSE_URL = "https://www.googleapis.com/customsearch/v1";
  private static final String CARD_LIST_LABEL = "cardList";

  private final List<Client> clients =
      Arrays.asList(
          new W3SchoolsClient(W3_CSE_ID),
          new StackOverflowClient(STACK_CSE_ID),
          new GeeksForGeeksClient(GEEKS_CSE_ID));

  private static String encodeValue(String value) {
    try {
      return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    } catch (UnsupportedEncodingException ex) {
      return null;
    }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String redirectPath = request.getRequestURI();
    if (request.getQueryString() != null) {
      redirectPath += "?" + request.getQueryString();
    }

    UserService userService = UserServiceFactory.getUserService();
    if (userService.isUserLoggedIn()) {
      request.setAttribute(AUTH_URL, userService.createLogoutURL(redirectPath));
      request.setAttribute(AUTH_LABEL, "Logout");
    } else {
      request.setAttribute(AUTH_URL, userService.createLoginURL(redirectPath));
      request.setAttribute(AUTH_LABEL, "Login");
    }

    String param = request.getParameter("q");
    if (param == null || encodeValue(param) == null) {
      response.setContentType("text/html;");
      response.getWriter().println("Invalid Query");
      return;
    }
    String query = encodeValue(param);
    List<Card> allCards = new ArrayList<>();
    for (Client client : clients) {
      String link = getLink(client.getCseId(), query);
      if (link == null) {
        break;
      }
      Card card = client.search(link);
      if (card != null) {
        allCards.add(card);
      }
    }
    request.setAttribute(CARD_LIST_LABEL, allCards);
    request.getRequestDispatcher("WEB-INF/templates/search.jsp").forward(request, response);
  }

  private String getLink(String id, String query) {
    String url = CSE_URL + "?key=" + API_KEY + "&cx=" + id + "&q=" + query;
    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      try (CloseableHttpResponse response = httpClient.execute(new HttpGet(url))) {
        if (response.getStatusLine().getStatusCode() != 200) {
          return null;
        }
        HttpEntity entity = response.getEntity();
        if (entity == null) {
          return null;
        }
        JsonObject obj;
        try {
          obj =
              JsonParser.parseReader(new InputStreamReader(entity.getContent())).getAsJsonObject();
        } catch (JsonParseException
            | IllegalStateException
            | UnsupportedOperationException
            | IOException e) {
          return null;
        }
        if (obj.has(CSE_ITEMS) && obj.getAsJsonArray(CSE_ITEMS).size() > 0) {
          JsonObject topResult = obj.getAsJsonArray(CSE_ITEMS).get(0).getAsJsonObject();
          if (topResult.has(CSE_LINK)) {
            return topResult.get(CSE_LINK).getAsString();
          }
        }
      } catch (IOException | IllegalStateException | ClassCastException e) {
        return null;
      }
    } catch (IOException e) {
      return null;
    }
    return null;
  }
}
