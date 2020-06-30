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

  private static final String W3_CSE =
      "https://www.googleapis.com/customsearch/v1?key=AIzaSyCSKQy6t7HgEc1zI2thwgGncFVzyB8zZ00&cx=010448377421452380243:pat2rmwjvb8&q=";

  private static final String STACK_CSE =
      "https://www.googleapis.com/customsearch/v1?key=AIzaSyCSKQy6t7HgEc1zI2thwgGncFVzyB8zZ00&cx=010448377421452380243:6b8rs1ze5oy&q=";

  private static final String GEEKS_CSE =
      "https://www.googleapis.com/customsearch/v1?key=AIzaSyCSKQy6t7HgEc1zI2thwgGncFVzyB8zZ00&cx=010448377421452380243:ieq7z84z2qq&q=";

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

    String query = encodeValue(request.getParameter("q"));
    List<String> allLinks = new ArrayList<>();

    try {
      String w3Link = getLink(W3_CSE + query);
      allLinks.add(w3Link);
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      String stackLink = getLink(STACK_CSE + query);
      allLinks.add(stackLink);
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      String geeksLink = getLink(GEEKS_CSE + query);
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
      JSONObject obj = new JSONObject(linkResponse.body());
      String link = obj.getJSONArray("items").getJSONObject(0).getString("link");
      return link;
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Link not found!");
    }
  }
}
