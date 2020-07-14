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

import com.google.step.snippet.data.Card;
import com.google.step.snippet.external.Client;
import com.google.step.snippet.external.StackOverflowClient;
import com.google.step.snippet.external.W3SchoolClient;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/** Servlet that handles searches. */
@WebServlet("/search")
public class SearchServlet extends HttpServlet {

  private static final String W3_CSE_ID = "INSERT_W3SCHOOL_CSE_ID";
  private static final String STACK_CSE_ID = "INSERT_STACKOVERFLOW_CSE_ID";
  private static final String GEEKS_CSE_ID = "INSERT_GEEKSFORGEEKS_CSE_ID";
  private static final String API_KEY = "INSERT_API_KEY";
  private static final String CSE_URL = "https://www.googleapis.com/customsearch/v1";

  private final HttpClient httpClient =
      HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

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
    String param = request.getParameter("q");
    if (param == null || encodeValue(param) == null) {
      response.setContentType("text/html;");
      response.getWriter().println("Invalid Query");
      return;
    }

    String query = encodeValue(param);
    List<Card> allCards = new ArrayList<>();
    List<Client> clients = new ArrayList<>();
    clients.add(new W3SchoolClient(W3_CSE_ID));
    clients.add(new StackOverflowClient(STACK_CSE_ID));
    // TODO: Add GFG client.
    for (Client client : clients) {
      String link = getLink(client.getCSEId(), query);
      Card card = client.search(link);
      allCards.add(card);
    }

    request.setAttribute("card_list", allCards);
    request.getRequestDispatcher("WEB-INF/templates/search.jsp").forward(request, response);
  }

  private String getLink(String id, String query) {
    String cse_request = CSE_URL + "?key=" + API_KEY + "&cx=" + id + "&q=" + query;
    HttpRequest linkRequest =
        HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(cse_request))
            .setHeader("User-Agent", "Java 11 HttpClient Bot")
            .build();
    HttpResponse<String> linkResponse;
    try {
      linkResponse = httpClient.send(linkRequest, HttpResponse.BodyHandlers.ofString());
    } catch (IOException | InterruptedException ex) {
      return null;
    }
    /* Parse JSON of CSE SRP to retrieve link from only the first search result */
    JSONObject obj = new JSONObject(linkResponse.body());
    String link = obj.getJSONArray("items").getJSONObject(0).getString("link");
    return link;
  }
}
