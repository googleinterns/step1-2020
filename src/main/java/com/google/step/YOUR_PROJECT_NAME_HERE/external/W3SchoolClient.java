package com.google.step.YOUR_PROJECT_NAME_HERE.external;

import com.google.step.YOUR_PROJECT_NAME_HERE.data.Card;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public final class W3SchoolClient {
  private static final String TITLE_ID = "h1";
  private static final String DESC_ID = "p";
  private static final String SNIPPET_ID = "w3-example";
  private static final String CODE_ID = "w3-code";

  public Card search(String w3Link) throws IOException {

    Document doc = Jsoup.connect(w3Link).get();
    Elements titles = doc.getElementsByTag(TITLE_ID);
    Elements descriptions = doc.getElementsByTag(DESC_ID);
    Elements snippets = doc.getElementsByClass(SNIPPET_ID);

    String title = "";
    String description = "";
    String code = "";

    if (!isNullOrEmpty(titles) && !isNullOrEmpty(descriptions) && !isNullOrEmpty(snippets)) {
      title = titles.first().text();
      description = descriptions.first().text();
      code = snippets.first().getElementsByClass(CODE_ID).text();
    }

    if (!title.isEmpty() && !description.isEmpty() && !code.isEmpty()) {
      Card w3Card = new Card(title, code, w3Link, description);
      return w3Card;
    }
    return null;
  }

  public boolean isNullOrEmpty(Elements elements) {
    return elements == null || elements.isEmpty();
  }
}
