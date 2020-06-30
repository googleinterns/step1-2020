package com.google.step.YOUR_PROJECT_NAME_HERE.servlets;

import com.google.gson.Gson;
import com.google.step.YOUR_PROJECT_NAME_HERE.data.Card;
import com.google.step.YOUR_PROJECT_NAME_HERE.external.StackOverflowClient;
import java.io.IOException;
import java.util.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/query")
public class QueryServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String query = request.getParameter("query");
    System.out.print(query);
    List<String> so_urls = new ArrayList<>();
    so_urls.add(query);
    Gson gson = new Gson();
    StackOverflowClient so_client = new StackOverflowClient();
    List<Card> cards = so_client.search(so_urls);
    // TODO: add cards from other two website
    response.setContentType("application/json");
    response.getWriter().print(gson.toJson(cards));
  }
}
