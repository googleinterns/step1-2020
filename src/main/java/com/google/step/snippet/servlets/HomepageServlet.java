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

import com.google.step.snippet.data.Auth;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that renders a basic homepage. */
@WebServlet("/")
public class HomepageServlet extends HttpServlet {

  private static final String AUTH_URL = "authUrl";
  private static final String AUTH_LABEL = "authLabel";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String redirectPath = request.getRequestURI();
    if (request.getQueryString() != null) {
      redirectPath += "?" + request.getQueryString();
    }
    Auth authUser = new Auth();
    request.setAttribute(AUTH_URL, authUser.getUrl(redirectPath));
    request.setAttribute(AUTH_LABEL, authUser.getLabel());
    // Forward the request to the template (which is a servlet itself).
    request.getRequestDispatcher("WEB-INF/templates/homepage.jsp").forward(request, response);
  }
}
