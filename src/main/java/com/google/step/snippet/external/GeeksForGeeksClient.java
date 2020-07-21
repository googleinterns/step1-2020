package com.google.step.snippet.external;

import com.google.appengine.api.datastore.Entity;
import com.google.step.snippet.data.Card;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public final class GeeksForGeeksClient implements Client {
  private static final String TITLE_TAG = "h1";
  private static final String DESC_TAG = "p";
  private static final String SNIPPET_CLASS = "code-block";
  private static final String CODE_CLASS = "code-container";
  private static final String UP = "upvote";
  private static final String DOWN = "downvote";

  private final String cseId;

  public GeeksForGeeksClient(String cseId) {
    this.cseId = cseId;
  }

  @Override
  public String getCseId() {
    return cseId;
  }

  /**
   * Creates and returns a {@code Card} for the given GeeksForGeeks URL.
   *
   * @param geeksLink the URL of the GeeksForGeeks web page to create the card for
   * @return the created card, or {@code null} if a card could not be created
   */
  @Override
  public Card search(String geeksLink) {
    Document doc = null;
    try {
      doc = Jsoup.connect(geeksLink).get();
    } catch (IOException e) {
      return null;
    }
    Elements titles = doc.getElementsByTag(TITLE_TAG);
    if (titles.isEmpty() || titles.first().text().isEmpty()) {
      return null;
    }
    Elements descriptions = doc.getElementsByTag(DESC_TAG);
    if (descriptions.isEmpty() || descriptions.first().text().isEmpty()) {
      return null;
    }
    Elements snippets = doc.getElementsByClass(SNIPPET_CLASS);
    if (snippets.isEmpty() || snippets.first().getElementsByClass(CODE_CLASS).text().isEmpty()) {
      return null;
    }
    String title = titles.first().text();
    String description = descriptions.first().text();
    String code = snippets.first().getElementsByClass(CODE_CLASS).text();

    /* Retrieve feedback, if stored feedback exists */
    long upvote = 0;
    long downvote = 0;
    Entity feedback = getFeedback(geeksLink);
    if (feedback != null) {
      upvote = (long) feedback.getProperty(UP);
      downvote = (long) feedback.getProperty(DOWN);
    }
    return new Card(title, code, geeksLink, description, upvote, downvote);
  }
}
