package com.google.step.YOUR_PROJECT_NAME_HERE.external;

import com.google.step.YOUR_PROJECT_NAME_HERE.data.Card;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public final class W3SchoolClient {

  public Card search(String w3Link) throws IOException {

    Document doc = Jsoup.connect(w3Link).get();
    Elements titles = doc.getElementsByTag("h1");
    Elements descriptions = doc.getElementsByTag("p");
    Elements snippets = doc.getElementsByClass("w3-example");

    String title = "";
    String description = "";
    String code = "";

    if (titles != null
        && !titles.isEmpty()
        && descriptions != null
        && !descriptions.isEmpty()
        && snippets != null
        && !snippets.isEmpty()) {
      title = titles.first().text();
      description = descriptions.first().text();
      code = snippets.first().getElementsByClass("w3-code").text();
    }

    if (!title.isEmpty() && !description.isEmpty() && !code.isEmpty()) {
      Card w3Card = new Card(title, code, w3Link, description);
      return w3Card;
    }
    return null;
  }
}
