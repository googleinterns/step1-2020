package com.google.step.YOUR_PROJECT_NAME_HERE.external;

import com.google.step.YOUR_PROJECT_NAME_HERE.data.Card;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public final class StackOverflowClient {
  private static final String SEARCH_URL_TEMPLATE =
      "https://api.stackexchange.com/2.2/questions/%s?"
          + "order=desc&sort=activity&site=stackoverflow";
  private static final String QUESTION_URL_TEMPLATE =
      "https://api.stackexchange.com/2.2/questions/%s/answers?"
          + "order=desc&sort=votes&site=stackoverflow";
  private static final String ANSWER_URL_TEMPLATE =
      "https://api.stackexchange.com/2.2/answers/%s?order"
          + "=desc&sort=activity&site=stackoverflow&filter=!9_bDE(fI5";
  private static final int ID_INDEX = 2;
  public Card search(String so_url) {
    try {
      Card card = getQuestion(so_url);
      card = getAnswerId(card);
      card = getAnswer(card);
      return card;
    } catch (URISyntaxException e) {
      return new Card();
    }
  }

  /* Get the question id based on url from the CSE result */
  private Card getQuestion(String so_url) throws URISyntaxException {
    Card card = new Card();
    if (so_url == null) {
      return card;
    }
    URI uri = new URI(so_url);
    // Parse the URL to get the question id.
    String[] segments = uri.getPath().split("/");
    String idStr = segments[ID_INDEX];
    String url = String.format(SEARCH_URL_TEMPLATE, idStr);
    try {
      JSONObject res = getResponse(url);
      String title = res.getJSONArray("items").getJSONObject(0).get("title").toString();
      card.setLink(uri.toString());
      // Store the id of the question in order to get the code body of the answer.
      card.setCode(idStr);
      card.setTitle(title);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return card;
  }

  /* Get the most voted answer's id and store it in the card */
  private Card getAnswerId(Card card) {
    String url = String.format(QUESTION_URL_TEMPLATE, card.getCode());
    try {
      JSONObject res = getResponse(url);
      JSONArray answer_list = res.getJSONArray("items");
      String answer_id = res.getJSONArray("items").getJSONObject(0).get("answer_id").toString();
      // Replace the question id by the answer id in order to retrieve the code body next.
      card.setCode(answer_id);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return card;
  }

  /* Get the content of the answer and store it in the card */
  private Card getAnswer(Card card) {
    String url = String.format(ANSWER_URL_TEMPLATE, card.getCode());
    try {
      JSONObject res = getResponse(url);
      String body = res.getJSONArray("items").getJSONObject(0).get("body").toString();
      Document doc = Jsoup.parse(body);
      // Combine all description in the answer body.
      Elements description_html = doc.select("p");
      String description = "";
      for (Element e : description_html) {
        description += e.outerHtml();
      }
      // Get the first 100 characters for displaying.
      description = description.substring(0, 99);
      // Combine all code in the answer body.
      Elements code_html = doc.select("code");
      String code = "";
      for (Element e : code_html) {
        code += e.outerHtml();
      }
      card.setDescription(description);
      card.setCode(code);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return card;
  }

  private JSONObject getResponse(String url) throws IOException {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    CloseableHttpResponse response = httpClient.execute(new HttpGet(url));
    if (response.getStatusLine().getStatusCode() != 200) {
      return new JSONObject();
    }
    HttpEntity entity = response.getEntity();
    if (entity == null) {
      return new JSONObject();
    }
    BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
    StringBuilder responseBody = new StringBuilder();
    String line = null;
    while ((line = reader.readLine()) != null) {
      responseBody.append(line);
    }
    JSONObject res = new JSONObject(responseBody.toString());
    return res;
  }
}
