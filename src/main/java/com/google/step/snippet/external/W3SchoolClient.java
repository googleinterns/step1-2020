package com.google.step.snippet.external;

import com.google.step.snippet.data.Card;
import java.io.IOException;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public final class W3SchoolClient {
  private static final String TITLE_TAG = "h1";
  private static final String DESC_TAG = "p";
  private static final String SNIPPET_CLASS = "w3-example";
  private static final String CODE_CLASS = "w3-code";

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
    String code =
        StringEscapeUtils.escapeHtml4(snippets.first().getElementsByClass(CODE_CLASS).text());
    return new Card(title, code, w3Link, description);
  }
}
