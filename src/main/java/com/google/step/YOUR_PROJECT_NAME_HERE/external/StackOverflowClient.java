package com.google.step.YOUR_PROJECT_NAME_HERE.external;

import com.google.step.YOUR_PROJECT_NAME_HERE.data.Card;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

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
  private static final String DEFAULT_KEYWORD = "";
  private static final int DEFAULT_ANSWER_AMOUNT = 1;

  public List<Card> search(List<String> so_urls) {
    List<Card> cards = getQuestions(so_urls);
    cards = getAnswersId(cards);
    cards = getAnswers(cards);
    return cards;
  }

  private List<Card> getQuestions(List<String> so_urls) {
    List<Card> cards = new ArrayList<>();
    if (so_urls == null || so_urls.size() == 0) {
      return cards;
    }
    for (int i = 0; i < so_urls.size(); i++) {
      CloseableHttpClient httpClient = HttpClients.createDefault();
      try {
        URI uri = new URI(so_urls.get(i));
        System.out.println("path = " + uri.getPath());
        String[] segments = uri.getPath().split("/");
        String idStr = segments[2];
        String url = String.format(SEARCH_URL_TEMPLATE, idStr);
        CloseableHttpResponse response = httpClient.execute(new HttpGet(url));
        if (response.getStatusLine().getStatusCode() != 200) {
          return cards;
        }
        HttpEntity entity = response.getEntity();
        if (entity == null) {
          return cards;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
        StringBuilder responseBody = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
          responseBody.append(line);
        }
        JSONObject res = new JSONObject(responseBody.toString());
        String title = res.getJSONArray("items").getJSONObject(0).get("title").toString();
        Card c = new Card();
        c.setLink(uri.toString());
        c.setCode(idStr);
        c.setTitle(title);
        cards.add(c);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return cards;
  }

  private List<Card> getAnswersId(List<Card> cards) {
    for (int i = 0; i < DEFAULT_ANSWER_AMOUNT; i++) {
      String url = String.format(QUESTION_URL_TEMPLATE, cards.get(i).getCode());
      CloseableHttpClient httpClient = HttpClients.createDefault();
      try {
        CloseableHttpResponse response = httpClient.execute(new HttpGet(url));
        if (response.getStatusLine().getStatusCode() != 200) {
          return cards;
        }
        HttpEntity entity = response.getEntity();
        if (entity == null) {
          return cards;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
        StringBuilder responseBody = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
          responseBody.append(line);
        }
        JSONObject res = new JSONObject(responseBody.toString());
        JSONArray answer_list = res.getJSONArray("items");
        String answer_id = res.getJSONArray("items").getJSONObject(0).get("answer_id").toString();
        cards.get(i).setCode(answer_id);
      } catch (ClientProtocolException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return cards;
  }

  private List<Card> getAnswers(List<Card> cards) {
    for (int i = 0; i < DEFAULT_ANSWER_AMOUNT; i++) {
      String url = String.format(ANSWER_URL_TEMPLATE, cards.get(i).getCode());
      CloseableHttpClient httpClient = HttpClients.createDefault();
      try {
        CloseableHttpResponse response = httpClient.execute(new HttpGet(url));
        if (response.getStatusLine().getStatusCode() != 200) {
          return cards;
        }
        HttpEntity entity = response.getEntity();
        if (entity == null) {
          return cards;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
        StringBuilder responseBody = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
          responseBody.append(line);
        }
        JSONObject res = new JSONObject(responseBody.toString());
        String body = res.getJSONArray("items").getJSONObject(0).get("body").toString();
        cards.get(i).setCode(body);
      } catch (ClientProtocolException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return cards;
  }
}
