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

package com.google.step.YOUR_PROJECT_NAME_HERE.servlets;

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

  private static final String W3_CSE_ID = "010448377421452380243:pat2rmwjvb8";
  private static final String STACK_CSE_ID = "010448377421452380243:6b8rs1ze5oy";
  private static final String GEEKS_CSE_ID = "010448377421452380243:ieq7z84z2qq";
  private static final String API_KEY = "AIzaSyCMg08fxt9IX8LOAdwJGR0DyphMFpXPe5k";
  private static final String CSE_URL =
      "https://www.googleapis.com/customsearch/v1?key=" + API_KEY + "&cx=";

  private final HttpClient httpClient =
      HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

  private static String encodeValue(String value) {
    try {
      return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    } catch (UnsupportedEncodingException ex) {
      throw new RuntimeException(ex.getCause());
    }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String query = "&q=" + encodeValue(request.getParameter("q"));
    List<String> allLinks = new ArrayList<>();

    /** Send request to retrieve card content through w3School site link from Google CSE */
    try {
      String w3Link = getLink(CSE_URL + W3_CSE_ID + query);
      allLinks.add(w3Link);
    } catch (Exception e) {
      e.printStackTrace();
    }

    /** Send request to retrieve card content through StackOverflow site link from Google CSE */
    try {
      String stackLink = getLink(CSE_URL + STACK_CSE_ID + query);
      allLinks.add(stackLink);
    } catch (Exception e) {
      e.printStackTrace();
    }

    /** Send request to retrieve card content through GeeksForGeeks site link from Google CSE */
    try {
      String geeksLink = getLink(CSE_URL + GEEKS_CSE_ID + query);
      allLinks.add(geeksLink);
    } catch (Exception e) {
      e.printStackTrace();
    }

    response.setContentType("text/html;");
    response.getWriter().println(allLinks);
  }

  private String getLink(String CSE) throws Exception {
    HttpRequest linkRequest =
        HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(CSE))
            .setHeader("User-Agent", "Java 11 HttpClient Bot")
            .build();

    try {
      HttpResponse<String> linkResponse =
          httpClient.send(linkRequest, HttpResponse.BodyHandlers.ofString());
      /** Parse JSON of CSE SRP to retrieve link from only the first search result */
      JSONObject obj = new JSONObject(linkResponse.body());
      String link = obj.getJSONArray("items").getJSONObject(0).getString("link");
      return link;
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Link not found!");
    }
  }
}
