package com.google.step.YOUR_PROJECT_NAME_HERE.external;

import com.google.step.YOUR_PROJECT_NAME_HERE.data.Card;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.regex.Pattern;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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
  // This url specify filter to generate answer body.
  private static final String ANSWER_URL_TEMPLATE =
      "https://api.stackexchange.com/2.2/answers/%s?order"
          + "=desc&sort=activity&site=stackoverflow&filter=!9_bDE(fI5";
  private static final int ID_INDEX = 2;
  private static final String ITEM_PARAMETER = "items";
  private static final String TITLE_PARAMETER = "title";
  private static final String BODY_PARAMETER = "body";
  private static final String CODE_PARAMETER = "code";
  private static final String ANSWER_ID_PARAMETER = "answer_id";
  private static final int DESCRIPTION_LENGTH_PARAMETER = 200;

  public Card search(String url) {
    try {
      String questionId = getQuestionId(url);
      String title = getTitle(questionId);
      String answerId = getAnswerId(questionId);
      String answerBody = getAnswerBody(answerId);
      String description = getDescription(answerBody);
      String code = getCode(answerBody);
      Card card = new Card(title, code, url, description);
      return card;
    } catch (URISyntaxException e) {
      // Return null card if no valid card available.
      return null;
    }
  }

  /* Get the question id of passed in URL. */
  private String getQuestionId(String url) throws URISyntaxException {
    URI uri = new URI(url);
    // Parse the URL to get the question id.
    String[] segments = uri.getPath().split("/");
    String questionId = segments[ID_INDEX];
    if (!Pattern.matches("[0-9]+", questionId)) {
      return null;
    }
    return questionId;
  }

  /* Get the question title using question id */
  private String getTitle(String questionId) {
    String searchUrl = String.format(SEARCH_URL_TEMPLATE, questionId);
    try {
      String title = getResponse(searchUrl, TITLE_PARAMETER);
      return title;
    } catch (IOException e) {
      return null;
    }
  }

  /* Get the most voted answer's id and store it in the card. */
  private String getAnswerId(String questionId) {
    String questionUrl = String.format(QUESTION_URL_TEMPLATE, questionId);
    try {
      String answerId = getResponse(questionUrl, ANSWER_ID_PARAMETER)
      // Replace the question id by the answer id in order to retrieve the code body next.
      return answerId;
    } catch (IOException e) {
     return null;
    }
  }

  /* Get the content of the answer and store it in the card. */
  private String getAnswerBody(String answerId) {
    String answerUrl = String.format(ANSWER_URL_TEMPLATE, answerId);
    try {
      String body = getResponse(answerUrl, BODY_PARAMETER);
      return body;
    } catch (IOException e) {
      return null;
    }
  }

  /* Get the description using return answer body. */
  private String getDescription(String body) {
    Document doc = Jsoup.parse(body);
    // Combine all description in the answer body.
    Elements descriptionHtml = doc.select("p");
    String description = "";
    for (Element e : descriptionHtml) {
      description += e.outerHtml();
    }
    if (description.length() >= DESCRIPTION_LENGTH_PARAMETER) {
      description = description.substring(0, DESCRIPTION_LENGTH_PARAMETER);
    }
    return description;
  }

  /* Get the code using return answer body. */
  private String getCode(String body) {
    Document doc = Jsoup.parse(body);
    // Combine all code in the answer body.
    Elements codeHtml = doc.select(CODE_PARAMETER);
    String code = "";
    for (Element e : codeHtml) {
      code += e.outerHtml();
    }
    return code;
  }

  private String getResponse(String url, String fieldParam) throws IOException {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    CloseableHttpResponse response = httpClient.execute(new HttpGet(url));
    if (response.getStatusLine().getStatusCode() != 200) {
      return null;
    }
    HttpEntity entity = response.getEntity();
    if (entity == null) {
      return null;
    }
    BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
    StringBuilder responseBody = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null) {
      responseBody.append(line);
    }
    JSONObject json = new JSONObject(responseBody.toString());
    String res = json.getJSONArray(ITEM_PARAMETER).getJSONObject(0).get(fieldParam).toString();
    return res;
  }
}
