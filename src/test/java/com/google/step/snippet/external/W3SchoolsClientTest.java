package com.google.step.snippet.external;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.step.snippet.data.Card;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class W3SchoolsClientTest {

  private final W3SchoolsClient client = new W3SchoolsClient("CSE_ID");

  @Test
  public void htmlCodeCard() {
    Card actual = client.search("https://www.w3schools.com/tags/tag_img.asp");
    Card expected =
        new Card(
            "HTML &lt;img&gt; Tag",
            "&lt;img alt=&quot;Girl in a jacket&quot; width=&quot;500&quot;"
                + " height=&quot;600&quot;&gt;",
            "https://www.w3schools.com/tags/tag_img.asp",
            "How to insert an image:");
    assertEquals(expected, actual);
  }

  @Test
  public void jsonCodeCard() {
    Card actual = client.search("https://www.w3schools.com/js/js_json_intro.asp");
    Card expected =
        new Card(
            "JSON - Introduction",
            "var myObj = {name: \"John\", age: 31, city: \"New York\"}; var myJSON ="
                + " JSON.stringify(myObj); window.location = \"demo_json.php?x=\" + myJSON;",
            "https://www.w3schools.com/js/js_json_intro.asp",
            "JSON: JavaScript Object Notation.");
    assertEquals(expected, actual);
  }

  @Test
  public void partiallyFilledCard() {
    Card actual = client.search("https://www.w3schools.com/html/html_quiz.asp");
    assertNull(actual);
  }

  @Test
  public void nonexistentLink() {
    Card actual = client.search("https://www.w3schools.com/css/html.asp");
    assertNull(actual);
  }

  @Test
  public void blankPageLink() {
    Card actual = client.search("https://www.w3schools.com/");
    assertNull(actual);
  }
}
