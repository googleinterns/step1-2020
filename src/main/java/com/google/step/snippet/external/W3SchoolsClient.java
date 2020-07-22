package com.google.step.snippet.external;

import com.google.step.snippet.data.Card;
import java.io.IOException;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public final class W3SchoolsClient implements Client {
  private static final String TITLE_TAG = "h1";
  private static final String DESC_TAG = "p";
  private static final String SNIPPET_CLASS = "w3-example";
  private static final String CODE_CLASS = "w3-code";
  private static final String START_TAG =
      "\\<\\w+((\\s+\\w+(\\s*\\=\\s*(?:\".*?\"|'.*?'|[^'\"\\>\\s]+))?)+\\s*|\\s*)\\>";
  public static final String END_TAG = "\\</\\w+\\>";
  public static final String SELF_CLOSE_TAG =
      "\\<\\w+((\\s+\\w+(\\s*\\=\\s*(?:\".*?\"|'.*?'|[^'\"\\>\\s]+))?)+\\s*|\\s*)/\\>";
  public static final String HTML_ENTITY = "&[a-zA-Z][a-zA-Z0-9]+;";

  private String cseId = null;

  public W3SchoolsClient(String cseId) {
    this.cseId = cseId;
  }

  @Override
  public String getCseId() {
    return cseId;
  }

  /**
   * Creates and returns a {@code Card} for the given W3Schools URL.
   *
   * @param w3Link the URL of the W3Schools web page to create the card for
   * @return the created card, or {@code null} if a card could not be created
   */
  @Override
  public Card search(String w3Link) {
    Document doc = null;
    try {
      doc = Jsoup.connect(w3Link).get();
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
    if (containsHtml(code)) {
      System.out.println("hello");
      code = StringEscapeUtils.escapeHtml4(code);
    }
    return new Card(title, code, w3Link, description);
  }

  public boolean containsHtml(String toValidate) {
    Pattern htmlPattern =
        Pattern.compile(
            "(" + START_TAG + ".*" + END_TAG + ")|(" + SELF_CLOSE_TAG + ")|(" + HTML_ENTITY + ")",
            Pattern.DOTALL);
    return htmlPattern.matcher(toValidate).find();
  }
}
