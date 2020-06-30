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
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/** Servlet that handles searches. */
@WebServlet("/search")
public class SearchServlet extends HttpServlet {

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

    /** Send Request to W3 Restricted Google CSE */
    String w3GoogleCSE =
        "https://www.googleapis.com/customsearch/v1?key=AIzaSyCSKQy6t7HgEc1zI2thwgGncFVzyB8zZ00&cx=010448377421452380243:pat2rmwjvb8&q="
            + query;

    HttpRequest w3LinkRequest =
        HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(w3GoogleCSE))
            .setHeader("User-Agent", "Java 11 HttpClient Bot")
            .build();

    try {
      HttpResponse<String> w3LinkResponse =
          httpClient.send(w3LinkRequest, HttpResponse.BodyHandlers.ofString());

      JSONObject obj = new JSONObject(w3LinkResponse.body());
      String w3Link = obj.getJSONArray("items").getJSONObject(0).getString("link");

      response.setContentType("text/html;");
      response.getWriter().println(w3Link);

    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
