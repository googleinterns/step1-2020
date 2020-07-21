package com.google.step.snippet.external;

import com.google.step.snippet.data.Card;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
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

public final class StackOverflowClient implements Client {
  private static final String SEARCH_URL_TEMPLATE =
      "https://api.stackexchange.com/2.2/questions/%s?"
          + "order=desc&sort=activity&site=stackoverflow";
  private static final String QUESTION_URL_TEMPLATE =
      "https://api.stackexchange.com/2.2/questions/%s/answers?"
          + "order=desc&sort=votes&site=stackoverflow";
  // This URL specifies a custom StackExchange API filter that generates answer body.
  private static final String ANSWER_URL_TEMPLATE =
      "https://api.stackexchange.com/2.2/answers/%s?order"
          + "=desc&sort=activity&site=stackoverflow&filter=!9_bDE(fI5";
  // The URL is in the pattern of stackoverlow.com/questions/question_id/title.
  // The ID_INDEX help retrieve the question_id from parsed URL.
  private static final int ID_INDEX = 2;
  private static final String ITEM_PARAMETER = "items";
  private static final String TITLE_PARAMETER = "title";
  private static final String BODY_PARAMETER = "body";
  private static final String CODE_PARAMETER = "code";
  private static final String ANSWER_ID_PARAMETER = "answer_id";
  // Set 200 to be the maximum length of description for MVP.
  private static final int MAX_DESCRIPTION_LENGTH = 200;

  private final String cseId;

  public StackOverflowClient(String cseId) {
    this.cseId = cseId;
  }

  @Override
  public String getCseId() {
    return cseId;
  }

  /**
   * Creates and returns a {@code Card} for the given StackOverflow URL.
   *
   * @param url the URL of the StackOverflow question to create the card for
   * @return the created card, or {@code null} if a card could not be created
   */
  @Override
  public Card search(String url) {
    String questionId = getQuestionId(url);
    if (questionId == null) {
      return null;
    }
    String answerId = getAnswerId(questionId);
    if (answerId == null) {
      return null;
    }
    String title = getTitle(questionId);
    String answerBody = getAnswerBody(answerId);
    if (title == null || answerBody == null) {
      return null;
    }
    // No description or code is allowed for StackOverflow.
    String description = getDescription(answerBody);
    String code = getCode(answerBody);
    return new Card(title, code, url, description, "StackOverflow");
  }

  /* Get the question id of passed in URL. */
  private String getQuestionId(String url) {
    URI uri;
    try {
      uri = new URI(url);
    } catch (URISyntaxException e) {
      return null;
    }
    // Parse the URL to get the question id.
    String[] segments = uri.getPath().split("/");
    String questionId = segments[ID_INDEX];
    if (!Pattern.matches("[0-9]+", questionId)) {
      return null;
    }
    return questionId;
  }

  /* Return the most voted answer's id. */
  private String getAnswerId(String questionId) {
    String questionUrl = String.format(QUESTION_URL_TEMPLATE, questionId);
    return getResponse(questionUrl, ANSWER_ID_PARAMETER);
  }

  /* Return the question title using question id */
  private String getTitle(String questionId) {
    String searchUrl = String.format(SEARCH_URL_TEMPLATE, questionId);
    return getResponse(searchUrl, TITLE_PARAMETER);
  }

  /* Get the content of the answer and store it in the card. */
  private String getAnswerBody(String answerId) {
    String answerUrl = String.format(ANSWER_URL_TEMPLATE, answerId);
    return getResponse(answerUrl, BODY_PARAMETER);
  }

  /* Return the description parsed from answer body. */
  private String getDescription(String body) {
    Document doc = Jsoup.parse(body);
    // Combine all description in the answer body.
    Elements descriptionHtml = doc.select("p");
    String description = "";
    for (Element e : descriptionHtml) {
      description += e.outerHtml();
      if (description.length() >= MAX_DESCRIPTION_LENGTH) {
        description = description.substring(0, MAX_DESCRIPTION_LENGTH);
        break;
      }
    }
    return description;
  }

  /* Return the code parsed from answer body. */
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

  private String getResponse(String url, String fieldParam) {
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
    String res = json.getJSONArray(ITEM_PARAMETER).getJSONObject(0).get(fieldParam).toString();
    if (response.getStatusLine().getStatusCode() != 200) {
      return null;
    }
    return res;
  }
}
