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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
import org.json.JSONObject;

/** Servlet that handles searches. */
@WebServlet("/search")
public class SearchServlet extends HttpServlet {

  private static final String W3_CSE_ID = "INSERT_W3SCHOOL_CSE_ID";
  private static final String STACK_CSE_ID = "INSERT_STACKOVERFLOW_CSE_ID";
  private static final String GEEKS_CSE_ID = "INSERT_GEEKSFORGEEKS_CSE_ID";
  private static final String API_KEY = "INSERT_API_KEY";
  private static final String CSE_URL = "https://www.googleapis.com/customsearch/v1";
  private static final String CSE_ITEMS = "items";
  private static final String CSE_LINK = "link";

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
    List<String> allLinks = new ArrayList<>();

    // TODO: after implementing scraping, consider changing getLink calls to a
    // for-loop

    /*
     * Send request to retrieve card content through w3School site link from Google
     * CSE
     */
    String w3Link = getLink(W3_CSE_ID, query);
    if (w3Link != null) {
      allLinks.add(w3Link);
      // TODO: Call scraping function to return JSON card content
    }

    /*
     * Send request to retrieve card content through StackOverflow site link from
     * Google CSE
     */
    String stackLink = getLink(STACK_CSE_ID, query);
    if (stackLink != null) {
      allLinks.add(stackLink);
      // TODO: Call stackoverflow API to return JSON card content
    }

    /*
     * Send request to retrieve card content through GeeksForGeeks site link from
     * Google CSE
     */
    String geeksLink = getLink(GEEKS_CSE_ID, query);
    if (geeksLink != null) {
      allLinks.add(geeksLink);
      // TODO: Call scraping function to return JSON card content
    }

    request.getRequestDispatcher("WEB-INF/templates/search.jsp").forward(request, response);
  }

  private String getLink(String id, String query) {
    String url = CSE_URL + "?key=" + API_KEY + "&cx=" + id + "&q=" + query;
    CloseableHttpClient httpClient = HttpClients.createDefault();
    CloseableHttpResponse response;
    try {
      response = httpClient.execute(new HttpGet(url));
    } catch (IOException e) {
      return null;
    }
    if (response.getStatusLine().getStatusCode() != 200) {
      return null;
    }
    HttpEntity entity = response.getEntity();
    if (entity == null) {
      return null;
    }
    BufferedReader reader;
    try {
      reader = new BufferedReader(new InputStreamReader(entity.getContent()));
    } catch (IOException e) {
      return null;
    }
    StringBuilder responseBody = new StringBuilder();
    String line;
    try {
      while ((line = reader.readLine()) != null) {
        responseBody.append(line);
      }
      reader.close();
    } catch (IOException e) {
      return null;
    }
    JSONObject json = new JSONObject(responseBody.toString());

    if (response.getStatusLine().getStatusCode() != 200) {
      return null;
    }

    if (!json.isNull(CSE_ITEMS) && !json.getJSONArray(CSE_ITEMS).isNull(0)) {
      JSONObject obj = json.getJSONArray(CSE_ITEMS).getJSONObject(0);
      if (!obj.isNull(CSE_LINK)) {
        return obj.getString(CSE_LINK);
      }
    }
    return null;
  }
}
